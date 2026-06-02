package com.example.bancamovil.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bancamovil.R
import com.example.bancamovil.data.repository.FirebaseAuthRepositoryImpl
import com.example.bancamovil.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase = LoginUseCase(FirebaseAuthRepositoryImpl())
) : ViewModel() {

    fun login(
        documentNumber: String,
        password: String,
        onResult: (Boolean, Int) -> Unit
    ) {
        if (documentNumber.isBlank() || password.isBlank()) {
            onResult(false, R.string.error_fields_empty)
            return
        }
        if (documentNumber.length < 6) {
            onResult(false, R.string.error_document_length)
            return
        }
        viewModelScope.launch {
            val (success, _) = loginUseCase(documentNumber, password)
            if (success) {
                onResult(true, 0)
            } else {
                onResult(false, R.string.error_login_failed)
            }
        }
    }
}