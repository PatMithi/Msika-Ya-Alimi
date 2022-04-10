package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionsAndAnswers(
    val quizName:String = "",
    val questions:ArrayList<QuizQuestions> = ArrayList()
):Parcelable
