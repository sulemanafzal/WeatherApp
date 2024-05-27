package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.weatherapp.databinding.ActivityStartScreenBinding

class StartScreen : AppCompatActivity() {
    private lateinit var binding: ActivityStartScreenBinding
    val activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed(
            {
                startActivity(
                    Intent(activity, MainActivity::class.java)

                )
                finish()
            }, 4000
        )

    }
}