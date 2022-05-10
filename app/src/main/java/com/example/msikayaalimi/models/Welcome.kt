package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data model used to create the fields required to retrieve
 * the information displayed on the welcome activity
 */
@Parcelize
data class Welcome(
    val id:String = "",
    val image:String = "",
    val title:String = "",
    val type:String = ""
):Parcelable
