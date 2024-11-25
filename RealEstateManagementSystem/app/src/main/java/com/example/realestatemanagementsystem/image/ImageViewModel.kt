package com.example.realestatemanagementsystem.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImageViewModel(private val imageDao: ImageDao) : ViewModel() {

    // State for image URLs
    private val _imageUrls = MutableStateFlow<List<String>>(emptyList())
    val imageUrls: StateFlow<List<String>> get() = _imageUrls

    // Function to insert images
    fun insertImages(propertyId: Long, images: List<ImageEntity>) {
        viewModelScope.launch {
            imageDao.insertImages(propertyId, images)
        }
    }

    // Function to fetch images and update state
    fun fetchImagesForProperty(propertyId: Int) {
        viewModelScope.launch {
            val urls = imageDao.getImageUrlsForProperty(propertyId)
            _imageUrls.value = urls.filterNotNull() // Remove any null values
        }
    }
}