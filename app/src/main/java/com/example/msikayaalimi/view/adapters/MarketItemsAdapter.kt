package com.example.msikayaalimi.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.view.activities.ViewProductDetailsActivity
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.GlideLoader
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.MYATextViewBold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MarketItemsAdapter (
    private val context: Context,
    private var list: ArrayList<Product>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            // return custom ViewHolder

            return MarketViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.grid_items_layout,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val model = list[position]

            GlobalScope.launch(Dispatchers.Main) {
                if (holder is MarketViewHolder) {
                    GlideLoader(context).loadProductImage(model.productImage, holder.itemView.findViewById(
                        R.id.iv_item_image))
                    holder.itemView.findViewById<MYATextViewBold>(R.id.tv_item_name).text = model.productTitle
                    holder.itemView.findViewById<MYATextView>(R.id.tv_item_price).text = "MWK ${model.productPrice}"

                    // allow user to view the product details after selecting a product
                    holder.itemView.setOnClickListener {
                        val intent = Intent(context, ViewProductDetailsActivity::class.java)
                        intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                        intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
                        context.startActivity(intent)
                    }
                }
            }


        }

        override fun getItemCount(): Int {
            return list.size
        }



    class MarketViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

