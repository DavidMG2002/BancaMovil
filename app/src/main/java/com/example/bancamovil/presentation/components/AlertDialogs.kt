package com.example.bancamovil.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.bancamovil.R
import com.example.bancamovil.presentation.login.Pink

@Composable
fun ShowLoadingAlertDialog() {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(R.string.text_loading), color = Color.White) },
        text = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Pink)
            }
        },
        confirmButton = { },
        containerColor = Color(0xFF1A1A1A)
    )
}

@Composable
fun ShowMessageAlertDialog(
    onConfirmation: () -> Unit,
    dialogTitle: Int,
    dialogText: Int
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(dialogTitle), color = Color.White) },
        text = { Text(stringResource(dialogText), color = Color.Gray) },
        confirmButton = {
            Button(
                onClick = { onConfirmation() },
                colors = ButtonDefaults.buttonColors(containerColor = Pink)
            ) {
                Text(stringResource(R.string.btn_accept), color = Color.White)
            }
        },
        containerColor = Color(0xFF1A1A1A)
    )
}