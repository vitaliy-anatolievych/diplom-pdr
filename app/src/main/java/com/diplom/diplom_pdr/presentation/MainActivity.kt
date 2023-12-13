package com.diplom.diplom_pdr.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.diplom.diplom_pdr.R
import com.diplom.diplom_pdr.databinding.ActivityMainBinding
import com.diplom.diplom_pdr.models.UserEntity
import com.diplom.diplom_pdr.presentation.utils.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        requestPermission()
        viewModel.fillData()
        loadBottomNav()

        viewModel.userData.observe(this) {
            _binding.tvFlame.text = it.currentInterval.toString()
            _binding.tvStar.text = it.rating.toString()
        }

        _binding.root.postDelayed({
            viewModel.userData.value?.let {
                if (it.enterDate == null) {
                    it.enterDate = System.currentTimeMillis()
                    viewModel.updateUser(it)
                } else {
                    val result = checkDate(it)
                    if (!result) {
                        it.currentInterval = 0
                        viewModel.updateUser(it)
                    }
                }
            }
        }, 1500)

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Log.e("PERMISSIONS", "${permissions.entries}")
        permissions.entries.forEach { permission ->
            with(permission) {
                if (!value) {
                    Toast.makeText(
                        this@MainActivity,
                        "Надайте дозвіл для використання программи",
                        Toast.LENGTH_SHORT
                    ).show()
                    this@MainActivity.finish()
                    return@registerForActivityResult
                }
            }
        }
    }

    private fun checkDate(userEntity: UserEntity): Boolean {
        val systemTime = Calendar.getInstance()
        systemTime.timeInMillis = System.currentTimeMillis()

        systemTime.clear(Calendar.HOUR_OF_DAY)
        systemTime.clear(Calendar.MINUTE)
        systemTime.clear(Calendar.SECOND)
        systemTime.clear(Calendar.MILLISECOND)

        val userTime = Calendar.getInstance()
        userTime.timeInMillis = userEntity.enterDate!!

        userTime.clear(Calendar.HOUR_OF_DAY)
        userTime.clear(Calendar.MINUTE)
        userTime.clear(Calendar.SECOND)
        userTime.clear(Calendar.MILLISECOND)

        val comparisonResult = systemTime.compareTo(userTime)
        return when {
            comparisonResult == 0 -> true
            comparisonResult < 0 -> true
            else -> false
        }
    }

    private fun requestPermission() {
        val permissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                ) == PackageManager.PERMISSION_DENIED
//            ) {
//                permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//            }
//        }

        Log.e("PERMISSIONS", "$permissions")
        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun loadBottomNav() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHost.navController

        _binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.rules_page -> {
                    navController.navigate(R.id.rulesScreen)
                    true
                }

                R.id.drive_page -> {
                    navController.navigate(R.id.driveScreen)
                    true
                }

                R.id.test_page -> {
                    navController.navigate(R.id.testsScreen)
                    true
                }

                R.id.profile_page -> {
                    navController.navigate(R.id.profileScreen)
                    true
                }

                else -> false
            }
        }
    }
}