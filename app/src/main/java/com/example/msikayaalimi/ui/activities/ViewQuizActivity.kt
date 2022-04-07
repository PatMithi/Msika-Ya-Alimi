package com.example.msikayaalimi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.QuizQuestions

class ViewQuizActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_quiz)

        setupActionBar()


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
}