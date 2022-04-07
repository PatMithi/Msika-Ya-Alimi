package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val user_id:String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val address: Address = Address(),
    val title:String = "",
    val image:String = "",
    val subtotal_amount:String = "",
    val transaction_fee:String = "",
    val total_amount:String = "",
    val order_datetime: Long = 0L,
    var id:String = ""
):Parcelable
