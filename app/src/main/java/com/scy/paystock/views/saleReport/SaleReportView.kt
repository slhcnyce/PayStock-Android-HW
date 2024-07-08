package com.scy.paystock.views.saleReport

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.scy.paystock.views.companents.ErrorDialog
import com.scy.paystock.views.login.ui.theme.PayStockTheme
import com.scy.paystock.views.notes.CreateTopAppBar
import com.scy.paystock.views.sale.SaleListItem
import com.scy.paystock.views.stockQuery.ProductListItem
import com.scy.paystock.views.stockQuery.StockQueryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SaleReportView(navController: NavHostController,
                   viewModel: SaleReportViewModel = SaleReportViewModel()
) {
    val reportsList by viewModel.response.collectAsState()
    val showLoading by viewModel.showLoading.collectAsState(initial = true)
    val showDialog by viewModel.showDialog.collectAsState()
    val errorMessage by viewModel.message.collectAsState()
    Scaffold(
        topBar = {
            CreateTopAppBar(navController = navController,
                title = "Satış Notları")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = AppBarHeight)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (showLoading) {
                    // Show loading indicator when loading is true
                    CircularProgressIndicator()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        reportsList?.let { reportsList ->
                            items(reportsList) { report ->
                                PaymentDetailItem(report)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        } ?: run {
                            // Else case, handle empty state or loading state
                        }
                    }
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
@Preview(showBackground = true)
@Composable
fun SaleReportViewPreview() {
    PayStockTheme {
        SaleReportView( navController = rememberNavController())
    }
}
private val AppBarHeight = 56.dp

@Composable
fun PaymentDetailItem(paymentDetail: PaymentDetailDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(text = "Date: ${paymentDetail.date}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Price: ${paymentDetail.price}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}