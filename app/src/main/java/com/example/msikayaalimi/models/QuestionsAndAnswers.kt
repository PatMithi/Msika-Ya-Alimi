package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data model used to store the quiz questions and their answers
 */
@Parcelize
data class QuestionsAndAnswers(
    val quizName:String = "",
    val questions:ArrayList<QuizQuestions> = ArrayList()
):Parcelable
