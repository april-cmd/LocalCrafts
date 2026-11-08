package com.zut.localcrafts.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artisans")
data class Artisan(
    @PrimaryKey
    val artisanId: String,
    val fullName: String,
    val businessName: String,
    val email: String,
    val location: String,
    val description: String
)