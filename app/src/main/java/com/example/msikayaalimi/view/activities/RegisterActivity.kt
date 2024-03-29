package com.example.msikayaalimi.view.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYAEditText
import com.example.msikayaalimi.controller.MYARadioButton
import com.example.msikayaalimi.controller.MYATextViewBold
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Class to register user to the application
 * Code adapted from online course
 */
class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupActionBar()

        val loginScreen : MYATextViewBold = findViewById(R.id.tv_login)

        loginScreen.setOnClickListener {
            // Launch register screen when the user clicks on Register text
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        val btnRegister:Button = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener{
            registerUser()
        }
    }

    /**
     * Code to display the toolbar and add the activity title and back button
     */
    private fun setupActionBar() {
        val toolbarRegister = findViewById<Toolbar>(R.id.toolbar_register_activity)
        setSupportActionBar(toolbarRegister)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbarRegister.setNavigationOnClickListener{ onBackPressed()}
    }

    /**
     * Validating user entries in the registration form
     */

    private fun validateRegistrationDetails(): Boolean {
        val fstName= findViewById<MYAEditText>(R.id.et_first_name)
        val lstName = findViewById<MYAEditText>(R.id.et_last_name)
        val eMail = findViewById<MYAEditText>(R.id.et_email)
        val fstPassword = findViewById<MYAEditText>(R.id.et_password)
        val confirmPassword = findViewById<MYAEditText>(R.id.et_confirm_password)
        val cbTerms = findViewById<AppCompatCheckBox>(R.id.cb_terms_and_conditions)

        return when {
            TextUtils.isEmpty(fstName.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(lstName.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(eMail.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_no_email_address), true)
                false
            }
            TextUtils.isEmpty(fstPassword.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.error_msg_no_password_entered), true)
                false
            }
            TextUtils.isEmpty(confirmPassword.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_confirm_empty), true)
                false
            }

            fstPassword.text.toString().trim { it <= ' '}!= confirmPassword.text.toString().trim { it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_mismatch), true)
                false
            }
            !cbTerms.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_terms_and_conditions), true)
                false
            }
            else -> {
                //showErrorSnackBar(getString(R.string.successful_registration), false)
                true
            }
        }
    }

    /**
     * Function to verify the user then create their account upon successful validation
     */
    private fun registerUser() {
        val eMail = findViewById<MYAEditText>(R.id.et_email)
        val fstPassword = findViewById<MYAEditText>(R.id.et_password)
        val fstName= findViewById<MYAEditText>(R.id.et_first_name)
        val lstName = findViewById<MYAEditText>(R.id.et_last_name)
        val confirmPassword = findViewById<MYAEditText>(R.id.et_confirm_password)
        val rbUserType:MYARadioButton = findViewById(R.id.rb_customer)
        val userType = if(rbUserType.isChecked) {
            Constants.CUSTOMER
        } else {
            Constants.FARMER
        }



        // check with the validation function whether entries are valid or not.
        if (validateRegistrationDetails()) {


            displayProgressDialog(resources.getString(R.string.please_wait))

            val email:String = eMail.text.toString().trim { it <=' '}
            val password: String = fstPassword.text.toString().trim {it <= ' '}

            // Create and instance and register a user with their email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If successfully done
                        if (task.isSuccessful) {

                            //Firebase register user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val user = User(
                                firebaseUser.uid,
                                fstName.text.toString().trim { it <=' '},
                                lstName.text.toString().trim { it <=' '},
                                email,
                                userType
                            )

                            // call to store the information above into the firebase database
                            FirestoreClass().registerUser(this@RegisterActivity, user)

//                            FirebaseAuth.getInstance().signOut()
//                            finish()

                        }else {
                            dismissProgressDialog()
                            // If registrations is not successful
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )
        }
    }

    fun userRegistrationSuccess() {

        // Hide progress dialog
        dismissProgressDialog()

        // Log user out and return to the welcome screen
        FirebaseAuth.getInstance().signOut()
        finish()

        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()

    }
}