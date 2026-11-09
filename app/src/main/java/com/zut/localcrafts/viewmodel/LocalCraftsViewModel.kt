package com.zut.localcrafts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zut.localcrafts.data.Artisan
import com.zut.localcrafts.data.LocalCraftsRepository
import com.zut.localcrafts.data.PasswordHasher
import com.zut.localcrafts.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    /**
     * Checks the email and password against the saved artisan accounts.
     * Calls onResult with the artisan if they match, or null if they do not.
     */
    fun login(email: String, password: String, onResult: (Artisan?) -> Unit) {
        viewModelScope.launch {
            val artisan = repository.getArtisanByEmail(email.trim())
            val matches = artisan != null && withContext(Dispatchers.Default) {
                PasswordHasher.verify(password, artisan.passwordSalt, artisan.passwordHash)
            }
            onResult(if (matches) artisan else null)
        }
    }

    /**
     * Creates a new artisan account with a hashed password.
     * Calls onResult with the new artisan, or null if the email is already registered.
     */
    fun register(artisan: Artisan, password: String, onResult: (Artisan?) -> Unit) {
        viewModelScope.launch {
            val email = artisan.email.trim()
            val existing = repository.getArtisanByEmail(email)

            // An account made before passwords existed has no password yet,
            // so it can be claimed by registering again with the same email.
            if (existing != null && existing.passwordHash.isNotEmpty()) {
                onResult(null)
                return@launch
            }

            val salt = PasswordHasher.newSalt()
            val hash = withContext(Dispatchers.Default) { PasswordHasher.hash(password, salt) }

            val newArtisan = artisan.copy(
                artisanId = existing?.artisanId ?: artisan.artisanId,
                email = email,
                passwordHash = hash,
                passwordSalt = salt
            )
            repository.insertArtisan(newArtisan)
            onResult(newArtisan)
        }
    }
}
