package com.example.prueba_n2.repository

import com.example.prueba_n2.model.Comentario
import com.example.prueba_n2.model.ComentarioDao
import kotlinx.coroutines.flow.Flow

class ComentarioRepository(private val comentarioDao: ComentarioDao) {
    fun getComentarios(productoId: String): Flow<List<Comentario>> = 
        comentarioDao.getComentariosPorProducto(productoId)

    suspend fun agregarComentario(comentario: Comentario) {
        comentarioDao.insertComentario(comentario)
    }
}
