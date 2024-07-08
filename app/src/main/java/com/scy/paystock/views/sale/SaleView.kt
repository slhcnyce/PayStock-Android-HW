package com.scy.paystock.views.sale

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.scy.paystock.R
import com.scy.paystock.views.companents.ErrorDialog
import com.scy.paystock.views.login.ui.theme.PayStockTheme
import com.scy.paystock.views.notes.CreateTopAppBar
import com.scy.paystock.views.stockQuery.Product

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SaleView(navController: NavHostController, viewModel: SaleViewModel = SaleViewModel()) {
    // Dummy list of products
    val productList by viewModel.productList.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val errorMessage by viewModel.message.collectAsState()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val scannedBarcode = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("scannedBarcode")
    // State for text input in TextField
    val searchTextState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Calculate total price
    val totalPrice by viewModel.totalPrice.collectAsState()

    Scaffold(
        topBar = {
            CreateTopAppBar(
                navController = navController,
                title = "Satış"
            )
        },
        bottomBar = {
            // Bottom bar with total price and Ödeme Al button
            BottomAppBar(
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .heightIn(min= 48.dp),

                    ) {
                        // Total price display
                        Text(
                            text = "Toplam Tutar: $totalPrice TL",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        // Ödeme Al button
                        Button(
                            onClick = {
                                      viewModel.makePayment()
                                      },
                            modifier = Modifier
                                .fillMaxWidth()
                                 .heightIn(min= 48.dp)
                                .padding(horizontal = 8.dp)

                        ) {
                            Text(text = "Ödeme Al" ,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp)
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = AppBarHeight)
        ) {
            // TextField for searching or entering data
            OutlinedTextField(
                value = searchTextState.value,
                onValueChange = { searchTextState.value = it },
                label = { Text("Ürün Ekle") },
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
                },// Label for the TextField
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done // Specify the IME action
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Perform your action here (e.g., submit search)
                        viewModel.searchProduct(searchTextState.value)
                        // Hide the keyboard programmatically
                        keyboardController?.hide()
                    }
                )
            )

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (showLoading) {
                    // Show loading indicator when loading is true
                    CircularProgressIndicator()
                } else {
                    // Show product list when loading is false
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        productList?.let { productList ->
                            items(productList) { product ->
                                SaleListItem(
                                    product = product,
                                    onIncrement = { viewModel.increment(product.productCode) },
                                    onDecrement = { viewModel.decrement(product.productCode) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        } ?: run {
                            // Else case, handle empty state or loading state
                        }
                    }
                }
            }

            // Error dialog if there's an error
            ErrorDialog(
                showDialog = showDialog,
                onDismiss = { viewModel.dismissDialog() },
                message = errorMessage
            )
        }
        if (scannedBarcode != null) {
            LaunchedEffect(scannedBarcode.value) {
                scannedBarcode.let { barcode ->
                    Log.d("AddProductView", "Scanned Barcode: $barcode")
                    // Handle the scanned barcode data here
                    val barcodeValue = barcode.value.toString()
                    if (barcodeValue.isNotEmpty() && barcodeValue != "null") {
                        searchTextState.value = barcodeValue
                        Log.d("AddProductView", "Scanned Barcode: $barcodeValue")
                    } else {
                        Log.d("AddProductView", "No valid barcode detected")
                    }

                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SaleViewPreview() {
    PayStockTheme {
        SaleView( navController = rememberNavController())
    }
}
private val AppBarHeight = 56.dp // Adjust this value based on your app's top app bar height


@Composable
fun SaleListItem(
    product: Product,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Product Code: ${product.productCode}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Product Name: ${product.productName}")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Price: ${product.productPrice}")
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onDecrement() }) {
                    Icon(Icons.Default.Clear, contentDescription = "Decrement")
                }
                Text(text = "${product.productTempQuantity}")
                IconButton(onClick = { onIncrement() }) {
                    Icon(Icons.Default.Add, contentDescription = "Increment")
                }
            }
        }
    }
}