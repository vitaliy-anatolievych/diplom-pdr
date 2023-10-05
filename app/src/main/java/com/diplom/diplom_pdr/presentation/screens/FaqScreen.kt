package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diplom.diplom_pdr.databinding.FragmentFaqBinding
import com.diplom.diplom_pdr.models.FaqItem
import com.diplom.diplom_pdr.presentation.utils.adapters.FaqAdapter

class FaqScreen: Fragment() {

    private var _binding: FragmentFaqBinding? = null
    private val binding: FragmentFaqBinding
        get() = _binding ?: throw NullPointerException("FragmentFaqBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaqBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val questionItems = mutableListOf<FaqItem>()
        questionItems.add(
            FaqItem("Question 1", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s")
        )
        questionItems.add(
            FaqItem("Question 2", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s")
        )
        questionItems.add(
            FaqItem("Question 3", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s")
        )
        questionItems.add(
            FaqItem("Question 4", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s")
        )
        questionItems.add(
            FaqItem("Question 5", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s")
        )
        questionItems.add(
            FaqItem("Question 6", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s")
        )
        questionItems.add(
            FaqItem("Question 7", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s")
        )

        val adapter = FaqAdapter()
        adapter.menuList = questionItems

        binding.rvFaq.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}