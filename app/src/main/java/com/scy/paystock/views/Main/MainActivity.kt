package com.scy.paystock.views.Main
import android.os.Handler
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.scy.paystock.R
import com.scy.paystock.navigation.AppNavHost
import com.scy.paystock.views.Main.ui.theme.PayStockTheme

import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavHost()
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.splash_image), // Replace R.drawable.splash_image with your image resource
        contentDescription = null, // Content description for accessibility
        modifier = Modifier.fillMaxSize() // Fill the entire screen
    )
    LaunchedEffect(Unit) {
        delay(1000) // Delay for 2 seconds (adjust as needed)
        navController.navigate("RenterCode")
    }
}

