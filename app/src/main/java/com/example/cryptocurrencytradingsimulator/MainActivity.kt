package com.example.cryptocurrencytradingsimulator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cryptocurrencytradingsimulator.notifications.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NotificationService.setAlarm(applicationContext)
        setupViews()
    }

    private fun setupViews() {
        var navController = findNavController(R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
    }


}