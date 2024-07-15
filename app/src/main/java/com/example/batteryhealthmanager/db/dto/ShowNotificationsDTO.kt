package com.example.batteryhealthmanager.db.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShowNotificationsDTO(
    var enableBatteryRangeNotifications: Boolean = true,
    var enableBatteryTempNotifications: Boolean = true
) : Parcelable