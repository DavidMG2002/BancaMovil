package com.example.bancamovil.domain.usecase

import com.example.bancamovil.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        documentNumber: String,
        password: String
    ): Pair<Boolean, Int> {
        return repository.login(documentNumber, password)
    }
}
