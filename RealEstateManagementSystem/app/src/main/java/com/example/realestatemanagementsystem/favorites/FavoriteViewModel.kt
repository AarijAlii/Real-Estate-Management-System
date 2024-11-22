package com.example.realestatemanagementsystem.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.Property.Property
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteDao = AppDatabase.getDatabase(application).favoriteDao()

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> = _favorites

    private val _favoriteProperties = MutableStateFlow<List<Property>>(emptyList())
    val favoriteProperties: StateFlow<List<Property>> = _favoriteProperties



    fun getFavoritessByEmail(email: String) {
        viewModelScope.launch {
          _favoriteProperties.value= favoriteDao.getFavoritePropertiesByEmail(email)
        }
    }


    private fun getFavoritesByEmail(email: String) {
        viewModelScope.launch {
            // Assuming you have an email set for the logged-in user
            // Replace with actual user email
            _favorites.value = favoriteDao.getFavoritesByEmail(email)
        }
    }
    suspend fun isFavorite(email: String, propertyId: Int): Boolean{
        if(favoriteDao.isFavorite(email,propertyId)){
            return true
        }
        else{
            return false
        }
    }
    fun addOrRemoveFavorite(email: String, propertyId: Int) {
        viewModelScope.launch {
            if (!favoriteDao.isFavorite(email,propertyId)) {
                val favorite = Favorite(email = email, propertyId = propertyId)
                favoriteDao.addToFavorites(favorite) // Insert the favorite
            } else {

                favoriteDao.removeFromFavorites(email, propertyId) // Remove the favorite
            }
            getFavoritesByEmail(email) // Refresh favorites list after add/remove
        }
    }
//    init() {
//        getFavoritesByEmail() // Load favorites initially
//    }
}