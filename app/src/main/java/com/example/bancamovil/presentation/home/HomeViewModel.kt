package com.example.bancamovil.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bancamovil.data.repository.FirebaseUserRepositoryImpl
import com.example.bancamovil.domain.usecase.GetUserUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserUseCase: GetUserUseCase = GetUserUseCase(FirebaseUserRepositoryImpl())
) : ViewModel() {

    val fullName = mutableStateOf("")
    val saldo = mutableStateOf(0.0)

    fun cargarDatos(documento: String) {
        viewModelScope.launch {
            val userProfile = getUserUseCase(documento)
            if (userProfile != null) {
                fullName.value = userProfile.fullName
                saldo.value = userProfile.saldo
            }
        }
    }
}