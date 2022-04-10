package com.example.msikayaalimi.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.TrainingMenuItem
import com.example.msikayaalimi.ui.activities.ViewQuizActivity
import com.example.msikayaalimi.utils.Constants
import com.example.msikayaalimi.utils.MYAButton
import com.example.msikayaalimi.utils.MYATextView
import com.example.msikayaalimi.utils.MYATextViewBold

class QuizMenuAdapter (
    private val context: Context,
    private var list: ArrayList<TrainingMenuItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_quiz_menu,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            if(model.type == Constants.QUIZ_MENU_ITEM) {
                holder.itemView.findViewById<MYATextViewBold>(R.id.tv_quiz_menu_item_title).text = model.title
            }
        }

        holder.itemView.findViewById<ImageButton>(R.id.ib_quiz_menu_arrow).setOnClickListener {
            val intent = Intent(context, ViewQuizActivity::class.java)
            intent.putExtra(Constants.EXTRA_QUIZ_NAME, model.title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}