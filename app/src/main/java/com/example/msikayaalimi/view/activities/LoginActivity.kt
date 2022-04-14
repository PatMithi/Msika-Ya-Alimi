package com.example.msikayaalimi.view.activities

import android.os.Bundle

import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.controller.MYAButton
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.MYATextViewBold
import com.example.msikayaalimi.controller.*
import com.google.firebase.auth.FirebaseAuth

//import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val forgot: MYATextView = findViewById(R.id.ty_forgot_password)
        val login: MYAButton = findViewById(R.id.btn_login)
        val register: MYATextViewBold = findViewById(R.id.ty_register)
        forgot.setOnClickListener(this)
        login.setOnClickListener(this)
        register.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        if (v != null) {
            when (v.id) {
                R.id.ty_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)

                }
                R.id.btn_login -> {
                    loginRegisteredUser()
                }
                R.id.ty_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        val email = findViewById(R.id.et_email) as MYAEditText
        val password = findViewById(R.id.et_password) as MYAEditText
        return when {
            TextUtils.isEmpty(email.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(password.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loginRegisteredUser() {
        val email = findViewById<MYAEditText>(R.id.et_email)
        val password = findViewById<MYAEditText>(R.id.et_password)
        if (validateLoginDetails()) {

            //show progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            //Get text from the editText and trim the space
            val email:String = email.text.toString().trim { it <=' '}
            val password: String = password.text.toString().trim {it <= ' '}

            // Log-in using Firebase
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        FirestoreClass().getUserDetails(this@LoginActivity)
                    }else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccessfully(user: User) {


        // Hide progress dialog
        hideProgressDialog()

        // Redirect the user to the appropriate Main screen depending on what type of user they are and completion of profile
        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
            intent.putExtra(Constants.ADDITIONAL_USER_DETAILS, user)
            startActivity(intent)
//            startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
        }
        else if (user.profileCompleted == 1 && user.userType == "customer"){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        else {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }
}