package com.example.realestatemanagementsystem.user.UserProfile

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
//    private val _userProfileDao = AppDatabase.getDatabase(application).userProfileDao()
//    val userProfileDao : UserProfileDao = _userProfileDao
////    val userProf : LiveData<UserProfile> = userProfileDao.getUserProfile(1)
//
//    fun saveProfile(userProfile: UserProfile) {
//        viewModelScope.launch {
//            userProfileDao.insertOrUpdate(userProfile)
//        }
//    }
//
//    fun getProfile(id: Int): LiveData<UserProfile> {
//            return userProfileDao.getUserProfile(id)
//    }
//
//    fun clearProfile(id: Int) {
//        viewModelScope.launch {
//            userProfileDao.clearProfile(id)
//        }
//    }
//}

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//class UserProfileViewModel(private val userProfileDao: UserProfileDao) : ViewModel() {
//    val userProfile = mutableStateOf<UserProfile?>(null)
//
//    fun fetchUserProfile(email: String) {
//        viewModelScope.launch {
//            userProfile.value = userProfileDao.getUserByEmail(email)
//        }
//    }
//}

//val appDatabase = AppDatabase.getDatabase(context)
//val userProfileDao = appDatabase.userProfileDao()
//
//// Inserting user profile:
//userProfileDao.insert(userProfile)
//
//// Fetching user profile:
//val userProfile = userProfileDao.getUserProfileByEmail(userEmail)




import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserProfileViewModel(private val appDatabase: AppDatabase) : ViewModel() {

    // Function to get the user profile from the local database by email
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

    // Function to insert user profile data into the local database
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
}
