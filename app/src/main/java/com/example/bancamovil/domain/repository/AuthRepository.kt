package com.example.bancamovil.domain.repository

import com.example.bancamovil.domain.model.User

interface AuthRepository {
    suspend fun login(documentNumber: String, password: String): Pair<Boolean, Int>
    suspend fun register(user: User): Pair<Boolean, Int>
}
