package com.example.batteryhealthmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.batteryhealthmanager.db.dto.BatteryLevelDTO
import com.example.batteryhealthmanager.db.dto.BatteryTemperatureDTO
import com.example.batteryhealthmanager.db.dto.ShowNotificationsDTO
import com.example.batteryhealthmanager.db.dto.ThresholdCrossedDTO
import com.example.batteryhealthmanager.utils.CHANNEL_ID
import com.example.batteryhealthmanager.utils.CHANNEL_NAME
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.example.batteryhealthmanager.utils.BATTERY_LEVEL_VALUES
import com.example.batteryhealthmanager.utils.BATTERY_TEMPERATURE_VALUES
import com.example.batteryhealthmanager.utils.NOTIFICATIONS_TRIGGER_RANGE
import com.example.batteryhealthmanager.utils.SHOW_NOTIFICATIONS
import com.example.batteryhealthmanager.utils.WORKER_NAME
import com.example.batteryhealthmanager.workers.BHMWorker
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class BHMApplication : Application() {

    @Inject
    lateinit var mmkv: MMKV

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        MMKV.initialize(this)
        initMMKVKeys()
        initWorkManager()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun initMMKVKeys() {
        if (!mmkv.containsKey(SHOW_NOTIFICATIONS)) {
            mmkv.encode(
                SHOW_NOTIFICATIONS, ShowNotificationsDTO(
                    enableBatteryRangeNotifications = true,
                    enableBatteryTempNotifications = true
                )
            )

            mmkv.encode(
                BATTERY_LEVEL_VALUES, BatteryLevelDTO(
                    lowerBound = -1,
                    upperBound = -1
                )
            )

            mmkv.encode(
                BATTERY_TEMPERATURE_VALUES, BatteryTemperatureDTO(
                    temperatureThreshold = -1
                )
            )

            mmkv.encode(
                NOTIFICATIONS_TRIGGER_RANGE, ThresholdCrossedDTO(
                    crossedLowerBound = false,
                    crossedUpperBound = false
                )
            )
        }

    }

    private fun initWorkManager() {
        val batteryWorkRequest = PeriodicWorkRequestBuilder<BHMWorker>(20, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            batteryWorkRequest
        )
    }

}