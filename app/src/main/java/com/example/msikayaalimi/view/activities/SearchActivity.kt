package com.example.msikayaalimi.view.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.view.adapters.MarketItemsAdapter
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYATextView
import com.google.firebase.firestore.Query

/**
 * Class used to display the search and filter options a user can utilise
 */

class SearchActivity : BaseActivity() {

    /**
     * Variable used to store the item which has been searched in the search fragment
     */
    private lateinit var mSearch:String


    /**
     * Variable to store which sort option has been selected from the previous search fragment
     */
    private lateinit var mSortOrder:String

    /**
     * Variable used to store which field has been used as the filter
     * criteria e.g product category, owner, location etc.
     */
    private lateinit var mSortField:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SEARCH_VALUE)){
            mSearch = intent.getStringExtra(Constants.EXTRA_SEARCH_VALUE)!!
            if (mSearch != ""){
                getSearchProducts(mSearch)
            }
        }



        if (intent.hasExtra(Constants.EXTRA_SORT_ORDER) && intent.hasExtra(Constants.EXTRA_SORT_FIELD)){
            mSortOrder = intent.getStringExtra(Constants.EXTRA_SORT_ORDER)!!
            mSortField = intent.getStringExtra(Constants.EXTRA_SORT_FIELD)!!

            getSortedItems(mSortField, mSortOrder)
        }

    }

    /*
    Inserting back button in the actionbar to
    allow a user to return to the previous activity
     */
    private fun setupActionBar() {
        val addressListToolbar: Toolbar = findViewById(R.id.toolbar_search_activity)

        setSupportActionBar(addressListToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        addressListToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getSearchProducts(word:String){

        FirestoreClass().getProductSearchResults(this, word)

    }

    fun successfullyLoadedSearchResults(productsList: ArrayList<Product>){

        val rvSearchResults:RecyclerView = findViewById(R.id.rv_search_results)
        val tvEmptySearch:MYATextView = findViewById(R.id.tv_no_items_found_for_search)

        if (productsList.size > 0) {
            rvSearchResults.visibility = View.VISIBLE
            tvEmptySearch.visibility = View.GONE

            rvSearchResults.layoutManager = GridLayoutManager(this, 2)
            rvSearchResults.setHasFixedSize(true)

            val productsAdapter = MarketItemsAdapter(this,productsList)
            rvSearchResults.adapter = productsAdapter
        } else {
            rvSearchResults.visibility = View.GONE
            tvEmptySearch.visibility = View.VISIBLE
        }
    }

    private fun getSortedItems(fieldName:String, direction:String){
        if (direction == Constants.ASCENDING){
            FirestoreClass().getProductsSorted(this, fieldName,Query.Direction.ASCENDING)
        }else{
            FirestoreClass().getProductsSorted(this, fieldName,Query.Direction.DESCENDING)
        }
    }

}