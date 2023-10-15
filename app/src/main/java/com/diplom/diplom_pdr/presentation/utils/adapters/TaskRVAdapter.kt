package com.diplom.diplom_pdr.presentation.utils.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.ItemNumberQuestionBinding
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.presentation.screens.TaskScreen

class TaskRVAdapter : ListAdapter<TaskScreen.Question, TaskRVAdapter.TaskViewHolder>(DiffCallBack()) {

    override fun onCurrentListChanged(
        previousList: MutableList<TaskScreen.Question>,
        currentList: MutableList<TaskScreen.Question>
    ) {
        notifyDataSetChanged()
        super.onCurrentListChanged(previousList, currentList)
    }

    inner class TaskViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemNumberQuestionBinding.bind(view)

        fun bind(model: TaskScreen.Question) = with(binding) {
            tvNumber.text = "${model.id}"

            when (model.taskItem.status) {
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

    class DiffCallBack() : DiffUtil.ItemCallback<TaskScreen.Question>() {
        override fun areItemsTheSame(oldItem: TaskScreen.Question, newItem: TaskScreen.Question): Boolean {
            return oldItem.taskItem.status == newItem.taskItem.status
        }

        override fun areContentsTheSame(
            oldItem: TaskScreen.Question,
            newItem: TaskScreen.Question
        ): Boolean {
            return oldItem.taskItem == newItem.taskItem
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