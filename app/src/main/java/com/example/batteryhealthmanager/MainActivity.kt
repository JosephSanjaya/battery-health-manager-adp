package com.example.batteryhealthmanager

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.batteryhealthmanager.db.dto.BatteryLevelDTO
import com.example.batteryhealthmanager.db.dto.BatteryTemperatureDTO
import com.example.batteryhealthmanager.db.dto.ShowNotificationsDTO
import com.example.batteryhealthmanager.db.dto.ThresholdCrossedDTO
import com.example.batteryhealthmanager.ui.homescreen.HomeScreen
import com.example.batteryhealthmanager.ui.theme.BatteryHealthManagerTheme
import com.example.batteryhealthmanager.utils.BATTERY_LEVEL_VALUES
import com.example.batteryhealthmanager.utils.BATTERY_TEMPERATURE_VALUES
import com.example.batteryhealthmanager.utils.NOTIFICATIONS_TRIGGER_RANGE
import com.example.batteryhealthmanager.utils.SHOW_NOTIFICATIONS
import com.tencent.mmkv.MMKV
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var mmkv: MMKV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val levelValues = mmkv.decodeParcelable(BATTERY_LEVEL_VALUES, BatteryLevelDTO::class.java)
        val tempValues = mmkv.decodeParcelable(BATTERY_TEMPERATURE_VALUES, BatteryTemperatureDTO::class.java)
        val thresholdValues = mmkv.decodeParcelable(NOTIFICATIONS_TRIGGER_RANGE, ThresholdCrossedDTO::class.java)
        val showNotifications = mmkv.decodeParcelable(SHOW_NOTIFICATIONS, ShowNotificationsDTO::class.java)
        Log.d(TAG, "onCreate: levelValues are: $levelValues")
        Log.d(TAG, "onCreate: tempValues are: $tempValues")
        Log.d(TAG, "onCreate: thresholdValues are: $thresholdValues")
        Log.d(TAG, "onCreate: showNotifications are :$showNotifications")
        setContent {
            BatteryHealthManagerTheme {
                HomeScreen()
            }
        }
    }
}



