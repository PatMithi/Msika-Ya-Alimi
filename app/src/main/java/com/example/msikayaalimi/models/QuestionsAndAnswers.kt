package com.example.msikayaalimi.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionsAndAnswers(
    val question1:QuizQuestions = QuizQuestions(),
    val question2:QuizQuestions = QuizQuestions(),
    val question3:QuizQuestions = QuizQuestions(),
    val question4:QuizQuestions = QuizQuestions(),
    val question5:QuizQuestions = QuizQuestions(),
    val question6:QuizQuestions = QuizQuestions(),
    val question7:QuizQuestions = QuizQuestions(),
    val question8:QuizQuestions = QuizQuestions(),
    val question9:QuizQuestions = QuizQuestions(),
    val question10:QuizQuestions = QuizQuestions(),
    val quizItem:String = ""
):Parcelable
