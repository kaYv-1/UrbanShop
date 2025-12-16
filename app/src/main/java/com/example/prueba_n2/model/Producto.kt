package com.example.prueba_n2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey
    @SerializedName("id")
    val id: String = "", 
    
    @SerializedName("name")
    val name: String = "",
    
    @SerializedName("price")
    val price: Int = 0,
    
    @SerializedName("rating")
    val rating: Float = 0.0f,
    
    @SerializedName("description")
    val description: String = "",
    
    @SerializedName("category")
    val category: String = "Varios",
    
    // Xano: image_uri
    @SerializedName("image_uri", alternate = ["imageUri"])
    val imageUri: String? = null,
    
    // Xano: image_res_id
    @SerializedName("image_res_id", alternate = ["imageResId"])
    val imageResId: Int? = null,
    
    // Xano: seller_id
    @SerializedName("seller_id", alternate = ["sellerId"])
    val sellerId: String = "",
    
    // Xano: seller_name
    @SerializedName("seller_name", alternate = ["sellerName"])
    val sellerName: String = "",
    
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)