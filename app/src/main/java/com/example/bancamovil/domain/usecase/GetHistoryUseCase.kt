package com.example.bancamovil.domain.usecase

import com.example.bancamovil.domain.model.Transfer
import com.example.bancamovil.domain.repository.UserRepository

class GetHistoryUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(documentNumber: String): List<Transfer> {
        return repository.getHistory(documentNumber)
    }
}
