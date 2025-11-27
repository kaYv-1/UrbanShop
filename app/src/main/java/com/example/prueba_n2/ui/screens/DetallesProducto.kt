package com.example.prueba_n2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prueba_n2.R
import com.example.prueba_n2.model.Producto
//para obtener la categoría seleccionada y cambiarla
import com.example.prueba_n2.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesProducto(
    productos: List<Producto>,
    productoViewModel: ProductoViewModel, // <--- Pasamos el ViewModel
    onNavigateToPublish: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCart: () -> Unit // <--- Nuevo callback
) {
    val categorias = listOf("Todos", "Polerón", "Gorro", "Zapatillas", "Pantalones", "Accesorios")
    val categoriaActual by productoViewModel.categoriaSeleccionada.collectAsState()
    val carritoItems by productoViewModel.carrito.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Filled.Person, contentDescription = "Mi Perfil")
                    }
                },
                actions = {
                    // BOTÓN CARRITO
                    IconButton(onClick = onNavigateToCart) {
                        BadgedBox(
                            badge = {
                                if (carritoItems.isNotEmpty()) {
                                    Badge { Text(carritoItems.size.toString()) }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToPublish) {
                Icon(Icons.Default.Add, contentDescription = "Publicar Producto")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {

            // --- FILTROS ---
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { categoria ->
                    FilterChip(
                        selected = (categoria == categoriaActual),
                        onClick = { productoViewModel.cambiarCategoria(categoria) },
                        label = { Text(categoria) }
                    )
                }
            }
            // ----------------

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(productos) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        onItemClick = { onNavigateToDetail(producto.id) }
                    )
                    Divider()
                }
            }
        }
    }
}
