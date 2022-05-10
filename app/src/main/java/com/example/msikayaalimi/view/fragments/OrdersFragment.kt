package com.example.msikayaalimi.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.*
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Order
import com.example.msikayaalimi.models.SoldProduct
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.view.adapters.MyOrdersListAdapter
import com.example.msikayaalimi.view.adapters.SoldProductsListAdapter
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYATextView
import com.facebook.shimmer.ShimmerFrameLayout

/**
 * Fragment used to display the items a user has sold if the user is a farmer
 * or the orders a customer has purchased
 * Code adapted from online course
 */
class OrdersFragment : BaseFragment() {

  private lateinit var binding:View
  private lateinit var mUser:User

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_orders, container, false)
    binding = root
    return root
  }

  override fun onResume() {
    super.onResume()
    getUserDetails()
  }

  /**
   * Gets the current user's details to determine which items should be displayed in the fragment
   * If the user is a farmer it displays an option tab to allow the farmer to decide whether they
   * want to see the items they have sold or the orders they have made.
   */
  private fun getUserDetails() {
    FirestoreClass().getUserDetailsUserType(this)
  }

  fun successfullyLoadedUserDetails(user: User) {
//    hideProgressDialog()
    mUser = user
    val tvEmptyOrders:MYATextView = binding.findViewById(R.id.tv_no_orders_yet)
    val tvEmptySoldProducts:MYATextView = binding.findViewById(R.id.tv_no_sold_products_yet)
    val rgOrders:RadioGroup = binding.findViewById(R.id.rg_orders_sold)
    val rbSold:RadioButton = binding.findViewById(R.id.rb_sold_products)
    val rbOrders:RadioButton = binding.findViewById(R.id.rb_orders)
    val rvMyOrders:RecyclerView = binding.findViewById(R.id.rv_my_orders_items)
    val rvSoldProductsItems:RecyclerView = binding.findViewById(R.id.rv_my_sold_products_items)


    if (user.userType == Constants.CUSTOMER ){
      getMyOrders()
      rgOrders.visibility = View.GONE
    } else {
      rgOrders.visibility = View.VISIBLE
      rbOrders.setOnClickListener {
        rvSoldProductsItems.visibility = View.GONE
        getMyOrders()
        tvEmptySoldProducts.visibility = View.GONE
      }
      rbSold.setOnClickListener {
        rvSoldProductsItems.visibility = View.VISIBLE
        tvEmptyOrders.visibility = View.GONE
        getSoldProducts()
      }
    }
  }

  private fun getMyOrders(){

    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_orders).visibility = View.VISIBLE
    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_orders).startShimmerAnimation()

    FirestoreClass().getMyOrders(this)

  }

  fun successfullyLoadedOrdersList(ordersList: ArrayList<Order>){

    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_orders).stopShimmerAnimation()
    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_orders).visibility = View.GONE

    val rvMyOrders:RecyclerView = binding.findViewById(R.id.rv_my_orders_items)
    val tvEmptyOrders:MYATextView = binding.findViewById(R.id.tv_no_orders_yet)

    if (ordersList.size > 0) {

      rvMyOrders.visibility = View.VISIBLE
      tvEmptyOrders.visibility = View.GONE

      rvMyOrders.layoutManager = LinearLayoutManager(activity)
      rvMyOrders.setHasFixedSize(true)

      val myOrdersAdapter = MyOrdersListAdapter(requireActivity(), ordersList)
      rvMyOrders.adapter = myOrdersAdapter


    }else{
      rvMyOrders.visibility = View.GONE
      tvEmptyOrders.visibility = View.VISIBLE
    }
  }

  private fun getSoldProducts(){

    FirestoreClass().getSoldProducts(this)
    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_orders).startShimmerAnimation()
    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_orders).visibility = View.VISIBLE
  }

  fun successfullyLoadedSoldProducts(soldProductsList:ArrayList<SoldProduct>){

    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_orders).stopShimmerAnimation()
    binding.findViewById<ShimmerFrameLayout>(R.id.shimmerFrameLayout_orders).visibility = View.GONE

    val rvSoldProductsItems:RecyclerView = binding.findViewById(R.id.rv_my_sold_products_items)
    val tvEmptySoldProducts:MYATextView = binding.findViewById(R.id.tv_no_sold_products_yet)

    if (soldProductsList.size > 0) {

      tvEmptySoldProducts.visibility = View.GONE
      rvSoldProductsItems.visibility = View.VISIBLE

      rvSoldProductsItems.layoutManager = LinearLayoutManager(activity)
      rvSoldProductsItems.setHasFixedSize(true)

      val soldProductsListAdapter = SoldProductsListAdapter(requireActivity(), soldProductsList)
      rvSoldProductsItems.adapter = soldProductsListAdapter

    } else{
      rvSoldProductsItems.visibility = View.GONE
      tvEmptySoldProducts.visibility = View.VISIBLE
    }
  }
}