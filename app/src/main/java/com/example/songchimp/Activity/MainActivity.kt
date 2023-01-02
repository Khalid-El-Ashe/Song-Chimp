package com.example.songchimp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.songchimp.R
import com.example.songchimp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.hostFragments)
        binding.bottomNav.setupWithNavController(navController)

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            binding.bottomNav.visibility = View.VISIBLE
        }, 3000)
    }
}