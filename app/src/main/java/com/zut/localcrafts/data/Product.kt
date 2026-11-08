package com.zut.localcrafts.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val productId: String,
    val artisanId: String,
    val productName: String,
    val description: String,
    val category: String,
    val price: Double,
    val imageUri: String? = null
)