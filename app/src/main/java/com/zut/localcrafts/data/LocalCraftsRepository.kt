package com.zut.localcrafts.data

import kotlinx.coroutines.flow.Flow

class LocalCraftsRepository(
    private val artisanDao: ArtisanDao,
    private val productDao: ProductDao
) {

    // Artisan operations
    suspend fun insertArtisan(artisan: Artisan) {
        artisanDao.insertArtisan(artisan)
    }

    suspend fun getArtisanByEmail(email: String): Artisan? {
        return artisanDao.getArtisanByEmail(email)
    }

    fun getAllArtisans(): Flow<List<Artisan>> {
        return artisanDao.getAllArtisans()
    }

    // Product operations
    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }

    fun getProductsByArtisan(artisanId: String): Flow<List<Product>> {
        return productDao.getProductsByArtisan(artisanId)
    }
}