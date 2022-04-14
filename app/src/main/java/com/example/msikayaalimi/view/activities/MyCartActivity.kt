package com.example.msikayaalimi.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.CartItem
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.view.adapters.CartItemsAdapter
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYAButton
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.MYATextViewBold

class MyCartActivity : BaseActivity() {

    // global variable to take care of the lists of products and cart items
    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartListItems:ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        setupActionBar()

        val btnCheckout:MYAButton = findViewById(R.id.btn_checkout)

        btnCheckout.setOnClickListener {
            val intent = Intent(this, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductsList()
    }

    /*
    Inserting back button in the actionbar to
    allow a user to return to the previous activity
     */
    private fun setupActionBar() {
        val myCartToolbar: Toolbar = findViewById(R.id.toolbar_my_cart_activity)

        setSupportActionBar(myCartToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        myCartToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    /*
    Function is called once the application has
    successfully retrieved the cart list from Firebase
     */
    fun successfullyLoadedCartList(cartList: ArrayList<CartItem>) {
        val rvCartItems:RecyclerView = findViewById(R.id.rv_my_cart_list)
        val llCheckout:LinearLayout = findViewById(R.id.ll_checkout)
        val tvNoCartItem:MYATextView = findViewById(R.id.tv_no_cart_items)
        val tvSubtotal:MYATextView = findViewById(R.id.tv_subtotal)
        val tvTotalAmount: MYATextViewBold = findViewById(R.id.tv_total_amount)

        hideProgressDialog()

        /*
        for each item that is in the cart, the application
        searches for the corresponding product id and sets
        the cart stock quantity to match the products quantity
         */
        for (product in mProductList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.product_id) {

                    cartItem.stock_quantity = product.productQuantity

                    if (product.productQuantity.toInt() === 0){
                        cartItem.cart_quantity = product.productQuantity
                    }
                }
            }
        }

        mCartListItems = cartList

        /*
        set the application to view the layout elements which
        will display the cart and hide the "no items" text
        when there is at least one item in the cart
         */
        if (mCartListItems.size > 0) {
            rvCartItems.visibility = View.VISIBLE
            llCheckout.visibility = View.VISIBLE
            tvNoCartItem.visibility = View.GONE

            /*
            load the data into a recyclerView to
            display the items in a scrollView
             */
            rvCartItems.layoutManager = LinearLayoutManager(this@MyCartActivity)
            rvCartItems.setHasFixedSize(true)

            /*
            Set the adapter used to display the items
             */
            val cartListAdapter = CartItemsAdapter(this@MyCartActivity, mCartListItems, true)
            rvCartItems.adapter = cartListAdapter

            /*
            Perform calculations to get the totals for the items in the cart
             */
            var subtotal: Double = 0.0
            for (item in mCartListItems) {
                val availableQuantity = item.stock_quantity.toInt()
                if (availableQuantity > 0){
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subtotal += (price * quantity)
                }
            }
            tvSubtotal.text = "MWK ${subtotal}"

            if (subtotal > 0) {
                llCheckout.visibility = View.VISIBLE

                val total = subtotal + 400
                tvTotalAmount.text = "MWK ${total}"

            }else {
                llCheckout.visibility = View.GONE
            }



        }
        else{
            rvCartItems.visibility = View.GONE
            llCheckout.visibility = View.GONE
            tvNoCartItem.visibility = View.VISIBLE
        }
    }

    /*
    Function to call the method in the
    Firestore class to load the cart items
     */
    private fun getCartItemsList() {

        //showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCartList(this@MyCartActivity)
    }

    /*
    Function to call the method in the
    Firestore class to load all the products in the market
     */
    private fun getProductsList() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getListOfAllProducts(this@MyCartActivity)
    }

    /*
    Function to handle the list of products once it has
    been successfully retrieved from the Firestore class
     */
    fun successfullyLoadedProductsList(productsList: ArrayList<Product>) {
        hideProgressDialog()
        mProductList = productsList
        getCartItemsList()
    }

    /*
    Function to display message once an item has
    been successfully removed from the cart
     */
    fun successfullyDeletedItemFromCart(){

        hideProgressDialog()

        Toast.makeText(

            this@MyCartActivity,
            resources.getString(R.string.msg_item_remoed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()
    }

    fun successfullyUpdatedCart() {
        hideProgressDialog()
        getCartItemsList()
    }
}