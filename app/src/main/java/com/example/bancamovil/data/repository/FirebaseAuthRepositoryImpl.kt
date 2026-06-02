package com.example.bancamovil.data.repository

import com.example.bancamovil.R
import com.example.bancamovil.data.datasource.FirebaseUserDataSource
import com.example.bancamovil.domain.model.User
import com.example.bancamovil.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val dataSource: FirebaseUserDataSource = FirebaseUserDataSource()
) : AuthRepository {

    override suspend fun login(documentNumber: String, password: String): Pair<Boolean, Int> {
        return try {
            val dataUser = dataSource.getUser(documentNumber).await()
            if (!dataUser.exists()) return Pair(false, R.string.error_login_failed)
            val dbPassword = dataUser.child("contrasena").value.toString()
            if (dbPassword.trim() == password.trim()) Pair(true, 0)
            else Pair(false, R.string.error_login_failed)
        } catch (e: Exception) {
            Pair(false, R.string.error_login_failed)
        }
    }

    override suspend fun register(user: User): Pair<Boolean, Int> {
        val userData = mapOf<String, Any>(
            "fullName" to user.fullName,
            "contrasena" to user.password,
            "saldo" to 0
        )
        return try {
            dataSource.saveUser(user.documentNumber, userData).await()
            Pair(true, R.string.register_success_message)
        } catch (e: Exception) {
            android.util.Log.e("FIREBASE_ERROR", "Error al registrar: ${e.message}")
            Pair(false, R.string.error_register_failed)
        }
    }
}