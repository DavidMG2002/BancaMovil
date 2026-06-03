package com.example.bancamovil.presentation.history

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bancamovil.data.repository.FirebaseUserRepositoryImpl
import com.example.bancamovil.domain.model.Transfer
import com.example.bancamovil.domain.usecase.GetHistoryUseCase
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getHistoryUseCase: GetHistoryUseCase = GetHistoryUseCase(FirebaseUserRepositoryImpl())
) : ViewModel() {

    val transferencias = mutableStateOf<List<Transfer>>(emptyList())
    val isLoading = mutableStateOf(true)

    fun cargarHistorial(documentNumber: String) {
        viewModelScope.launch {
            try {
                transferencias.value = getHistoryUseCase(documentNumber)
            } catch (e: Exception) {
                transferencias.value = emptyList()
            } finally {
                isLoading.value = false
            }
        }
    }
}
