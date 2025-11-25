package com.example.prueba_n2.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prueba_n2.R
import com.example.prueba_n2.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesProducto(
    productos: List<Producto>,
    onNavigateToPublish: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: (String) -> Unit // Nuevo callback
) {
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
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Mi Perfil"
                        )
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(productos) { producto ->
                TarjetaProducto(
                    producto = producto,
                    onItemClick = {
                        onNavigateToDetail(producto.id)
                    }
                )
                Divider() 
            }
        }
    }
}
