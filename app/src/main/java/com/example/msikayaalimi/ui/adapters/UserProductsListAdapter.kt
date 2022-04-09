package com.example.msikayaalimi.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.ui.activities.*
import com.example.msikayaalimi.ui.fragments.ProductsFragment
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.GlideLoader
import com.example.msikayaalimi.utils.MYATextView
import com.example.msikayaalimi.utils.MYATextViewBold

open class UserProductsListAdapter (
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment:ProductsFragment
        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // return custom ViewHolder

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
            GlideLoader(context).loadProductImage(model.productImage, holder.itemView.findViewById(R.id.iv_item_image))
            holder.itemView.findViewById<MYATextViewBold>(R.id.tv_item_name).text = model.productTitle
            holder.itemView.findViewById<MYATextView>(R.id.tv_item_specification).text = model.productSpecification
            holder.itemView.findViewById<MYATextView>(R.id.tv_item_price).text = "MWK ${model.productPrice}"

            // allow user to click on product to view product details
            holder.itemView.setOnClickListener{
                val intent = Intent(context, ViewProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                context.startActivity(intent)
            }

            /*
            / code to set the delete icon
            as an on click listener and delete the specific product
             */
            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).setOnClickListener{
                fragment.deleteProduct(model.product_id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyEditItemProducts(activity: Activity, position: Int){
        val intent = Intent(context, AddProductActivity::class.java)
        intent.putExtra(Constants.EXTRA_PRODUCTS_EDIT,list[position])
        activity.startActivity(intent)
        notifyItemChanged(position)
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}