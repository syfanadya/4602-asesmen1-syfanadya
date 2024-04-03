package org.d3if3128.asesmenmobpro.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import org.d3if3128.asesmenmobpro.R
import org.d3if3128.asesmenmobpro.ui.theme.AsesmenMobproTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier) {
    var nama by remember { mutableStateOf("") }
    var berat by remember { mutableStateOf("") }
//    var tinggi by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val list = listOf(
        "0 bulan", "1 bulan", "2 bulan", "3 bulan", "4 bulan", "5 bulan", "6 bulan",
        "7 bulan", "8 bulan", "9 bulan", "10 bulan", "11 bulan", "12 bulan"
    )
    var pilihUsia by remember { mutableStateOf("") }
    var textFiledSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    val radioOptions = listOf(
        stringResource(id = R.string.laki_laki),
        stringResource(id = R.string.perempuan)
    )
    var jeniskelamin by remember { mutableStateOf(radioOptions[0]) }
    var kategoriGizi by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.nutribaby_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text(text = stringResource(R.string.nama_bayi)) },
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            radioOptions.forEach { text ->
                GenderOption(
                    label = text,
                    isSelected = jeniskelamin == text,
                    modifier = Modifier
                        .selectable(
                            selected = jeniskelamin == text,
                            onClick = { jeniskelamin = text },
                            role = Role.RadioButton
                        )
                        .weight(1f)
                        .padding(16.dp)
                )
            }
        }
        OutlinedTextField(
            value = pilihUsia,
            onValueChange = { pilihUsia = it },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFiledSize = coordinates.size.toSize()
                },
            label = { Text(text = stringResource(R.string.usia_bayi)) },
            trailingIcon = {
                Icon(icon, "", Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.size(width = 328.dp, height = 300.dp)
        ) {
            list.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        pilihUsia = label
                        expanded = false
                    }
                )
            }
        }
        OutlinedTextField(
            value = berat,
            onValueChange = { berat = it },
            label = { Text(text = stringResource(R.string.berat_badan)) },
            trailingIcon = { Text(text = "kg") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                kategoriGizi = getKategoriGizi(
                    berat.toFloat(),
                    jeniskelamin == radioOptions[0],
                    pilihUsia
                )
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.tentukan))
        }
        if (kategoriGizi != 0){
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            Text(
                text = stringResource(id = kategoriGizi).uppercase(),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
fun GenderOption(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}



private fun getKategoriGizi(berat: Float, isMale: Boolean, pilihUsia: String): Int {
    return if (isMale) {
        when (pilihUsia) {
            "0 bulan" -> {
                if (berat < 2.5) {
                    R.string.gizi_kurang
                } else if (berat > 2.4 && berat < 4.0) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "1 bulan" -> {
                if (berat < 3.4) {
                    R.string.gizi_kurang
                } else if (berat > 3.3 && berat < 5.1) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "2 bulan" -> {
                if (berat < 4.3) {
                    R.string.gizi_kurang
                } else if (berat > 4.2 && berat < 6.4) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "3 bulan" -> {
                if (berat < 5.0) {
                    R.string.gizi_kurang
                } else if (berat > 4.9 && berat < 7.3) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "4 bulan" -> {
                if (berat <= 5.5) {
                    R.string.gizi_kurang
                } else if (berat > 5.5 && berat < 7.9) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "5 bulan" -> {
                if (berat < 6.0) {
                    R.string.gizi_kurang
                } else if (berat > 5.9 && berat < 8.5) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "6 bulan" -> {
                if (berat < 6.4) {
                    R.string.gizi_kurang
                } else if (berat > 6.3 && berat < 8.9) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "7 bulan" -> {
                if (berat <= 6.6) {
                    R.string.gizi_kurang
                } else if (berat > 6.6 && berat < 9.3) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "8 bulan" -> {
                if (berat < 6.9) {
                    R.string.gizi_kurang
                } else if (berat > 6.8 && berat < 9.7) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "9 bulan" -> {
                if (berat <= 7.0) {
                    R.string.gizi_kurang
                } else if (berat > 7.0 && berat < 10) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "10 bulan" -> {
                if (berat < 7.4) {
                    R.string.gizi_kurang
                } else if (berat > 7.3 &&  berat < 10.3) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "11 bulan" -> {
                if (berat <= 7.5) {
                    R.string.gizi_kurang
                } else if (berat > 7.5 && berat < 10.6) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "12 bulan" -> {
                if (berat <= 7.6) {
                    R.string.gizi_kurang
                } else if (berat > 7.6 && berat < 10.9) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            else -> R.string.gizi_default
        }
    } else {
        when (pilihUsia) {
            "0 bulan" -> {
                if (berat < 2.4) {
                    R.string.gizi_kurang
                } else if (berat >= 2.4 && berat < 3.8) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "1 bulan" -> {
                if (berat <= 3.1) {
                    R.string.gizi_kurang
                } else if (berat > 3.1 && berat < 4.9) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "2 bulan" -> {
                if (berat < 3.9) {
                    R.string.gizi_kurang
                } else if (berat > 3.8 && berat < 5.9) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "3 bulan" -> {
                if (berat < 4.5) {
                    R.string.gizi_kurang
                } else if (berat > 4.4 && berat < 6.7) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "4 bulan" -> {
                if (berat < 5.0) {
                    R.string.gizi_kurang
                } else if (berat > 4.9 && berat < 7.4) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "5 bulan" -> {
                if (berat < 5.4) {
                    R.string.gizi_kurang
                } else if (berat > 5.3 && berat < 7.9) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "6 bulan" -> {
                if (berat <= 5.6) {
                    R.string.gizi_kurang
                } else if (berat > 5.6 && berat < 8.3) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "7 bulan" -> {
                if (berat < 6.0) {
                    R.string.gizi_kurang
                } else if (berat > 5.9 && berat < 8.7) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "8 bulan" -> {
                if (berat < 6.3) {
                    R.string.gizi_kurang
                } else if (berat > 6.2 && berat < 9.1) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "9 bulan" -> {
                if (berat < 6.5) {
                    R.string.gizi_kurang
                } else if (berat > 6.4 && berat < 9.4) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "10 bulan" -> {
                if (berat <= 6.6) {
                    R.string.gizi_kurang
                } else if (berat > 6.6 && berat < 9.7) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "11 bulan" -> {
                if (berat < 6.9) {
                    R.string.gizi_kurang
                } else if (berat > 6.8 && berat < 10) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            "12 bulan" -> {
                if (berat < 7.0) {
                    R.string.gizi_kurang
                } else if (berat > 6.9 && berat < 10.2) {
                    R.string.gizi_baik
                } else {
                    R.string.gizi_lebih
                }
            }
            else -> R.string.gizi_default
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    AsesmenMobproTheme {
        MainScreen()
    }
}
