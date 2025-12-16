package com.example.prueba_n2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "comentarios")
data class Comentario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    // Xano: producto_id
    @SerializedName("producto_id", alternate = ["productoId"])
    val productoId: String = "",
    
    // Xano: usuario_nombre
    @SerializedName("usuario_nombre", alternate = ["usuarioNombre"])
    val usuarioNombre: String = "Anónimo",
    
    @SerializedName("texto")
    val texto: String = "",
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)