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

    // обработка события по нажатию системной кнопки "Назад"
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is BackPressedMonitor) {
            // проверяем если нажатие кнопки "Назад" во фрагменте,
            // унаследованном от BackPressedMonitor вернуло false,
            // то отдаем обработку системной кнопки "Назад" активити
            if (!fragment.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}