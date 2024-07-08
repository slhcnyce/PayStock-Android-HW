package com.scy.paystock.views.renterCode

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scy.paystock.navigation.Common
import com.scy.paystock.views.companents.ErrorDialog


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RenterCodeView(viewModel: RenterCodeViewModel = RenterCodeViewModel(),
                   navController: NavController
) {
    var text by remember { mutableStateOf("") }
    val showDialog by viewModel.showDialog.collectAsState()
    val errorMessage by viewModel.message.collectAsState()
    val response by viewModel.response.collectAsState()

    Scaffold {
        TopAppBar(
            title = { Text("PayStock") }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center // Center items vertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { newText ->
                    if (newText.all { it.isDigit() }) {
                        text = newText
                    }
                },
                label = { Text("Kiracı Kodu") },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 40.dp)// Center horizontally
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End), // Align button to bottom center
                onClick = {


                    if (text.isNotEmpty()) {

                        viewModel.checkRenterCode(text.toLong())
                        Log.d("ButtonClick",   "Buttonclicked! Renter code: ${text}")

                        Log.d("MyComposableUI", "Button clicked! Renter code: ${text.toLong()}")

                    }
                }
            ) {
                Text("Giriş Yap")
            }

            LaunchedEffect(response) {
                response?.let {
                    if (it.isSuccess) {
                        Common.renterCode = text.toLong()
                        navController.navigate("Login/${text.toLong()}")
                    }
                }
            }

            ErrorDialog(
                showDialog = showDialog,
                onDismiss = { viewModel.dismissDialog() },
                message = errorMessage
            )
        }
    }
}
