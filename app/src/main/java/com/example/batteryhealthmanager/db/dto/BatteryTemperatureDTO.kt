package com.example.batteryhealthmanager.db.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BatteryTemperatureDTO(
    var temperatureThreshold: Int
) : Parcelable
