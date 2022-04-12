package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Welcome(
    val id:String = "",
    val image:String = "",
    val title:String = "",
    val type:String = ""
):Parcelable
