package com.example.msikayaalimi.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.R
import com.example.msikayaalimi.controller.MYATextView
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import android.widget.Toast

import com.flutterwave.raveandroid.RavePayActivity

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.models.*
import com.example.msikayaalimi.view.adapters.CartItemsAdapter
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYAButton
import com.example.msikayaalimi.controller.MYATextViewBold


/**
 * Class used to load the items ready to be checked out and display the Flutterwave API
 * Code adapted from online course
 */
class CheckoutActivity : BaseActivity() {

    private lateinit var mUser: User
    private var mUserAddress:Address? = null
    private lateinit var mProductsList:ArrayList<Product>
    private lateinit var mCartList: ArrayList<CartItem>
    private var mSubTotal:Double = 0.0
    private var mTotal:Double = 0.0
    private lateinit var mOrderDetails: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setupActionBar()
        getUserDetails()
        val btnPay:MYAButton = findViewById(R.id.btn_pay)


        /**
         * Button to connect to Flutterwave API to complete the payment
         */
        btnPay.setOnClickListener {
            payFlutter()
        }

        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS_CHECKOUT)){
            mUserAddress = intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS_CHECKOUT)

            if (mUserAddress != null) {
                val tvFName:MYATextViewBold = findViewById(R.id.tv_address_full_checkout)
                val tvAddressDetails:MYATextView = findViewById(R.id.tv_address_details_checkout)
                val tvAddressType:MYATextView = findViewById(R.id.tv_address_type_checkout)
                val tvPhoneNumber:MYATextView = findViewById(R.id.tv_address_mobile_number_checkout)

                tvAddressType.text = mUserAddress?.type
                tvFName.text = mUserAddress?.fulName
                tvAddressDetails.text = "${mUserAddress!!.address}, ${mUserAddress!!.district}"
                tvPhoneNumber.text = mUserAddress?.mobileNumber

            }
        }

        getProducts()
    }

    private fun getCartItems(){

        FirestoreClass().getCartList(this)

    }

    fun successfullyLoadedCartList(cartList: ArrayList<CartItem>){
        dismissProgressDialog()
        val rvCartItems:RecyclerView = findViewById(R.id.rv_checkout_cart_items)
        val tvSubtotal:MYATextView = findViewById(R.id.tv_subtotal_checkout)
        val tvTransaction:MYATextView = findViewById(R.id.tv_transaction_fee_checkout)
        val tvTotalAmount:MYATextViewBold = findViewById(R.id.tv_total_amount_checkout)
        val llPay:LinearLayout = findViewById(R.id.ll_pay)

        for (product in mProductsList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.product_id) {
                    cartItem.stock_quantity = product.productQuantity
                }
            }
        }
        mCartList = cartList

        rvCartItems.layoutManager = LinearLayoutManager(this)
        rvCartItems.setHasFixedSize(true)

        /*
            Set the adapter used to display the items
             */
        val cartListAdapter = CartItemsAdapter(this, mCartList, false)
        rvCartItems.adapter = cartListAdapter

        for (item in mCartList) {
            val availableQuantity = item.stock_quantity.toInt()
            if (availableQuantity > 0){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price * quantity)
            }
        }
        tvSubtotal.text = "MWK ${mSubTotal}"
        tvTransaction.text = "MWK 400"


        if (mSubTotal > 0) {
            llPay.visibility = View.VISIBLE

            mTotal = mSubTotal + 400
            tvTotalAmount.text = "MWK $mTotal"

        }else {
            llPay.visibility = View.GONE
        }



    }

    private fun getProducts(){

        displayProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getListOfAllProducts(this)

    }

    fun successfullyLoadedProductsList(productsList:ArrayList<Product>){

        mProductsList = productsList
        getCartItems()

    }

    private fun getUserDetails(){
        FirestoreClass().getUserDetails(this)
//        showProgressDialog(resources.getString(R.string.please_wait))
    }

    fun successfullyLoadedUserDetails(user: User){
        dismissProgressDialog()
        mUser = user
    }

    private fun setupActionBar() {
        val toolbarCheckout = findViewById<Toolbar>(R.id.checkout_activity_toolbar)
        setSupportActionBar(toolbarCheckout)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbarCheckout.setNavigationOnClickListener{ onBackPressed()}
    }


    /**
     * function to implement flutterwave payment process
     * code adapted from Flutterwave API
     */
    private fun payFlutter(){

        RaveUiManager(this)
            .setAmount(mSubTotal)
            .setCurrency("MWK")
            .setCountry("MW")
            .setfName(mUser.firstName)
            .setlName(mUser.lastName)
            .setEmail("")
            .setPublicKey("FLWPUBK_TEST-331b0de545bb36c644adf5f0414ac936-X")
            .setEncryptionKey("FLWSECK_TEST70343a356a30")
            .setTxRef(System.currentTimeMillis().toString() + "Ref")
            .acceptAccountPayments(true)
            .acceptCardPayments(true)
            .acceptUssdPayments(true)
            .acceptUkPayments(true)
            .onStagingEnv(true)
            .shouldDisplayFee(true)
            .showStagingLabel(true)
            .initialize()
    }


    private fun orderItems(){
        displayProgressDialog(resources.getString(R.string.please_wait))

        if (mUserAddress != null){
            mOrderDetails = Order(
                FirestoreClass().getCurrentUserID(),
                mCartList,
                mUserAddress!!,
                "Order ${FirestoreClass().getCurrentUserID()} ${System.currentTimeMillis()}",
                mCartList[0].image,
                mSubTotal.toString(),
                "MWK 400",
                mTotal.toString(),
                System.currentTimeMillis()


            )
            FirestoreClass().placeOrder(this, mOrderDetails)
            displayProgressDialog(resources.getString(R.string.please_wait))
        }

    }

    fun successfullyUpdatedAllDetails(){
        dismissProgressDialog()

        Toast.makeText(
            this,
            resources.getString(R.string.msg_successfully_placed_order),
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(this, MarketActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun successfullyPlacedOrder(){

        FirestoreClass().updateAllDetails(this, mCartList, mOrderDetails)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null){
            val message: String = intent.getStringExtra("response").toString()
            if (resultCode === RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS $message", Toast.LENGTH_SHORT).show()
                orderItems()
            } else if (resultCode === RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR $message", Toast.LENGTH_SHORT).show()
            } else if (resultCode === RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}