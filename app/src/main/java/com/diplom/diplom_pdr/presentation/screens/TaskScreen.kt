package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentTaskBinding
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.presentation.utils.CountUpTimer
import com.diplom.diplom_pdr.presentation.utils.adapters.TaskRVAdapter
import com.diplom.diplom_pdr.presentation.utils.viewmodels.QuestViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskScreen : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding: FragmentTaskBinding
        get() = _binding ?: throw NullPointerException("FragmentTaskBinding is null")

    private lateinit var timer: CountUpTimer
    private var isFavorite = false
    private var currentTask: TaskItem? = null
    private val viewModel: QuestViewModel by viewModel()
    private var step = 0
    private var rightAnswers = 0
    private var wrongAnswers = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        val tasks = TaskScreenArgs
            .fromBundle(requireArguments())
            .taskItem
            .toMutableList()

        viewModel.setCurrentTasksList(tasks)

        /**
         * first start fragment
         */
        var isNew = true
        for (task in tasks) {
            if (task.status != TaskItem.STATUS.CLOSE) {
                isNew = false
            }
        }

        if (isNew) {
            tasks[0].status = TaskItem.STATUS.SELECTED
            viewModel.setCurrentTask(tasks[0])
            step = 0
        } else {
            if (tasks.last().status != TaskItem.STATUS.CLOSE) {
                step = tasks.size - 1
                viewModel.setCurrentTask(tasks.last())
            } else {
                for (taskId in 0 until tasks.size) {
                    if (tasks[taskId].status == TaskItem.STATUS.SELECTED) {
                        viewModel.setCurrentTask(tasks[taskId])
                        step = taskId
                    }
                }
            }
        }

        with(binding) {
            val adapter = TaskRVAdapter()
            rvNumberQuest.adapter = adapter

            viewModel.favoriteData.observe(viewLifecycleOwner) {
                /**
                 * favorite
                 */
                setFavorite(it)
            }

            viewModel.gameEnd.observe(viewLifecycleOwner) {
                currentTask?.let {
                    viewModel.updateIsTestPassed(title = it.themeItemTitle, isTestPassed = true)
                }

                view?.postDelayed({
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.tasks_completed), Toast.LENGTH_SHORT
                    )
                        .show()
                    activity?.onBackPressed()
                }, 500)
            }

            viewModel.tasksList.observe(viewLifecycleOwner) {
                /**
                 * fill top question
                 */
                val questions = tasks.mapIndexed { index, taskItem ->
                    Question(
                        id = index + 1,
                        taskItem = taskItem
                    )
                }

                viewModel.questionList = questions

                adapter.submitList(questions)
            }

            viewModel.currentQuest.observe(viewLifecycleOwner) {
                /**
                 * current Task
                 */
                currentTask = it


                /**
                 * fill question
                 */
                val url = if (it.imgURL.contains("https")) {
                    it.imgURL
                } else {
                    ""
                }

                /**
                 * favorite
                 */
                setFavorite(it.isFavorite)

                val piscasso = Picasso.Builder(requireContext())
                piscasso.listener { picasso, uri, exception -> exception.printStackTrace() }

                if (url.isNotBlank()) {
                    imgTask.visibility = View.VISIBLE
                    piscasso.build()
                        .load(url.trim())
                        .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                        .error(R.drawable.no_image_uk)
                        .into(imgTask)
                } else {
                    imgTask.visibility = View.GONE
                }


                tvQuestion.text = it.question


                /**
                 * answers
                 */

                val data = it.answers
                    .mapIndexed { index, s ->
                        Answer(id = index, name = s)
                    }

                viewModel.answersList = data

                binding.llAnswers.adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.simple_answer,
                    R.id.tv_answer,
                    data
                )

                binding.llAnswers.setOnItemClickListener { parent, view, position, id ->

                    val answer = data[position]
                    currentTask?.let { task ->
                        if (task.status == TaskItem.STATUS.SELECTED) {
                            if (task.rightAnswer == answer.name) {
                                task.status = TaskItem.STATUS.RIGHT
                                rightAnswers++
                            } else {
                                task.status = TaskItem.STATUS.FAIL
                                wrongAnswers++
                            }
                            viewModel.nextQuestion(task, step++)
                        }
                    }
                }
            }



            btnFavorite.setOnClickListener {
                currentTask?.let { task ->
                    viewModel.setFavorite(task.id!!, !isFavorite)
                }
            }
        }

        return binding.root
    }

    private fun setFavorite(it: Boolean) = with(binding) {
        isFavorite = it
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_fill)
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_empty)
        }
    }

    private fun formatMilliseconds(milliseconds: Long): String {
        val format = SimpleDateFormat("mm:ss", Locale.getDefault())
        return format.format(Date(milliseconds))
    }

    private fun startTimer() {
        timer = CountUpTimer(1000) { elapsedTime ->
            binding.tvTime.text = formatMilliseconds(elapsedTime)
        }

        timer.start()
    }

    override fun onResume() {
        super.onResume()
        startTimer()
    }

    override fun onPause() {
        super.onPause()
        timer.stop() // TODO fix timer
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentTask?.let {
            viewModel.updateRightAnswers(title = it.themeItemTitle, rightAnswers = rightAnswers)
            viewModel.updateWrongAnswers(title = it.themeItemTitle, wrongAnswers = wrongAnswers)

            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            val parsed = dateFormat.parse(binding.tvTime.text.toString())?.time ?: 0L
            viewModel.updateTotalTestTime(
                title = it.themeItemTitle,
                totalTestTime = parsed
            )
        }
        _binding = null
    }

    data class Question(val id: Int, val taskItem: TaskItem)
    data class Answer(val id: Int, val name: String) {
        override fun toString(): String {
            return name
        }
    }
}