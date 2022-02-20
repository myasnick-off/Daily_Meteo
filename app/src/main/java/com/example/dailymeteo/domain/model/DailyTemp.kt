package com.example.dailymeteo.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyTemp(
    val morning: Int,
    val day: Int,
    val evening: Int,
    val night: Int,
    val min: Int,
    val max: Int
) : Parcelable
