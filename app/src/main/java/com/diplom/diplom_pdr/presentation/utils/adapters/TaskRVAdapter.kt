package com.diplom.diplom_pdr.presentation.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.ItemNumberQuestionBinding
import com.diplom.diplom_pdr.models.TaskItem

class TaskRVAdapter : ListAdapter<TaskItem, TaskRVAdapter.TaskViewHolder>(DiffCallBack()) {

    inner class TaskViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemNumberQuestionBinding.bind(view)

        fun bind(model: TaskItem) = with(binding) {
            tvNumber.text = "${model.id + 1}"

            when (model.status) {
                TaskItem.STATUS.FAIL -> cardTask.setCardBackgroundColor(
                    view.context.resources.getColor(
                        android.R.color.holo_red_dark
                    )
                )

                TaskItem.STATUS.RIGHT -> cardTask.setCardBackgroundColor(
                    view.context.resources.getColor(
                        R.color.green_bg
                    )
                )

                TaskItem.STATUS.SELECTED -> cardTask.setCardBackgroundColor(
                    view.context.resources.getColor(
                        R.color.white
                    )
                )

                TaskItem.STATUS.CLOSE -> cardTask.setCardBackgroundColor(
                    view.context.resources.getColor(
                        R.color.gray_bg
                    )
                )
            }
        }
    }

    class DiffCallBack() : DiffUtil.ItemCallback<TaskItem>() {
        override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TaskItem,
            newItem: TaskItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_number_question, parent, false)

        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}