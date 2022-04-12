package com.example.msikayaalimi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Welcome
import com.example.msikayaalimi.ui.adapters.WelcomeFilterAdapter
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.GlideLoader
import com.example.msikayaalimi.utils.MYAButton

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnGoToMarket:MYAButton = findViewById(R.id.btn_welcome)
        btnGoToMarket.setOnClickListener {
            startActivity(Intent(this, MarketActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getWelcomeImages()
    }

    private fun getWelcomeImages(){
        FirestoreClass().getWelcomeImages(this)
        showProgressDialog(resources.getString(R.string.please_wait))
    }

    fun successfullyLoadedWelcomeImages(images:ArrayList<Welcome>){
        hideProgressDialog()
        val rvFilters:RecyclerView = findViewById(R.id.rv_welcome_filters)

        if (images.size > 0){
            val ivGoToMarket:ImageView = findViewById(R.id.iv_welcome_three)

            rvFilters.layoutManager = GridLayoutManager(this, 2)
            rvFilters.setHasFixedSize(true)
            val filterImages = ArrayList<Welcome>()
            for (image in images){
                if (image.type == Constants.FILTER_IMAGE){
                    filterImages.add(image)
                }
            }

            for (image in images){

                if (image.title == "Go to Market"){
                    GlideLoader(this).loadProductImage(image.image, ivGoToMarket)
                }
            }

            val welcomeAdapter = WelcomeFilterAdapter(this, filterImages)
            rvFilters.adapter = welcomeAdapter
        } else{
            rvFilters.visibility = View.GONE
        }



    }
}