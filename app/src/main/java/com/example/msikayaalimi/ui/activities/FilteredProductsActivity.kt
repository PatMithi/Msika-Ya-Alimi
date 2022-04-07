package com.example.msikayaalimi.ui.activities


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.ui.adapters.MarketItemsAdapter
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.MYATextView
import com.example.msikayaalimi.utils.MYATextViewBold

class FilteredProductsActivity : BaseActivity() {

    private lateinit var mCategory:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_products)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_FILTERED_ITEMS)){
            mCategory = intent.getStringExtra(Constants.EXTRA_FILTERED_ITEMS)!!

            val tvTitle:MYATextViewBold = findViewById(R.id.tv_title_filtered)

            tvTitle.text = mCategory

            getFilteredProducts(mCategory)
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

        tvTitle.text = mCategory

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

        FirestoreClass().filterForCategory(this, category)
//        showProgressDialog(resources.getString(R.string.please_wait))

    }



}