package com.scy.paystock.views.home

import com.scy.paystock.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.scy.paystock.views.login.LoginView
import com.scy.paystock.views.login.LoginViewModel
import com.scy.paystock.views.login.ui.theme.PayStockTheme

@Composable
fun GridItem(
    iconId: Int, // ID of the icon resource
    text: String, // Text to be displayed
    onClick: () -> Unit, // Navigation callback
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp), // Set rounded corners
        modifier = modifier
            .size(200.dp)
            .padding(8.dp) // Add padding around the button content
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = text
            )
            Text(text)
        }
    }
}

@Composable
fun HomeView(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GridItem(
                iconId = R.drawable.ic_launcher_foreground, // Replace with your icon resources
                text = "Satış",
                onClick = { navController.navigate("Sale") }
            )
            GridItem(
                iconId = R.drawable.ic_launcher_foreground,
                text = "Ürün Ekle",
                onClick = { navController.navigate("AddProduct") }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GridItem(
                iconId = R.drawable.ic_launcher_foreground,
                text = "Stok Sorgulama",
                onClick = { navController.navigate("StockQuery") }
            )
            GridItem(
                iconId = R.drawable.ic_launcher_foreground,
                text = "Satış Raporu Sorgulama",
                onClick = { navController.navigate("SaleReport") }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GridItem(
                iconId = R.drawable.ic_launcher_foreground,
                text = "Sevkiyat Notları",
                onClick = { navController.navigate("Notes") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PayStockTheme {
        HomeView( navController = rememberNavController())
    }
}