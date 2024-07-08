package com.scy.paystock.views.addProduct

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.scy.paystock.views.login.ui.theme.PayStockTheme
import com.scy.paystock.views.notes.CreateTopAppBar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.scy.paystock.R
import com.scy.paystock.views.companents.ErrorDialog

@ExperimentalPermissionsApi

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddProductView(navController: NavHostController , viewModel: AddProductViewModel = AddProductViewModel()) {
    var productCode by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val scannedBarcode = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("scannedBarcode")
    val showDialog by viewModel.showDialog.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val errorMessage by viewModel.message.collectAsState()
    val errorTitle by viewModel.title.collectAsState()
    Scaffold(
        topBar = {
            CreateTopAppBar(navController = navController, title = "Ürün Ekle")
        }
    ) {
       Box (
           modifier = Modifier
               .fillMaxSize(),
           contentAlignment = Alignment.Center
       ){
           Column(
               modifier = Modifier.padding(16.dp),
                       horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Center,

           ) {
               if (showLoading) {
                   // Show loading indicator when showDialog is true
                   CircularProgressIndicator(
                       modifier = Modifier.size(50.dp),
                       color = MaterialTheme.colorScheme.primary
                   )
               }
              else {
                   TextField(
                       value = productCode,
                       onValueChange = { productCode = it },
                       singleLine = true,
                       label = { Text("Ürün Kodu") },

                       trailingIcon = {
                           IconButton(onClick = {


                               if (cameraPermissionState.status.isGranted) {
                                   navController.navigate("Camera")
                               } else if (cameraPermissionState.status.shouldShowRationale) {

                               } else {

                                   cameraPermissionState.run { launchPermissionRequest() }


                               }

                           }) {

                               // Please provide localized description for accessibility services
                               Image(
                                   painter = painterResource(id = R.drawable.scanner), // Replace R.drawable.splash_image with your image resource
                                   contentDescription = null,  // Fill the entire screen
                               )

                           }
                       },
                       modifier = Modifier.fillMaxWidth()
                   )
                   Spacer(modifier = Modifier.height(16.dp))
                   TextField(
                       value = productName,
                       onValueChange = { productName = it },
                       label = { Text("Ürün Adı") },
                       singleLine = true,
                       modifier = Modifier.fillMaxWidth()
                   )
                   Spacer(modifier = Modifier.height(16.dp))
                   TextField(
                       value = productQuantity,
                       onValueChange = { productQuantity = it },
                       label = { Text("Ürün Adedi") },
                       singleLine = true,
                       modifier = Modifier.fillMaxWidth()
                   )
                   Spacer(modifier = Modifier.height(16.dp))
                   TextField(
                       value = productPrice,
                       onValueChange = { productPrice = it },
                       label = { Text("Ürün Fiyatı") },
                       singleLine = true,
                       modifier = Modifier.fillMaxWidth()
                   )
                   Spacer(modifier = Modifier.height(16.dp))
                   Button(
                       onClick = {
                           viewModel.createProduct(dto = AddProductDto(
                               productName = productName,
                               productCode = productCode,
                               productPrice = productPrice,
                               productQuantity = productQuantity
                           ))
                       },
                       modifier = Modifier
                           .align(  Alignment.End)
                   ) {
                       Text("Ekle")
                   }

                   ErrorDialog(
                       title = errorTitle,
                       showDialog = showDialog,
                       onDismiss = { viewModel.dismissDialog() },
                       message = errorMessage
                   )
              }
           }
       }
        if (scannedBarcode != null) {
            LaunchedEffect(scannedBarcode.value) {
                scannedBarcode.let { barcode ->
                    Log.d("AddProductView", "Scanned Barcode: $barcode")
                    // Handle the scanned barcode data here
                    val barcodeValue = barcode.value.toString()
                    if (barcodeValue.isNotEmpty() && barcodeValue != "null") {
                        productCode = barcodeValue
                        Log.d("AddProductView", "Scanned Barcode: $barcodeValue")
                    } else {
                        Log.d("AddProductView", "No valid barcode detected")
                    }

                }
            }
        }
    }

    }

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
fun AddProductViewPreview() {
    PayStockTheme {
        AddProductView( navController = rememberNavController(), viewModel = AddProductViewModel())
    }
}