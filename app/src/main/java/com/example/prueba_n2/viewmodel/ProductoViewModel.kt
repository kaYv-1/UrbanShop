package com.example.prueba_n2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_n2.model.Producto
import com.example.prueba_n2.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repository.allProductos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado: StateFlow<Producto?> = _productoSeleccionado.asStateFlow()

    fun addProducto(producto: Producto) {
        viewModelScope.launch {
            repository.insertProducto(producto)
        }
    }

    fun getProducto(id: String) {
        viewModelScope.launch {
            val producto = repository.getProductoById(id)
            _productoSeleccionado.value = producto
        }
    }
    
    fun getProductosBySeller(sellerId: String): Flow<List<Producto>> {
        return repository.getProductosBySeller(sellerId)
    }

    fun deleteProducto(productoId: String) {
        viewModelScope.launch {
            repository.deleteProducto(productoId)
        }
    }
}
