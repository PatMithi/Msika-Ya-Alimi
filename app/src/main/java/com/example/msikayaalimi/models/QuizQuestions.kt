package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizQuestions(
    val image:String = "",
    val question:String = "",
    val answerOne:String = "",
    val answerTwo:String = "",
    val answerThree:String = "",
    val answerFour:String = "",
    val correctAnswer:String = "",
    val quizId:String = ""):Parcelable
