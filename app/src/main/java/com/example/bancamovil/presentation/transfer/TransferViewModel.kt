package com.example.bancamovil.presentation.transfer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bancamovil.R
import com.example.bancamovil.data.repository.FirebaseUserRepositoryImpl
import com.example.bancamovil.domain.usecase.TransferUseCase
import kotlinx.coroutines.launch

class TransferViewModel(
    private val transferUseCase: TransferUseCase = TransferUseCase(FirebaseUserRepositoryImpl())
) : ViewModel() {

    val accountNumber = mutableStateOf("")
    val amount = mutableStateOf("")
    val message = mutableStateOf(0)

    fun onAccountChange(value: String) { accountNumber.value = value }
    fun onAmountChange(value: String) { amount.value = value }

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
            val (success, messageRes) = transferUseCase(currentUser, accountNumber.value, transferAmount)
            message.value = messageRes
        }
    }
}
