package com.example.prueba_n2.repository

import com.example.prueba_n2.model.Comentario
import com.example.prueba_n2.model.ComentarioDao
import com.example.prueba_n2.network.RetrofitClient
import kotlinx.coroutines.flow.Flow

class ComentarioRepository(private val comentarioDao: ComentarioDao) {

    private val api = RetrofitClient.comentariosApi

    fun getComentarios(productoId: String): Flow<List<Comentario>> = 
        comentarioDao.getComentariosPorProducto(productoId)

    suspend fun refreshComentarios(productoId: String) {
        try {
            val comentariosRemotos = api.getComentarios(productoId)
            comentariosRemotos.forEach { comentarioDao.insertComentario(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun agregarComentario(comentario: Comentario) {
        try {
            api.crearComentario(comentario)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        comentarioDao.insertComentario(comentario)
    }
}