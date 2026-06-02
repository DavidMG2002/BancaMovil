package com.example.bancamovil.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bancamovil.R
import java.util.Locale

@Composable
fun HomeView(
    navController: NavController,
    documento: String = "",
    viewModel: HomeViewModel = viewModel()
) {
    LaunchedEffect(documento) {
        if (documento.isNotEmpty()) viewModel.cargarDatos(documento)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(MaterialTheme.colorScheme.tertiary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👤", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(stringResource(R.string.text_hello), color = MaterialTheme.colorScheme.secondary, fontSize = 13.sp)
                    Text(
                        text = viewModel.fullName.value.ifEmpty { "Usuario" },
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            TextButton(onClick = { navController.navigate("profile/$documento") }) {
                Text(stringResource(R.string.text_profile), color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            }
        }

        // Tarjeta de saldo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            Column {
                Text(stringResource(R.string.text_available), color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$ ${String.format(Locale.getDefault(), "%,.0f", viewModel.saldo.value)}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Botones de acción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { navController.navigate("transactions/$documento") },
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.btn_transfer), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary)
            }

            OutlinedButton(
                onClick = { navController.navigate("historial/$documento") },
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
            ) {
                Text(stringResource(R.string.btn_history), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}