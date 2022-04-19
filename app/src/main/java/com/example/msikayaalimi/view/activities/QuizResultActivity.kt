package com.example.msikayaalimi.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.msikayaalimi.R
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYAButton
import com.example.msikayaalimi.controller.MYATextView

class QuizResultActivity : AppCompatActivity() {

    private var mQuizResult:String = "0"
    private var mTotalQuestions:String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_result)

        if (intent.hasExtra(Constants.EXTRA_QUIZ_RESULT)){
            mQuizResult = intent.getStringExtra(Constants.EXTRA_QUIZ_RESULT)!!
        }
        if (intent.hasExtra(Constants.EXTRA_TOTAL_QUIZ_QUESTIONS)){
            mTotalQuestions = intent.getStringExtra(Constants.EXTRA_TOTAL_QUIZ_QUESTIONS)!!
        }

        val tvScore:MYATextView = findViewById(R.id.tv_quiz_result_score)
        val btnFinish:MYAButton = findViewById(R.id.btn_completed_quiz_result)
        tvScore.text = "Your score is: " + mQuizResult + "/" + mTotalQuestions

        btnFinish.setOnClickListener{
            startActivity(Intent(this, MarketActivity::class.java))
            finish()
        }
    }
}