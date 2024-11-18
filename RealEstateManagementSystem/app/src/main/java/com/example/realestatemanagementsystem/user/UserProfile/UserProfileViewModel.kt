package com.example.realestatemanagementsystem.user.UserProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileViewModel(private val appDatabase: AppDatabase) : ViewModel() {
    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> get() = _userProfile

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val userProfileDao = appDatabase.userProfileDao()




    fun getUserByEmail(email: String, onSuccess: (UserProfile) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch user profile from the database by email
                val userProfile = appDatabase.userProfileDao().getUserByEmail(email)

                if (userProfile != null) {
                    // If user is found, pass the user profile to the success callback
                    onSuccess(userProfile)
                } else {
                    // If no user is found, show an error message
                    onError("User not found in database")
                }
            } catch (e: Exception) {
                // Handle any errors during database operations
                onError("Failed to retrieve user profile: ${e.message}")
            }
        }
    }
    fun insertUserProfile(userProfile: UserProfile, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Insert the user profile data into the database
                appDatabase.userProfileDao().insert(userProfile)
                onSuccess()
            } catch (e: Exception) {
                // Handle any errors during the insertion
                onError("Failed to insert user profile: ${e.message}")
            }
        }
    }
    fun updateUserrProfile(
        email: String,
        firstName: String,
        lastName: String,
        contact: String,
        city: String,
        region: String,
        postalCode: String
    ) {
        viewModelScope.launch {
            try {
                userProfileDao.updateUserr(
                    email, firstName, lastName, contact, city, region, postalCode
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error updating profile: ${e.message}"
            }
        }
    }

}









