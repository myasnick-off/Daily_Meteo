package com.example.dailymeteo.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.dailymeteo.databinding.ActivitySplashBinding

// Активити заставки приложения
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    // лениво создаем поток для отложенного старта основного активити приложения
    private val handler: Handler by lazy { Handler(mainLooper) }

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.sunImageView.animate()
            .rotationBy(360F)
            .alphaBy(1.0F)
            .duration = 1500
        binding.leftCloudImageView.animate()
            .translationXBy(-500.0F)
            .alpha(0.2F)
            .duration = 1200
        binding.rightCloudImageView.animate()
            .translationXBy(500.0F)
            .alpha(0.2F)
            .duration = 1200

        // запускаем поток для отложенного старта основного активити приложения
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}