package com.example.realestatemanagementsystem.image
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImageViewModelFactory(private val imageDao: ImageDao) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            return ImageViewModel(imageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
