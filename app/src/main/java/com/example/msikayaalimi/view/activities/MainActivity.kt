package com.example.msikayaalimi.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Welcome
import com.example.msikayaalimi.view.adapters.WelcomeFilterAdapter
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYAButton
import com.example.msikayaalimi.models.WelcomeSlideshow

class MainActivity : BaseActivity() {
    private lateinit var mSlideShowImages:ArrayList<String>
    private lateinit var mSlideShowTitles:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val svWelcome:androidx.appcompat.widget.SearchView = findViewById(R.id.sv_welcome)
        svWelcome.setOnQueryTextListener (
            (object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    svWelcome.clearFocus()
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SEARCH_VALUE, query)
                    startActivity(intent)

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }


            })
        )

        val btnLaunch:MYAButton = findViewById(R.id.btn_launch_market)
        btnLaunch.setOnClickListener {
            startActivity(Intent(this, MarketActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        getWelcomeImages()
        getSlideshowImages()
    }

    override fun onBackPressed() {
        doubleClickToExit()
    }

    private fun getWelcomeImages(){
        FirestoreClass().getWelcomeImages(this)
        displayProgressDialog(resources.getString(R.string.please_wait))
    }

    fun successfullyLoadedWelcomeImages(images:ArrayList<Welcome>){
        dismissProgressDialog()
        val rvFilters:RecyclerView = findViewById(R.id.rv_welcome_filters)

        if (images.size > 0){

            rvFilters.layoutManager = GridLayoutManager(this, 2)
            rvFilters.setHasFixedSize(true)
            val filterImages = ArrayList<Welcome>()
            for (image in images){
                if (image.type == Constants.FILTER_IMAGE){
                    filterImages.add(image)
                }
            }

            val welcomeAdapter = WelcomeFilterAdapter(this, filterImages)
            rvFilters.adapter = welcomeAdapter
        } else{
            rvFilters.visibility = View.GONE
        }

    }

    private fun getSlideshowImages(){

//        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getSlideshowImage(this)

    }

    fun successfullyLoadedSlideshow(images:WelcomeSlideshow){

//        hideProgressDialog()
        mSlideShowImages = images.images
        mSlideShowTitles = images.titles
        val imageList = ArrayList<SlideModel>()

        Log.e("Slide:", mSlideShowImages[0])

        val image1 = mSlideShowImages[0]
        val image2 = mSlideShowImages[1]
        val image3 = mSlideShowImages[2]

        imageList.add(SlideModel(image1, "Welcome"))
        imageList.add(SlideModel(image2, "Shop best Deals!"))
        imageList.add(SlideModel(image3, "Msika Ya Alimi! \nUniting farmers and" +
                " customers at the palm of your hand"))

        val isWelcome:ImageSlider = findViewById(R.id.is_welcome)
        isWelcome.setImageList(imageList, ScaleTypes.FIT)

//        isWelcome.setOnClickListener{
//            startActivity(Intent(this, MarketActivity::class.java))
//        }
        isWelcome.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here
                when (position){
                    0 -> {
                        startActivity(Intent(this@MainActivity, MarketActivity::class.java))
                    }

                    1 -> {
                        startActivity(Intent(this@MainActivity, MarketActivity::class.java))
                    }

                    2 -> {

                    }
                }
            }
        })

    }
}