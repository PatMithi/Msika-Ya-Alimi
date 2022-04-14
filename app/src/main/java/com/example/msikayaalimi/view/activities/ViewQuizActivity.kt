package com.example.msikayaalimi.view.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.QuestionsAndAnswers
import com.example.msikayaalimi.models.QuizQuestions
import com.example.msikayaalimi.controller.*

class ViewQuizActivity : BaseActivity() {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList:ArrayList<QuizQuestions>? = null
    private var mSelectedAnswer:Int = 0
    private lateinit var mQuiz:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_quiz)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_QUIZ_NAME)){
            mQuiz = intent.getStringExtra(Constants.EXTRA_QUIZ_NAME)!!
        }

        getQuizQuestions()


    }

    private fun setupActionBar() {
        val toolbar = findViewById(R.id.view_quiz_activity_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar.setNavigationOnClickListener{ onBackPressed()}
    }

    private fun getQuizQuestions(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getQuestions(this, mQuiz)
    }

    fun successfullyLoadedQuestions(questions:QuestionsAndAnswers){

        hideProgressDialog()
        val tvQuestion:MYATextViewBold = findViewById(R.id.tv_question_quiz)
        val tvAnswerOne:MYATextView = findViewById(R.id.tv_answer_one)
        val tvAnswerTwo:MYATextView = findViewById(R.id.tv_answer_two)
        val tvAnswerThree:MYATextView = findViewById(R.id.tv_answer_three)
        val tvAnswerFour:MYATextView = findViewById(R.id.tv_answer_four)
        val ivQuestionImage:ImageView = findViewById(R.id.iv_quiz_photo)

        val quizQuestions = questions

        val question = questions.questions[0]

        GlideLoader(this).loadProductImage(question.image, ivQuestionImage)
        tvQuestion.text = question.question
        tvAnswerOne.text = question.answerOne
        tvAnswerTwo.text = question.answerTwo
        tvAnswerThree.text = question.answerThree
        tvAnswerFour.text = question.answerFour

    }


}