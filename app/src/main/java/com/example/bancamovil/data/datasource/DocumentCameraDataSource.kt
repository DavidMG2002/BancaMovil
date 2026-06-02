package com.example.bancamovil.data.datasource

import android.content.Context
import android.net.Uri
import android.util.Log
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DocumentCameraDataSource {

    suspend fun uploadDocument(
        context: Context,
        imageUri: Uri,
        documentNumber: String
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SUPABASE_DEBUG", "Leyendo imagen...")
                val bytes = context.contentResolver
                    .openInputStream(imageUri)
                    ?.readBytes()
                    ?: throw Exception("No se pudo leer la imagen")

                Log.d("SUPABASE_DEBUG", "Imagen leída: ${bytes.size} bytes")

                val fileName = "documento_$documentNumber.jpg"
                Log.d("SUPABASE_DEBUG", "Subiendo archivo: $fileName")

                SupabaseClient.client.storage
                    .from("documentos")
                    .upload(fileName, bytes, upsert = true)

                Log.d("SUPABASE_DEBUG", "Subida exitosa")

                "https://eweetgaqfdbovbpkbbcm.supabase.co/storage/v1/object/public/documentos/$fileName"
            } catch (e: Exception) {
                Log.e("SUPABASE_DEBUG", "Error: ${e.message}", e)
                throw e
            }
        }
    }
}