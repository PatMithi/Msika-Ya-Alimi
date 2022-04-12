package com.example.msikayaalimi.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Address
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.utils.*
import java.io.IOException

/*
    Add products activity to allow users to add their products to the
    market activity.

 */

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedImageUri: Uri? = null
    private var mProductImageUrl: String = ""
    private var mProductDetails: Product? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionBar()

        val ivAddProductImage:ImageView = findViewById(R.id.iv_add_update_product)
        val btnSubmit:MYAButton = findViewById(R.id.btn_submit)
        val tvTitle:MYATextViewBold = findViewById(R.id.tv_add_products_title)
        val etProductTitle:MYAEditText = findViewById(R.id.et_product_title)
        val etProductPrice: MYAEditText = findViewById(R.id.et_product_price)
        val etProductDescription:MYAEditText = findViewById(R.id.et_product_description)
        val etProductSpecification:MYAEditText = findViewById(R.id.et_product_specification)
        val etProductLocation:MYAEditText = findViewById(R.id.et_product_location)
        val etProductQuantity:MYAEditText = findViewById(R.id.et_product_quantity)
        val rbAnimals:MYARadioButton = findViewById(R.id.rb_animals)
        val rbFruits:MYARadioButton = findViewById(R.id.rb_fruits)
        val rbVeg:MYARadioButton = findViewById(R.id.rb_vegetables)
        val rbEggs:MYARadioButton = findViewById(R.id.rb_eggs_and_dairy)
        val rbOther:MYARadioButton = findViewById(R.id.rb_other_category)
        val rbNuts:MYARadioButton = findViewById(R.id.rb_nuts)
        ivAddProductImage.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)

        if (intent.hasExtra(Constants.EXTRA_PRODUCTS_EDIT)){
            mProductDetails = intent.getParcelableExtra(Constants.EXTRA_PRODUCTS_EDIT)
        }

        if (mProductDetails != null){
            GlideLoader(this).loadProductImage(mProductDetails!!.productImage, ivAddProductImage)

            tvTitle.setText(mProductDetails!!.productTitle)
            etProductTitle.setText(mProductDetails!!.productTitle)
            etProductPrice.setText(mProductDetails!!.productPrice)
            etProductDescription.setText(mProductDetails!!.productDescription)
            etProductLocation.setText(mProductDetails!!.productLocation)
            etProductQuantity.setText(mProductDetails!!.productQuantity)
            etProductSpecification.setText(mProductDetails!!.productSpecification)

            if (mProductDetails!!.product_category == Constants.ANIMALS){
                rbAnimals.isChecked = true
            }
            else if (mProductDetails!!.product_category == Constants.NUTS){
                rbNuts.isChecked = true
            }
            else if (mProductDetails!!.product_category == Constants.OTHER_CATEGORY){
                rbOther.isChecked = true
            }
            else if (mProductDetails!!.product_category == Constants.FRUITS){
                rbFruits.isChecked = true
            }
            else if (mProductDetails!!.product_category == Constants.VEGETABLES){
                rbVeg.isChecked = true
            }
            else {
                rbEggs.isChecked = true
            }

            btnSubmit.text = resources.getString(R.string.update_btn_label)

        }




    }

    private fun setupActionBar(){
        val addProductsToolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.add_product_activity_toolbar)
        setSupportActionBar(addProductsToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        addProductsToolbar.setNavigationOnClickListener{onBackPressed()}
    }

    override fun onClick(v: View?) {
        if (v!= null) {
            when(v.id) {
                R.id.iv_add_update_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showSelectedImage(this@AddProductActivity)
                    } else {
                        /* Request permission to be granted to the application. These permissions
                        must be requested in the manifest. User may not have uploaded profile photo
                        so it is essential to ask for permissions again
                         */
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit ->{
                    if (mProductDetails != null){
                        updateProduct()
                    }

                    else if (validateProductDetails()){
                        uploadProductImage()
                    }
                }
            }
        }
    }

    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageUri, Constants.PRODUCT_IMAGE)
    }

    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@AddProductActivity,
            resources.getString(R.string.product_Successfully_uploaded),
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    fun imageSuccessfullyUploaded(imageURL: String) {
//        hideProgressDialog()
//        showErrorSnackBar("Product image has been successfully uploaded. Image URL: $imageURL", false)
        mProductImageUrl = imageURL

        uploadProductDetails()

    }

    private fun uploadProductDetails() {
        val etProductTitle:MYAEditText = findViewById(R.id.et_product_title)
        val etProductPrice: MYAEditText = findViewById(R.id.et_product_price)
        val etProductDescription:MYAEditText = findViewById(R.id.et_product_description)
        val etProductSpecification:MYAEditText = findViewById(R.id.et_product_specification)
        val etProductLocation:MYAEditText = findViewById(R.id.et_product_location)
        val etProductQuantity:MYAEditText = findViewById(R.id.et_product_quantity)
        val rbAnimals:MYARadioButton = findViewById(R.id.rb_animals)
        val rbFruits:MYARadioButton = findViewById(R.id.rb_fruits)
        val rbVeg:MYARadioButton = findViewById(R.id.rb_vegetables)
        val rbEggs:MYARadioButton = findViewById(R.id.rb_eggs_and_dairy)
        val rbOther:MYARadioButton = findViewById(R.id.rb_other_category)
        val rbNuts:MYARadioButton = findViewById(R.id.rb_nuts)

        val category = if(rbAnimals.isChecked){
            Constants.ANIMALS
        }else if (rbEggs.isChecked){
            Constants.EGGS_AND_DAIRY
        }else if (rbFruits.isChecked){
            Constants.FRUITS
        }else if (rbNuts.isChecked){
            Constants.NUTS
        }else if (rbEggs.isChecked){
            Constants.EGGS_AND_DAIRY
        }else if (rbVeg.isChecked){
            Constants.VEGETABLES
        }
        else{
            Constants.OTHER_CATEGORY
        }

        val username = this.getSharedPreferences(
            Constants.MSIKAYAALIMI_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME,"")!!

        val product = Product(
            FirestoreClass().getCurrentUserID(),
            username,
            etProductTitle.text.toString().trim { it <= ' '},
            etProductPrice.text.toString().trim { it <= ' '},
            etProductDescription.text.toString().trim { it <= ' '},
            etProductSpecification.text.toString().trim { it <= ' '},
            etProductLocation.text.toString().trim { it <= ' '},
            etProductQuantity.text.toString().trim { it <= ' '},
            mProductImageUrl,
            category
        )

        FirestoreClass().uploadProductDetails(this, product)
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
        val ivAddProductImage:ImageView = findViewById(R.id.iv_add_update_product)
        val ivProductImage:ImageView = findViewById(R.id.iv_product_photo)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.IMAGE_SELECTED_CODE) {
                if (data != null) {
                    if (data != null) {
                        ivAddProductImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_24))

                        mSelectedImageUri = data.data!!

                        try {
                            GlideLoader(this).loadUserImage(mSelectedImageUri!!, ivProductImage)
                        }catch (e:IOException){
                            e.printStackTrace()

                        }                        }
                }
            }
            else if (requestCode == Activity.RESULT_CANCELED) {
                Log.i("Error:", "You have cancelled your profile photo upload")
            }
        }
    }

    private fun validateProductDetails(): Boolean {
        val etProductTitle:MYAEditText = findViewById(R.id.et_product_title)
        val etProductPrice: MYAEditText = findViewById(R.id.et_product_price)
        val etProductDescription:MYAEditText = findViewById(R.id.et_product_description)
        val etProductSpecification:MYAEditText = findViewById(R.id.et_product_specification)
        val etProductLocation:MYAEditText = findViewById(R.id.et_product_location)
        val etProductQuantity:MYAEditText = findViewById(R.id.et_product_quantity)
        val rbAnimals:MYARadioButton = findViewById(R.id.rb_animals)
        val rbFruits:MYARadioButton = findViewById(R.id.rb_fruits)
        val rbVeg:MYARadioButton = findViewById(R.id.rb_vegetables)
        val rbEggs:MYARadioButton = findViewById(R.id.rb_eggs_and_dairy)
        val rbOther:MYARadioButton = findViewById(R.id.rb_other_category)
        val rbNuts:MYARadioButton = findViewById(R.id.rb_nuts)
        return when {
            mSelectedImageUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
                false
            }

            TextUtils.isEmpty(etProductTitle.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_title_prompt), true)
                false
            }

            TextUtils.isEmpty(etProductPrice.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_price_prompt), true)
                false
            }

            TextUtils.isEmpty(etProductDescription.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_description_prompt), true)
                false
            }

            TextUtils.isEmpty(etProductSpecification.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_specification_prompt), true)
                false
            }

            TextUtils.isEmpty(etProductLocation.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_location_prompt), true)
                false
            }

            TextUtils.isEmpty(etProductQuantity.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_product_quantity_prompt), true)
                false
            }
            !rbAnimals.isChecked&&!rbEggs.isChecked&&!rbFruits.isChecked&&!rbNuts.isChecked&&!rbOther.isChecked&&!rbVeg.isChecked->{
                showErrorSnackBar(resources.getString(R.string.err_msg_please_select_a_category),true)
                false
            }
            else -> {
                true
            }

        }
    }

    fun updateProduct(){
        val etProductTitle:MYAEditText = findViewById(R.id.et_product_title)
        val etProductPrice: MYAEditText = findViewById(R.id.et_product_price)
        val etProductDescription:MYAEditText = findViewById(R.id.et_product_description)
        val etProductSpecification:MYAEditText = findViewById(R.id.et_product_specification)
        val etProductLocation:MYAEditText = findViewById(R.id.et_product_location)
        val etProductQuantity:MYAEditText = findViewById(R.id.et_product_quantity)
        val rbAnimals:MYARadioButton = findViewById(R.id.rb_animals)
        val rbFruits:MYARadioButton = findViewById(R.id.rb_fruits)
        val rbVeg:MYARadioButton = findViewById(R.id.rb_vegetables)
        val rbEggs:MYARadioButton = findViewById(R.id.rb_eggs_and_dairy)
        val rbOther:MYARadioButton = findViewById(R.id.rb_other_category)
        val rbNuts:MYARadioButton = findViewById(R.id.rb_nuts)
        val productHashMap = HashMap<String, Any>()

        if(mProductImageUrl.isNotEmpty()) {
            productHashMap[Constants.IMAGE] = mProductDetails!!.productImage
        }

        if(etProductTitle.text.toString().trim { it <= ' '}.isNotEmpty()) {
            productHashMap[Constants.PRODUCTS_TITLE] = etProductTitle.text.toString().trim{ it <= ' '}
        }

        if(etProductDescription.text.toString().trim { it <= ' '}.isNotEmpty()) {
            productHashMap[Constants.DESCRIPTION] = etProductDescription.text.toString().trim{ it <= ' '}
        }

        if(etProductPrice.text.toString().trim { it <= ' '}.isNotEmpty()) {
            productHashMap[Constants.PRODUCTS_PRICE] = etProductPrice.text.toString().trim{ it <= ' '}
        }
        if(etProductSpecification.text.toString().trim { it <= ' '}.isNotEmpty()) {
            productHashMap[Constants.SPECIFICATION] = etProductSpecification.text.toString().trim{ it <= ' '}
        }

        if(etProductQuantity.text.toString().trim { it <= ' '}.isNotEmpty()) {
            productHashMap[Constants.QUANTITY] = etProductQuantity.text.toString().trim{ it <= ' '}
        }

        if(etProductLocation.text.toString().trim { it <= ' '}.isNotEmpty()) {
            productHashMap[Constants.LOCATION] = etProductLocation.text.toString().trim{ it <= ' '}
        }

        val category = if (rbAnimals.isChecked){
            Constants.ANIMALS
        } else if (rbEggs.isChecked){
            Constants.EGGS_AND_DAIRY
        } else if (rbFruits.isChecked){
            Constants.FRUITS
        } else if (rbNuts.isChecked){
            Constants.NUTS
        } else if (rbOther.isChecked){
            Constants.OTHER_CATEGORY
        }else{
            Constants.VEGETABLES
        }

        productHashMap[Constants.PRODUCT_CATEGORY] = category

        FirestoreClass().updateProductInfo(this, mProductDetails!!.product_id, productHashMap)
        showProgressDialog(resources.getString(R.string.please_wait))


    }

    fun successfullyUpdatedProductInfo(){
        hideProgressDialog()

        Toast.makeText(
            this,
            resources.getString(R.string.msg_successfully_updated_product),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this, MarketActivity::class.java))
        finish()
    }
}