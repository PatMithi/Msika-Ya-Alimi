package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrainingInfo(
    val title:String = "",
    val image:String = "",
    val introInfo:String = "",
    val mainInfo:String = "",
    val secondImage:String = "",
    val thirdImage:String = "",
    val id:String = ""
):Parcelable
