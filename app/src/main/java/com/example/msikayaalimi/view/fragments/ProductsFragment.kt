package com.example.msikayaalimi.view.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.models.TrainingMenuItem
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.view.activities.AddProductActivity
import com.example.msikayaalimi.view.adapters.TrainingMenuAdapter
import com.example.msikayaalimi.view.adapters.UserProductsListAdapter
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.SwipeToEditCallback
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ProductsFragment : BaseFragment() {

  private lateinit var binding:View
  private lateinit var mUser: User
  private lateinit var mMenuItemList:ArrayList<TrainingMenuItem>
  private lateinit var mFilteredProducts:ArrayList<Product>

  // TODO Step 4: Override the onCreate function and add the setHasOptionMenu with the value true init. Which is used to create option menu in fragment.
  // START
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }
  // END



  private suspend fun getProductsList() {
    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout).startShimmerAnimation()
    FirestoreClass().getListOfProducts(this)
  }

  override fun onResume() {
    super.onResume()

    GlobalScope.launch(Dispatchers.Main) {
      getUserDetails()
    }

  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_products, container, false)
    binding = root
    return root

  }

  suspend  fun gotProductsListSuccessfully(productsList: ArrayList<Product>) {

    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout).stopShimmerAnimation()
    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout).visibility = View.GONE

    if (productsList.size > 0) {

      binding.findViewById<RecyclerView>(R.id.rv_my_products).visibility = View.VISIBLE
      binding.findViewById<MYATextView>(R.id.tv_no_products_yet).visibility = View.GONE

      binding.findViewById<RecyclerView>(R.id.rv_my_products).layoutManager = LinearLayoutManager(activity)
      binding.findViewById<RecyclerView>(R.id.rv_my_products).setHasFixedSize(true)

      val productsAdapter = UserProductsListAdapter(requireActivity(),productsList, this@ProductsFragment)
      binding.findViewById<RecyclerView>(R.id.rv_my_products).adapter = productsAdapter

      val editSwipeHandler = object : SwipeToEditCallback(context!!){
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

          val adapter:UserProductsListAdapter = binding.findViewById<RecyclerView>(R.id.rv_my_products).adapter as UserProductsListAdapter
          adapter.notifyEditItemProducts(
            activity!!,
            viewHolder.adapterPosition
          )
        }
      }

      val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
      editItemTouchHelper.attachToRecyclerView(binding.findViewById<RecyclerView>(R.id.rv_my_products))
    } else {
      binding.findViewById<RecyclerView>(R.id.rv_my_products).visibility = View.GONE
      binding.findViewById<MYATextView>(R.id.tv_no_products_yet).visibility = View.VISIBLE
    }

  }

  private suspend fun getUserDetails() {
    FirestoreClass().getUserDetailsUserType(this@ProductsFragment)
//    showProgressDialog(resources.getString(R.string.please_wait))
  }

  fun successfullyLoadedUserDetails(user: User) {
//    hideProgressDialog()
    mUser = user

    if (user.userType == Constants.CUSTOMER) {
      binding.findViewById<RecyclerView>(R.id.rv_my_products).visibility = View.VISIBLE
      binding.findViewById<MYATextView>(R.id.tv_no_products_yet).visibility = View.GONE
      binding.findViewById<RecyclerView>(R.id.rv_category_items).visibility = View.GONE
      binding.findViewById<FloatingActionButton>(R.id.fab_products).visibility = View.GONE

      setHasOptionsMenu(false)


      getMenuItems()

//      val categoriesAdapter = TrainingMenuAdapter(requireActivity(), )

    }else {

      GlobalScope.launch(Dispatchers.Main) {
        getProductsList()
      }
      binding.findViewById<FloatingActionButton>(R.id.fab_products).setOnClickListener{
        startActivity(Intent(activity, AddProductActivity::class.java))
      }

    }
  }

  suspend fun successfullyLoadedMenuItems(menuItemsList:ArrayList<TrainingMenuItem>) {
    hideProgressDialog()
    val rvMenuItems:RecyclerView = binding.findViewById(R.id.rv_my_products)


    mMenuItemList = menuItemsList



    val menuItemsAdapter = TrainingMenuAdapter(requireActivity(),mMenuItemList)
    rvMenuItems.layoutManager = LinearLayoutManager(activity)
    rvMenuItems.adapter = menuItemsAdapter
    rvMenuItems.visibility = View.VISIBLE
    rvMenuItems.setHasFixedSize(true)
  }

  fun deleteProduct(productID: String) {
    alertDialogToDeleteProduct(productID)
  }

  fun deletedProductSuccessfully() {
    hideProgressDialog()

    Toast.makeText(
      requireActivity(),
      resources.getString(R.string.product_successfully_deleted_message),
      Toast.LENGTH_SHORT
    ).show()

    GlobalScope.launch(Dispatchers.IO) {
      getProductsList()
    }

  }

  private fun getMenuItems() {
    FirestoreClass().getMenuItems(this, Constants.CATEGORY_ITEM)
    showProgressDialog(resources.getString(R.string.please_wait))
  }


  private fun alertDialogToDeleteProduct(productID: String) {

    val builder = AlertDialog.Builder(requireActivity())

    // set the title of the alert dialog
    builder.setTitle(resources.getString(R.string.delete_dialog_title))

    // set the message to be displayed to the user
    builder.setMessage(resources.getString(R.string.delete_dialog_message))
    builder.setIcon(android.R.drawable.ic_dialog_alert)

    // if the action is confirmed, the Firestore class will be called to delete the product
    builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
      showProgressDialog(resources.getString(R.string.please_wait))
      FirestoreClass().deleteProduct(this, productID)
      dialogInterface.dismiss()
    }

    // if the action is negative, alertDialog will just be dismissed
    builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

      dialogInterface.dismiss()
    }

    // create the AlertDialog
    val alertDialog: AlertDialog = builder.create()
    // set the other properties of the dialog
    alertDialog.setCancelable(false)
    alertDialog.show()

  }
  // END
}