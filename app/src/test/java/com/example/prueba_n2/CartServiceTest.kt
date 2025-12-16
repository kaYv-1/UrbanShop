package com.example.prueba_n2

import com.example.prueba_n2.model.Producto
import com.example.prueba_n2.utils.CartService
import org.junit.Assert.assertEquals
import org.junit.Test

class CartServiceTest {

    private val cartService = CartService()

    private val dummyProducto = Producto(
        id = "1",
        name = "Polera",
        price = 10000,
        rating = 4.5f,
        description = "Una polera",
        category = "Polerón",
        sellerId = "s1",
        sellerName = "Vendedor",
        timestamp = 123456L
    )

    @Test
    fun `agregar nuevo producto al carrito`() {
        // Setup
        val currentCart = emptyList<com.example.prueba_n2.model.CartItem>()
        
        // Action
        val newCart = cartService.addItem(currentCart, dummyProducto, 2)

        // Verify
        assertEquals(1, newCart.size)
        assertEquals(2, newCart[0].cantidad)
        assertEquals(dummyProducto, newCart[0].producto)
    }

    @Test
    fun `agregar producto existente suma cantidad`() {
        // Setup: Carrito inicial con 1 producto (cantidad 2)
        val initialCart = cartService.addItem(emptyList(), dummyProducto, 2)
        
        // Action: Agregar mismo producto (cantidad 3)
        val newCart = cartService.addItem(initialCart, dummyProducto, 3)

        // Verify
        assertEquals(1, newCart.size)
        assertEquals(5, newCart[0].cantidad) // 2 + 3 = 5
    }
    
    @Test
    fun `calcular total correcto`() {
        // Setup
        val cart = cartService.addItem(emptyList(), dummyProducto, 3) // 3 * 10000 = 30000
        
        // Action
        val total = cartService.calculateTotal(cart)
        
        // Verify
        assertEquals(30000, total)
    }
}