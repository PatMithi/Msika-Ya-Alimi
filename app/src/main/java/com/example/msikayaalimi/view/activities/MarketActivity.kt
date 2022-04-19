package com.example.msikayaalimi.view.activities

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.view.fragments.ProductsFragment
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.view.fragments.TrainingFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView

class MarketActivity : BaseActivity() {
    private lateinit var mUser: User

    fun successfullyLoadedUserDetails(user:User) {
        hideProgressDialog()
        mUser = user
        val bottomNavMenu:BottomNavigationView = findViewById(R.id.nav_view)
        val bottomNav:NavController = findNavController(R.id.nav_host_fragment_activity_market)
        val navCategories:BottomNavigationItemView = findViewById(R.id.navigation_categories)
        if (mUser.userType == Constants.CUSTOMER) {
//            navCategories.visibility = View.GONE
            val nv =bottomNavMenu.menu
            val productsFragment:ProductsFragment

            nv.findItem(R.id.navigation_categories).icon = ContextCompat.getDrawable(
                this,
                R.drawable.ic_categories)

            nv.findItem(R.id.navigation_categories).title = "Categories"


            nv.findItem(R.id.navigation_training).icon = ContextCompat.getDrawable(
                this,
                R.drawable.ic_search)

            nv.findItem(R.id.navigation_training).title = "Search"
            nv.findItem(R.id.navigation_training)

            nv.findItem(R.id.navigation_search).icon = ContextCompat.getDrawable(this, R.drawable.ic_orders_bag)
            nv.findItem(R.id.navigation_search).title = "Orders"

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

        // Change the background color of the action bar

        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@MarketActivity,
                R.drawable.app_gradient_color_background
            )
        )

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment_activity_market)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_dashboard, R.id.navigation_categories, R.id.navigation_search))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        getUserDetails()
    }


    private fun getUserDetails() {
        FirestoreClass().getUserDetails(this@MarketActivity)
        showProgressDialog(resources.getString(R.string.please_wait))
    }




}