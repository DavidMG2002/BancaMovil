package com.example.bancamovil.presentation.history

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class Transferencia(
    val id: String,
    val monto: Double,
    val cuenta: String,
    val fecha: String,
    val tipo: String
)

class HistoryViewModel : ViewModel() {
    val transferencias = mutableStateOf<List<Transferencia>>(emptyList())
    val isLoading = mutableStateOf(true)

    private val database = FirebaseDatabase.getInstance().getReference("transferencias")

    fun cargarHistorial(documentNumber: String) {
        viewModelScope.launch {
            try {
                val snapshot = database.child(documentNumber).get().await()
                val lista = mutableListOf<Transferencia>()
                for (item in snapshot.children) {
                    val transferencia = Transferencia(
                        id = item.key ?: "",
                        monto = item.child("monto").getValue(Double::class.java) ?: 0.0,
                        cuenta = item.child("cuenta").value?.toString() ?: "",
                        fecha = item.child("fecha").value?.toString() ?: "",
                        tipo = item.child("tipo").value?.toString() ?: ""
                    )
                    lista.add(transferencia)
                }
                transferencias.value = lista.sortedByDescending { it.fecha }
            } catch (e: Exception) {
                // lista queda vacía
            } finally {
                isLoading.value = false
            }
        }
    }
}