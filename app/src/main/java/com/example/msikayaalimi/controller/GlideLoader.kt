package com.example.msikayaalimi.controller

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.msikayaalimi.R
import java.io.IOException

/**
 * Class used to load images from Firebase
 * Code adapted from open source Glide github repository
 */
class GlideLoader(val context: Context) {

    /**
     * A function to load image from URI for the user profile picture.
     */
    fun loadUserImage(image: Any, imageView: ImageView) {
        try {
            // Loads the user image in the ImageView.
            Glide
                .with(context)
                .load(image) // Uri or URL of the image
                .centerCrop() // Scale type of the image.
                .placeholder(R.drawable.ic_user_icon_image) // A default place holder if image is failed to load.
                .into(imageView) // the view in which the image will be loaded.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadProductImage(image: Any, imageView: ImageView) {
        try {
            // Load the user image in the ImageView.
            Glide
                .with(context)
                .load(image) // Uri or URL of the image
                .centerCrop() // Scale type of the image.
                .placeholder(R.drawable.ic_tag) // set default product image  if created by USSD
                .into(imageView) // the view in which the image will be loaded.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}