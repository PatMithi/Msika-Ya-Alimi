package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrainingMenuItem(
    var id:String = "",
    val title:String = "",
    val image:String = "",
    val type: String = ""
):Parcelable
