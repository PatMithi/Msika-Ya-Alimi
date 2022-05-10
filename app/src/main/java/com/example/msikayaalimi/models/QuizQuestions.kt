package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data model used to create the fields required to store information about the
 * individual questions
 */
@Parcelize
data class QuizQuestions(
    val image:String = "",
    val question:String = "",
    val answerOne:String = "",
    val answerTwo:String = "",
    val answerThree:String = "",
    val answerFour:String = "",
    val correctAnswer:Int = 0
    ):Parcelable
