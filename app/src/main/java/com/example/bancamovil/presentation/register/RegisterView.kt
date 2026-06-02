package com.example.bancamovil.presentation.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bancamovil.R
import java.io.File

@Composable
fun RegisterView(
    viewModel: RegisterViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var documentNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showLoadingAlert by remember { mutableStateOf(false) }
    var showMessageAlert by remember { mutableStateOf(false) }
    var titleDialog by remember { mutableStateOf("") }
    var messageDialog by remember { mutableIntStateOf(0) }
    var documentImageUri by remember { mutableStateOf<Uri?>(null) }
    var documentUploaded by remember { mutableStateOf(false) }

    val successTitle = stringResource(R.string.dialog_success_title)
    val errorTitle = stringResource(R.string.dialog_error_title)

    // URI temporal para la foto
    val photoUri = remember {
        val file = File(context.cacheDir, "documento_temp.jpg")
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    // Launcher de cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            documentImageUri = photoUri
        }
    }

    // Launcher de permiso de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) cameraLauncher.launch(photoUri)
    }

    if (showLoadingAlert) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = { Text(stringResource(R.string.text_loading), color = MaterialTheme.colorScheme.onSurface) },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    if (showMessageAlert) {
        AlertDialog(
            onDismissRequest = { showMessageAlert = false },
            confirmButton = {
                TextButton(onClick = {
                    showMessageAlert = false
                    if (titleDialog == successTitle) navController.popBackStack()
                }) {
                    Text(stringResource(R.string.btn_accept), color = MaterialTheme.colorScheme.primary)
                }
            },
            title = { Text(titleDialog, color = MaterialTheme.colorScheme.onSurface) },
            text = { Text(stringResource(id = messageDialog), color = MaterialTheme.colorScheme.secondary) },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Title zone
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f).wrapContentHeight(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.login_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.register_title),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Form + Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val fieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text(stringResource(R.string.label_full_name), color = MaterialTheme.colorScheme.secondary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value = documentNumber,
                onValueChange = { documentNumber = it },
                label = { Text(stringResource(R.string.label_document_number), color = MaterialTheme.colorScheme.secondary) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.label_password), color = MaterialTheme.colorScheme.secondary) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.label_confirm_password), color = MaterialTheme.colorScheme.secondary) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            // Foto del documento
            if (documentImageUri != null) {
                AsyncImage(
                    model = documentImageUri,
                    contentDescription = "Documento",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            2.dp,
                            if (documentUploaded) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(12.dp)
                        )
                )
            }

            // Botón tomar foto
            OutlinedButton(
                onClick = {
                    if (documentImageUri == null) {
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    } else if (!documentUploaded) {
                        viewModel.uploadDocument(context, documentImageUri!!, documentNumber) { success, _ ->
                            if (success) {
                                documentUploaded = true
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (documentUploaded)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = if (documentImageUri == null) "Tomar foto del documento"
                    else if (documentUploaded) "✓ Documento subido"
                    else "Subir documento",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Botón registrarse
            Button(
                onClick = {
                    showLoadingAlert = true

                    if (documentImageUri != null && !documentUploaded) {
                        viewModel.uploadDocument(context, documentImageUri!!, documentNumber) { success, _ ->
                            if (success) {
                                documentUploaded = true
                                viewModel.register(fullName, documentNumber, password, confirmPassword) { regSuccess, message ->
                                    titleDialog = if (regSuccess) successTitle else errorTitle
                                    messageDialog = message
                                    showLoadingAlert = false
                                    showMessageAlert = true
                                }
                            } else {
                                titleDialog = errorTitle
                                messageDialog = R.string.error_register_failed
                                showLoadingAlert = false
                                showMessageAlert = true
                            }
                        }
                    } else {
                        viewModel.register(fullName, documentNumber, password, confirmPassword) { regSuccess, message ->
                            titleDialog = if (regSuccess) successTitle else errorTitle
                            messageDialog = message
                            showLoadingAlert = false
                            showMessageAlert = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    stringResource(R.string.btn_register),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.text_already_have_account),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 13.sp
                )
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(
                        stringResource(R.string.text_login_here),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}