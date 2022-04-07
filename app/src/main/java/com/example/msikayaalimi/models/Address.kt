package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val user_id:String = "",
    val fulName:String = "",
    val mobileNumber:String = "",

    val address: String = "",
    val district: String = "",
    val additionalNote: String = "",

    val type: String = "",
    var id: String = ""
):Parcelable
