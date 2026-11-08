package com.zut.localcrafts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zut.localcrafts.data.Artisan
import com.zut.localcrafts.data.LocalCraftsRepository
import com.zut.localcrafts.data.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocalCraftsViewModel(
    private val repository: LocalCraftsRepository
) : ViewModel() {

    val artisans = repository.getAllArtisans().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val products = repository.getAllProducts().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun saveArtisan(artisan: Artisan) {
        viewModelScope.launch {
            repository.insertArtisan(artisan)
        }
    }

    fun saveProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}