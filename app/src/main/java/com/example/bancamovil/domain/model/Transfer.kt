package com.example.bancamovil.domain.model

data class Transfer(
    val id: String,
    val monto: Double,
    val cuenta: String,
    val fecha: String,
    val tipo: String
)
