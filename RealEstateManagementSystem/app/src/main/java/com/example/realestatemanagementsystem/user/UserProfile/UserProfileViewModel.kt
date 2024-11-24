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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await


class UserProfileViewModel(private val appDatabase: AppDatabase,
                           private val firebaseFirestore: FirebaseFirestore) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfile?>(
        null
    )
    val userProfile: StateFlow<UserProfile?> get() = _userProfile

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    private val _filteredProperties = MutableStateFlow<List<Property>>(emptyList())
    val filteredProperties: StateFlow<List<Property>> = _filteredProperties
    private val userProfileDao = appDatabase.userProfileDao()


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


        private val firestore = FirebaseFirestore.getInstance()  // Firestore instance



        // Function to save or update user profile in Firestore
        suspend fun saveUserProfileToFirestore(userProfile: FirestoreUserProfile) {
            try {
                firestore.collection("users").document(userProfile.email).set(userProfile).await()
                Log.d("UserProfileViewModel", "User profile saved to Firestore")
            } catch (e: Exception) {
                Log.e("UserProfileViewModel", "Error saving user profile to Firestore: ${e.message}")
            }
        }

        // Sync user profile data between Room and Firestore
        fun syncUserProfileToFirestore(userProfile: UserProfile) {
            viewModelScope.launch {
                val firestoreUserProfile = FirestoreUserProfile(
                    email = userProfile.email,
                    firstName = userProfile.firstName,
                    lastName = userProfile.lastName,
                    contact = userProfile.contact,
                    city = userProfile.city,
                    region = userProfile.region,
                    postalCode = userProfile.postalCode,
                    rating = userProfile.rating
                )
                saveUserProfileToFirestore(firestoreUserProfile)
            }
        }

    fun getUserProfileFromFirestore(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Try fetching from Firestore
                val documentSnapshot = firebaseFirestore.collection("users").document(email).get().await()

                if (documentSnapshot.exists()) {
                    val userProfile = documentSnapshot.toObject(UserProfile::class.java)
                    if (userProfile != null) {
                        // If data found in Firestore, update Room database
                        userProfileDao.insert(userProfile)
                    }
                }
            } catch (e: Exception) {
                Log.e("UserProfile", "Error fetching user from Firestore: ${e.message}")
            }
        }
    }




    // Function to update user profile in Firestore and Room database
    fun updateUserProfileInFirestoreAndRoom(
        email: String,
        firstName: String,
        lastName: String,
        contact: String,
        city: String,
        region: String,
        postalCode: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Update Firestore
                val userProfile = UserProfile(
                    email,
                    firstName,
                    lastName,
                    contact,
                    city,
                    region,
                    postalCode
                )
                firebaseFirestore.collection("users").document(email).set(userProfile).await()

                // Update Room database
                userProfileDao.updateUserr(email, firstName, lastName, contact, city, region, postalCode)
            } catch (e: Exception) {
                Log.e("UserProfile", "Error updating user profile: ${e.message}")
            }
        }
    }
}

