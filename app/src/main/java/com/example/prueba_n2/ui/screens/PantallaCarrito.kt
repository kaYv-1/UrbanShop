package com.example.prueba_n2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.prueba_n2.model.CartItem
import com.example.prueba_n2.viewmodel.ProductoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(
    viewModel: ProductoViewModel,
    onBack: () -> Unit
) {
    val itemsCarrito by viewModel.carrito.collectAsState()
    val context = LocalContext.current
    val total = itemsCarrito.sumOf { it.producto.price * it.cantidad }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (itemsCarrito.isNotEmpty()) {
                Surface(tonalElevation = 8.dp, shadowElevation = 8.dp) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", style = MaterialTheme.typography.titleLarge)
                            Text(
                                formatPrice(total),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.vaciarCarrito()
                                Toast.makeText(context, "Compra realizada con éxito", Toast.LENGTH_LONG).show()
                                onBack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Pagar")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (itemsCarrito.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(itemsCarrito) { item ->
                    ItemCarrito(cartItem = item, onDelete = { viewModel.eliminarDelCarrito(item.producto) })
                }
            }
        }
    }
}

@Composable
fun ItemCarrito(cartItem: CartItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${cartItem.producto.name} x ${cartItem.cantidad}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(cartItem.producto.category, style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "${formatPrice(cartItem.producto.price)} c/u", 
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Subtotal: ${formatPrice(cartItem.producto.price * cartItem.cantidad)}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return try { format.format(price).replace("CLP", "$") } catch (e: Exception) { "$ $price" }
}