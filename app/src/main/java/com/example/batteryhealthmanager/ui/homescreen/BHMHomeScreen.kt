package com.example.batteryhealthmanager.ui.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.batteryhealthmanager.R
import com.example.batteryhealthmanager.ui.bottomsheet.PartialBottomSheet


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: BHMViewModel = viewModel()
) {

    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            HomeHeaderComposable()
            HomeInputsComposable()
            HomeFooterComposable(onClick = { showBottomSheet = true })
            if (showBottomSheet) {
                PartialBottomSheet(onDismiss = { showBottomSheet = false })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenComposable(modifier: Modifier = Modifier) {
    
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item { HomeHeaderComposable() }
                item { HomeInputsComposable() }
                item { HomeInputsComposable() }
                item { HomeFooterComposable(onClick = { showBottomSheet = true }) }
                if (showBottomSheet) {
                    item { PartialBottomSheet(onDismiss = { showBottomSheet = false }) }
                }
            }
        }
    }
}



@Composable
fun HomeHeaderComposable(modifier: Modifier = Modifier) {

    /*Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),

    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(maxLines = 2, text = "sdlkjs", modifier = modifier.align(Alignment.CenterVertically))
            Switch(checked = true, onCheckedChange = {})
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(maxLines = 2, text = "sdlkjs", modifier = modifier.align(Alignment.CenterVertically))
            Switch(checked = true, onCheckedChange = {})
        }
    }*/

    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier // Use a new Modifier instance here
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.secondaryContainer
        )// Outer padding for the Card
    ) {
        Row(
            modifier = Modifier // Use a new Modifier instance here
                .fillMaxWidth()
                .padding(8.dp) // Inner padding for the first Row
                .padding(8.dp), // Additional padding to simulate margin
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                maxLines = 2,
                text = "sdlkjs",
                modifier = Modifier.align(Alignment.CenterVertically) // Use a new Modifier instance here
            )
            Switch(checked = true, onCheckedChange = {})
        }

        Row(
            modifier = Modifier // Use a new Modifier instance here
                .fillMaxWidth()
                .padding(8.dp) // Inner padding for the second Row
                .background(Color.Green)
                .padding(8.dp), // Additional padding to simulate margin
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                maxLines = 2,
                text = "sdlkjs",
                modifier = Modifier.align(Alignment.CenterVertically) // Use a new Modifier instance here
            )
            Switch(checked = true, onCheckedChange = {})
        }
    }
}

@Preview
@Composable
private fun HomeHeaderPreview() {
    HomeHeaderComposable()
}

@Composable
fun HomeInputsComposable(
    modifier: Modifier = Modifier
) {

    val rangeLabels = remember{ listOf("skdlsd", "sldkjsdl") }
    val tempLabels = remember { listOf("sldjsd") }
    
    Column {
        ElevatedCard(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ){
            Text(
                text = "Battery Range Values",
                modifier = modifier.padding(top = 16.dp, start = 16.dp)
            )
            SeveralTextFieldInputs(
                numberOfInputs = 2,
                labels = rangeLabels
            )
            Button(
                onClick = {},
                modifier = modifier
                    .inputFieldModifier(16.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Submit")
            }
        }
        ElevatedCard(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ){
            Text(
                text = "Battery Temp Values",
                modifier = modifier.padding(top = 16.dp, start = 16.dp)
            )
            SeveralTextFieldInputs(
                numberOfInputs = 1,
                labels = tempLabels
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = modifier
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

@Preview
@Composable
private fun HomeInputsPreview() {
    HomeInputsComposable()
}

@Composable
fun HomeFooterComposable(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column {
        Text(text = "please read wisely to to do the things")
        Row {
            Text(text = "Hello bechee kdnskdj sldknslkdj ")
            Text(
                text = "sldkjs", 
                modifier = modifier.clickable {
                    onClick()
                }
            )
        }
    }
}



fun Modifier.inputFieldModifier(padding: Dp): Modifier = this.padding(top = padding, start = padding, end = padding)

@Composable
fun SeveralTextFieldInputs(numberOfInputs: Int, modifier: Modifier = Modifier, labels: List<String>) {
    val textFieldValues = remember { mutableStateListOf<String>().apply { repeat(numberOfInputs) { add("") } } }
    
    LazyColumn {
        itemsIndexed(textFieldValues) { index, text ->
            val extraPadding = if (index == numberOfInputs - 1) 16 else 0
            TextField(
                value = text,
                onValueChange = { newValue ->
                    textFieldValues[index] = newValue
                },
                modifier = Modifier
                    .inputFieldModifier(16.dp)
                    .fillMaxWidth(),
                label = { Text(labels[index]) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

fun batteryLevelValidations(
    lowerValue: Int,
    upperValue: Int
): Boolean {

    /*
    * neg value
    * greater than 100
    * lower greater than upper
    * */

    return true
}

fun performTempValidations(
    value: Int
) : Boolean {



    return true
}