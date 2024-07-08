package com.scy.paystock.views.stockQuery

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.scy.paystock.views.companents.ErrorDialog
import com.scy.paystock.views.login.ui.theme.PayStockTheme
import com.scy.paystock.views.notes.CreateTopAppBar
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StockQueryView(navController: NavHostController , viewModel: StockQueryViewModel = StockQueryViewModel()) {

    // Dummy list of products
    val productList by viewModel.response.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState()
    val errorMessage by viewModel.message.collectAsState()


    Scaffold(
        topBar = {
            CreateTopAppBar(navController = navController,
                title = "Stok Sorgu")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = AppBarHeight)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (showLoading ) {
                    // Show loading indicator when loading is true
                    CircularProgressIndicator()
                } else {
                    // Show product list when loading is false
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        productList?.let{productList ->
                            items(productList) { product ->
                                ProductListItem(product)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        } ?: run{
                            //else case
                        }

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



@Composable
fun ProductListItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Ürün Adı: ${product.productName}")
            Text("Ürün Adedi: ${product.productQuantity}")
            Text("Ürün Kodu: ${product.productCode}")
            Text("Ürün Fiyatı: ${product.productPrice}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StockQueryViewPreview() {

    PayStockTheme {
        StockQueryView( navController = rememberNavController())
    }
}
private val AppBarHeight = 56.dp // Adjust this value based on your app's top app bar height
