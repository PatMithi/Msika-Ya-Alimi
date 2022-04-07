package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Product (
    val user_id:String = "",
    val user_name:String = "",
    val productTitle: String = "",
    val productPrice: String = "",
    val productDescription: String = "",
    val productSpecification:String = "",
    val productLocation:String = "",
    val productQuantity:String = "",
    val productImage:String = "",
    val product_category: String="",
    var product_id: String = ""
    ):Parcelable