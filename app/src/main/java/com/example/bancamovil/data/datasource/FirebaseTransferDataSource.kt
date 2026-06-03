package com.example.bancamovil.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FirebaseTransferDataSource {
    private val transferencias = FirebaseDatabase.getInstance().getReference("transferencias")

    fun saveTransfer(
        senderDocument: String,
        receiverDocument: String,
        amount: Double
    ): Task<Void> {
        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        transferencias.child(senderDocument).push().setValue(
            mapOf("monto" to amount, "cuenta" to receiverDocument, "fecha" to fecha, "tipo" to "enviado")
        )
        return transferencias.child(receiverDocument).push().setValue(
            mapOf("monto" to amount, "cuenta" to senderDocument, "fecha" to fecha, "tipo" to "recibido")
        )
    }

    fun getHistory(documentNumber: String): Task<DataSnapshot> {
        return transferencias.child(documentNumber).get()
    }
}