package com.example.bancamovil.presentation.register

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bancamovil.R
import com.example.bancamovil.data.datasource.DocumentCameraDataSource
import com.example.bancamovil.data.repository.FirebaseAuthRepositoryImpl
import com.example.bancamovil.domain.model.User
import com.example.bancamovil.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase = RegisterUseCase(FirebaseAuthRepositoryImpl()),
    private val documentDataSource: DocumentCameraDataSource = DocumentCameraDataSource()
) : ViewModel() {

    private val specialChars = Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]")

    fun register(
        fullName: String,
        documentNumber: String,
        password: String,
        confirmPassword: String,
        onResult: (Boolean, Int) -> Unit
    ) {
        if (fullName.isBlank() || documentNumber.isBlank() ||
            password.isBlank() || confirmPassword.isBlank()
        ) {
            onResult(false, R.string.error_fields_empty)
            return
        }
        if (fullName.length > 50) {
            onResult(false, R.string.error_name_length)
            return
        }
        if (documentNumber.length < 6) {
            onResult(false, R.string.error_document_length)
            return
        }
        if (password.length < 6 || password.length > 20) {
            onResult(false, R.string.error_password_length)
            return
        }
        if (!specialChars.containsMatchIn(password)) {
            onResult(false, R.string.error_password_special_char)
            return
        }
        if (password != confirmPassword) {
            onResult(false, R.string.error_passwords_match)
            return
        }
        val user = User(
            fullName = fullName,
            documentNumber = documentNumber,
            password = password
        )
        viewModelScope.launch {
            val (success, messageRes) = registerUseCase(user)
            onResult(success, messageRes)
        }
    }

    fun uploadDocument(
        context: Context,
        imageUri: Uri,
        documentNumber: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val url = documentDataSource.uploadDocument(context, imageUri, documentNumber)
                onResult(true, url)
            } catch (e: Exception) {
                onResult(false, e.message ?: "Error al subir imagen")
            }
        }
    }
}
