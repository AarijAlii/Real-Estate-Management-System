package com.example.realestatemanagementsystem.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.realestatemanagementsystem.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteDao = AppDatabase.getDatabase(application).favoriteDao()

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> = _favorites

//    init() {
//        getFavoritesByEmail() // Load favorites initially
//    }


    fun getFavoritessByEmail(email: String) {
        viewModelScope.launch {
            favoriteDao.getFavoritesByEmail(email)
        }
    }


    private fun getFavoritesByEmail(email: String) {
        viewModelScope.launch {
            // Assuming you have an email set for the logged-in user
              // Replace with actual user email
            _favorites.value = favoriteDao.getFavoritesByEmail(email)
        }
    }

    fun addOrRemoveFavorite(email: String, propertyId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                val favorite = Favorite(email = email, propertyId = propertyId)
                favoriteDao.addToFavorites(favorite) // Insert the favorite
            } else {
                val favorite = Favorite(email = email, propertyId = propertyId)
                favoriteDao.removeFromFavorites(email, propertyId) // Remove the favorite
            }
            getFavoritesByEmail(email) // Refresh favorites list after add/remove
        }
    }
}