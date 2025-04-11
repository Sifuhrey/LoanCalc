package com.fakhri0079.loancalc

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fakhri0079.loancalc.ui.theme.LoanCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoanCalcTheme {
                MainScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    LoanCalcTheme {
        MainScreen()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
       ScreenContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier){
    var pokok by remember { mutableStateOf("") }
    var pokokError by remember { mutableStateOf(false) }
    var sukuBungaError by remember { mutableStateOf(false) }
    var tenorError by remember { mutableStateOf(false) }

    var sukuBunga by remember { mutableStateOf("") }
    var tenor by remember { mutableStateOf("") }
    var total by remember { mutableFloatStateOf(0f) }
    var angsuran by remember { mutableFloatStateOf(0f) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = stringResource(id = R.string.loan),
            style = MaterialTheme.typography.bodyLarge
        )
        OutlinedTextField(
            value = pokok,
            onValueChange = { pokok = it },
            label = { Text(text = stringResource(R.string.pokok)) },
            trailingIcon = { IconPicker(pokokError,"Rp") },
            supportingText = { ErrorHint(pokokError) },
            isError = pokokError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = sukuBunga,
            onValueChange = { sukuBunga = it },
            label = { Text(text = stringResource(R.string.suku_bunga)) },
            trailingIcon = { IconPicker(sukuBungaError,"%") },
            supportingText = { ErrorHint(sukuBungaError) },
            isError = sukuBungaError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tenor,
            onValueChange = { tenor = it },
            label = { Text(text = stringResource(R.string.tenor)) },
            trailingIcon = { IconPicker(tenorError,"bulan") },
            supportingText = { ErrorHint(tenorError) },
            isError = tenorError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                pokokError = (pokok == "" || pokok == "0")
                sukuBungaError = (sukuBunga == "" || sukuBunga == "0")
                tenorError = (tenor == "" || tenor == "0")
                if (pokokError || sukuBungaError || tenorError) return@Button
                total = hitungtotal(pokok.toFloat(),sukuBunga.toFloat(),tenor.toFloat())
                angsuran = hitungperbulan(total,tenor.toFloat())
                      },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.hitung))
        }
        if (total!= 0f){
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Text(
                text = stringResource(R.string.hasil,angsuran),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.hasilTotal,total),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}



private fun hitungtotal(pokok: Float, sukuBunga: Float, tenor: Float): Float{
    return pokok+(pokok*(sukuBunga/tenor)*tenor)
}
private fun hitungperbulan(total:Float, waktu:Float): Float{
    return total/ waktu
}

@Composable
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)

    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.invalid))
    }
}
