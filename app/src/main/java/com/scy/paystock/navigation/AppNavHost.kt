package com.scy.paystock.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.scy.paystock.views.Main.SplashScreen
import com.scy.paystock.views.addProduct.AddProductView
import com.scy.paystock.views.addProduct.AnalyzerType
import com.scy.paystock.views.addProduct.CameraView
import com.scy.paystock.views.home.HomeView
import com.scy.paystock.views.login.LoginView
import com.scy.paystock.views.login.LoginViewModel
import com.scy.paystock.views.notes.NotesView
import com.scy.paystock.views.renterCode.RenterCodeView
import com.scy.paystock.views.renterCode.RenterCodeViewModel
import com.scy.paystock.views.sale.SaleView
import com.scy.paystock.views.saleReport.SaleReportView
import com.scy.paystock.views.stockQuery.StockQueryView
import com.scy.paystock.views.stockQuery.StockQueryViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppNavHost(){


    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavigationItem.Splash.route) {
        composable(route =  NavigationItem.Splash.route) {
            SplashScreen(navController)
        }
        composable(route =  NavigationItem.RenterCode.route) {
            RenterCodeView(viewModel = RenterCodeViewModel(),
                navController)
        }
        composable(route = NavigationItem.Login.route) {backStackEntry ->
            val renterCode = backStackEntry.arguments?.getString("renterCode")
            LoginView(viewModel = LoginViewModel(renterCode?.toLong() ?: 0),navController)
        }
        composable(route = NavigationItem.Home.route) {
            HomeView(navController)
        }
        composable(route = NavigationItem.SaleReport.route) {
            SaleReportView(navController)
        }
        composable(route = NavigationItem.Sale.route) {
            SaleView(navController)
        }
        composable(route = NavigationItem.Notes.route) {
            NotesView(navController)
        }
        composable(route = NavigationItem.StockQuery.route) {
            StockQueryView(navController )
        }
        composable(route = NavigationItem.AddProduct.route) {
            AddProductView(navController)
        }
        composable(route = NavigationItem.Camera.route) {
            CameraView(analyzerType = AnalyzerType.BARCODE, navController = navController)
        }

    }
}