package com.example.msikayaalimi.view.activities

import android.os.Bundle

import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ScrollView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.controller.MYAButton
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.MYATextViewBold
import com.example.msikayaalimi.controller.*
import com.facebook.shimmer.ShimmerFrameLayout
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
        val forgot: MYATextView = findViewById(R.id.tv_forgot_password)
        val login: MYAButton = findViewById(R.id.btn_login)
        val register: MYATextViewBold = findViewById(R.id.tv_register)
        forgot.setOnClickListener(this)
        login.setOnClickListener(this)
        register.setOnClickListener(this)

    }

    /**
     * Code below adapted from online courses
     */
    override fun onClick(v: View) {
        if (v != null) {
            when (v.id) {
                R.id.tv_register -> {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    loginUser()
                }

                R.id.tv_forgot_password -> {
                    val intent = Intent(this, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        val email = findViewById<MYAEditText>(R.id.et_email)
        val password = findViewById<MYAEditText>(R.id.et_password)
        return when {
            TextUtils.isEmpty(email.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_no_email_address), true)
                false
            }
            TextUtils.isEmpty(password.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_no_password_entered), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loginUser() {
        val email = findViewById<MYAEditText>(R.id.et_email)
        val password = findViewById<MYAEditText>(R.id.et_password)
        if (validateLoginDetails()) {
            val shimmerFrameLayout: ShimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_login_activity)
            shimmerFrameLayout.visibility = View.VISIBLE
            shimmerFrameLayout.startShimmerAnimation()
            val scrollView:ScrollView = findViewById(R.id.sv_login)
            scrollView.visibility = View.GONE


            //Get text from the editText and trim the space
            val email:String = email.text.toString().trim { it <=' '}
            val password: String = password.text.toString().trim {it <= ' '}

            // Function to handle the login process
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    /**
                     * If the user is successfully logged in their user details are
                     * retrieved to help determine which activity to display
                     */

                    if (task.isSuccessful) {
                        FirestoreClass().getUserDetails(this)
                    }else {

                        shimmerFrameLayout.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccessfully(user: User) {

        val shimmerFrameLayout: ShimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_login_activity)
        shimmerFrameLayout.visibility = View.GONE
        shimmerFrameLayout.stopShimmerAnimation()

        // Redirect the user to the appropriate Main screen depending on what type of user they are and completion of profile
        if (user.profileCompleted == 0) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(Constants.ADDITIONAL_USER_DETAILS, user)
            startActivity(intent)
//            startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
        }
        else if (user.profileCompleted == 1 && user.userType == "customer"){
            startActivity(Intent(this, MainActivity::class.java))
        }
        else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}