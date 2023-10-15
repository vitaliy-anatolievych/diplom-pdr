package com.diplom.diplom_pdr.presentation.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.ItemThemeBinding
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.models.ThemeItemWithTasks

class ThemesRVAdapter: ListAdapter<ThemeItemWithTasks, ThemesRVAdapter.ThemesViewHolder>(DiffCallBack()) {

    private var clickListener: ((List<TaskItem>) -> Unit)? = null

    override fun onCurrentListChanged(
        previousList: MutableList<ThemeItemWithTasks>,
        currentList: MutableList<ThemeItemWithTasks>
    ) {
        notifyDataSetChanged()
        super.onCurrentListChanged(previousList, currentList)
    }

    inner class ThemesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemThemeBinding.bind(view)

        fun bind(model: ThemeItemWithTasks) = with(binding) {
            title.text = model.themeItem.title
            valueRightAnswers.text = "${model.themeItem.rightAnswers}"
            valueAllQuestions.text = "${model.taskItem.size}"

            if (model.themeItem.isStarted) {
                btnNext.visibility = View.VISIBLE


                btnNext.setOnClickListener {
                    clickListener?.invoke(model.taskItem)
                }
            }

            btnTheme.setOnClickListener {
                clickListener?.invoke(model.taskItem)
            }
        }
    }

    fun onTaskClick(listener: (List<TaskItem>) -> Unit) {
        clickListener = listener
    }

    class DiffCallBack : DiffUtil.ItemCallback<ThemeItemWithTasks>() {
        override fun areItemsTheSame(oldItem: ThemeItemWithTasks, newItem: ThemeItemWithTasks): Boolean {
            return oldItem.themeItem.id == newItem.themeItem.id
        }

        override fun areContentsTheSame(
            oldItem: ThemeItemWithTasks,
            newItem: ThemeItemWithTasks
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemesViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_theme, parent, false)

        return ThemesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThemesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}