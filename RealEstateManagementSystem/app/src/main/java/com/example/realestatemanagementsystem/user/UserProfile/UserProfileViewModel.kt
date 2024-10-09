package com.example.realestatemanagementsystem.user.UserProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _userProfileDao = AppDatabase.getDatabase(application).userProfileDao()
    val userProfileDao : UserProfileDao = _userProfileDao
//    val userProf : LiveData<UserProfile> = userProfileDao.getUserProfile(1)

    fun saveProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            userProfileDao.insertOrUpdate(userProfile)
        }
    }

    fun getProfile(id: Int): LiveData<UserProfile> {
            return userProfileDao.getUserProfile(id)
    }

    fun clearProfile(id: Int) {
        viewModelScope.launch {
            userProfileDao.clearProfile(id)
        }
    }
}









