package com.example.prueba_n2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPago(
    montoTotal: String,
    metodoPago: String,
    onPaymentSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var nombreTitular by remember { mutableStateOf("") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var fechaCaducidad by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Pago") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Monto a pagar: $montoTotal", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))
            
            OutlinedTextField(
                value = nombreTitular,
                onValueChange = { nombreTitular = it },
                label = { Text("Nombre del Titular") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = numeroTarjeta,
                onValueChange = { numeroTarjeta = it },
                label = { Text("Número de Tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = fechaCaducidad,
                    onValueChange = { fechaCaducidad = it },
                    label = { Text("Fecha de Caducidad (MM/AA)") },
                    modifier = Modifier.weight(1f),
                     keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    if (nombreTitular.isNotBlank() && numeroTarjeta.isNotBlank() && fechaCaducidad.isNotBlank() && cvv.isNotBlank()) {
                         Toast.makeText(context, "Pago con $metodoPago realizado exitosamente", Toast.LENGTH_LONG).show()
                         onPaymentSuccess()
                    } else {
                        Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pagar")
            }
        }
    }
}
