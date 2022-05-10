package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data model used to create the fields required to load the
 * filters displayed on the welcome activity
 */
@Parcelize
data class WelcomeSlideshow(
    val id:String = "",
    val images:ArrayList<String> = ArrayList(),
    val titles:ArrayList<String> = ArrayList()
):Parcelable
