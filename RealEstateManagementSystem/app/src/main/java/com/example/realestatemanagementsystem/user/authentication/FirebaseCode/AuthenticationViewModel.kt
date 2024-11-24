package com.example.realestatemanagementsystem.user.authentication.FirebaseCode

import androidx.lifecycle.ViewModel
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Failed)
    val authState: StateFlow<AuthState> = _authState
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val firestore = FirebaseFirestore.getInstance()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Failed
        } else {
            _authState.value = AuthState.Success
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Failed
    }

    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val currentUserEmail = auth.currentUser?.email
                if (currentUserEmail != null) {
                    onSuccess(currentUserEmail)
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
        firebaseFirestore: FirebaseFirestore
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
                    val userProfileViewModel = UserProfileViewModel(appDatabase, firebaseFirestore)
                    userProfileViewModel.insertUserProfile(
                        userProfile = userProfile,
                        onSuccess = {
                            saveEmailInFirestore(email)  // Save email to Firestore
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

    // Save the user's email to Firestore
    private fun saveEmailInFirestore(email: String) {
        val userMap = mapOf(
            "email" to email,
            "firstName" to "",
            "lastName" to "",
            "contact" to "",
            "city" to "",
            "region" to "",
            "postalCode" to ""
        )
        firestore.collection("users").document(email).set(userMap)
            .addOnSuccessListener {
                // Successfully saved to Firestore
            }
            .addOnFailureListener { e ->
                // Handle error
            }
    }
}

//class AuthViewModel() : ViewModel() {
//    private val _authState = MutableStateFlow<AuthState>(AuthState.Failed)
//    val authState: StateFlow<AuthState> = _authState
//    //val userEmail = mutableStateOf<String?>(null)
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//
//    //private val userRepository: UserRepository
//
//    init {
//        checkAuthStatus()
//    }
//
//    private fun checkAuthStatus() {
//        if (auth.currentUser == null) {
//            _authState.value = AuthState.Failed
//        } else {
//            _authState.value = AuthState.Success
//        }
//    }
//
//
//    fun signOut() {
//        auth.signOut()
//        _authState.value = AuthState.Failed
//    }
//
//
//    fun getCurrentUserEmail(): String? {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        return currentUser?.email
//    }
//
//    fun signIn(
//        email: String,
//        password: String,
//        onSuccess: (String) -> Unit,
//        onError: (String) -> Unit
//    ) {
//        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                // Get the logged-in user's email
//                val currentUserEmail = auth.currentUser?.email
//
//                    if (currentUserEmail != null) {
//                    onSuccess(currentUserEmail) // Pass the email to the onSuccess callback
//                } else {
//                    onError("Unable to retrieve user email.")
//                }
//            } else {
//                onError(task.exception?.message ?: "Sign in failed.")
//            }
//        }
//    }
//
//
//    fun signUp(
//        email: String,
//        password: String,
//        confirmPassword: String,
//        userProfile: UserProfile,
//        appDatabase: AppDatabase,
//        firebaseFirestore: FirebaseFirestore
//    ) {
//        if (email.isEmpty() || password.isEmpty()) {
//            _authState.value = AuthState.Error("Please fill all fields")
//            return
//        }
//
//        if (password != confirmPassword) {
//            _authState.value = AuthState.Error("Passwords do not match")
//            return
//        }
//
//        _authState.value = AuthState.Loading
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val currentUserEmail = auth.currentUser?.email
//                if (currentUserEmail != null) {
//                    val userProfileViewModel = UserProfileViewModel(appDatabase,firebaseFirestore )
//                    userProfileViewModel.insertUserProfile(
//                        userProfile = userProfile,
//                        onSuccess = {
//                            _authState.value = AuthState.Success
//                        },
//                        onError = { error ->
//                            _authState.value = AuthState.Error("Error inserting user profile: $error")
//                        }
//                    )
//                }
//            } else {
//                _authState.value = AuthState.Error(task.exception?.message ?: "SignUp failed")
//            }
//        }
//    }
//}