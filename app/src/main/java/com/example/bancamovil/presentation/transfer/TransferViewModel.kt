package com.example.bancamovil.presentation.transfer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bancamovil.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TransferViewModel : ViewModel() {

    val accountNumber = mutableStateOf("")
    val amount = mutableStateOf("")
    val message = mutableStateOf(0)

    private val database = FirebaseDatabase.getInstance().getReference("usuarios")

    fun onAccountChange(value: String) {
        accountNumber.value = value
    }

    fun onAmountChange(value: String) {
        amount.value = value
    }

    fun transfer(currentUser: String) {
        if (accountNumber.value.isEmpty() || amount.value.isEmpty()) {
            message.value = R.string.error_fields_empty
            return
        }

        val transferAmount = amount.value.toDoubleOrNull()
        if (transferAmount == null || transferAmount <= 0) {
            message.value = R.string.msg_invalid_amount
            return
        }

        viewModelScope.launch {
            try {
                val senderSnapshot = database.child(currentUser).get().await()
                val senderBalance =
                    senderSnapshot.child("saldo").getValue(Double::class.java) ?: 0.0

                if (senderBalance < transferAmount) {
                    message.value = R.string.msg_insufficient_balance
                    return@launch
                }

                val receiverSnapshot = database.child(accountNumber.value).get().await()
                if (!receiverSnapshot.exists()) {
                    message.value = R.string.msg_account_not_found
                    return@launch
                }

                val receiverBalance =
                    receiverSnapshot.child("saldo").getValue(Double::class.java) ?: 0.0

                database.child(currentUser).child("saldo").setValue(senderBalance - transferAmount)
                database.child(accountNumber.value).child("saldo")
                    .setValue(receiverBalance + transferAmount)

                message.value = R.string.msg_transfer_success
            } catch (e: Exception) {
                message.value = R.string.error_login_failed
            }
        }
    }
}