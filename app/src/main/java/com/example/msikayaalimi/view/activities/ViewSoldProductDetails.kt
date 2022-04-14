package com.example.msikayaalimi.view.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.SoldProduct
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.GlideLoader
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.MYATextViewBold
import java.text.SimpleDateFormat
import java.util.*

class ViewSoldProductDetails : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_sold_product_details)

        setupActionBar()

        var productDetails: SoldProduct = SoldProduct()

        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCTS_DETAILS)) {
            productDetails = intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_PRODUCTS_DETAILS)!!
        }

        getSoldProductDetails(productDetails)
    }

    private fun setupActionBar(){
        val orderDetailsToolbar: Toolbar = findViewById(R.id.toolbar_view_sold_product_details)

        setSupportActionBar(orderDetailsToolbar)

        val actionBar = supportActionBar

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        orderDetailsToolbar.setNavigationOnClickListener{onBackPressed()}


    }

    private fun getSoldProductDetails(productDetails: SoldProduct) {

        val tvSoldProductId:MYATextView = findViewById(R.id.tv_sold_products_order_id)

        tvSoldProductId.text = productDetails.order_id

        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.order_date

        val tvSoldProductDetails:MYATextView = findViewById(R.id.tv_sold_product_date)
        tvSoldProductDetails.text = formatter.format(calendar.time)

        val ivProductImage:ImageView = findViewById(R.id.iv_sold_product_image)

        GlideLoader(this).loadProductImage(
            productDetails.image,
            ivProductImage
        )

        val tvProductItemName:MYATextViewBold = findViewById(R.id.tv_sold_product_item_name)
        val tvProductItemPrice:MYATextView = findViewById(R.id.tv_sold_product_price)

        tvProductItemName.text = productDetails.title
        tvProductItemPrice.text = "MWK ${productDetails.price}"

        val tvSoldAddressType:MYATextView = findViewById(R.id.tv_sold_product_address_type)
        val tvFullName: MYATextViewBold = findViewById(R.id.tv_sold_product_full_name)
        tvSoldAddressType.text = productDetails.address.type
        tvFullName.text = productDetails.address.fulName

        val tvSoldProductAddress:MYATextView = findViewById(R.id.tv_sold_product_address)
        tvSoldProductAddress.text =
            "${productDetails.address.address}. ${productDetails.address.district}"

        val tvAdditionalInfo:MYATextView = findViewById(R.id.tv_sold_product_additional_notes)
        tvAdditionalInfo.text = productDetails.address.additionalNote

        val tvMobileNumber:MYATextView = findViewById(R.id.tv_sold_product_mobile_number)
        tvMobileNumber.text = productDetails.address.mobileNumber

        val tvSubtotal:MYATextView = findViewById(R.id.tv_sold_product_subtotal)
        tvSubtotal.text = productDetails.sub_total_amount

        val tvTransFee:MYATextView = findViewById(R.id.tv_sold_product_trans_fee)
        tvTransFee.text = productDetails.transaction_fee

        val tvTotalAmount:MYATextViewBold = findViewById(R.id.tv_sold_products_total_amount)
        tvTotalAmount.text = productDetails.total_amount
    }
}