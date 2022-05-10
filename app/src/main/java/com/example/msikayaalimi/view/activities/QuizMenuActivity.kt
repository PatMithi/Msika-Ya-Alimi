package com.example.msikayaalimi.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.R

/**
 * Class to display the quiz menu in the training section
 */
class QuizMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_menu)

        setupActionBar()
    }

    private fun setupActionBar() {
        val toolbarQuizMenu = findViewById(R.id.quiz_menu_activity_toolbar) as Toolbar
        setSupportActionBar(toolbarQuizMenu)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbarQuizMenu.setNavigationOnClickListener{ onBackPressed()}
    }
}