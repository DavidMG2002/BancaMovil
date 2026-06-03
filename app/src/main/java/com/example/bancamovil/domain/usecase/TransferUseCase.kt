package com.example.bancamovil.domain.usecase

import com.example.bancamovil.domain.repository.UserRepository

class TransferUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(
        senderDocument: String,
        receiverDocument: String,
        amount: Double
    ): Pair<Boolean, Int> {
        return repository.transfer(senderDocument, receiverDocument, amount)
    }
}
