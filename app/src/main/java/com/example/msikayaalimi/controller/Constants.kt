package com.example.msikayaalimi.controller

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    val READ_STORAGE_PERMISSION_CODE = 2


    // Collections in Firestore Database
    const val USERS: String = "users"
    const val PRODUCTS:String = "products"
    const val ORDERS:String = "orders"
    const val CART_ITEMS:String = "cart_items"
    const val ADDRESSES:String = "addresses"
    const val TRAINING_INFO:String = "training_info"
    const val TRAINING_MENU_ITEMS:String = "training_menu_items"
    const val WELCOME_IMAGES:String = "welcome_images"
    const val WELCOME_SLIDESHOW:String = "welcome_slideshow"


    // Constants for uploading and accessing user profile data from the Cloud database
    const val MSIKAYAALIMI_PREFERENCES: String = "MsikaYaAlimiPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val ADDITIONAL_USER_DETAILS: String = "additional_user_details"
    const val IMAGE_SELECTED_CODE = 1
    const val FIRST_NAME = "firstName"
    const val LAST_NAME = "lastName"
    const val MALE:String = "male"
    const val FEMALE:String = "female"
    const val  MOBILE:String = "mobile"
    const val GENDER:String = "gender"
    const val IMAGE:String = "image"

    // Constants to upload user type to Firebase
    const val FARMER:String = "farmer"
    const val CUSTOMER:String = "customer"


    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"

    // Constants used when profile has been completed
    const val COMPLETED_PROFILE:String = "profileCompleted"

    // constants for products page
    const val PRODUCT_IMAGE:String = "Product_Image"
    const val USER_ID:String = "user_id"

    // Constants for products details page
    const val EXTRA_PRODUCT_ID:String = "extra_product_id"

    // Constants to use when adding items to the cart
    const val DEFAULT_CART_QUANTITY:String = "1"
    const val PRODUCT_ID: String = "product_id"

    // Constant used to update the cart
    const val CART_QUANTITY:String = "cart_quantity"

    // Constants to get menuItems from Firestore
    const val TRAINING_ITEM:String = "training_menu"
    const val CATEGORY_ITEM = "category_item"
    const val QUIZ_MENU_ITEM:String = "quiz_menu_item"

    // Constants to add addresses
    const val HOME:String = "home"
    const val OFFICE:String = "office"
    const val OTHER:String = "other"

    //Constants for quiz page
    const val EXTRA_MENU_ITEM_ID = "extra_product_item_id"

    //Constant for category filter
    const val PRODUCT_CATEGORY:String = "product_category"
    const val EXTRA_FILTERED_ITEMS:String ="extra_filtered_items"
    const val ANIMALS:String= "Animals"
    const val EGGS_AND_DAIRY:String= "Eggs and Dairy"
    const val NUTS:String= "Nuts"
    const val VEGETABLES:String= "Vegetables"
    const val OTHER_CATEGORY:String ="Other"
    const val FRUITS:String="Fruits"

    //Constants to get quiz questions
    const val QUIZ_QUESTIONS:String = "quiz_questions"

    const val EXTRA_ADDRESS_DETAILS = "extra_address_details"
    const val EXTRA_SELECTED_ADDRESS:String = "extra_selected_address"
    const val ADD_ADDRESS_REQUEST_CODE:Int = 121
    const val EXTRA_SELECTED_ADDRESS_CHECKOUT:String = "extra_selected_address_checkout"
    const val EXTRA_SEARCH_VALUE:String = "extra_search_value"
    const val PRODUCTS_TITLE:String = "productTitle"
    const val PRODUCTS_PRICE:String = "productPrice"
    const val EXTRA_SORT_ORDER:String = "extra_sort_order"
    const val EXTRA_SORT_FIELD:String = "extra_sort_field"
    const val ASCENDING:String = "ascending"
    const val DESCENDING:String = "descending"
    const val EXTRA_PRODUCTS_EDIT = "extra_products_edit"
    const val TITLE:String = "title"
    const val TYPE:String = "type"
    const val DESCRIPTION:String = "productDescription"
    const val SPECIFICATION:String = "productSpecification"
    const val QUANTITY:String = "productQuantity"
    const val LOCATION:String = "productLocation"
    const val PRODUCT_TITLE:String = "productTitle"
    const val STOCK_QUANTITY:String = "stock_quantity"
    const val EXTRA_ORDER_DETAILS:String = "extra_order_details"
    const val SOLD_PRODUCTS:String = "sold_products"
    const val EXTRA_PRODUCT_OWNER_ID:String = "extra_product_owner_id"
    const val EXTRA_SOLD_PRODUCTS_DETAILS:String = "extra_sold_products"
    const val EXTRA_CREATOR_NAME:String = "extra_creator_name"
    const val QUESTIONS:String = "questions"
    const val QUIZ_NAME = "quizName"
    const val EXTRA_QUIZ_NAME:String = "extra_quiz_name"
    const val WELCOME_IMAGE:String = "welcome_image"
    const val FILTER_IMAGE:String = "filter_image"
    const val EXTRA_QUIZ_RESULT:String = "quiz_result"
    const val EXTRA_TOTAL_QUIZ_QUESTIONS:String = "extra_total_quiz_questions"



    fun showSelectedImage(activity: Activity) {

        // Intent to launch the image gallery
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        //Launches the image gallery of the phone storage using the constant code
        activity.startActivityForResult(galleryIntent, IMAGE_SELECTED_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
        MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice vers.

        getSingleton(): Get the singleton instance of the MimeType

        getExtensionFromType: Return the registered extension for the given MIME type

        contentResolver.getType: Return the MIME type of the given content URL
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}