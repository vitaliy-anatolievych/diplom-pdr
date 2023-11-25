package com.diplom.diplom_pdr.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        registerPermissionListener()
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
        Log.e("TIME", "$comparisonResult")
        return when {
            comparisonResult == 0 -> true
            comparisonResult < 0 -> true
            else -> false
        }
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
            }

            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
            }

            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
            }

            else -> {
                pLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    )
                )
            }
        }
    }

    private fun registerPermissionListener() {
        pLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                Log.e("MAP", "${it}")
                if (it[Manifest.permission.ACCESS_FINE_LOCATION] == false) {
                    Toast.makeText(
                        this,
                        "Надайте дозвіл для використання программи",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.finish()
                }
                if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == false) {
                    Toast.makeText(
                        this,
                        "Надайте дозвіл для використання программи",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.finish()
                }

                if (it[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == false) {
                    Toast.makeText(
                        this,
                        "Надайте дозвіл для використання программи",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.finish()
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Не надано дозвін геолокації", Toast.LENGTH_SHORT).show()
                this.finish()
            }
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