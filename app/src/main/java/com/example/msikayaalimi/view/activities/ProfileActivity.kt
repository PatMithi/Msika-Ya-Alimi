package com.example.msikayaalimi.view.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.User
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.controller.*
import com.example.msikayaalimi.controller.GlideLoader
import java.io.IOException

class ProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User
    private var mSelectedImageUri: Uri? = null // link on device
    private var mProfilePhotoUrl:String = "" // link on the browser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        if(intent.hasExtra(Constants.ADDITIONAL_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.ADDITIONAL_USER_DETAILS)!!
        }

        val tvTitle:MYATextViewBold = findViewById(R.id.tv_title_profile)
        val etFirstName:MYAEditText = findViewById(R.id.et_first_name)
        val etLastName:MYAEditText = findViewById(R.id.et_last_name)
        val etEmail:MYAEditText = findViewById(R.id.et_email)
        val ivUserPhoto:ImageView = findViewById(R.id.iv_user_photo)
        val etMobileNumber:MYAEditText = findViewById(R.id.et_mobile_number)
        val rbMale:MYARadioButton = findViewById(R.id.rb_male)
        val rbFemale:MYARadioButton = findViewById(R.id.rb_female)
        val btnSave:MYAButton = findViewById(R.id.btn_submit)

        if(mUserDetails.profileCompleted == 0){

            tvTitle.text = resources.getString(R.string.complete_profile_title)

            etFirstName.isEnabled = false
            etFirstName.setText(mUserDetails.firstName)

            etEmail.isEnabled = false
            etEmail.setText(mUserDetails.email)

            etLastName.isEnabled = false
            etLastName.setText(mUserDetails.lastName)
        } else{

            setupActionBar()

            tvTitle.text = resources.getString(R.string.update_profile_title)

            GlideLoader(this).loadUserImage(mUserDetails.image, ivUserPhoto)

            etFirstName.isEnabled = false
            etFirstName.setText(mUserDetails.firstName)

            etEmail.isEnabled = false
            etEmail.setText(mUserDetails.email)

            etLastName.isEnabled = false
            etLastName.setText(mUserDetails.lastName)

            if (mUserDetails.mobile != 0L){
                etMobileNumber.setText(mUserDetails.mobile.toString())
            }

            if (mUserDetails.gender == Constants.MALE) {
                rbMale.isChecked = true
            } else {
                rbFemale.isChecked = true
            }



        }

        ivUserPhoto.setOnClickListener(this@ProfileActivity)
        btnSave.setOnClickListener(this@ProfileActivity)

    }

    private fun setupActionBar() {
        val profileToolbar:Toolbar = findViewById(R.id.toolbar_profile_activity)

        setSupportActionBar(profileToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        profileToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_photo -> {
                    // Checks user permissions to determine whether we can access photos or not
                    // The first check will be done on the READ_EXTERNAL_STORAGE permission
                    if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showSelectedImage(this)
                    } else {
                        /*
                        Code below is used to request permission to access the external storage.
                         */

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {
                    if (validateUserProfileDetails()) {

                        showProgressDialog(
                            resources.getString(R.string.please_wait)
                        )

                        // uploads image url if the image url is not empty
                        if (mSelectedImageUri != null) {

                            FirestoreClass().uploadImageToCloudStorage(
                                this@ProfileActivity,
                                mSelectedImageUri,
                                Constants.USER_PROFILE_IMAGE)
                        }
                        else {
                            updateProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateProfileDetails() {
        val etMobileNumber:MYAEditText = findViewById(R.id.et_mobile_number)
        val rbMale:MYARadioButton = findViewById(R.id.rb_male)
            val userHashMap = HashMap<String, Any>()
        val mobileNumber = etMobileNumber.text.toString().trim { it <= ' '}

        if(mProfilePhotoUrl.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mProfilePhotoUrl
        }

        val gender = if(rbMale.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }
        // key:gender value: male
        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETED_PROFILE] = 1


        FirestoreClass().updateUserProfileDate(this, userHashMap)
    }

    fun userProfileUpdatedSuccessfully() {
        hideProgressDialog()

        Toast.makeText(
            this@ProfileActivity,
            resources.getString(R.string.msg_profile_successfully_updated),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@ProfileActivity, MarketActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {

            //Action taken if permission is granted
            if (grantResults.isNotEmpty() && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showSelectedImage(this)
            } else {

                // Message to be displayed if access not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.denied_storage_access),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val ivUserPhoto:ImageView = findViewById(R.id.iv_user_photo)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.IMAGE_SELECTED_CODE) {
                if (data != null) {
                    try {
                        // URI of selected image
                        mSelectedImageUri = data.data!!

                        GlideLoader(this).loadUserImage(mSelectedImageUri!!, ivUserPhoto)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@ProfileActivity,
                            resources.getString(R.string.image_upload_failure),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
            else if (requestCode == Activity.RESULT_CANCELED) {
                Log.i("Error:", "You have cancelled your profile photo upload")
            }
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        val etMobileNumber:MYAEditText = findViewById(R.id.et_mobile_number)
        return when {
            TextUtils.isEmpty(etMobileNumber.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageSuccessfullyUploaded(imageURL: String) {
        mProfilePhotoUrl = imageURL
        updateProfileDetails()
    }
}