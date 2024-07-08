package com.scy.paystock.views.companents

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String = "Hata",
    message: String = "İşlem başarısız oldu. Lütfen tekrar deneyin."
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = title) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Tamam")
                }
            }
        )
    }
}