package com.example.prueba_n2.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ComentarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComentario(comentario: Comentario)

    @Query("SELECT * FROM comentarios WHERE productoId = :productoId ORDER BY timestamp ASC")
    fun getComentariosPorProducto(productoId: String): Flow<List<Comentario>>
}
