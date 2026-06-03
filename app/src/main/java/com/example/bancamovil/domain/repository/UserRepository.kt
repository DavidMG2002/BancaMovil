package com.example.bancamovil.domain.repository

import com.example.bancamovil.domain.model.Transfer
import com.example.bancamovil.domain.model.UserProfile

interface UserRepository {
    suspend fun getUser(documentNumber: String): UserProfile?
    suspend fun transfer(
        senderDocument: String,
        receiverDocument: String,
        amount: Double
    ): Pair<Boolean, Int>
    suspend fun getHistory(documentNumber: String): List<Transfer>
}
