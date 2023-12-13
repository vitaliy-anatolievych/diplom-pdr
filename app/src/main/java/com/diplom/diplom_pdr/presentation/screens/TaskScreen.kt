package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.FragmentTaskBinding
import com.diplom.diplom_pdr.models.Answer
import com.diplom.diplom_pdr.models.TaskItem
import com.diplom.diplom_pdr.presentation.utils.CountUpTimer
import com.diplom.diplom_pdr.presentation.utils.adapters.AnswerRVAdapter
import com.diplom.diplom_pdr.presentation.utils.adapters.TaskRVAdapter
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel
import com.diplom.diplom_pdr.presentation.utils.viewmodels.QuestViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
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
    private val mainViewModel: MainViewModel by activityViewModels()
    private var step = 0
    private var rightAnswers = 0
    private var wrongAnswers = 0

    private var isRandQuestions = false
    private var isQuestionOfDay = false
    private var testIsEnded = false

    var totalTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        val bundle = TaskScreenArgs.fromBundle(requireArguments())
        val tasks = bundle.taskItem.toMutableList()

        isRandQuestions = bundle.isRandQuestion
        isQuestionOfDay = bundle.isQuestionOfDay

        viewModel.setCurrentTasksList(tasks)

        /**
         * first start fragment
         */
        var isNew = true
        if (tasks[0].status != TaskItem.STATUS.CLOSE) {
            isNew = false
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

            adapter.setOnTaskClickListener {
                viewModel.setCurrentTask(it)
            }

            viewModel.favoriteData.observe(viewLifecycleOwner) {
                /**
                 * favorite
                 */
                setFavorite(it)
            }

            viewModel.gameEnd.observe(viewLifecycleOwner) {
                currentTask?.let {
                    if (!isRandQuestions) viewModel.updateIsTestPassed(title = it.themeItemTitle, isTestPassed = true)
                }

                testIsEnded = true

                if (isRandQuestions && !isQuestionOfDay) {
                    val userData = mainViewModel.userData.value

                    var calculateRating = 0
                    userData?.let { user ->
                        when (wrongAnswers) {
                            0 -> calculateRating =+ 10
                            in 1..2 -> calculateRating =+ 5
                            in 3..5 -> {}
                            else -> {
                                calculateRating =- 5
                            }
                        }

                        val newRating = user.rating + calculateRating
                        if (newRating < 100) {
                            mainViewModel.updateUser(user.copy(rating = newRating))
                        }
                    }
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
                viewModel.getAnswerList(it.question, isRandQuestions)
            }

            viewModel.answerList.observe(viewLifecycleOwner) {
                /**
                 * answers
                 */

                val answerRVAdapter = AnswerRVAdapter()
                binding.llAnswers.adapter = answerRVAdapter
                answerRVAdapter.submitList(it)

                answerRVAdapter.onClickItemListener { answer ->
                    currentTask?.let { task ->
                        if (task.status == TaskItem.STATUS.SELECTED) {
                            if (task.rightAnswer == answer.name) {
                                task.status = TaskItem.STATUS.RIGHT
                                rightAnswers++
                                val newAnswer = answer.copy(type = Answer.TYPE.RIGHT)
                                viewModel.insertAnswer(newAnswer, isRandQuestions)
                            } else {
                                task.status = TaskItem.STATUS.FAIL
                                wrongAnswers++
                                val newAnswer = answer.copy(type = Answer.TYPE.WRONG)
                                val rightAnswer =
                                    it.find { answer -> answer.name == task.rightAnswer }
                                        ?.copy(type = Answer.TYPE.RIGHT)

                                viewModel.insertAnswer(newAnswer, isRandQuestions)

                                rightAnswer?.let {
                                    viewModel.insertAnswer(rightAnswer, isRandQuestions)
                                }
                            }

                            viewModel.nextQuestion(task, step++, isRandQuestions)
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
            totalTime = elapsedTime
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
        if (!isRandQuestions && !testIsEnded && !isQuestionOfDay) {
            val userData = mainViewModel.userData.value
            userData?.let { user ->
                mainViewModel.updateUser(user.copy(rating = user.rating - 5))
            }
        }

        if (!isRandQuestions) {
            currentTask?.let {
                viewModel.updateRightAnswers(title = it.themeItemTitle, rightAnswers = rightAnswers)
                viewModel.updateWrongAnswers(title = it.themeItemTitle, wrongAnswers = wrongAnswers)

                viewModel.updateTotalTestTime(
                    title = it.themeItemTitle,
                    totalTestTime = totalTime
                )
            }
        }

        if (isRandQuestions && testIsEnded && !isQuestionOfDay) {
            val today = Calendar.getInstance()
            today.add(Calendar.DAY_OF_YEAR, 1)

            today.clear(Calendar.HOUR_OF_DAY)
            today.clear(Calendar.MINUTE)
            today.clear(Calendar.SECOND)
            today.clear(Calendar.MILLISECOND)

            val tomorrow = today.time

            val user = mainViewModel.userData.value!!.copy()

            if (user.enterDate!! != tomorrow.time) {
                user.enterDate = tomorrow.time
                user.currentInterval += 1
                mainViewModel.updateUser(user)
            }
        }

        _binding = null
    }

    data class Question(val id: Int, val taskItem: TaskItem)
}