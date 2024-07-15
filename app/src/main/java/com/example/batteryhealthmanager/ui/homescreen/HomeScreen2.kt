package com.example.batteryhealthmanager.ui.homescreen

import android.widget.Toast
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val viewModel: BHMViewModel = viewModel(factory = BHMViewModel.Factory)

    // get clarity here on how to ideally store them or show them, and why no boolean in optimised state variable type
    var showBottomSheet by remember { mutableStateOf(false) }

    val switchValues: SnapshotStateList<Boolean> = remember { mutableStateListOf(false, false) }

    // check about disposing snapshots
    val levelLabels = listOf("Min Value", "Max Value")
    val levelFieldValues: SnapshotStateList<String> = remember { mutableStateListOf("", "") }

    val tempLabels = listOf("Battery Temperature")
    val tempFieldValues: SnapshotStateList<String> = remember { mutableStateListOf("") }


    // currently not scrollable

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            HomeHeader(
                viewModel = viewModel,
                onToggled = { index, value ->
                    switchValues[index] = value
                    viewModel.updateNotificationPreferences(
                        switchValues[0],
                        switchValues[1]
                    )
                }
            )

            HomeInput(
                tempLabels = tempLabels,
                tempFieldValues = tempFieldValues,
                tempInputCount = 1,
                levelLabels = levelLabels,
                levelFieldValues = levelFieldValues,
                levelInputCount = 2,
                onLevelValuesChange = { index, newValue ->
                    levelFieldValues[index] = newValue
                },
                onTempValuesChange = { index, newValue ->
                    tempFieldValues[index] = newValue
                },
                viewModel = viewModel
            )

            HomeFooter(
                onClick = { showBottomSheet = true }
            )

            if (showBottomSheet) {
                PartialBottomSheet(onDismiss = { showBottomSheet = false })
            }
        }
    }
}

@Composable
fun SeveralTextFieldInput(
    modifier: Modifier = Modifier,
    inputCount: Int,
    labels: List<String>,
    values: List<String>,
    onValueChange: (Int, String) -> Unit
) {

    LazyColumn {
        itemsIndexed(values) { index, text ->
            val extraPadding = if (index == inputCount - 1) 16 else 0
            TextField(
                value = text,
                onValueChange = { newValue ->
                    onValueChange(index, newValue)
                },
                modifier = Modifier
                    .inputFieldModifier(16.dp)
                    .padding(bottom = extraPadding.dp),
                label = { Text(text = labels[index]) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun HomeInput(
    modifier: Modifier = Modifier,
    tempLabels: List<String>,
    tempFieldValues: SnapshotStateList<String>,
    tempInputCount: Int,
    levelLabels: List<String>,
    levelFieldValues: SnapshotStateList<String>,
    levelInputCount: Int,
    onLevelValuesChange: (Int, String) -> Unit,
    onTempValuesChange: (Int, String) -> Unit,
    viewModel: BHMViewModel
) {

    val context = LocalContext.current

    Column {
        ElevatedCard(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Battery Range Values",
                modifier = modifier.padding(top = 16.dp, start = 16.dp)
            )
            SeveralTextFieldInput(
                inputCount = levelInputCount,
                labels = levelLabels,
                values = levelFieldValues,
                onValueChange = { index, newValue ->
                    onLevelValuesChange(index, newValue)
                }
            )
            Button(
                onClick = {
                    val lowerBoundValue = levelFieldValues[0].toInt()
                    val upperBoundValue = levelFieldValues[1].toInt()
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
                },
                modifier = Modifier
                    .inputFieldModifier(16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Submit")
            }
        }
        
        ElevatedCard(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Battery Temp Values",
                modifier = modifier.padding(top = 16.dp, start = 16.dp)
            )
            SeveralTextFieldInput(
                inputCount = tempInputCount,
                labels = tempLabels,
                values = tempFieldValues,
                onValueChange = { index, value -> 
                    onTempValuesChange(index, value) 
                }
            )
            Button(
                onClick = {
                    val temperature = tempFieldValues[0].toInt()
                    if (performTempValidations(temperature)) {
                        Toast.makeText(
                            context,
                            "You updated battery values.",
                            Toast.LENGTH_LONG)
                            .show()
                        viewModel.updateTempValues(BatteryTemperatureDTO(temperature))
                    }
                },
                modifier = Modifier
                    .inputFieldModifier(16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    viewModel: BHMViewModel,
    onToggled: (Int, Boolean) -> Unit
) {

    val currValues = viewModel.showNotifications.collectAsState()
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier // Use a new Modifier instance here
                .fillMaxWidth()
                .padding(8.dp) // Inner padding for the first Row
                .padding(8.dp), // Additional padding to simulate margin
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Level notifications",
                maxLines = 2,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Switch(
                checked = currValues.value.enableBatteryRangeNotifications,
                onCheckedChange = { onToggled(0, !currValues.value.enableBatteryRangeNotifications) }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Temperature Notifications",
                maxLines = 2,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Switch(
                checked = currValues.value.enableBatteryTempNotifications,
                onCheckedChange = { onToggled(1, !currValues.value.enableBatteryTempNotifications) }
            )
        }
    }
}

@Composable
fun HomeFooter(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val onTermClick = remember {
        onClick
    }
    Column {
        Text(text = "please read wisely to to do the things")
        Row {
            Text(text = "Hello bechee kdnskdj sldknslkdj ")
            Text(
                text = "sldkjs",
                modifier = modifier.clickable(onClick = onTermClick)
            )
        }
    }
}

//----------------------------------------------- END OF CODE ----------------------------------------------------------------


enum class NotificationType {
    BATTERY_RANGE, TEMPERATURE
}

@Composable
fun HomeHeader2(
    modifier: Modifier = Modifier,
    viewModel: BHMViewModel = hiltViewModel(),
    onNotificationToggled: (NotificationType, Boolean) -> Unit
) {
    val currValues = viewModel.showNotifications.collectAsState()
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            NotificationToggle(
                title = "Level notifications",
                checked = currValues.value.enableBatteryRangeNotifications,
                onCheckedChange = { isChecked ->
                    onNotificationToggled(NotificationType.BATTERY_RANGE, isChecked)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            NotificationToggle(
                title = "Temperature Notifications",
                checked = currValues.value.enableBatteryTempNotifications,
                onCheckedChange = { isChecked ->
                    onNotificationToggled(NotificationType.TEMPERATURE, isChecked)
                }
            )
        }
    }
}

@Composable
private fun NotificationToggle(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            maxLines = 2,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

