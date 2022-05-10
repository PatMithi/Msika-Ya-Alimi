package com.example.msikayaalimi.view.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R

/**
 * Code to show the application logo while loading the application
 * Code adapted from online course
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            /**
             * Hides the notifications bar to display the application in full screen mode
             *
             */
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        @Suppress("DEPRECATION")
        /**
         *
         */
        Handler().postDelayed(
            {
                /**
                 * Checks if the user is still logged into the application,
                 * if they are they are redirected to the Welcome screen,
                 * otherwise they are sent to the login screen
                 */
                val userID = FirestoreClass().getCurrentUserID()

                if (userID.isNotEmpty()) {
                    val intent  = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                finish()
            },
            3000
        )
    }
}