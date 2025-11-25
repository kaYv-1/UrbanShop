package com.example.prueba_n2.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.prueba_n2.model.Producto
import com.example.prueba_n2.model.Usuario
import java.io.File
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicarProducto(
    currentUser: Usuario?,
    onProductoPublicado: (Producto) -> Unit,
    onBack: () -> Unit
) {
    var nombreArticulo by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success: Boolean ->
            if (success) {
                selectedImageUri = photoUri
            } else {
                Toast.makeText(context, "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                val newUri = createImageUri(context)
                photoUri = newUri
                cameraLauncher.launch(newUri)
            } else {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Artículo") },
                navigationIcon = { IconButton(onClick = onBack) { Text("<") } }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = nombreArticulo,
                onValueChange = { nombreArticulo = it },
                label = { Text("Nombre del Artículo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) { precio = newValue }
                },
                label = { Text("Precio (CLP)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(ImageRequest.Builder(context).data(selectedImageUri).crossfade(true).build()),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.size(200.dp).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier.size(200.dp).fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("No hay imagen seleccionada")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { galleryLauncher.launch("image/*") }) {
                    Text("Galería")
                }

                Button(onClick = {
                    val permission = Manifest.permission.CAMERA
                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        val newUri = createImageUri(context)
                        photoUri = newUri
                        cameraLauncher.launch(newUri)
                    } else {
                        permissionLauncher.launch(permission)
                    }
                }) {
                    Text("Usar Cámara")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val nuevoProducto = Producto(
                        id = UUID.randomUUID().toString(),
                        name = nombreArticulo,
                        price = precio.toIntOrNull() ?: 0,
                        description = descripcion,
                        imageUri = selectedImageUri?.toString(),
                        rating = 0.0f,
                        sellerId = currentUser?.email ?: "Desconocido",
                        sellerName = currentUser?.nombre ?: "Usuario", // Guardamos el nombre real del vendedor
                        timestamp = System.currentTimeMillis()
                    )
                    onProductoPublicado(nuevoProducto)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nombreArticulo.isNotBlank() && precio.isNotBlank() && descripcion.isNotBlank() && currentUser != null
            ) {
                Text("Publicar Artículo")
            }
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val file = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".fileprovider",
        file
    )
}
