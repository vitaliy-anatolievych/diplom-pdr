package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentTaskBinding
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.presentation.utils.adapters.TaskRVAdapter
import java.text.SimpleDateFormat
import java.util.Date

class TaskScreen: Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding: FragmentTaskBinding
        get() = _binding ?: throw NullPointerException("FragmentTaskBinding is null")

    private var timer: CountDownTimer? = null
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        with(binding) {
            timer = object : CountDownTimer(100_000L, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    tvTime.text = formatMilliseconds(millisUntilFinished)
                }

                override fun onFinish() {
                }

            }.start()

            val adapter = TaskRVAdapter()
            rvNumberQuest.adapter = adapter

            val list = listOf(
                TaskItem(
                    id = 0,
                    status = TaskItem.STATUS.SELECTED,
                ),
                TaskItem(
                    id = 1,
                    status = TaskItem.STATUS.CLOSE,
                ),
                TaskItem(
                    id = 2,
                    status = TaskItem.STATUS.CLOSE,
                ),
                TaskItem(
                    id = 3,
                    status = TaskItem.STATUS.CLOSE,
                ),
                TaskItem(
                    id = 4,
                    status = TaskItem.STATUS.CLOSE,
                ),
                TaskItem(
                    id = 5,
                    status = TaskItem.STATUS.CLOSE,
                ),
            )

            adapter.submitList(list)

            btnFavorite.setOnClickListener {

                if (isFavorite) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_fill)
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_empty)
                }

                isFavorite = !isFavorite
            }

            tvQuestion.text = "Згідно з правилами дорожнього руху на вимогу поліцейського водій повинен..."
            tvAnswer1.text = "Дати можливість оглянути транспортний засіб, навіть якщо на те немає ніяких підстав"
            tvAnswer2.text = "Дати можливість оглянути транспортний засіб тільки за наявності на те законних підстав відповідно до законодавства України"
        }

        return binding.root
    }

    fun formatMilliseconds(milliseconds: Long): String {
        val format = SimpleDateFormat("mm:ss")
        return format.format(Date(milliseconds))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        _binding = null
    }
}