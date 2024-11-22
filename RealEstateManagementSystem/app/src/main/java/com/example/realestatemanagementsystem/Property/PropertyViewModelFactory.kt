package com.example.realestatemanagementsystem.Property

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanagementsystem.image.ImageDao


class PropertyViewModelFactory(
    private val propertyDao: PropertyDao,
    private val imageDao: ImageDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyViewModel::class.java)) {
            return PropertyViewModel(propertyDao, imageDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
