package com.example.realestatemanagementsystem.user.UserProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanagementsystem.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore

class UserViewModelFactory(
    private val appDatabase: AppDatabase,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
            return UserProfileViewModel(appDatabase, firebaseFirestore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
