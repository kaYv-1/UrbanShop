package com.example.prueba_n2.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prueba_n2.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducto(producto: Producto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productos: List<Producto>)

    @Query("SELECT * FROM productos ORDER BY timestamp DESC")
    fun getAllProductos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getProductoById(id: String): Producto?

    @Query("DELETE FROM productos")
    suspend fun deleteAll()

    @Query("DELETE FROM productos WHERE id = :productoId")
    suspend fun deleteProductoById(productoId: String)

    @Query("SELECT COUNT(*) FROM productos")
    suspend fun count(): Int

    @Query("SELECT * FROM productos WHERE sellerId = :sellerId ORDER BY timestamp DESC")
    fun getProductosBySeller(sellerId: String): Flow<List<Producto>>
}
