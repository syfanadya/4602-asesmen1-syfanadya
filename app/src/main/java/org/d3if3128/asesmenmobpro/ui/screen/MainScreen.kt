package org.d3if3128.asesmenmobpro.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3128.asesmenmobpro.R
import org.d3if3128.asesmenmobpro.navigation.Screen
import org.d3if3128.asesmenmobpro.ui.theme.AsesmenMobproTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                            navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                            )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@SuppressLint("StringFormatMatches")
@Composable
fun ScreenContent(modifier: Modifier) {
    var nama by rememberSaveable { mutableStateOf("") }
    var namaError by rememberSaveable { mutableStateOf(false) }
    var berat by rememberSaveable { mutableStateOf("") }
    var beratError by rememberSaveable { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val list = listOf(
        "0 bulan", "1 bulan", "2 bulan", "3 bulan", "4 bulan", "5 bulan", "6 bulan",
        "7 bulan", "8 bulan", "9 bulan", "10 bulan", "11 bulan", "12 bulan"
    )
    var pilihUsia by rememberSaveable { mutableStateOf("") }
    var pilihUsiaError by rememberSaveable { mutableStateOf(false) }

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
    var jeniskelamin by rememberSaveable { mutableStateOf(radioOptions[0]) }
    var kategoriGizi by rememberSaveable { mutableIntStateOf(0) }

    val context = LocalContext.current

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
            isError = namaError,
            supportingText = { ErrorHint(namaError)},
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
            isError = pilihUsiaError,
            trailingIcon = {
                Icon(icon, "", Modifier.clickable { expanded = !expanded }
                )
            },
                    supportingText = { ErrorHint(pilihUsiaError)},
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
            isError = beratError,
            trailingIcon = { IconPicker(beratError, "kg" ) },
            supportingText = { ErrorHint(beratError)},
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                namaError = (nama == "" ||  nama == "0")
                beratError = (berat == "" || berat == "0" || berat.toFloatOrNull() == null || berat.toFloat() <= 0)
                pilihUsiaError =(pilihUsia == "" || pilihUsia == "0")
                if (namaError || beratError || pilihUsiaError) return@Button
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
            Text(text = stringResource(id = R.string.kategori_gizi),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(id = kategoriGizi).uppercase(),
                style = MaterialTheme.typography.headlineLarge
            )
            Button(onClick = {
                shareData(
                    context = context,
                    message = context.getString(R.string.bagikan_template,
                        nama, jeniskelamin, pilihUsia, berat,
                        context.getString(kategoriGizi).uppercase())
                )
            },
                modifier = Modifier.padding(top = 8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.bagikan))
            }
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

@Composable
fun  IconPicker(isError:Boolean, unit: String){
    if (isError){
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else{
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean){
    if(isError){
        Text(text = stringResource(id = R.string.input_invalid))
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

private fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type ="text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if(shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    AsesmenMobproTheme {
        MainScreen(rememberNavController())
    }
}
