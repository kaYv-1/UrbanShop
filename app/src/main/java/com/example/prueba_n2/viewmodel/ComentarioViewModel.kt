package com.example.prueba_n2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.prueba_n2.model.Comentario
import com.example.prueba_n2.repository.ComentarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ComentarioViewModel(private val repository: ComentarioRepository) : ViewModel() {

    fun getComentarios(productoId: String): Flow<List<Comentario>> {
        // Disparar actualización desde la API externa
        viewModelScope.launch {
            repository.refreshComentarios(productoId)
        }
        return repository.getComentarios(productoId)
    }

    fun agregarComentario(productoId: String, usuarioNombre: String, texto: String) {
        viewModelScope.launch {
            val nuevoComentario = Comentario(
                productoId = productoId,
                usuarioNombre = usuarioNombre,
                texto = texto
            )
            repository.agregarComentario(nuevoComentario)
        }
    }
}

class ComentarioViewModelFactory(private val repository: ComentarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComentarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ComentarioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}