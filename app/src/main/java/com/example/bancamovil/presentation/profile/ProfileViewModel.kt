package com.example.bancamovil.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bancamovil.data.repository.FirebaseUserRepositoryImpl
import com.example.bancamovil.domain.usecase.GetUserUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepositoryImpl())
) : ViewModel() {

    fun loadProfile(
        documentNumber: String,
        onResult: (Boolean, Map<String, String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val profile = getUserUseCase(documentNumber)
                if (profile == null) {
                    onResult(false, emptyMap())
                    return@launch
                }
                val data = mapOf(
                    "fullName" to profile.fullName,
                    "documentNumber" to profile.documentNumber,
                    "saldo" to profile.saldo.toString()
                )
                onResult(true, data)
            } catch (e: Exception) {
                onResult(false, emptyMap())
            }
        }
    }
}