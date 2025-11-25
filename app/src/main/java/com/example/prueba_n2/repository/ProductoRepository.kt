package com.example.prueba_n2.repository

import com.example.prueba_n2.model.Producto
import com.example.prueba_n2.model.ProductoDao
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    val allProductos: Flow<List<Producto>> = productoDao.getAllProductos()

    suspend fun insertProducto(producto: Producto) {
        productoDao.insertProducto(producto)
    }

    suspend fun getProductoById(id: String): Producto? {
        return productoDao.getProductoById(id)
    }

    fun getProductosBySeller(sellerId: String): Flow<List<Producto>> {
        return productoDao.getProductosBySeller(sellerId)
    }

    suspend fun deleteProducto(productoId: String) {
        productoDao.deleteProductoById(productoId)
    }
}
