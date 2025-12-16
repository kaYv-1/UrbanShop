package com.example.prueba_n2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val URL_USUARIOS = "https://x8ki-letl-twmt.n7.xano.io/api:NTFduBgI/"
    private const val URL_PRODUCTOS = "https://x8ki-letl-twmt.n7.xano.io/api:35fvan-R/"
    private const val URL_COMENTARIOS = "https://x8ki-letl-twmt.n7.xano.io/api:Cma3Wph7/"

    val usuariosApi: UsuarioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_USUARIOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApiService::class.java)
    }

    val productosApi: ProductoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_PRODUCTOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductoApiService::class.java)
    }

    val comentariosApi: ComentarioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_COMENTARIOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ComentarioApiService::class.java)
    }
}