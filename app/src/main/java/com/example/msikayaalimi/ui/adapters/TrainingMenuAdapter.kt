package com.example.msikayaalimi.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.Product
import com.example.msikayaalimi.models.TrainingMenuItem
import com.example.msikayaalimi.ui.activities.FilteredProductsActivity
import com.example.msikayaalimi.ui.activities.MarketActivity
import com.example.msikayaalimi.ui.activities.QuizMenuActivity
import com.example.msikayaalimi.ui.activities.ViewTrainingActivity
import com.example.msikayaalimi.ui.fragments.DashboardFragment
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.GlideLoader
import com.example.msikayaalimi.utils.MYATextView
import com.google.android.material.bottomnavigation.BottomNavigationItemView

class TrainingMenuAdapter (
    private val context: Context,
    private var list: ArrayList<TrainingMenuItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mFilteredProducts:ArrayList<Product>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.ilist_item_training_menu,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProductImage(model.image, holder.itemView.findViewById(R.id.iv_training_menu_item))
            holder.itemView.findViewById<MYATextView>(R.id.tv_farm_practices).text = model.title

            if (model.title == "Farm Practices"){
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )

            }
            if (model.title == "Herd Management"){
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.orange
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.orange
                    )
                )

            }
            if (model.title == "Quizzes"){
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.burgundy
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.burgundy
                    )
                )

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    context.startActivity(Intent(context, QuizMenuActivity::class.java))
                }
            }
            if (model.title == "Self-sufficiency") {
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pink
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pink
                    )
                )

            }
            if (model.title == "Fruits") {
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.neutralPink
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.neutralPink
                    )
                )

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    context.startActivity(Intent(context, MarketActivity::class.java))
                }
            }
            if (model.title == "Vegetables") {
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.darkBlue
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.darkBlue
                    )
                )

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    context.startActivity(Intent(context, MarketActivity::class.java))
                }
            }
            if (model.title == "Animals") {
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.turquoise
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.turquoise
                    )
                )

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    context.startActivity(Intent(context, MarketActivity::class.java))
                }
            }
            if (model.title == "Eggs and Dairy") {
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    context.startActivity(Intent(context, MarketActivity::class.java))
                }
            }

            if (model.title == "Other") {
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.orange
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.orange
                    )
                )

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    context.startActivity(Intent(context, MarketActivity::class.java))
                }
            }

            if (model.title == "Introduction to Land Management") {
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.turquoise
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.turquoise
                    )
                )

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    context.startActivity(Intent(context, MarketActivity::class.java))
                }
            }

            if (model.title == "Nuts") {
                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pink
                    )
                )
                holder.itemView.findViewById<LinearLayout>(R.id.ll_training_item).setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pink
                    )
                )

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    context.startActivity(Intent(context, MarketActivity::class.java))
                }
            }



            if (model.type == Constants.TRAINING_ITEM) {

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    val intent = Intent(context, ViewTrainingActivity::class.java)
                    intent.putExtra(Constants.EXTRA_MENU_ITEM_ID, model.id)
                    context.startActivity(intent)
                }
            }
            if (model.type == Constants.CATEGORY_ITEM) {

                holder.itemView.findViewById<LinearLayout>(R.id.ll_farm_practices).setOnClickListener{
                    val intent = Intent(context, FilteredProductsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_FILTERED_ITEMS, model.title)
                    context.startActivity(intent)
                }
            }
        }
    }





    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}