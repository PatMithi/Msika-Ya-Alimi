package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data model used to create the fields required to retrieve
 * the training menu items
 */
@Parcelize
data class TrainingMenuItem(
    var id:String = "",
    val title:String = "",
    val image:String = "",
    val type: String = ""
):Parcelable
