package com.example.msikayaalimi.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Address
import com.example.msikayaalimi.view.adapters.AddressListAdapter
import com.example.msikayaalimi.controller.*
import com.facebook.shimmer.ShimmerFrameLayout

/**
 * Code to load the list of addresses for the current user
 * Code adapted from online course
 * NB: All documentation with "code adapted from online course"
 * indicate that the some code in the class has been adapted from the course
 * taught by Denis Panjuta titles "Android Firebase Firestore - Masterclass"
 */
class AddressListActivity : BaseActivity(), View.OnClickListener {

    private var mAddressSelected:Boolean = false

    private var mAddressDetails:Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }

        val addAddressBtn:MYAButton = findViewById(R.id.btn_add_address)
        addAddressBtn.setOnClickListener(this@AddressListActivity)

        getAddressList()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){

            mAddressSelected = intent.getBooleanExtra(Constants.EXTRA_SELECTED_ADDRESS, false)
        }

        if (mAddressSelected){

            val tvTitle:MYATextViewBold =findViewById(R.id.tv_title_address_list)

            tvTitle.text = resources.getString(R.string.select_address_title)
        }

    }

    /*
    Inserting back button in the actionbar to
    allow a user to return to the previous activity
     */
    private fun setupActionBar() {
        val addressListToolbar: Toolbar = findViewById(R.id.toolbar_address_list_activity)

        setSupportActionBar(addressListToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        addressListToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            getAddressList()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_add_address -> {
                    val intent = Intent(this, UpdateAddressActivity::class.java)
                    startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
                }
            }
        }
    }

    private fun getAddressList(){
        FirestoreClass().getAddressesList(this)
        val shimmerFrameLayout: ShimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_address_list_activity)
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

    }

    fun successfullyLoadedAddressList(addressList:ArrayList<Address>){
        val shimmerFrameLayout: ShimmerFrameLayout = findViewById(R.id.shimmerFrameLayout_address_list_activity)
        shimmerFrameLayout.visibility = View.GONE
        shimmerFrameLayout.stopShimmerAnimation()
        val addAddressBtn:MYAButton = findViewById(R.id.btn_add_address)
        addAddressBtn.visibility = View.VISIBLE

        val tvEmptyAddress:MYATextView = findViewById(R.id.tv_no_addresses_yet)
        val rvAddressList:RecyclerView = findViewById(R.id.rv_address_list)
        if (addressList.size > 0) {

            tvEmptyAddress.visibility = View.GONE
            rvAddressList.visibility = View.VISIBLE

            rvAddressList.layoutManager = LinearLayoutManager(this)
            rvAddressList.adapter = AddressListAdapter(this, addressList, mAddressSelected)


            if (!mAddressSelected){

                val editSwipeHandler = object : SwipeToEditCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        val adapter:AddressListAdapter = rvAddressList.adapter as AddressListAdapter
                        adapter.notifyEditItem(
                            this@AddressListActivity,
                            viewHolder.adapterPosition
                        )
                    }
                }

                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(rvAddressList)

                val deleteSwipeHandler = object : SwipeToDeleteCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                        displayProgressDialog(resources.getString(R.string.please_wait))
                        FirestoreClass().deleteAddress(this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id)

                    }

                }

                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(rvAddressList)
            }



        }else{
            tvEmptyAddress.visibility = View.VISIBLE
            rvAddressList.visibility = View.GONE
        }
    }

    fun successfullyDeletedAddress(){
        dismissProgressDialog()

        Toast.makeText(
            this,
            resources.getString(R.string.msg_successfully_deleted_address),
            Toast.LENGTH_SHORT
        ).show()

        getAddressList()
    }
}