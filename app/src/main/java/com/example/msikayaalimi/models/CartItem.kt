package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *Data model used to create the fields to store cart item information
 */
@Parcelize
data class CartItem (
    val user_id: String = "",
    val product_owner_id:String = "",
    val product_id: String = "",
    val title:String = "",
    val price:String = "",
    val image:String = "",
    var cart_quantity:String = "",
    var stock_quantity:String = "",
    var id: String = ""
) : Parcelable