package com.zut.localcrafts.data


data class Product(
    val productId: String,
    val artisanId: String,
    val productName: String,
    val description: String,
    val category: String,
    val price: Double,
    val imageUri: String? = null
)
