package com.example.prueba_n2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @SerializedName("nombre", alternate = ["name"])
    val nombre: String? = null,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("contrasena", alternate = ["password"])
    val contrasena: String? = null
)