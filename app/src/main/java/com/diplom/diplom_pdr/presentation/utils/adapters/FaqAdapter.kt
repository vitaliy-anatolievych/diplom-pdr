package com.diplom.diplom_pdr.presentation.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.models.FaqItem

class FaqAdapter: RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {
    var menuList = listOf<FaqItem>()

    class FaqViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val question = view.findViewById<TextView>(R.id.tv_faq_question)
        val answer = view.findViewById<TextView>(R.id.tv_faq_answer)

        val containerFaq = view.findViewById<RelativeLayout>(R.id.container_faq_answer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.faq_item, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val questionItem = menuList[position]

        with(holder) {
            question.text = questionItem.question
            answer.text = questionItem.answer

            itemView.setOnClickListener {
                containerFaq.visibility = if (containerFaq.isVisible) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int = menuList.size
}