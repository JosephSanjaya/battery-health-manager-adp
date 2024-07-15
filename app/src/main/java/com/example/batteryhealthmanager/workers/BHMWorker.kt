package com.example.batteryhealthmanager.workers

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.batteryhealthmanager.db.dto.BatteryLevelDTO
import com.example.batteryhealthmanager.db.dto.BatteryTemperatureDTO
import com.example.batteryhealthmanager.db.dto.ShowNotificationsDTO
import com.example.batteryhealthmanager.db.dto.ThresholdCrossedDTO
import com.example.batteryhealthmanager.utils.BATTERY_LEVEL_VALUES
import com.example.batteryhealthmanager.utils.BATTERY_TEMPERATURE_VALUES
import com.example.batteryhealthmanager.utils.NOTIFICATIONS_TRIGGER_RANGE
import com.example.batteryhealthmanager.utils.SHOW_NOTIFICATIONS
import com.example.batteryhealthmanager.utils.makeStatusNotification
import com.tencent.mmkv.MMKV
import javax.inject.Inject

// usage of assisted inject
class BHMWorker(
    private val context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var mmkv: MMKV

    override suspend fun doWork(): Result {
        return try {

            val batteryLevelInfo =
                mmkv.decodeParcelable(BATTERY_LEVEL_VALUES, BatteryLevelDTO::class.java)

            // check for casting
            val batteryTemperatureInfo = mmkv.decodeParcelable(
                BATTERY_TEMPERATURE_VALUES,
                BatteryTemperatureDTO::class.java
            ) as BatteryTemperatureDTO

            val showNotificationsInfo =
                mmkv.decodeParcelable(
                    SHOW_NOTIFICATIONS,
                    ShowNotificationsDTO::class.java
                ) as ShowNotificationsDTO

            val notificationsTriggerRange = mmkv.decodeParcelable(
                NOTIFICATIONS_TRIGGER_RANGE,
                ThresholdCrossedDTO::class.java
            ) as ThresholdCrossedDTO

            val batteryManager =
                applicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val isCharging = batteryManager.isCharging

            val batteryStatus: Intent? =
                IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { iFilter ->
                    applicationContext.registerReceiver(null, iFilter)
                }

            batteryStatus?.let { intent ->
                val currLevel: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val currTemperature: Int =
                    intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)

                // resetting the values
                // casting check for variables inside if
                batteryLevelInfo?.let { batteryLevelInfo ->
                    if (currLevel >= batteryLevelInfo.lowerBound && currLevel <= batteryLevelInfo.upperBound) {
                        notificationsTriggerRange.crossedUpperBound = false
                        notificationsTriggerRange.crossedLowerBound = false
                    }
                }

                // lower bound condition for notification
                if (
                    !isCharging &&
                    currLevel <= (batteryLevelInfo?.lowerBound ?: 0) &&
                    showNotificationsInfo.enableBatteryRangeNotifications == true &&
                    !notificationsTriggerRange.crossedLowerBound
                ) {

                    makeStatusNotification(
                        message = "Value has gone below the required par and can potentially damage battery health, please plug in for charging",
                        context = context,
                        title = "Lower bound notification"
                    )

                    notificationsTriggerRange.crossedLowerBound = true
                }

                // upper bound condition for notification

                if (
                    isCharging &&
                    currLevel >= (batteryLevelInfo?.upperBound ?: 0) &&
                    showNotificationsInfo.enableBatteryRangeNotifications == true &&
                    !notificationsTriggerRange.crossedUpperBound
                ) {

                    makeStatusNotification(
                        message = "Value has gone above the required par and can potentially damage battery health, please plug off the device from charging",
                        context = context,
                        title = "Upper Bound Notification"
                    )

                    notificationsTriggerRange.crossedUpperBound = true
                }


                // temperature based notifications
                // again check for best practises in terms of casting
                if (
                    currTemperature >= batteryTemperatureInfo.temperatureThreshold
                ) {
                    makeStatusNotification(
                        message = "Value has gone below the required par and can potentially damage battery health",
                        context = context,
                        title = "Temperature Notification"
                    )
                }

            }
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            Result.failure()
        }
    }

    companion object {
        const val TAG = "BHMWorker"
    }

}


