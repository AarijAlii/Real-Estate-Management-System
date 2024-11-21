package com.example.realestatemanagementsystem.property.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteDao: FavoriteDao) : ViewModel() {

    private val _favoriteProperties = MutableStateFlow<List<Favorite>>(emptyList())
    val favoriteProperties: StateFlow<List<Favorite>> = _favoriteProperties

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun addFavorite(email: String, propertyId: Int) {
        viewModelScope.launch {
            try {
                favoriteDao.addFavorite(email, propertyId)
                loadFavorites(email)
            } catch (e: Exception) {
                _errorMessage.value = "Error adding favorite: ${e.message}"
            }
        }
    }

    fun removeFavorite(email: String, propertyId: Int) {
        viewModelScope.launch {
            try {
                favoriteDao.removeFavorite(email, propertyId)
                loadFavorites(email)
            } catch (e: Exception) {
                _errorMessage.value = "Error removing favorite: ${e.message}"
            }
        }
    }

    fun checkIfFavorite(email: String, propertyId: Int) {
        viewModelScope.launch {
            try {
                _isFavorite.value = favoriteDao.isFavorite(email, propertyId)
            } catch (e: Exception) {
                _errorMessage.value = "Error checking favorite: ${e.message}"
            }
        }
    }

    fun loadFavorites(email: String) {
        viewModelScope.launch {
            try {
                _favoriteProperties.value = favoriteDao.getFavoritesByEmail(email)
            } catch (e: Exception) {
                _errorMessage.value = "Error loading favorites: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
