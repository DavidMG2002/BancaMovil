package com.example.bancamovil.data.datasource

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://roolsxviqdwchomspvsz.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJvb2xzeHZpcWR3Y2hvbXNwdnN6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODAzNDQwMjAsImV4cCI6MjA5NTkyMDAyMH0.7G6I0tP5tuspTnL5h5kVUyo12GS0VIZGH8uboDc6vcA"
    ) {
        install(Storage)
    }
}