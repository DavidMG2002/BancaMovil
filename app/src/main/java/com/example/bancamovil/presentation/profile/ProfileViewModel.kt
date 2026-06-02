package com.example.bancamovil.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bancamovil.data.datasource.FirebaseUserDataSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel(
    private val dataSource: FirebaseUserDataSource = FirebaseUserDataSource()
) : ViewModel() {

    fun loadProfile(
        documentNumber: String,
        onResult: (Boolean, Map<String, String>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val snapshot = dataSource.getUser(documentNumber).await()
                if (!snapshot.exists()) {
                    onResult(false, emptyMap())
                    return@launch
                }
                val data = mapOf(
                    "fullName" to (snapshot.child("fullName").value?.toString() ?: ""),
                    "documentNumber" to documentNumber,
                    "contrasena" to (snapshot.child("contrasena").value?.toString() ?: ""),
                    "saldo" to (snapshot.child("saldo").value?.toString() ?: "0")
                )
                onResult(true, data)
            } catch (e: Exception) {
                onResult(false, emptyMap())
            }
        }
    }
}