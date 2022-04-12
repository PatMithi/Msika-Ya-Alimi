package com.example.msikayaalimi.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Welcome
import com.example.msikayaalimi.ui.activities.FilteredProductsActivity
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.GlideLoader
import com.example.msikayaalimi.utils.MYATextViewBold

class WelcomeFilterAdapter(
    private val context: Context,
    private var list: ArrayList<Welcome>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_grid_welcome,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (model.title != "Welcome" || model.title != "Go to Market"){
            GlideLoader(context).loadProductImage(model.image, holder.itemView.findViewById(R.id.iv_welcome_filter_image))
            holder.itemView.findViewById<MYATextViewBold>(R.id.tv_filter_name).text = model.title
        }

        holder.itemView.findViewById<ImageView>(R.id.iv_welcome_filter_image).setOnClickListener{
            val intent = Intent(context, FilteredProductsActivity::class.java)
            intent.putExtra(Constants.EXTRA_FILTERED_ITEMS, model.title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}