package com.example.msikayaalimi.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.fragment.findNavController
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

  private fun getUserDetails() {
    FirestoreClass().getUserDetailsUserType(this)
    showProgressDialog(resources.getString(R.string.please_wait))
  }

  fun successfullyLoadedUserDetails(user: User) {
    hideProgressDialog()
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

    showProgressDialog(resources.getString((R.string.please_wait)))

    FirestoreClass().getMyOrders(this)

  }

  fun successfullyLoadedOrdersList(ordersList: ArrayList<Order>){

    hideProgressDialog()
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
    showProgressDialog(resources.getString(R.string.please_wait))
  }

  fun successfullyLoadedSoldProducts(soldProductsList:ArrayList<SoldProduct>){

    hideProgressDialog()
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