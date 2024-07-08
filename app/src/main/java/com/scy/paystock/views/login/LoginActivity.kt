package com.scy.paystock.views.login

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.scy.paystock.views.companents.ErrorDialog
import com.scy.paystock.views.login.ui.theme.PayStockTheme






@Composable
fun LoginView(viewModel: LoginViewModel, navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val showDialog by viewModel.showDialog.collectAsState()
    val errorMessage by viewModel.message.collectAsState()
    val response by viewModel.response.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center // Center items vertically
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { newUsername ->
                username = newUsername
            },
            label = { Text("Kullanıcı Adı") },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp) // Center horizontally
        )
        OutlinedTextField(
            value = password,
            onValueChange = { newPassword ->
                password = newPassword
            },
            label = { Text("Şifre") },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp) // Center horizontally
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End), // Align button to bottom center
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(username, password)
                    // Consider adding success/error handling based on viewModel response
                    // You can use Snackbar or Toast for user feedback
                    Log.d("MyComposableUI", "Button clicked! Username: $username, Password: $password")
                }
            }
        ) {
            Text("Giriş Yap")
        }
        LaunchedEffect(response) {
            response?.let {
                if (it.isSuccess) {
                    navController.navigate("Home")
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
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    PayStockTheme {
        LoginView(LoginViewModel(0), navController = rememberNavController())
    }
}