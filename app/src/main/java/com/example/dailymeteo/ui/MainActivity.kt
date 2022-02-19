package com.example.dailymeteo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dailymeteo.R
import com.example.dailymeteo.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment.newInstance(null), "")
                .commit()
        }
    }
}