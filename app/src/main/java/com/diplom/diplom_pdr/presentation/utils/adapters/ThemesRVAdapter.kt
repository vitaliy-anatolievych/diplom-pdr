package com.diplom.diplom_pdr.presentation.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.ItemThemeBinding
import com.diplom.diplom_pdr.models.ThemeItem

class ThemesRVAdapter: ListAdapter<ThemeItem, ThemesRVAdapter.ThemesViewHolder>(DiffCallBack()) {

    private var clickListener: ((ThemeItem) -> Unit)? = null

    inner class ThemesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemThemeBinding.bind(view)

        fun bind(model: ThemeItem) = with(binding) {
            title.text = model.title
            valueRightAnswers.text = "${model.rightAnswers}"
            valueAllQuestions.text = "${model.allQuestion}"

            if (model.isStarted) {
                btnNext.visibility = View.VISIBLE


                btnNext.setOnClickListener {
                    clickListener?.invoke(model)
                }
            }

            btnTheme.setOnClickListener {
                clickListener?.invoke(model)
            }
        }
    }

    fun onTaskClick(listener: (ThemeItem) -> Unit) {
        clickListener = listener
    }

    class DiffCallBack : DiffUtil.ItemCallback<ThemeItem>() {
        override fun areItemsTheSame(oldItem: ThemeItem, newItem: ThemeItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ThemeItem,
            newItem: ThemeItem
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