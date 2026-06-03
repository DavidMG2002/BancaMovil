package com.example.bancamovil.data.repository

import com.example.bancamovil.R
import com.example.bancamovil.data.datasource.FirebaseUserDataSource
import com.example.bancamovil.domain.model.User
import com.example.bancamovil.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest

class FirebaseAuthRepositoryImpl(
    private val dataSource: FirebaseUserDataSource = FirebaseUserDataSource()
) : AuthRepository {

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    override suspend fun login(documentNumber: String, password: String): Pair<Boolean, Int> {
        return try {
            val snapshot = dataSource.getUser(documentNumber).await()
            if (!snapshot.exists()) return Pair(false, R.string.error_login_failed)
            val dbPassword = snapshot.child("contrasena").value?.toString() ?: ""
            val hashedInput = hashPassword(password)
            if (dbPassword.trim() == hashedInput.trim()) Pair(true, 0)
            else Pair(false, R.string.error_login_failed)
        } catch (e: Exception) {
            Pair(false, R.string.error_login_failed)
        }
    }

    override suspend fun register(user: User): Pair<Boolean, Int> {
        return try {
            // Verificar si la cédula ya existe
            val existing = dataSource.getUser(user.documentNumber).await()
            if (existing.exists()) {
                return Pair(false, R.string.error_document_already_registered)
            }
            val userData = mapOf<String, Any>(
                "fullName" to user.fullName,
                "contrasena" to hashPassword(user.password),
                "saldo" to 0
            )
            dataSource.saveUser(user.documentNumber, userData).await()
            Pair(true, R.string.register_success_message)
        } catch (e: Exception) {
            Pair(false, R.string.error_register_failed)
        }
    }
}
