package com.example.msikayaalimi.view.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.R
import com.example.msikayaalimi.controller.MYAButton
import com.example.msikayaalimi.controller.MYAEditText
import com.google.firebase.auth.FirebaseAuth

/**
 * Class used to display options a user can take when they have forgotten their password
 * Code adapted from online course
 */
class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()
    }

    private fun setupActionBar() {
        val toolbarForgotPassword = findViewById<Toolbar>(R.id.toolbar_forgot_password_activity)
        setSupportActionBar(toolbarForgotPassword)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbarForgotPassword.setNavigationOnClickListener{ onBackPressed()}

        val btnSubmit: MYAButton = findViewById(R.id.btn_submit)

        btnSubmit.setOnClickListener{
            val email = findViewById<MYAEditText>(R.id.et_email_forgotten)


            if (email.text.toString().trim { it <= ' '}.isEmpty())  {
                showErrorSnackBar(resources.getString(R.string.err_msg_no_email_address),true)
            }else{
                displayProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.text.toString().trim { it <= ' '})
                    .addOnCompleteListener{task ->
                        dismissProgressDialog()

                        if (task.isSuccessful) {
                            // Show the toast message and finish the forgot password activity to return to login screen
                            Toast.makeText(
                                this,
                                resources.getString(R.string.email_successfully_sent),
                                Toast.LENGTH_LONG
                            ).show()

                            // closes forgot password activity once completed
                            finish()
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
            }
        }
    }
}