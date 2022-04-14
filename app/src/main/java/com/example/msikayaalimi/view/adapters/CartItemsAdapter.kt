package com.example.msikayaalimi.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.CartItem
import com.example.msikayaalimi.view.activities.MyCartActivity
import com.example.msikayaalimi.view.activities.ViewProductDetailsActivity
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.GlideLoader
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.MYATextViewBold

class CartItemsAdapter (
    private val context: Context,
    private var list: ArrayList<CartItem>,
    private var mUpdateCart:Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_cart_layout,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        if (holder is MyViewHolder) {
            GlideLoader(context).loadProductImage(model.image, holder.itemView.findViewById(
                R.id.iv_cart_item_image))
            holder.itemView.findViewById<MYATextView>(R.id.tv_cart_item_title).text = model.title
            holder.itemView.findViewById<MYATextViewBold>(R.id.tv_cart_item_price).text = "MWK ${model.price}"
            holder.itemView.findViewById<MYATextView>(R.id.tv_cart_quantity).text = model.cart_quantity

            if (model.cart_quantity == "0") {

                holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility = View.GONE
                holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.GONE

                holder.itemView.findViewById<MYATextView>(R.id.tv_cart_quantity).text =
                    context.resources.getString(R.string.out_of_stock_label)

                holder.itemView.findViewById<MYATextView>(R.id.tv_cart_quantity).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSnackBarError
                    )
                )

                if (mUpdateCart){
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).visibility = View.VISIBLE
                }else{
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).visibility = View.GONE
                }
            }else{

                if (mUpdateCart){
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).visibility = View.VISIBLE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility = View.VISIBLE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.VISIBLE
                }else{
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).visibility = View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility = View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility = View.GONE
                }

                holder.itemView.findViewById<MYATextView>(R.id.tv_cart_quantity).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )
            }



            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).setOnClickListener {
                when(context){
                    is MyCartActivity ->{
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }
                }
                FirestoreClass().deleteItemFromCart(context, model.id)
            }

            /*
            functionality to increase and decrease the number of items in the cart
             */
            holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).setOnClickListener {
                if (model.cart_quantity == "1") {
                    FirestoreClass().deleteItemFromCart(context, model.id)
                }else {
                     val cartQuantity: Int = model.cart_quantity.toInt()

                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity -1).toString()

                    // show progress dialog to make user wait until process is completed

                    if (context is MyCartActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }

                    FirestoreClass().updateCartItems(context, model.id, itemHashMap)
                }
            }

            holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).setOnClickListener {

                val cartQuantity: Int = model.cart_quantity.toInt()

                if (cartQuantity < model.stock_quantity.toInt()) {
                    val itemHashMap = HashMap<String, Any>()

                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity +1).toString()
                    if (context is MyCartActivity) {
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                    }

                    FirestoreClass().updateCartItems(context, model.id, itemHashMap)
                }else{
                    if (context is MyCartActivity) {
                        context.showErrorSnackBar(
                            context.resources.getString(
                                R.string.msg_quantity_max
                            ), true
                        )
                    }
                }



                // show progress dialog to make user wait until process is completed


            }

            // allow user to view the product details after selecting a product
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ViewProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
                context.startActivity(intent)
            }
        }
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return list.size
    }

}