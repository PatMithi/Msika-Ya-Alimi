package com.example.msikayaalimi.view.activities


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.view.adapters.MarketItemsAdapter
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.MYATextViewBold

class FilteredProductsActivity : BaseActivity() {

    private var mCategory:String = ""
    private var mProductOwner:String = ""
    private var mLocation:String = ""
    private lateinit var mCreatorName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_products)

        setupActionBar()

        val tvTitle:MYATextViewBold = findViewById(R.id.tv_title_filtered)

        if (intent.hasExtra(Constants.EXTRA_FILTERED_ITEMS)){
            mCategory = intent.getStringExtra(Constants.EXTRA_FILTERED_ITEMS)!!
            tvTitle.text = mCategory

            getFilteredProducts(mCategory!!)
        }

        if (intent.hasExtra(Constants.EXTRA_CREATOR_NAME)){
            mCreatorName = intent.getStringExtra(Constants.EXTRA_CREATOR_NAME)!!

            tvTitle.text = "Products by: " + mCreatorName
        }

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            mProductOwner = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
            getFilteredProducts(mProductOwner)
        }

        if (intent.hasExtra(Constants.EXTRA_LOCATION)){
            mLocation = intent.getStringExtra(Constants.EXTRA_LOCATION)!!
            tvTitle.text = "Products in " + mLocation
            getFilteredProducts(mLocation)

        }
    }

    private fun setupActionBar() {
        val addressListToolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.filtered_products_activity_toolbar)

        setSupportActionBar(addressListToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        addressListToolbar.setNavigationOnClickListener { onBackPressed() }

    }

    fun successfullyFilteredProducts(productsList:ArrayList<Product>) {
//        hideProgressDialog()
        val rvFilteredItems:RecyclerView = findViewById(R.id.filtered_items_rv)
        val tvEmptyFilter:MYATextView = findViewById(R.id.tv_no_items_found_for_filter)
        val tvTitle:MYATextViewBold = findViewById(R.id.tv_title_filtered)

//        if (mCategory!=null){
//            tvTitle.text = mCategory
//        }


        if (productsList.size > 0) {

            rvFilteredItems.visibility = View.VISIBLE
            tvEmptyFilter.visibility = View.GONE

            rvFilteredItems.layoutManager = GridLayoutManager(this, 2)
            rvFilteredItems.setHasFixedSize(true)

            val productsAdapter = MarketItemsAdapter(this,productsList)
            rvFilteredItems.adapter = productsAdapter
        } else {
            rvFilteredItems.visibility = View.GONE
            tvEmptyFilter.visibility = View.VISIBLE
        }
    }

    private fun getFilteredProducts(category:String){

        if (mProductOwner != ""){
            FirestoreClass().filterForProductUser(this, mProductOwner)
        } else if (mLocation!=""){
            FirestoreClass().filterForLocation(this, category)
        }
        else {
            FirestoreClass().filterForCategory(this, category)
        }


//        showProgressDialog(resources.getString(R.string.please_wait))

    }



}