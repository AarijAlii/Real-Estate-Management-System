package com.example.realestatemanagementsystem.user.UserProfile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.property.Property
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserProfileViewModel(private val appDatabase: AppDatabase) : ViewModel() {

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> get() = _userProfile

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    private val _filteredProperties = MutableStateFlow<List<Property>>(emptyList())
    val filteredProperties: StateFlow<List<Property>> = _filteredProperties
    private val userProfileDao = appDatabase.userProfileDao()



    //new material
//    private val userProfileDao = appDatabase.userProfileDao()
//    private val _userProfile = MutableStateFlow<UserProfile?>(null)
//    val userProfile: StateFlow<UserProfile?> = _userProfile
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    private val _errorMessage = MutableStateFlow("")
//    val errorMessage: StateFlow<String> = _errorMessage

    // Function to get the user profile from the local database by email(PK)
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

    fun getUserProfile(email: String) {
        viewModelScope.launch {
            try {
                val userProfile = userProfileDao.getUserByEmail(email)
                _userProfile.value = userProfile
                Log.d("UserProfileViewModel", "User Profile: ${userProfile.toString()}")
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching user profile: ${e.message}"
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


//    fun updateUserProfile(
//        email: String,
//        firstName: String,
//        lastName: String,
//        contact: String,
//        city: String,
//        region: String,
//        postalCode: String
//    ) {
//        viewModelScope.launch {
//            try {
//                val updatedProfile = _userProfile.value?.copy(
//                    firstName = firstName,
//                    lastName = lastName,
//                    contact = contact,
//                    city = city,
//                    region = region,
//                    postalCode = postalCode
//                )
//                if (updatedProfile != null) {
//                    userProfileDao.updateUser(updatedProfile)
//                    _userProfile.value = updatedProfile
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "Error updating profile: ${e.message}"
//            }
//        }
//    }

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
        // Create a Retrofit instance for the API
        val userProfileApi = ApiClient.retrofit.create(UserProfileApi::class.java)

        // Function to fetch user profile from the API by email
        fun getUserProfileFromApi(email: String) {
            viewModelScope.launch {
                userProfileApi.getUserProfileByEmail(email).enqueue(object : Callback<UserProfile> {
                    override fun onResponse(
                        call: Call<UserProfile>,
                        response: Response<UserProfile>
                    ) {
                        if (response.isSuccessful) {
                            _userProfile.value = response.body()
                            Log.d(
                                "UserProfileViewModel",
                                "Fetched user profile: ${response.body()}"
                            )
                        } else {
                            _errorMessage.value = "Error fetching user profile"
                        }
                    }

                    override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                        _errorMessage.value = "Failed to connect to server: ${t.message}"
                    }
                })
            }
        }

        // Function to create a user profile using the API
        fun createUserProfile(userProfile: UserProfile) {
            viewModelScope.launch {
                userProfileApi.createUserProfile(userProfile)
                    .enqueue(object : Callback<UserProfile> {
                        override fun onResponse(
                            call: Call<UserProfile>,
                            response: Response<UserProfile>
                        ) {
                            if (response.isSuccessful) {
                                _userProfile.value = response.body()
                                Log.d(
                                    "UserProfileViewModel",
                                    "User profile created: ${response.body()}"
                                )
                            } else {
                                _errorMessage.value = "Failed to create user profile"
                            }
                        }

                        override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                            _errorMessage.value = "Failed to connect to server: ${t.message}"
                        }
                    })
            }
        }

        // Function to update user profile via API
//        fun updateUserProfile(email: String, firstName: String, lastName: String, contact: String, city: String, region: String, postalCode: String) {
//            viewModelScope.launch {
//                try {
//                    val apiService = ApiClient.retrofit.create(UserProfileApi::class.java)
//                    val response = apiService.updateUserProfile(email, firstName, lastName, contact, city, region, postalCode)
//                    if (response.isSuccessful) {
//                        // Handle successful response
//                        _userProfile.value = response.body()
//                    } else {
//                        // Log error message from the response
//                        Log.e("UpdateProfile", "Error: ${response.message()}")
//                        _errorMessage.value = "Failed to update profile: ${response.message()}"
//                    }
//                } catch (e: Exception) {
//                    // Log any exception
//                    Log.e("UpdateProfile", "Exception: ${e.message}")
//                    _errorMessage.value = "Error updating profile: ${e.message}"
//                }
//            }
//        }

}