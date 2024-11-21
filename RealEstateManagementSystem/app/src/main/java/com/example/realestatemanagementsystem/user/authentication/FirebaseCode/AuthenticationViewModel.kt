package com.example.realestatemanagementsystem.user.authentication.FirebaseCode


import androidx.lifecycle.ViewModel
import com.example.realestatemanagementsystem.AppDatabase
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AuthViewModel() : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Failed)
    val authState: StateFlow<AuthState> = _authState
    //val userEmail = mutableStateOf<String?>(null)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //private val userRepository: UserRepository

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


//    fun login(email: String, password: String) {
//        if (email.isEmpty() || password.isEmpty()) {
//            _authState.value = AuthState.Error("Please fill all fields")
//            return
//        }
//
//        _authState.value = AuthState.Loading
//        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                _authState.value = AuthState.Success
//            } else {
//                _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
//            }
//        }
//    }

//    fun signIn(
//        email: String,
//        password: String,
//        onSuccess: (UserProfile) -> Unit, // Return UserProfile on success
//        onError: (String) -> Unit,
//        appDatabase: AppDatabase // Pass the appDatabase here
//    ) {
//        if (email.isEmpty() || password.isEmpty()) {
//            onError("Please fill all fields")
//            return
//        }
//
//        _authState.value = AuthState.Loading
//        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                // Get the logged-in user's email
//                val currentUserEmail = auth.currentUser?.email
//                if (currentUserEmail != null) {
//                    // Fetch the user profile from the local database using UserProfileViewModel
//                    val userProfileViewModel = UserProfileViewModel(appDatabase)
//                    userProfileViewModel.getUserByEmail(
//                        email = currentUserEmail,
//                        onSuccess = { userProfile ->
//                            onSuccess(userProfile) // Pass the user profile to the callback
//                        },
//                        onError = { error ->
//                            onError(error) // Pass the error to the callback
//                        }
//                    )
//                } else {
//                    onError("Unable to retrieve user email.")
//                }
//            } else {
//                onError(task.exception?.message ?: "Sign-In failed")
//            }
//        }
//    }


//    fun signUp(email: String, password: String) {
//        if (email.isEmpty() || password.isEmpty()) {
//            _authState.value = AuthState.Error("Please fill all fields")
//            return
//        }
//
//        _authState.value = AuthState.Loading
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                _authState.value = AuthState.Success
//            } else {
//                _authState.value = AuthState.Error(task.exception?.message ?: "SignUp failed")
//            }
//        }
//    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Failed
    }


    fun getCurrentUserEmail(): String? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.email
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
}