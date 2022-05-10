package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data model used to create the fields required to retrieve
 * the training information
 */
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
