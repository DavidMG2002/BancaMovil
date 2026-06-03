package com.example.bancamovil.data.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class FirebaseUserDataSource {
    private val database = FirebaseDatabase.getInstance().getReference("usuarios")

    fun getUser(documentNumber: String): Task<DataSnapshot> {
        return database.child(documentNumber).get()
    }

    fun saveUser(documentNumber: String, userData: Map<String, Any>): Task<Void> {
        return database.child(documentNumber).setValue(userData)
    }

    fun updateBalance(documentNumber: String, newBalance: Double): Task<Void> {
        return database.child(documentNumber).child("saldo").setValue(newBalance)
    }
}