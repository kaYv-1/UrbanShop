package com.example.prueba_n2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prueba_n2.model.CartItem
import com.example.prueba_n2.model.Producto
import com.example.prueba_n2.repository.ProductoRepository
import com.example.prueba_n2.utils.CartService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductoViewModel(private val repository: ProductoRepository) : ViewModel() {

    private val _todosLosProductos = repository.allProductos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    private val _categoriaSeleccionada = MutableStateFlow("Todos")
    val categoriaSeleccionada = _categoriaSeleccionada.asStateFlow()

    val productos: StateFlow<List<Producto>> = combine(_todosLosProductos, _categoriaSeleccionada) { lista, cat ->
        if (cat == "Todos") lista else lista.filter { it.category == cat }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    private val _carrito = MutableStateFlow<List<CartItem>>(emptyList())
    val carrito = _carrito.asStateFlow()
    
    private val cartService = CartService()

    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado: StateFlow<Producto?> = _productoSeleccionado.asStateFlow()

    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1) {
        _carrito.value = cartService.addItem(_carrito.value, producto, cantidad)
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
    }

    fun eliminarDelCarrito(producto: Producto) {
        _carrito.value = _carrito.value.filter { it.producto.id != producto.id }
    }

    fun cambiarCategoria(nuevaCategoria: String) {
        _categoriaSeleccionada.value = nuevaCategoria
    }

    fun addProducto(producto: Producto) {
        viewModelScope.launch { repository.insertProducto(producto) }
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
        viewModelScope.launch { repository.deleteProducto(productoId) }
    }
}