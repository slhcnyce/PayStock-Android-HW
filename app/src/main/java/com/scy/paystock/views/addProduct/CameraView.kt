package com.scy.paystock.views.addProduct

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.scy.paystock.views.notes.CreateTopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CameraView(analyzerType: AnalyzerType, navController: NavHostController ) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }
    Scaffold(
        topBar = {
            CreateTopAppBar(navController = navController, title = "Ürün Ekle")
        }
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                preview.setSurfaceProvider(previewView.surfaceProvider)

                val imageAnalysis = ImageAnalysis.Builder().build()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),

                    BarcodeAnalyzer(context) {scannedData ->
                        navController.previousBackStackEntry?.savedStateHandle?.set("scannedBarcode", scannedData)
                        navController.popBackStack()

                    }

                )

                runCatching {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis
                    )
                }.onFailure {
                    Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
                }
                previewView
            }
        )
    }
}