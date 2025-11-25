package com.example.prueba_n2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comentarios")
data class Comentario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: String,
    val usuarioNombre: String,
    val texto: String,
    val timestamp: Long = System.currentTimeMillis()
)
