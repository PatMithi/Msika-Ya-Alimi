package com.example.msikayaalimi.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.TrainingMenuItem
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.view.activities.SearchActivity
import com.example.msikayaalimi.view.adapters.TrainingMenuAdapter
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYATextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


open class TrainingFragment : BaseFragment() {

    private lateinit var mMenuItemList:ArrayList<TrainingMenuItem>
    private lateinit var binding:View
    private lateinit var mUser: User


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_training, container, false)
        binding = root
        return root
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.Main) {
            getUserDetails()
        }

    }

    private suspend fun getMenuItems() {
        FirestoreClass().getMenuItems(this, Constants.TRAINING_ITEM)
        showProgressDialog(resources.getString(R.string.please_wait))
    }

    private suspend fun getUserDetails() {
        FirestoreClass().getUserDetailsUserType(this@TrainingFragment)
        showProgressDialog(resources.getString(R.string.please_wait))
    }

    suspend fun successfullyLoadedUserDetails(user: User) {
        hideProgressDialog()
        mUser = user

        if (mUser.userType == Constants.CUSTOMER) {
            binding.findViewById<RecyclerView>(R.id.rv_training_menu).visibility = View.GONE
            binding.findViewById<SearchView>(R.id.sv_products).visibility = View.VISIBLE
            binding.findViewById<LinearLayout>(R.id.ll_order_filters).visibility = View.VISIBLE

            binding.findViewById<SearchView>(R.id.sv_products).setOnQueryTextListener (
                (object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.findViewById<SearchView>(R.id.sv_products).clearFocus()
                    val intent = Intent(activity, SearchActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SEARCH_VALUE, query)
                        startActivity(intent)

                    return false
                }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }


            })
            )

            binding.findViewById<MYATextView>(R.id.tv_sort_by_name_ascending).setOnClickListener {
                val intent = Intent(activity, SearchActivity::class.java)
                intent.putExtra(Constants.EXTRA_SORT_ORDER, Constants.ASCENDING)
                intent.putExtra(Constants.EXTRA_SORT_FIELD, Constants.PRODUCTS_TITLE)
                startActivity(intent)
            }
            binding.findViewById<MYATextView>(R.id.tv_sort_by_name_descending).setOnClickListener {
                val intent = Intent(activity, SearchActivity::class.java)
                intent.putExtra(Constants.EXTRA_SORT_ORDER, Constants.DESCENDING)
                intent.putExtra(Constants.EXTRA_SORT_FIELD, Constants.PRODUCTS_TITLE)
                startActivity(intent)
            }
            binding.findViewById<MYATextView>(R.id.tv_sort_by_price_ascending).setOnClickListener {
                val intent = Intent(activity, SearchActivity::class.java)
                intent.putExtra(Constants.EXTRA_SORT_ORDER, Constants.ASCENDING)
                intent.putExtra(Constants.EXTRA_SORT_FIELD, Constants.PRODUCTS_PRICE)
                startActivity(intent)
            }
            binding.findViewById<MYATextView>(R.id.tv_sort_by_price_descending).setOnClickListener {
                val intent = Intent(activity, SearchActivity::class.java)
                intent.putExtra(Constants.EXTRA_SORT_ORDER, Constants.DESCENDING)
                intent.putExtra(Constants.EXTRA_SORT_FIELD, Constants.PRODUCTS_PRICE)
                startActivity(intent)
            }

        }else{

            GlobalScope.launch(Dispatchers.Main) {
                getMenuItems()
            }

        }
    }

    suspend fun successfullyLoadedMenuItems(menuItemsList:ArrayList<TrainingMenuItem>) {
        val rvMenuItems: RecyclerView = binding.findViewById(R.id.rv_training_menu)
        hideProgressDialog()
        mMenuItemList = menuItemsList

        val menuItemsAdapter = TrainingMenuAdapter(requireActivity(),mMenuItemList)
        rvMenuItems.layoutManager = LinearLayoutManager(activity)
        rvMenuItems.adapter = menuItemsAdapter
        rvMenuItems.visibility = View.VISIBLE
        rvMenuItems.setHasFixedSize(true)
    }


}