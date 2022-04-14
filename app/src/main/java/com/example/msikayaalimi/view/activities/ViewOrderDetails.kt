package com.example.msikayaalimi.view.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Order
import com.example.msikayaalimi.view.adapters.CartItemsAdapter
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.MYATextViewBold
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ViewOrderDetails : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order_details)

        setupActionBar()

        var orderDetails: Order = Order()

        if (intent.hasExtra(Constants.EXTRA_ORDER_DETAILS)) {
            orderDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_ORDER_DETAILS)!!
            getOrderDetails(orderDetails)
        }
    }

    private fun setupActionBar(){
        val orderDetailsToolbar:Toolbar = findViewById(R.id.toolbar_view_orders_activity)

        setSupportActionBar(orderDetailsToolbar)

        val actionBar = supportActionBar

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        orderDetailsToolbar.setNavigationOnClickListener{onBackPressed()}


    }

    private fun getOrderDetails(orderDetails: Order) {
        val orderDetailsID:MYATextView = findViewById(R.id.tv_view_order_id)
        orderDetailsID.text = orderDetails.title

        // Format date will be displayed
        val dateFormat = "dd MMM yyyy HH:mm"

        //create a DateFormatter object for displaying the date
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // create  a calendar object that will display the date and time value in mm to date
        val calendar:Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        val tvOrderDate:MYATextView = findViewById(R.id.tv_view_order_date)
        tvOrderDate.text = orderDateTime

        // Get the difference between the order date time and current date time in hours.
        // If the difference in hours is 1 or less then the order status will be PENDING.
        // If the difference in hours is 2 or greater then 1 then the order status will be PROCESSING.
        // And, if the difference in hours is 3 or greater then the order status will be DELIVERED.

        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

        val tvOrderStatus:MYATextView = findViewById(R.id.tv_view_order_status)
        when {
            diffInHours < 1 -> {
                tvOrderStatus.text = resources.getString(R.string.order_status_pending)
                tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.red
                    )
                )
            }
            diffInHours < 2 -> {
                tvOrderStatus.text = resources.getString(R.string.order_status_in_process)
                tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.orange
                    )
                )
            }
            else -> {
                tvOrderStatus.text = resources.getString(R.string.order_status_delivered)
                tvOrderStatus.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ColorPrimary
                    )
                )
            }
        }
        val rvOrderDetailsList:RecyclerView = findViewById(R.id.rv_view_order_details_list)
        rvOrderDetailsList.layoutManager = LinearLayoutManager(this)
        rvOrderDetailsList.setHasFixedSize(true)

        val cartItemsAdapter = CartItemsAdapter(this, orderDetails.items,false)
        rvOrderDetailsList.adapter = cartItemsAdapter

        val tvAddressType:MYATextViewBold = findViewById(R.id.tv_view_order_details_address_type)
        tvAddressType.text = orderDetails.address.type
        val tvPayerFullName:MYATextViewBold = findViewById(R.id.tv_order_full_name)
        tvPayerFullName.text = orderDetails.address.fulName
        val tvOrderAddress:MYATextView = findViewById(R.id.tv_order_address)
        tvOrderAddress.text =
            "${orderDetails.address.address}, ${orderDetails.address.district}"
        val tvAdditionalDetails:MYATextView = findViewById(R.id.tv_my_order_details_additional_note)
        tvAdditionalDetails.text = orderDetails.address.additionalNote
        val tvOrderMobile:MYATextView = findViewById(R.id.tv_my_order_details_mobile_number)
        tvOrderMobile.text = orderDetails.address.mobileNumber
        val tvSubtotal:MYATextView = findViewById(R.id.tv_view_order_subtotal)
        tvSubtotal.text = orderDetails.subtotal_amount
        val transFee:MYATextView = findViewById(R.id.tv_order_details_total_amount)
        transFee.text = orderDetails.transaction_fee
        val tvTotalAmount:MYATextView = findViewById(R.id.tv_order_details_total_amount_end)
        tvTotalAmount.text = orderDetails.total_amount
    }

}