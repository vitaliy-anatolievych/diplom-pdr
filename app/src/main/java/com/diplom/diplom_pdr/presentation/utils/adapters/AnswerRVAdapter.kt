package com.diplom.diplom_pdr.presentation.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.SimpleAnswerBinding
import com.diplom.diplom_pdr.models.Answer

import com.google.android.material.card.MaterialCardView

class AnswerRVAdapter :
    ListAdapter<Answer, AnswerRVAdapter.ViewHolder>(AnswerDiffUtil()) {

    private var onClickListener: ((Answer) -> Unit)? = null

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = SimpleAnswerBinding.bind(view)

        fun bind(answer: Answer) = with(binding) {
            tvAnswer.text = answer.name

            btnAnswer.setOnClickListener {
                onClickListener?.invoke(answer)
            }

        }
    }

    fun onClickItemListener(listener: (Answer) -> Unit) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.simple_answer, parent, false)

        val btnAnswer = view.findViewById<MaterialCardView>(R.id.btn_answer)
        when(viewType) {
            WRONG_ANSWER -> {
                btnAnswer.setCardBackgroundColor(parent.context.getColor(android.R.color.holo_red_dark))
            }

            RIGHT_ANSWER -> {
                btnAnswer.setCardBackgroundColor(parent.context.getColor(android.R.color.holo_green_dark))
            }

            DEFAULT_ANSWER -> {
                btnAnswer.setCardBackgroundColor(parent.context.getColor(android.R.color.white))
            }
        }

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return when(item.type) {
            Answer.TYPE.RIGHT -> RIGHT_ANSWER
            Answer.TYPE.WRONG -> WRONG_ANSWER
            Answer.TYPE.DEFAULT -> DEFAULT_ANSWER
        }
    }

    class AnswerDiffUtil
        : DiffUtil.ItemCallback<Answer>() {
        override fun areItemsTheSame(
            oldItem: Answer,
            newItem: Answer
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Answer,
            newItem: Answer
        ): Boolean {
            return oldItem == newItem
        }

    }

    companion object {
        private const val RIGHT_ANSWER = 0
        private const val WRONG_ANSWER = 1
        private const val DEFAULT_ANSWER = 3
    }
}