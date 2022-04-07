package com.example.msikayaalimi.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Order
import com.example.msikayaalimi.ui.activities.ViewOrderDetails
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.GlideLoader
import com.example.msikayaalimi.utils.MYATextView
import com.example.msikayaalimi.utils.MYATextViewBold
import com.google.firestore.v1.StructuredQuery

open class MyOrdersListAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.list_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductImage(
                model.image,
                holder.itemView.findViewById(R.id.iv_item_image)
            )

            holder.itemView.findViewById<MYATextViewBold>(R.id.tv_item_name).text = model.title
            holder.itemView.findViewById<MYATextView>(R.id.tv_item_price).text = "MWK ${model.total_amount}"

            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).visibility = View.GONE

            holder.itemView.setOnClickListener {
                val intent = Intent(context, ViewOrderDetails::class.java)
                intent.putExtra(Constants.EXTRA_ORDER_DETAILS, model)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}