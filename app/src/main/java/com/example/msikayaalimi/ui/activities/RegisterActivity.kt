package com.example.msikayaalimi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.MYAEditText
import com.example.msikayaalimi.utils.MYARadioButton
import com.example.msikayaalimi.utils.MYATextViewBold
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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
        val fstName= findViewById(R.id.et_first_name) as MYAEditText
        val lstName = findViewById(R.id.et_last_name) as MYAEditText
        val eMail = findViewById(R.id.et_email) as MYAEditText
        val fstPassword = findViewById(R.id.et_password) as MYAEditText
        val confirmPassword = findViewById(R.id.et_confirm_password) as MYAEditText
        val cbTerms = findViewById(R.id.cb_terms_and_conditions) as AppCompatCheckBox

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
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(fstPassword.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password), true)
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


    private fun registerUser() {
        val eMail = findViewById(R.id.et_email) as MYAEditText
        val fstPassword = findViewById(R.id.et_password) as MYAEditText
        val fstName= findViewById(R.id.et_first_name) as MYAEditText
        val lstName = findViewById(R.id.et_last_name) as MYAEditText
        val confirmPassword = findViewById(R.id.et_confirm_password) as MYAEditText
        val rbUserType:MYARadioButton = findViewById(R.id.rb_customer)
        val userType = if(rbUserType.isChecked) {
            Constants.CUSTOMER
        } else {
            Constants.FARMER
        }



        // check with the validation function whether entries are valid or not.
        if (validateRegistrationDetails()) {


            showProgressDialog(resources.getString(R.string.please_wait))

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
                            hideProgressDialog()
                            // If registrations is not successful
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )
        }
    }

    fun userRegistrationSuccess() {

        // Hide progress dialog
        hideProgressDialog()

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