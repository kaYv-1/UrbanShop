package com.example.prueba_n2.repository

import com.example.prueba_n2.model.Usuario
import com.example.prueba_n2.model.UsuarioDao
import com.example.prueba_n2.network.RetrofitClient

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    private val api = RetrofitClient.usuariosApi

    suspend fun insertUsuario(usuario: Usuario) {
        try {
            api.crearUsuario(usuario)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        usuarioDao.insertUsuario(usuario)
    }

    suspend fun getUsuarioByEmail(email: String): Usuario? {
        try {
            val usuarios = api.getUsuarioByEmail(email)
            val usuarioRemoto = usuarios.find { it.email.equals(email, ignoreCase = true) }
            
            if (usuarioRemoto != null) {
                usuarioDao.insertUsuario(usuarioRemoto)
                return usuarioRemoto
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return usuarioDao.getUsuarioByEmail(email)
    }
}