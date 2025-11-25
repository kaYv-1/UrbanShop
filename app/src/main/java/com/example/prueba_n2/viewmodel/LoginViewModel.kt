package com.example.prueba_n2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_n2.model.Usuario
import com.example.prueba_n2.repository.UsuarioRepository
import com.example.prueba_n2.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Empty : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(
    private val repository: UsuarioRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Empty)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        val savedEmail = sessionManager.getUserEmail()
        if (savedEmail != null) {
            viewModelScope.launch {
                val usuario = repository.getUsuarioByEmail(savedEmail)
                if (usuario != null) {
                    _currentUser.value = usuario
                    _loginState.value = LoginState.Success
                }
            }
        }
    }

    fun login(email: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val usuario = repository.getUsuarioByEmail(email)
                if (usuario == null) {
                    _loginState.value = LoginState.Error("La cuenta no existe. Por favor, regístrate.")
                    _currentUser.value = null
                } else if (usuario.contrasena != contrasena) {
                     _loginState.value = LoginState.Error("Contraseña incorrecta.")
                     _currentUser.value = null
                } else {
                    sessionManager.saveUserSession(email)
                    _loginState.value = LoginState.Success
                    _currentUser.value = usuario
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error al iniciar sesión: ${e.message}")
                _currentUser.value = null
            }
        }
    }

    fun registrarUsuario(nombre: String, email: String, contrasena: String) {
        viewModelScope.launch {
            try {
                if (repository.getUsuarioByEmail(email) != null) {
                    _loginState.value = LoginState.Error("El email ya está registrado.")
                    return@launch
                }
                val nuevoUsuario = Usuario(nombre = nombre, email = email, contrasena = contrasena)
                repository.insertUsuario(nuevoUsuario)
                sessionManager.saveUserSession(email)
                _loginState.value = LoginState.Success
                _currentUser.value = nuevoUsuario
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error durante el registro: ${e.message}")
            }
        }
    }

    fun logout() {
        sessionManager.clearSession()
        _currentUser.value = null
        _loginState.value = LoginState.Empty
    }
}
