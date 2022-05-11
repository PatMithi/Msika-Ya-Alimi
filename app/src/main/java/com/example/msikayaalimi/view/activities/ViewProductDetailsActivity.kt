package com.example.msikayaalimi.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.CartItem
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.GlideLoader
import com.example.msikayaalimi.controller.MYAButton
import com.example.msikayaalimi.controller.MYATextView

/**
 * Code used to view the details of a selected product
 * Code adapted from online course.
 */
class ViewProductDetailsActivity : BaseActivity(), View.OnClickListener{

    private var mProductID: String = ""
    private lateinit var mProductDetails: Product
    private var mProductOwnerId:String = ""
    private lateinit var mCreatorName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_product_details)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductID = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product Id", mProductID)
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mProductOwnerId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        getProductDetails()

        // set an onclicklistener to allow the user to add an item to their cart
        val addToCartButton:MYAButton = findViewById(R.id.btn_add_to_cart)
        val btnGoToCart:MYAButton = findViewById(R.id.btn_go_to_cart)
        val tvCreator:MYATextView = findViewById(R.id.tv_creator_name)
        val tvLocation:MYATextView = findViewById(R.id.tv_product_details_location)
        addToCartButton.setOnClickListener(this)
        btnGoToCart.setOnClickListener(this)
        tvCreator.setOnClickListener(this)
        tvLocation.setOnClickListener(this)
    }

    // adding back button to the activity

    fun setupActionBar() {
        val viewProductsToolbar:Toolbar = findViewById(R.id.view_products_activity_toolbar)

        setSupportActionBar(viewProductsToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        viewProductsToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    // fun to get the selected product

    private fun getProductDetails() {
        displayProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this, mProductID)

    }

    // function to store the loaded data into the appropriate elements
    fun productsSuccessfullyLoaded(product: Product) {

        val productImage:ImageView = findViewById(R.id.iv_view_product_details_image)
        val productTitle:MYATextView = findViewById(R.id.tv_product_details_title)
        val productPrice:MYATextView = findViewById(R.id.tv_product_details_price)
        val productSpecs:MYATextView = findViewById(R.id.products_specs)
        val productQuantity:MYATextView = findViewById(R.id.tv_product_quantity_value)
        val productLocation:MYATextView = findViewById(R.id.tv_product_details_location)
        val addToCartButton:MYAButton = findViewById(R.id.btn_add_to_cart)
        val btnGoToCart:MYAButton = findViewById(R.id.btn_go_to_cart)
        val user_ID = FirestoreClass().getCurrentUserID()
        mProductDetails = product

        getCreatorDetails()


        /*
        / once the product details have been loaded successfully,
          the details are stored in the appropriate layout elements
         */
        GlideLoader(this@ViewProductDetailsActivity).loadProductImage(
            product.productImage,
            productImage
        )

        productTitle.text = product.productTitle
        productLocation.text = product.productLocation
        productPrice.text = "MWK ${product.productPrice}"
        productQuantity.text = product.productQuantity
        productSpecs.text = product.productSpecification

        /*
        Check if the product is out of stock or not and set the
        add to cart button invisible and display that the item
        is out of stock
         */

        if (product.productQuantity.toInt() == 0) {
            dismissProgressDialog()

            addToCartButton.visibility = View.GONE
            productQuantity.text = resources.getString(R.string.out_of_stock_label)
            productQuantity.setTextColor(
                ContextCompat.getColor(
                    this@ViewProductDetailsActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{

            if(FirestoreClass().getCurrentUserID() == mProductOwnerId){
                dismissProgressDialog()
                addToCartButton.visibility = View.GONE
                btnGoToCart.visibility = View.GONE
            }
            else {
                FirestoreClass().checkIfItemInCart(this, mProductID)
                addToCartButton.visibility = View.VISIBLE
                btnGoToCart.visibility = View.VISIBLE
            }
        }


        /*
        display the add to cart button if the current user
        is not the user who created the product
         */
//        if (user_ID != product.user_id) {
//            addToCartButton.visibility = View.VISIBLE
//            btnGoToCart.visibility = View.VISIBLE
//        }else{
//            addToCartButton.visibility = View.GONE
//            btnGoToCart.visibility = View.GONE
//        }

        // checks if the item has already been added to the cart
        if(FirestoreClass().getCurrentUserID() == product.user_id){
            dismissProgressDialog()
        }else{
            FirestoreClass().checkIfItemInCart(this, mProductID)
        }

    }

    private fun getCreatorDetails(){

        FirestoreClass().getProductCreatorName(this, mProductDetails.user_id)

    }

    fun successfullyGotCreatorDetails(user: User){

        val tvCreator:MYATextView = findViewById(R.id.tv_creator_name)
        mCreatorName = "${user.firstName} ${user.lastName}"
        tvCreator.text = mCreatorName
    }

    private fun addToCart() {
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductOwnerId,
            mProductID,
            mProductDetails.productTitle,
            mProductDetails.productPrice,
            mProductDetails.productImage,
            Constants.DEFAULT_CART_QUANTITY
        )

        displayProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addCartItems(this, cartItem)
    }

    fun successfullyAddedItemToCart() {

        dismissProgressDialog()

        Toast.makeText(
            this@ViewProductDetailsActivity,
            resources.getString(R.string.successfully_added_an_item_to_cart),
            Toast.LENGTH_SHORT
        ).show()

        val addToCartButton:MYAButton = findViewById(R.id.btn_add_to_cart)
        val btnGoToCart:MYAButton = findViewById(R.id.btn_go_to_cart)

        /*
        hide the add to cart button once the item has been successfully
        added to the cart and display the button to navigate the user to the cart
         */
        btnGoToCart.visibility = View.VISIBLE
        addToCartButton.visibility = View.GONE


    }

    fun productInCart() {

        val addToCartButton:MYAButton = findViewById(R.id.btn_add_to_cart)
        dismissProgressDialog()
        addToCartButton.visibility = View.GONE

    }

    override fun onClick(v: View?) {

        if ( v!= null) {
            when (v.id) {

                R.id.btn_add_to_cart ->{
                    addToCart()
                }

                R.id.btn_go_to_cart ->{
                    startActivity(Intent(this@ViewProductDetailsActivity, MyCartActivity::class.java))
                }

                R.id.tv_creator_name ->{
                    val intent = Intent(this, FilteredProductsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, mProductDetails.user_id)
                    intent.putExtra(Constants.EXTRA_CREATOR_NAME, mCreatorName)
                    startActivity(intent)
                }

                R.id.tv_product_details_location ->{
                    val intent = Intent(this, FilteredProductsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_LOCATION, mProductDetails.productLocation)
                    startActivity(intent)
                }

            }
        }
    }


}