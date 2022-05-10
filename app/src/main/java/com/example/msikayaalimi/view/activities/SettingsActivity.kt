package com.example.msikayaalimi.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYAButton
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.GlideLoader
import com.google.firebase.auth.FirebaseAuth

/**
 * Class to display the setting page and show the user's profile image and email
 * Code adapted from online course
 */
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
        val llAddress:LinearLayout = findViewById(R.id.ll_address)
        val llProfile:LinearLayout = findViewById(R.id.ll_profile)


        btnLogout.setOnClickListener(this)
        llAddress.setOnClickListener(this)
        llProfile.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.ll_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra(Constants.ADDITIONAL_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }

                R.id.btn_logout -> {

                    FirebaseAuth.getInstance().signOut()

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                R.id.ll_address -> {
                    startActivity(Intent(this, AddressListActivity::class.java))
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
        displayProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this)
    }

    /**
     * A function to receive the user details and populate it in the UI.
     */
    fun userDetailsSuccess(user: User) {
        val settingsImage: ImageView = findViewById(R.id.iv_user_photo)
        val tvName:MYATextView = findViewById(R.id.tv_name)

        mUserDetails = user

        // Hide the progress dialog
        dismissProgressDialog()

        // Load the image using the Glide Loader class.
        GlideLoader(this).loadUserImage(user.image, settingsImage)

        tvName.text = "${user.firstName} ${user.lastName}"

    }
}