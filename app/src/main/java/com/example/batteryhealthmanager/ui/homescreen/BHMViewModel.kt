package com.example.batteryhealthmanager.ui.homescreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.batteryhealthmanager.db.dto.BatteryLevelDTO
import com.example.batteryhealthmanager.db.dto.BatteryTemperatureDTO
import com.example.batteryhealthmanager.db.dto.ShowNotificationsDTO
import com.example.batteryhealthmanager.di.MMKVEntryPoint
import com.example.batteryhealthmanager.utils.BATTERY_LEVEL_VALUES
import com.example.batteryhealthmanager.utils.BATTERY_TEMPERATURE_VALUES
import com.example.batteryhealthmanager.utils.SHOW_NOTIFICATIONS
import com.tencent.mmkv.MMKV
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/*
 val and private in the parameters
 access difference in init and function, variable not accessible in functino when it is val but is accessible in init block
 diff bw function and method
*/

@HiltViewModel(Factory::class)
class BHMViewModel @Inject constructor(
    private val mmkv: MMKV
) : ViewModel() {

    private val _batteryLevels = MutableStateFlow(BatteryLevelDTO(-1, -1))
    val batteryLevels: StateFlow<BatteryLevelDTO> = _batteryLevels.asStateFlow()

    private val _batteryTemperature = MutableStateFlow(BatteryTemperatureDTO(-1))
    val batteryTemperature: StateFlow<BatteryTemperatureDTO> = _batteryTemperature.asStateFlow()

    private val _showNotifications = MutableStateFlow(ShowNotificationsDTO(true, true))
    val showNotifications: StateFlow<ShowNotificationsDTO> = _showNotifications.asStateFlow()


    // is there a way to avoid non null assertion operator
    init {
        _batteryLevels.value = mmkv.decodeParcelable(
            BATTERY_LEVEL_VALUES, BatteryLevelDTO::class.java
        )!!

        _batteryTemperature.value = mmkv.decodeParcelable(
            BATTERY_TEMPERATURE_VALUES, BatteryTemperatureDTO::class.java
        )!!

        _showNotifications.value = mmkv.decodeParcelable(
            SHOW_NOTIFICATIONS, ShowNotificationsDTO::class.java
        )!!
    }

    fun updateLevelValues(value: BatteryLevelDTO) {
        mmkv.encode(BATTERY_LEVEL_VALUES, value)
    }

    fun updateTempValues(value: BatteryTemperatureDTO) {
        mmkv.encode(BATTERY_TEMPERATURE_VALUES, value)
    }

    fun updateNotificationPreferences(currLevelState: Boolean, currTempState: Boolean) {
        val curr = ShowNotificationsDTO(currLevelState, currTempState)
        mmkv.encode(SHOW_NOTIFICATIONS, curr)
        _showNotifications.value = curr
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                val mmkv = EntryPointAccessors.fromApplication(
                    application,
                    MMKVEntryPoint::class.java
                ).mmkv()
                BHMViewModel(mmkv)
            }
        }
    }
}

