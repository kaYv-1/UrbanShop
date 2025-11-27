package com.example.prueba_n2.model

import com.example.prueba_n2.R
import java.util.UUID

val dummyProductos = listOf(
    Producto(
        id = UUID.randomUUID().toString(),
        name = "Zapatillas Puma Suede",
        price = 64990,
        rating = 4.8f,
        description = "Un ícono del estilo urbano. Las Puma Suede son un clásico atemporal que combina con todo, perfectas para un look casual y cómodo.",
        category = "Zapatillas", // <--- NUEVO CAMPO
        imageResId = R.drawable.producto_zapatillas,
        sellerId = "tienda_oficial",
        sellerName = "Puma Store",
        timestamp = System.currentTimeMillis() - 2 * 60 * 1000
    ),
    Producto(
        id = UUID.randomUUID().toString(),
        name = "Polerón Oversized Cierre",
        price = 49990,
        description = "Polerón negro con cierre y capucha, de calce oversized. Ideal para un estilo relajado y moderno. Hecho de algodón de alta calidad.",
        category = "Polerón", // <--- NUEVO CAMPO
        imageResId = R.drawable.producto_poleron,
        rating = 4.6f,
        sellerId = "urban_style_cl",
        sellerName = "Urban Style CL",
        timestamp = System.currentTimeMillis() - 1 * 60 * 1000
    ),
    Producto(
        id = UUID.randomUUID().toString(),
        name = "Jeans Anchos (Baggy)",
        price = 59990,
        description = "Jeans de estilo baggy con un lavado claro. La prenda perfecta para un look de los 90s, cómodos y con mucho estilo.",
        category = "Pantalones", // <--- NUEVO CAMPO
        imageResId = R.drawable.producto_jeans,
        rating = 4.7f,
        sellerId = "denim_masters",
        sellerName = "Denim Masters",
        timestamp = System.currentTimeMillis()
    )
)
