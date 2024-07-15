package com.example.batteryhealthmanager.ui.homescreen

import android.renderscript.ScriptGroup.Input
import android.widget.Toast
import androidx.annotation.experimental.Experimental
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.batteryhealthmanager.R
import com.example.batteryhealthmanager.db.dto.BatteryLevelDTO
import com.example.batteryhealthmanager.db.dto.BatteryTemperatureDTO
import com.example.batteryhealthmanager.ui.bottomsheet.PartialBottomSheet


// what is * important here
class InputState(initialValues: List<String>) {
    private val _values = mutableStateListOf(*initialValues.toTypedArray())
    val values: List<String>
        get() = _values

    fun updateIndex(index: Int, newValue: String) {
        if (index in _values.indices) {
            _values[index] = newValue
        }
    }
}

class SwitchState(initialValues: List<Boolean>) {
    private val _values = mutableStateListOf(*initialValues.toTypedArray())
    val values: List<Boolean> get() = _values

    fun updateValue(index: Int, newValue: Boolean) {
        if (index in _values.indices) {
            _values[index] = newValue
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val viewModel: BHMViewModel = viewModel(factory = BHMViewModel.Factory)

    var showBottomSheet by remember { mutableStateOf(false) }

    val levelFieldState = remember { InputState(List(2) { "" }) }
    val tempFieldState = remember { InputState(List(1) { "" }) }
    val showNotificationsState = remember { SwitchState(List(2) { true }) }

    val tempLabels = listOf("Battery Temperature")
    val levelLabels = listOf("Min Value", "Max Value")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name))})
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    viewModel: BHMViewModel,
    showNotificationsState: SwitchState
) {
    
    val currValues = viewModel.showNotifications.collectAsState()
    
    
}

@Composable
fun Input(
    modifier: Modifier = Modifier,
    levelLabels: List<String>,
    tempLabels: List<String>,
    levelState: InputState,
    tempState: InputState,
    viewModel: BHMViewModel
) {
    
    val context = LocalContext.current
    
    Column {
        ElevatedCard(
            modifier = modifier.padding(12.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Battery Range Values",
                modifier = modifier.padding(
                    top = 16.dp,
                    start = 16.dp
                )
            )
            TextFieldInputs(labels = levelLabels, inputState = levelState)
            Button(onClick = { 
                val lowerBoundValue = levelState.values[0].toInt()
                val upperBoundValue = levelState.values[1].toInt()

                if (batteryLevelValidations(
                        lowerBoundValue,
                        upperBoundValue)
                ) {
                    Toast.makeText(
                        context,
                        "You updated level values.",
                        Toast.LENGTH_LONG).show()

                    viewModel.updateLevelValues(BatteryLevelDTO(
                        lowerBoundValue,
                        upperBoundValue
                    ))
                }
            }) {
                Text(text = "Submit")
            }
        }
        
        ElevatedCard(
            modifier = modifier.padding(12.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = "Temperature Value")
            TextFieldInputs(labels = tempLabels, inputState = tempState)
            Button(onClick = { /*TODO*/ }) {
                
            }
        }
    }
}

@Composable
fun TextFieldInputs(
    modifier: Modifier = Modifier,
    labels: List<String>,
    inputState: InputState
) {

    Column {
        inputState.values.forEachIndexed { index, value ->
            key(index) {
                TextField(
                    value = value,
                    onValueChange = { newValue ->
                        inputState.updateIndex(index, newValue)
                    },
                    label = { Text(text = labels[index]) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}