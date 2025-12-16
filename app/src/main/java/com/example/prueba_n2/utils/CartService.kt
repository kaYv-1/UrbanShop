package com.example.prueba_n2.utils

import com.example.prueba_n2.model.CartItem
import com.example.prueba_n2.model.Producto

class CartService {
    fun addItem(currentCart: List<CartItem>, producto: Producto, cantidad: Int): List<CartItem> {
        val mutableCart = currentCart.toMutableList()
        val existingIndex = mutableCart.indexOfFirst { it.producto.id == producto.id }
        
        if (existingIndex != -1) {
            val existing = mutableCart[existingIndex]
            mutableCart[existingIndex] = existing.copy(cantidad = existing.cantidad + cantidad)
        } else {
            mutableCart.add(CartItem(producto, cantidad))
        }
        return mutableCart
    }
    
    fun calculateTotal(cart: List<CartItem>): Int {
        return cart.sumOf { it.producto.price * it.cantidad }
    }
}