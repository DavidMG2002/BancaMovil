package com.example.bancamovil.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {

    val fullName = mutableStateOf("")
    val saldo = mutableStateOf(0.0)

    private val database = FirebaseDatabase.getInstance()
        .getReference("usuarios")

    fun cargarDatos(documento: String) {
        viewModelScope.launch {
            try {
                val snapshot = database.child(documento).get().await()
                fullName.value =
                    snapshot.child("fullName").getValue(String::class.java)
                        ?: snapshot.child("nombre").getValue(String::class.java)
                                ?: ""
                saldo.value = snapshot.child("saldo").getValue(Double::class.java) ?: 0.0
            } catch (e: Exception) {
                // Error al cargar datos
            }
        }
    }
}