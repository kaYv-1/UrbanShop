package com.example.prueba_n2.network

import com.example.prueba_n2.model.Comentario
import com.example.prueba_n2.model.Producto
import com.example.prueba_n2.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface UsuarioApiService {
    @GET("usuario")
    suspend fun getUsuarioByEmail(@Query("email") email: String): List<Usuario>

    @POST("usuario")
    suspend fun crearUsuario(@Body usuario: Usuario): Response<Usuario>
}

interface ProductoApiService {
    @POST("producto")
    suspend fun crearProducto(@Body producto: Producto): Response<Producto>

    @GET("producto")
    suspend fun getProductos(): List<Producto>

    @GET("producto/{producto_id}")
    suspend fun getProductoById(@Path("producto_id") id: String): Response<Producto>

    @PUT("producto/{producto_id}")
    suspend fun updateProducto(@Path("producto_id") id: String, @Body producto: Producto): Response<Producto>

    @PATCH("producto/{producto_id}")
    suspend fun editProducto(@Path("producto_id") id: String, @Body producto: Producto): Response<Producto>

    @DELETE("producto/{producto_id}")
    suspend fun deleteProducto(@Path("producto_id") id: String): Response<Unit>
}

interface ComentarioApiService {
    @GET("comentario")
    suspend fun getComentarios(@Query("producto_id") productoId: String): List<Comentario>

    @POST("comentario")
    suspend fun crearComentario(@Body comentario: Comentario): Response<Comentario>
}