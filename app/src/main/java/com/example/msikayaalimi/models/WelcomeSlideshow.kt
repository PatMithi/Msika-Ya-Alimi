package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WelcomeSlideshow(
    val id:String = "",
    val images:ArrayList<String> = ArrayList(),
    val titles:ArrayList<String> = ArrayList()
):Parcelable
