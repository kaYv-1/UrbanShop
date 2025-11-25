package com.example.prueba_n2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    onRegister: (nombre: String, email: String, pass: String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var emailError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    // Estado de scroll para pantallas pequeñas
    val scrollState = rememberScrollState()

    fun validateEmail(input: String): Boolean {
        // Regex simple para validar que tenga texto + @ + dominio + .com
        val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$".toRegex()
        return regex.matches(input)
    }

    fun validatePassword(input: String): Boolean {
        // Validaciones requeridas
        if (input.length < 12) return false
        val hasUpper = input.any { it.isUpperCase() }
        val hasLower = input.any { it.isLowerCase() }
        val hasDigit = input.any { it.isDigit() }
        val hasSpecial = input.any { !it.isLetterOrDigit() }
        return hasUpper && hasLower && hasDigit && hasSpecial
    }

    fun onRegisterClick() {
        var isValid = true
        
        if (!validateEmail(email)) {
            emailError = "El correo debe terminar en .com y ser válido"
            isValid = false
        } else {
            emailError = null
        }

        if (!validatePassword(password)) {
            // El feedback visual ya muestra qué falta, pero bloqueamos el registro
            isValid = false
        }

        if (password != confirmPassword) {
            confirmPasswordError = "Las contraseñas no coinciden."
            isValid = false
        } else {
            confirmPasswordError = null
        }

        if (isValid && nombre.isNotBlank()) {
            onRegister(nombre, email, password)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Crear Cuenta", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { 
                email = it 
                emailError = null 
            },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null,
            supportingText = { 
                if (emailError != null) Text(emailError!!, color = MaterialTheme.colorScheme.error)
                else Text("Ej: usuario@gmail.com")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it 
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
            singleLine = true
        )
        
        // Tarjeta de Requisitos de Contraseña
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = MaterialTheme.shapes.small
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "La contraseña debe tener:", 
                    style = MaterialTheme.typography.labelMedium, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                RequirementText("Mínimo 12 caracteres", password.length >= 12)
                RequirementText("Mayúsculas y minúsculas", password.any { it.isUpperCase() } && password.any { it.isLowerCase() })
                RequirementText("Números y símbolos", password.any { it.isDigit() } && password.any { !it.isLetterOrDigit() })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { 
                confirmPassword = it 
                confirmPasswordError = null
            },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = confirmPasswordError != null,
            supportingText = { if (confirmPasswordError != null) Text(confirmPasswordError!!, color = MaterialTheme.colorScheme.error) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onRegisterClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}

@Composable
fun RequirementText(text: String, isMet: Boolean) {
    val color = if (isMet) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    val icon = if (isMet) "✓" else "•"
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = icon,
            style = MaterialTheme.typography.bodySmall,
            color = color,
            modifier = Modifier.width(20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}
