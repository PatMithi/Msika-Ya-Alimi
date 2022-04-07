package com.example.msikayaalimi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Address
import com.example.msikayaalimi.utils.*
import com.google.android.material.textfield.TextInputLayout

class UpdateAddressActivity : BaseActivity(), View.OnClickListener {

    private var mAddressDetails:Address? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_address_actvity)

        setupActionBar()
        val btnAddAddress:MYAButton = findViewById(R.id.btn_add_new_address)
        btnAddAddress.setOnClickListener(this)

        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }

        if (mAddressDetails != null) {
            if (mAddressDetails!!.id.isNotEmpty()) {

                val fullName: MYAEditText = findViewById(R.id.et_full_name)
                val etAddress: MYAEditText = findViewById(R.id.et_address)
                val etDistrict: MYAEditText = findViewById(R.id.et_district)
                val etAdditionalNotes: MYAEditText = findViewById(R.id.et_additional_notes)
                val etPhoneNumber:MYAEditText = findViewById(R.id.et_phone_number)
                val btnAddAddress:MYAButton = findViewById(R.id.btn_add_new_address)
                val title:MYATextViewBold = findViewById(R.id.tv_add_address_title)
                val rbOther:MYARadioButton = findViewById(R.id.rb_other)
                val rbHome:MYARadioButton = findViewById(R.id.rb_home)
                val rbOffice:MYARadioButton= findViewById(R.id.rb_office)

                btnAddAddress.text = resources.getString(R.string.update_btn_label)
                title.text = resources.getString(R.string.title_edit_address)

                fullName.setText(mAddressDetails?.fulName)
                etPhoneNumber.setText(mAddressDetails?.mobileNumber)
                etAddress.setText(mAddressDetails?.address)
                etDistrict.setText(mAddressDetails?.district)
                if (mAddressDetails?.additionalNote != "") {
                    etAdditionalNotes.setText(mAddressDetails?.additionalNote)
                }


                when (mAddressDetails?.type){

                    Constants.HOME ->{
                        rbHome.isChecked = true
                    }

                    Constants.OTHER ->{

                        rbOther.isChecked = true
                    }

                    Constants.OFFICE ->{

                        rbOffice.isChecked = true

                    }
                }

            }
        }
    }

    /*
    Inserting back button in the actionbar to
    allow a user to return to the previous activity
     */
    private fun setupActionBar() {
        val addressListToolbar: Toolbar = findViewById(R.id.toolbar_update_addresses_activity)

        setSupportActionBar(addressListToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        addressListToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateAddressDetails():Boolean {
        val fullName: MYAEditText = findViewById(R.id.et_full_name)
        val etAddress: MYAEditText = findViewById(R.id.et_address)
        val etDistrict: MYAEditText = findViewById(R.id.et_district)
        val etAdditionalNotes: MYAEditText = findViewById(R.id.et_additional_notes)
        val etPhoneNumber:MYAEditText = findViewById(R.id.et_phone_number)
        val rbOther:MYARadioButton = findViewById(R.id.rb_other)


        return when {
            TextUtils.isEmpty(fullName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_full_name),
                    true)
                false
            }
            TextUtils.isEmpty(etAddress.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_address),
                    true)
                false
            }

            TextUtils.isEmpty(etPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_phone_number),
                    true)
                false
            }

            TextUtils.isEmpty(etDistrict.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_district),
                    true)
                false
            }

            rbOther.isChecked && TextUtils.isEmpty(etDistrict.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_district),
                    true)
                false
            }
            else ->{
                true
            }
        }
    }

    private fun uploadAddressDetails() {
        val etfullName: MYAEditText = findViewById(R.id.et_full_name)
        val etAddress: MYAEditText = findViewById(R.id.et_address)
        val etDistrict: MYAEditText = findViewById(R.id.et_district)
        val etAdditionalNotes: MYAEditText = findViewById(R.id.et_additional_notes)
        val etPhoneNumber:MYAEditText = findViewById(R.id.et_phone_number)
        val rbOther:MYARadioButton = findViewById(R.id.rb_other)
        val rbHome:MYARadioButton = findViewById(R.id.rb_home)
        val rbOffice:MYARadioButton= findViewById(R.id.rb_office)

        val fullName = etfullName.text.toString().trim { it <= ' '}
        val address = etAddress.text.toString().trim { it <= ' '}
        val district = etDistrict.text.toString().trim { it <= ' '}
        val additioncalNotes = etAdditionalNotes.text.toString().trim { it <= ' '}
        val phoneNumber = etPhoneNumber.text.toString().trim { it <= ' '}

        if (validateAddressDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val addressType: String = when {
                rbHome.isChecked -> {
                    Constants.HOME
                }
                rbOffice.isChecked ->{
                    Constants.OFFICE
                }
                else ->{
                    Constants.OTHER
                }
            }

            val addressModel = Address(
                FirestoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                district,
                additioncalNotes,
                addressType

            )

            if(mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()){
                FirestoreClass().updateAddressDetails(this, addressModel, mAddressDetails!!.id)
            }
            else {
                FirestoreClass().uploadAddressDetails(this@UpdateAddressActivity, addressModel)
            }


        }

    }

    fun successfullyUploadedAddress(){
        hideProgressDialog()

        val successMessage: String = if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()){
            resources.getString(R.string.msg_successfully_updated_address)
        } else{
            resources.getString(R.string.msg_successfully_added_address)
        }

        Toast.makeText(
            this@UpdateAddressActivity,
            successMessage,
            Toast.LENGTH_SHORT
        ).show()
        setResult(RESULT_OK)
        finish()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id) {
                R.id.btn_add_new_address ->{
                    uploadAddressDetails()
                }
            }
    }

}
}