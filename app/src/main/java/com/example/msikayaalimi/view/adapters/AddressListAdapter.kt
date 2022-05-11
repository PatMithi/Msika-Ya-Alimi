package com.example.msikayaalimi.view.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Address
import com.example.msikayaalimi.view.activities.CheckoutActivity
import com.example.msikayaalimi.view.activities.UpdateAddressActivity
import com.example.msikayaalimi.controller.Constants
import com.example.msikayaalimi.controller.MYATextView
import com.example.msikayaalimi.controller.MYATextViewBold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Adapter used to display the list of addresses in the address activity
 * Adapted from online course
 */
class AddressListAdapter (
    private val context: Context,
    private var list: ArrayList<Address>,
    private val selectAddress: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_address,
                parent,
                false
            )
        )
    }

    fun notifyEditItem(activity: Activity, position: Int){
        val intent = Intent(context, UpdateAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS,list[position])
        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlobalScope.launch(Dispatchers.Main) {
                holder.itemView.findViewById<MYATextViewBold>(R.id.tv_address_full).text = model.fulName
                holder.itemView.findViewById<MYATextView>(R.id.tv_address_type).text = model.type
                holder.itemView.findViewById<MYATextView>(R.id.tv_address_details).text = model.address
                holder.itemView.findViewById<MYATextView>(R.id.tv_address_mobile_number).text = model.mobileNumber

                if (selectAddress){
                    holder.itemView.setOnClickListener {
                        Toast.makeText(
                            context,
                            "You have selected:  ${model.address}, ${model.district}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(context, CheckoutActivity::class.java)
                        intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS_CHECKOUT, model)
                        context.startActivity(intent)
                    }
                }
            }

        }
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return list.size
    }

}