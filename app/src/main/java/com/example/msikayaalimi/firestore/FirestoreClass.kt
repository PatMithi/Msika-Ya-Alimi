package com.example.msikayaalimi.Firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import com.example.msikayaalimi.models.*
import com.example.msikayaalimi.ui.activities.*
import com.example.msikayaalimi.ui.adapters.TrainingMenuAdapter
import com.example.msikayaalimi.ui.fragments.*
import com.example.msikayaalimi.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.acos
import kotlin.math.sign

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // Users is collection name. If the collection is already created then it will not recreate the same one
        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)

            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering user",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser usingFirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserID if it is not null or els it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }


    fun getUserDetails(context: Context) {

        // Here we pass the collection name from which we wants the data.
        mFirestore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(context.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    context.getSharedPreferences(
                        Constants.MSIKAYAALIMI_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                // Create an instance of the editor which is help us to edit the SharedPreference.
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when (context) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        context.userLoggedInSuccessfully(user)
                    }

                    is SettingsActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        context.userDetailsSuccess(user)
                    }

                    is MarketActivity -> {
                        context.successfullyLoadedUserDetails(user)
                    }
                    is CheckoutActivity -> {
                        context.successfullyLoadedUserDetails(user)
                    }

                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (context) {
                    is LoginActivity -> {
                        context.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        context.hideProgressDialog()
                    }
                }

                Log.e(
                    context.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun getUserDetailsUserType(fragment: Fragment) {

        // Here we pass the collection name from which we wants the data.
        mFirestore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(fragment.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!


                when (fragment) {
                    is ProductsFragment -> {
                        // Call a function of base activity for transferring the result to it.
                        fragment.successfullyLoadedUserDetails(user)
                    }
                    is TrainingFragment -> {
                        GlobalScope.launch(Dispatchers.Main) {
                            fragment.successfullyLoadedUserDetails(user)
                        }

                    }

                    is OrdersFragment -> {
                        fragment.successfullyLoadedUserDetails(user)
                    }

                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.hideProgressDialog()
                    }
                }

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }


    // function to update user details depending on which activity its called from
    fun updateUserProfileDate(activity: Activity, userHashMap: HashMap<String, Any>) {

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is ProfileActivity -> {
                        activity.userProfileUpdatedSuccessfully()

                    }
                }


            }
            .addOnFailureListener { e ->
                when (activity) {
                    is ProfileActivity -> {
                        // Hide progress dialog
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName, "Error while updating the user details")
            }


    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType:String) {
        val storage = Firebase.storage("gs://msikayaalimi.appspot.com")
        var sRef = storage.reference

        //getting the storage reference
        val storageRef = sRef.child(
            "images/" + imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        storageRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.i(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.i("Downloadable Image URL", uri.toString())

                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is ProfileActivity -> {
                                activity.imageSuccessfullyUploaded(uri.toString())
                            }

                            is AddProductActivity -> {
                                activity.imageSuccessfullyUploaded(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is ProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProductActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product) {
        // creates or accesses the products collection
        mFirestore.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading product details!",
                    e
                )
            }
    }

    fun getListOfProducts (fragment: Fragment) {
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i("Products list", document.documents.toString())

                val productsList:ArrayList<Product> = ArrayList()

                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when(fragment){
                    is ProductsFragment -> {
                        GlobalScope.launch(Dispatchers.Main) {
                            fragment.gotProductsListSuccessfully(productsList)
                        }

                    }
                }
            }
    }

    fun getMarketItems(fragment: DashboardFragment){
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.documents.toString())

                GlobalScope.launch {

                }

                val listOfProducts: ArrayList<Product> = ArrayList()

                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    listOfProducts.add(product)
                }

                when(fragment){
                    is DashboardFragment -> {
                        GlobalScope.launch(Dispatchers.Main) {
                            fragment.marketItemsSuccessfullyLoaded(listOfProducts)
                        }
                    }
                }
            }
            .addOnFailureListener{
                e ->
                // hide progress dialog
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while loading market items!", e)
            }
    }

    fun deleteProduct(fragment: ProductsFragment, productID:String) {
        mFirestore.collection(Constants.PRODUCTS)
            .document(productID)
            .delete()
            .addOnSuccessListener {
                fragment.deletedProductSuccessfully()
            }
            .addOnFailureListener {
                e ->

                fragment.hideProgressDialog()

                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product",
                    e
                )
            }
    }

    fun getProductDetails(activity: ViewProductDetailsActivity, productID: String) {
        mFirestore.collection(Constants.PRODUCTS)
            .document(productID)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    activity.productsSuccessfullyLoaded(product)
                }

            }
            .addOnFailureListener{
                    e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while loading the product details",
                    e
                )

            }
    }

    fun addCartItems(activity:ViewProductDetailsActivity, addToCart: CartItem) {

        mFirestore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.successfullyAddedItemToCart()
            }
            .addOnFailureListener{
                e->
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating the document for the cart item",
                    e
                )
            }
    }

    // function to check if a specific item exists in the cart
    fun checkIfItemInCart(activity: ViewProductDetailsActivity, productID: String) {

        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID, productID)
            .get()
            .addOnSuccessListener { document ->
                   Log.i(activity.javaClass.simpleName, document.documents.toString())
                    if(document.documents.size > 0) {
                        activity.productInCart()
                    }else{
                        activity.hideProgressDialog()
                    }
            }
            .addOnFailureListener{
                e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking if the item is in thr cart",
                    e
                )
            }
    }

    // function to retrieve the items in the cart
    fun getCartList(activity: Activity) {
        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<CartItem> = ArrayList()

                for (i in document.documents) {

                    val cartItem = i.toObject(CartItem::class.java)!!
                    cartItem.id = i.id

                    list.add(cartItem)
                }

                when(activity){
                    is MyCartActivity -> {
                        activity.successfullyLoadedCartList(list)
                    }
                    is CheckoutActivity ->{
                        activity.successfullyLoadedCartList(list)
                    }
                }

            }

            .addOnFailureListener{
                e->

                when (activity){
                    is MyCartActivity ->{
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity ->{
                        activity.hideProgressDialog()
                    }
                }

                Log.e(activity.javaClass.simpleName, "Error while loading cart items", e)

            }
    }

    /*
    Function to get all the products available in the market,
    function is going to be used by the cart to help with the checkout process
     */
    fun getListOfAllProducts(activity: Activity) {
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->

                Log.i("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }
                when (activity){
                    is MyCartActivity ->{
                        activity.successfullyLoadedProductsList(productsList)
                    }

                    is CheckoutActivity ->{
                        activity.successfullyLoadedProductsList(productsList)
                    }
                }

            }
            .addOnFailureListener {
                e->

                when(activity){
                    is MyCartActivity ->{
                        activity.hideProgressDialog()
                    }

                    is CheckoutActivity ->{
                        activity.hideProgressDialog()
                    }
                }



                Log.e(activity.javaClass.simpleName, "Error while loading the list of products", e)

            }
    }

    fun deleteItemFromCart(context: Context, cartId: String) {
        mFirestore.collection(Constants.CART_ITEMS)
            .document(cartId)
            .delete()
            .addOnSuccessListener {
                when(context){
                    is MyCartActivity -> {
                        context.successfullyDeletedItemFromCart()
                    }
                }
            }
            .addOnFailureListener {
                    e ->
                when (context) {
                    is MyCartActivity -> {
                        context.hideProgressDialog()
                    }
                }


                Log.e(context.javaClass.simpleName, "Error while deleting product from cart",
                    e
                )
            }
    }

    /*
    Function to update the cart items to allow users
    to increase and decrease the number of items in the cart
     */
    fun updateCartItems(context: Context, cartId: String, itemHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.CART_ITEMS)
            .document(cartId)
            .update(itemHashMap)
            .addOnSuccessListener {
                when (context) {
                    is MyCartActivity -> {
                        context.successfullyUpdatedCart()
                    }
                }
            }
            .addOnFailureListener {
                e ->

                Log.e(context.javaClass.simpleName,
                    "Error while updating cart!",
                    e
                )
            }
    }

    fun getMenuItems(fragment: Fragment, menuType:String) {
        mFirestore.collection(Constants.TRAINING_MENU_ITEMS)
            .whereEqualTo("type", menuType)
            .get()
            .addOnSuccessListener { document ->
                Log.i("Products list", document.documents.toString())

                val menuItemList:ArrayList<TrainingMenuItem> = ArrayList()

                for (i in document.documents) {

                    val menuItem = i.toObject(TrainingMenuItem::class.java)
                    menuItem!!.id = i.id

                    menuItemList.add(menuItem)
                }

                when(fragment){
                    is TrainingFragment -> {
                        GlobalScope.launch(Dispatchers.Main) {
                            fragment.successfullyLoadedMenuItems(menuItemList)
                        }

                    }
                }

                when(fragment){
                    is ProductsFragment -> {

                        GlobalScope.launch(Dispatchers.Main) {
                            fragment.successfullyLoadedMenuItems(menuItemList)
                        }

                    }
                }
            }
            .addOnFailureListener {
                e ->

                Log.e(fragment.javaClass.simpleName, "Error while loading menu items", e)
            }
    }

    fun getQuizMenuItems(activity: ViewTrainingActivity) {
        mFirestore.collection(Constants.TRAINING_MENU_ITEMS)
            .whereEqualTo(Constants.TYPE, Constants.QUIZ_MENU_ITEM)
            .get()
            .addOnSuccessListener { document ->
                Log.i("Menu list", document.documents.toString())

                val menuItemList:ArrayList<TrainingMenuItem> = ArrayList()

                for (i in document.documents) {

                    val menuItem = i.toObject(TrainingMenuItem::class.java)
                    menuItem!!.id = i.id

                    menuItemList.add(menuItem)
                }

                        activity.successfullyLoadedQuizMenu(menuItemList)

                }

            .addOnFailureListener {
                    e ->
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while loading menu items", e)
            }
    }

    fun uploadAddressDetails(activity:UpdateAddressActivity, addressDetails:Address) {
        mFirestore.collection(Constants.ADDRESSES)
            .document()
            .set(addressDetails, SetOptions.merge())
            .addOnSuccessListener {

            activity.successfullyUploadedAddress()

            }
            .addOnFailureListener {

            }

    }

    fun getCurrentMenuItem(activity: Activity, menuItemId:String) {
        mFirestore.collection(Constants.TRAINING_MENU_ITEMS)
            .document(menuItemId)
            .get()
            .addOnSuccessListener { document ->

                val menuItem = document.toObject(TrainingMenuItem::class.java)!!
                when (activity) {
                    is ViewTrainingActivity -> {
                        activity.successfullyLoadedMenuItem(menuItem)
                    }
                }
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName, "Error while getting menu item", e)
                when(activity) {
                    is ViewTrainingActivity ->{
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun filterForCategory(activity: Activity, category:String) {
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.PRODUCT_CATEGORY, category)
            .get()
            .addOnSuccessListener { document ->

                Log.i("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)

                when (activity){
                    is FilteredProductsActivity->{

                        activity.successfullyFilteredProducts(productsList)
                    }
                }
                }
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while filtering items", e)

                when (activity){
                    is FilteredProductsActivity ->{
                        activity.hideProgressDialog()
                    }
            }

            }
    }

    fun filterForProductUser(activity: Activity, userID:String){
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, userID)
            .get()
            .addOnSuccessListener { document ->

                Log.i("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)

                    when (activity){
                        is FilteredProductsActivity->{

                            activity.successfullyFilteredProducts(productsList)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while filtering items", e)

                when (activity){
                    is FilteredProductsActivity ->{
                        activity.hideProgressDialog()
                    }
                }

            }
    }

    fun getAddressesList(activity:AddressListActivity){
        mFirestore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document ->

                //List of addresses
                Log.e(activity.javaClass.simpleName, document.documents.toString())

                val addressList:ArrayList<Address> = ArrayList()

                for (i in document.documents) {
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    addressList.add(address)
                }

                activity.successfullyLoadedAddressList(addressList)

            }
            .addOnFailureListener {
                e ->
                Log.e(activity.javaClass.simpleName,"Error while loading addresses!"
                    , e)
            }
    }

    fun updateAddressDetails(activity: UpdateAddressActivity, addressDetails: Address, addressID:String) {

        mFirestore.collection(Constants.ADDRESSES)
            .document(addressID)
            .set(addressDetails, SetOptions.merge())
            .addOnSuccessListener {
                activity.successfullyUploadedAddress()
            }
            .addOnFailureListener {

                e->

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating address details",
                    e
                )
            }
    }

    fun deleteAddress(activity: AddressListActivity, addressID: String){
        mFirestore.collection(Constants.ADDRESSES)
            .document(addressID)
            .delete()
            .addOnSuccessListener {

                activity.successfullyDeletedAddress()
            }
            .addOnFailureListener {
                e ->

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while deleting address",
                e
                )
            }
    }

    fun placeOrder(activity:CheckoutActivity, order: Order){
        mFirestore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.successfullyPlacedOrder()
            }
            .addOnFailureListener {
                e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading order details to cloud",
                    e
                )
            }
    }

    fun getProductSearchResults(activity: SearchActivity, word:String) {
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->

                Log.i("Products List for " + word, document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    if (word in product.productTitle || word in product.productTitle.toLowerCase()){
                        productsList.add(product)
                    }

                }
                activity.successfullyLoadedSearchResults(productsList)
            }
            .addOnFailureListener {

            }
    }

    fun getProductCreatorName(activity: ViewProductDetailsActivity, creatorId:String) {
        mFirestore.collection(Constants.USERS)
            .document(creatorId)
            .get()
            .addOnSuccessListener {

                    document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                activity.successfullyGotCreatorDetails(user)
            }
            .addOnFailureListener {
                e ->

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting creator ID",
                    e)
            }
    }

    fun getProductsSorted(activity:SearchActivity, field:String,  order:Query.Direction){
        mFirestore.collection(Constants.PRODUCTS)
            .orderBy(field, order)
            .get()
            .addOnSuccessListener { document ->

                Log.i("Products List ascending", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)

                }
                activity.successfullyLoadedSearchResults(productsList)
            }
            .addOnFailureListener {

                    e ->

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while ordering items",
                    e
                )

            }
    }


    fun getTrainingInfo(activity: ViewTrainingActivity, title:String) {
        mFirestore.collection(Constants.TRAINING_INFO)
            .whereEqualTo(Constants.TITLE, title)
            .get()
            .addOnSuccessListener {

                document ->

                val info = document.toObjects(TrainingInfo::class.java)

                activity.successfullyLoadedTrainingInfo(info)
            }
            .addOnFailureListener {

            }
    }

    fun updateProductInfo(activity: AddProductActivity, productID: String, productHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.PRODUCTS)
            .document(productID)
            .update(productHashMap)
            .addOnSuccessListener {

                activity.successfullyUpdatedProductInfo()

            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating product details",
                    e)

            }
    }

    fun updateAllDetails(activity: CheckoutActivity, cartList: ArrayList<CartItem>, order: Order){
        val writeBatch = mFirestore.batch()

        // Add the completed order to the sold products database
        for (cart in cartList) {

//            val productHashMap = HashMap<String, Any>()
//
//            productHashMap[Constants.QUANTITY] =
//                (cart.stock_quantity.toInt() - cart.cart_quantity.toInt()).toString()

            val soldProduct = SoldProduct(
                cart.product_owner_id,
                cart.title,
                cart.price,
                cart.cart_quantity,
                cart.image,
                order.title,
                order.order_datetime,
                order.subtotal_amount,
                order.transaction_fee,
                order.total_amount,
                order.address

            )

            val documentReference = mFirestore.collection(Constants.SOLD_PRODUCTS)
                .document(cart.product_id)

            writeBatch.set(documentReference, soldProduct)
        }

        // Here we will update the product stock in the products collection based to cart quantity.
        for (cart in cartList) {

            val productHashMap = HashMap<String, Any>()

            productHashMap[Constants.QUANTITY] =
                (cart.stock_quantity.toInt() - cart.cart_quantity.toInt()).toString()

            val documentReference = mFirestore.collection(Constants.PRODUCTS)
                .document(cart.product_id)
            writeBatch.update(documentReference, productHashMap)
        }

        // Delete the list of cart items
        for (cart in cartList) {

            val documentReference = mFirestore.collection(Constants.CART_ITEMS)
                .document(cart.id)
            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {

            // TODO Step 4: Finally after performing all the operation notify the user with the success result.
            // START
            activity.successfullyUpdatedAllDetails()
            // END

        }.addOnFailureListener { e ->
            // Here call a function of base activity for transferring the result to it.
            activity.hideProgressDialog()

            Log.e(activity.javaClass.simpleName, "Error while updating all the details after order placed.", e)
        }
    }

    fun getMyOrders(fragment: OrdersFragment) {
        mFirestore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document ->

                Log.e(fragment.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<Order> = ArrayList()

                for (i in document.documents) {

                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }

                fragment.successfullyLoadedOrdersList(list)
            }.addOnFailureListener { e ->

                fragment.hideProgressDialog()

                Log.e(fragment.javaClass.simpleName, "Error while getting the orders list.", e)

            }
    }

    fun getSoldProducts(fragment: OrdersFragment){

        mFirestore.collection(Constants.SOLD_PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(fragment.javaClass.simpleName, document.documents.toString())
                val list:ArrayList<SoldProduct> = ArrayList()

                for (i in document.documents) {

                    val soldProduct = i.toObject(SoldProduct::class.java)!!
                    soldProduct.id = i.id

                    list.add(soldProduct)

                }

                fragment.successfullyLoadedSoldProducts(list)

            }.addOnFailureListener { e ->

                fragment.hideProgressDialog()

                Log.e(fragment.javaClass.simpleName, "Error while loading sold products", e)

            }
    }

}

