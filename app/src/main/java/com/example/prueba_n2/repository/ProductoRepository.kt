package com.example.prueba_n2.repository

import com.example.prueba_n2.model.Producto
import com.example.prueba_n2.model.ProductoDao
import com.example.prueba_n2.network.RetrofitClient
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    private val api = RetrofitClient.productosApi

    val allProductos: Flow<List<Producto>> = productoDao.getAllProductos()

    suspend fun insertProducto(producto: Producto) {
        try {
            api.crearProducto(producto)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        productoDao.insertProducto(producto)
    }

    suspend fun getProductoById(id: String): Producto? {
        return productoDao.getProductoById(id)
    }

    fun getProductosBySeller(sellerId: String): Flow<List<Producto>> {
        return productoDao.getProductosBySeller(sellerId)
    }

    suspend fun deleteProducto(productoId: String) {
        try {
            api.deleteProducto(productoId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        productoDao.deleteProductoById(productoId)
    }
    
    suspend fun refreshProductos() {
        try {
            val productosRemotos = api.getProductos()
            productosRemotos.forEach { productoDao.insertProducto(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}