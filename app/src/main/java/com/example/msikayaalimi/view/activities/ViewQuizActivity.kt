package com.example.msikayaalimi.view.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.QuestionsAndAnswers
import com.example.msikayaalimi.models.QuizQuestions
import com.example.msikayaalimi.controller.*

class ViewQuizActivity : BaseActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList:ArrayList<QuizQuestions>? = null
    private var mSelectedAnswer:Int = 0
    private lateinit var mQuiz:String
    private var mCorrectAnswers:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_quiz)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_QUIZ_NAME)){
            mQuiz = intent.getStringExtra(Constants.EXTRA_QUIZ_NAME)!!
        }

        getQuizQuestions()

        val tvAnswerOne:MYATextView = findViewById(R.id.tv_answer_one)
        val tvAnswerTwo:MYATextView = findViewById(R.id.tv_answer_two)
        val tvAnswerThree:MYATextView = findViewById(R.id.tv_answer_three)
        val tvAnswerFour:MYATextView = findViewById(R.id.tv_answer_four)
        val btnSubmit:MYAButton = findViewById(R.id.btn_submit_answer)
        tvAnswerOne.setOnClickListener(this)
        tvAnswerTwo.setOnClickListener(this)
        tvAnswerThree.setOnClickListener(this)
        tvAnswerFour.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)


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


    /**
     * Function to call the Firestore class
     * used to retrieve the questions
     */
    private fun getQuizQuestions(){
        showProgressDialog(resources.getString(R.string.please_wait))

        // Resets the quiz so that none of the answers are selected.
        resetAnswers()

        FirestoreClass().getQuestions(this, mQuiz)
    }


    /**
     * function used to handle the data from Firestore
     * and storing it in the appropriate UI items
     */
    fun successfullyLoadedQuestions(questions:QuestionsAndAnswers){

        hideProgressDialog()
        val tvQuestion:MYATextViewBold = findViewById(R.id.tv_question_quiz)
        val tvAnswerOne:MYATextView = findViewById(R.id.tv_answer_one)
        val tvAnswerTwo:MYATextView = findViewById(R.id.tv_answer_two)
        val tvAnswerThree:MYATextView = findViewById(R.id.tv_answer_three)
        val tvAnswerFour:MYATextView = findViewById(R.id.tv_answer_four)
        val ivQuestionImage:ImageView = findViewById(R.id.iv_quiz_photo)
        val pbQuestion:ProgressBar = findViewById(R.id.pb_question)
        val tvQuestionNumber:MYATextView = findViewById(R.id.tv_question_number)
        val btnSubmit:MYAButton = findViewById(R.id.btn_submit_answer)


        mQuestionsList = questions.questions

        val question = mQuestionsList!![mCurrentPosition - 1]

        pbQuestion.progress = mCurrentPosition
        tvQuestionNumber.text = mCurrentPosition.toString() + "/" + mQuestionsList!!.size

        if (mCurrentPosition == mQuestionsList!!.size){
            btnSubmit.text = resources.getString(R.string.btn_finish_label)
        } else {
            btnSubmit.text = resources.getString(R.string.btn_labl_submit)
        }

        GlideLoader(this).loadProductImage(question.image, ivQuestionImage)
        tvQuestion.text = question.question
        tvAnswerOne.text = question.answerOne
        tvAnswerTwo.text = question.answerTwo
        tvAnswerThree.text = question.answerThree
        tvAnswerFour.text = question.answerFour


    }


    /**
     * OnClickListener used for this activity
     * when a user clicks on certain items.
     */
    override fun onClick(v: View?) {
        val tvAnswerOne:MYATextView = findViewById(R.id.tv_answer_one)
        val tvAnswerTwo:MYATextView = findViewById(R.id.tv_answer_two)
        val tvAnswerThree:MYATextView = findViewById(R.id.tv_answer_three)
        val tvAnswerFour:MYATextView = findViewById(R.id.tv_answer_four)
        val btnSubmit:MYAButton = findViewById(R.id.btn_submit_answer)
        when (v?.id){
            R.id.tv_answer_one ->{
                selectedAnswer(tvAnswerOne, 1)
            }

            R.id.tv_answer_two ->{
                selectedAnswer(tvAnswerTwo, 2)
            }

            R.id.tv_answer_three ->{
                selectedAnswer(tvAnswerThree, 3)
            }

            R.id.tv_answer_four ->{
                selectedAnswer(tvAnswerFour, 4)
            }

            R.id.btn_submit_answer -> {

                if (mSelectedAnswer == 0){
                    mCurrentPosition ++
                    when {
                        mCurrentPosition <= mQuestionsList!!.size ->{
                            getQuizQuestions()
                        } else ->{
                            Toast.makeText(this, "Completed!",
                            Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, QuizResultActivity::class.java)
                            intent.putExtra(Constants.EXTRA_QUIZ_RESULT, mCorrectAnswers.toString())
                            intent.putExtra(Constants.EXTRA_TOTAL_QUIZ_QUESTIONS, mQuestionsList!!.size.toString())
                            startActivity(intent)
                        }
                    }
                } else {
                    val question = mQuestionsList!![mCurrentPosition - 1]
                    resetAnswers()
                    if (question.correctAnswer != mSelectedAnswer) {
                        displayAnswer(mSelectedAnswer, R.color.red)
                    }
                    else {
                        mCorrectAnswers ++
                    }
                    displayAnswer(question!!.correctAnswer, R.color.ColorPrimary)

                    if (mCurrentPosition == mQuestionsList!!.size){
                        btnSubmit.text = resources.getString(R.string.btn_finish_label)
                    } else {
                        btnSubmit.text = resources.getString(R.string.btn_next_question)
                    }

                    mSelectedAnswer = 0
                }
            }
        }
    }

    /**
     * Function to set all the answers so that none of them are selected.
     */
    private fun resetAnswers(){
        val tvAnswerOne:MYATextView = findViewById(R.id.tv_answer_one)
        val tvAnswerTwo:MYATextView = findViewById(R.id.tv_answer_two)
        val tvAnswerThree:MYATextView = findViewById(R.id.tv_answer_three)
        val tvAnswerFour:MYATextView = findViewById(R.id.tv_answer_four)

        val answers = ArrayList<MYATextView>()
        answers.add(tvAnswerOne)
        answers.add(tvAnswerTwo)
        answers.add(tvAnswerThree)
        answers.add(tvAnswerFour)

        for (answer in answers){
            answer.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.orange_button_background
                )
            )
            answer.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
        }

    }

    /**
     * Function to set the selected answer to a different color scheme
     * so that it is highlighted.
     */

    private fun selectedAnswer(answer:MYATextView, selectedNumber:Int){

        resetAnswers()

        mSelectedAnswer = selectedNumber
        answer.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.button_background_orange
            )
        )
        answer.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.orange
            )
        )

    }

    /**
     * Function to display the correct answer once
     * a user has submitted their response
     */
    private fun displayAnswer(answer: Int, drawable: Int){

        val tvAnswerOne:MYATextView = findViewById(R.id.tv_answer_one)
        val tvAnswerTwo:MYATextView = findViewById(R.id.tv_answer_two)
        val tvAnswerThree:MYATextView = findViewById(R.id.tv_answer_three)
        val tvAnswerFour:MYATextView = findViewById(R.id.tv_answer_four)



        when(answer){
            1->{
                tvAnswerOne.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        drawable
                    )
                )
            }

            2 ->{
                tvAnswerTwo.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        drawable
                    )
                )
            }
            3 ->{
                tvAnswerThree.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        drawable
                    )
                )
            }
            4 ->{
                tvAnswerFour.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        drawable
                    )
                )
            }
        }
    }


}