package com.example.bancamovil.domain.usecase

import com.example.bancamovil.domain.model.UserProfile
import com.example.bancamovil.domain.repository.UserRepository

class GetUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(documentNumber: String): UserProfile? {
        return repository.getUser(documentNumber)
    }
}
