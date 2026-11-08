package com.zut.localcrafts.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zut.localcrafts.data.LocalCraftsDatabase
import com.zut.localcrafts.data.LocalCraftsRepository

class LocalCraftsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(LocalCraftsViewModel::class.java)) {

            val database = LocalCraftsDatabase.getDatabase(context)

            val repository = LocalCraftsRepository(
                database.artisanDao(),
                database.productDao()
            )

            @Suppress("UNCHECKED_CAST")
            return LocalCraftsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}