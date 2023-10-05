package com.diplom.diplom_pdr.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.diplom.diplom_pdr.R


class RulesScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rules, container, false).also { view ->

            view.findViewById<LinearLayout>(R.id.ll_buttons).apply {
                children.forEachIndexed { index, view ->
                    view.setOnClickListener {
                        val direction =
                            RulesScreenDirections.actionRulesScreenToWebScreen("https://vodiy.ua/pdr/${index + 1}/")

                        findNavController().navigate(direction)
                    }
                }
            }
        }

    }
}