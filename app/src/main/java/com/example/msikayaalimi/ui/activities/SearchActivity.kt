package com.example.msikayaalimi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.ui.adapters.MarketItemsAdapter
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.MYATextView
import com.google.firebase.firestore.Query

class SearchActivity : BaseActivity() {
    private lateinit var mSearch:String
    private lateinit var mSortOrder:String
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
//        showProgressDialog(resources.getString(R.string.please_wait))
    }

    fun successfullyLoadedSearchResults(productsList: ArrayList<Product>){
//
//        hideProgressDialog()
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