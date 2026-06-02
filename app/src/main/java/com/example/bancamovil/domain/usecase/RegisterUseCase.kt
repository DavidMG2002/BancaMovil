package com.example.bancamovil.domain.usecase

import com.example.bancamovil.domain.model.User
import com.example.bancamovil.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(user: User): Pair<Boolean, Int> {
        return repository.register(user)

    }
}