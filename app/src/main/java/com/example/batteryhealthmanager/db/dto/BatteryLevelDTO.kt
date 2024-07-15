package com.example.batteryhealthmanager.db.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BatteryLevelDTO(
    val lowerBound: Int,
    val upperBound: Int
) : Parcelable


// diff bw DTO and model
