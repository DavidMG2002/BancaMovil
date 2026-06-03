package com.example.bancamovil.data.repository

import com.example.bancamovil.R
import com.example.bancamovil.data.datasource.FirebaseTransferDataSource
import com.example.bancamovil.data.datasource.FirebaseUserDataSource
import com.example.bancamovil.domain.model.Transfer
import com.example.bancamovil.domain.model.UserProfile
import com.example.bancamovil.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class FirebaseUserRepositoryImpl(
    private val userDataSource: FirebaseUserDataSource = FirebaseUserDataSource(),
    private val transferDataSource: FirebaseTransferDataSource = FirebaseTransferDataSource()
) : UserRepository {

    override suspend fun getUser(documentNumber: String): UserProfile? {
        return try {
            val snapshot = userDataSource.getUser(documentNumber).await()
            if (!snapshot.exists()) return null
            UserProfile(
                fullName = snapshot.child("fullName").value?.toString() ?: "",
                documentNumber = documentNumber,
                saldo = snapshot.child("saldo").getValue(Double::class.java) ?: 0.0
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun transfer(
        senderDocument: String,
        receiverDocument: String,
        amount: Double
    ): Pair<Boolean, Int> {
        return try {
            val senderSnapshot = userDataSource.getUser(senderDocument).await()
            val senderBalance = senderSnapshot.child("saldo").getValue(Double::class.java) ?: 0.0

            if (senderBalance < amount) {
                return Pair(false, R.string.msg_insufficient_balance)
            }

            val receiverSnapshot = userDataSource.getUser(receiverDocument).await()
            if (!receiverSnapshot.exists()) {
                return Pair(false, R.string.msg_account_not_found)
            }

            val receiverBalance = receiverSnapshot.child("saldo").getValue(Double::class.java) ?: 0.0

            userDataSource.updateBalance(senderDocument, senderBalance - amount).await()
            userDataSource.updateBalance(receiverDocument, receiverBalance + amount).await()
            transferDataSource.saveTransfer(senderDocument, receiverDocument, amount).await()

            Pair(true, R.string.msg_transfer_success)
        } catch (e: Exception) {
            Pair(false, R.string.error_login_failed)
        }
    }

    override suspend fun getHistory(documentNumber: String): List<Transfer> {
        return try {
            val snapshot = transferDataSource.getHistory(documentNumber).await()
            val lista = mutableListOf<Transfer>()
            for (item in snapshot.children) {
                lista.add(
                    Transfer(
                        id = item.key ?: "",
                        monto = item.child("monto").getValue(Double::class.java) ?: 0.0,
                        cuenta = item.child("cuenta").value?.toString() ?: "",
                        fecha = item.child("fecha").value?.toString() ?: "",
                        tipo = item.child("tipo").value?.toString() ?: ""
                    )
                )
            }
            lista.sortedByDescending { it.fecha }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
