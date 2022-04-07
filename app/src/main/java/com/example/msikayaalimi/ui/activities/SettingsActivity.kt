package com.example.msikayaalimi.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.MYAButton
import com.example.msikayaalimi.utils.MYATextView
import com.example.msikayaalimi.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity(), View.OnClickListener {

    // A variable for user details which will be initialized later on.
    private lateinit var mUserDetails: User

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_settings)

        setupActionBar()
        getUserDetails()

        val btnLogout:MYAButton = findViewById(R.id.btn_logout)
        val tvEdit:MYATextView = findViewById(R.id.tv_edit)
        val llAddress:LinearLayout = findViewById(R.id.ll_address)


        tvEdit.setOnClickListener(this@SettingsActivity)
        btnLogout.setOnClickListener(this@SettingsActivity)
        llAddress.setOnClickListener(this@SettingsActivity)
    }

//    override fun onResume() {
//        super.onResume()
//
//        getUserDetails()
//    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.tv_edit -> {
                    val intent = Intent(this@SettingsActivity, ProfileActivity::class.java)
                    intent.putExtra(Constants.ADDITIONAL_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }

                R.id.btn_logout -> {

                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                R.id.ll_address -> {
                    startActivity(Intent(this@SettingsActivity, AddressListActivity::class.java))
                }
            }
        }
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        val settingsToolbar:Toolbar = findViewById(R.id.settings_activity_toolbar)

        setSupportActionBar(settingsToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        settingsToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to get the user details from firestore.
     */
    private fun getUserDetails() {

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@SettingsActivity)
    }

    /**
     * A function to receive the user details and populate it in the UI.
     */
    fun userDetailsSuccess(user: User) {
        val settingsImage: ImageView = findViewById(R.id.iv_user_photo)
        val tvName:MYATextView = findViewById(R.id.tv_name)
        val tvEmail: MYATextView = findViewById(R.id.tv_email)
        val tvGender: MYATextView = findViewById(R.id.tv_gender)
        val tvMobileNumber:MYATextView = findViewById(R.id.tv_mobile_number)

        mUserDetails = user

        // Hide the progress dialog
        hideProgressDialog()

        // Load the image using the Glide Loader class.
        GlideLoader(this@SettingsActivity).loadUserImage(user.image, settingsImage)

        tvName.text = "${user.firstName} ${user.lastName}"
        tvGender.text = user.gender
        tvEmail.text = user.email
        tvMobileNumber.text = "${user.mobile}"
    }
}