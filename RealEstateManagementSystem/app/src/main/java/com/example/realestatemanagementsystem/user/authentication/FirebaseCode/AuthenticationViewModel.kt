package com.example.realestatemanagementsystem.user.authentication.FirebaseCode

import android.app.Application
import android.widget.Toast
import androidx.activity.result.launch
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanagementsystem.user.UserProfile.AppDatabase
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth

import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    //private val userRepository: UserRepository
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState>  = _authState

    init{
        checkAuthStatus()
    }
//    init {
//        userRepository = UserRepository(
//            FirebaseAuth.getInstance(),
//            Injection.instance()
//        )
//    }
//    private val _authResult = MutableLiveData<Result<Boolean>>()
//    val authResult: LiveData<Result<Boolean>> get() = _authResult

//    fun signUp(email: String, password: String) {
//        viewModelScope.launch {
//            _authResult.value = userRepository.signUp(email, password)
//        }
//    }

//    fun login(email: String, password: String) {
//        viewModelScope.launch {
//            _authResult.value = userRepository.login(email, password)
//        }
//    }

    private fun checkAuthStatus(){
        if (auth.currentUser == null){
            _authState.value = AuthState.Failed
        }else {

            _authState.value = AuthState.Success
        }
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Get the logged-in user's email
                _authState.value=AuthState.Success
                val currentUserEmail = auth.currentUser?.email
                if (currentUserEmail != null) {
                    onSuccess(currentUserEmail) // Pass the email to the onSuccess callback
                } else {
                    onError("Unable to retrieve user email.")
                }
            } else {
                onError(task.exception?.message ?: "Sign in failed.")
            }
        }

    }

    fun signUp(
        email: String,
        password: String,
        confirmPassword: String,
        userProfile: UserProfile,
        appDatabase: AppDatabase,
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }

        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUserEmail = auth.currentUser?.email
                if (currentUserEmail != null) {
                    val userProfileViewModel = UserProfileViewModel(appDatabase )
                    userProfileViewModel.insertUserProfile(
                        userProfile = userProfile,

                        onSuccess = {
                            _authState.value = AuthState.Success
                        },
                        onError = { error ->
                            _authState.value = AuthState.Error("Error inserting user profile: $error")
                        }
                    )
                }
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "SignUp failed")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            // Sign out from Firebase or your authentication service
            FirebaseAuth.getInstance().signOut()

            // Clear authentication state (e.g., using DataStore)


            // Update authState to reflect the logout
            _authState.value = AuthState.Failed
        }
    }

}