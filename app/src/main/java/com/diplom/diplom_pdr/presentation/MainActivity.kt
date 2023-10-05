package com.diplom.diplom_pdr.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val navHost = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHost.navController

        _binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.rules_page -> {
                    Log.e("TEST", "RULES")
                    navController.navigate(R.id.rulesScreen)
                    true
                }

                R.id.drive_page -> {
                    Log.e("TEST", "DRIVE")
                    navController.navigate(R.id.driveScreen)
                    true
                }

                R.id.test_page -> {
                    Log.e("TEST", "TEST")
                    navController.navigate(R.id.testsScreen)
                    true
                }

                R.id.profile_page -> {
                    Log.e("TEST", "PROFILE")
                    navController.navigate(R.id.profileScreen)
                    true
                }

                else -> false
            }
        }
    }
}