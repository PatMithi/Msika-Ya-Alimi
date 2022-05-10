package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data model used to create the fields used to create a user
 * and retrieve their information.
 */
@Parcelize

class User  (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    var userType: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val gender: String ="",
    val profileCompleted: Int = 0
):Parcelable