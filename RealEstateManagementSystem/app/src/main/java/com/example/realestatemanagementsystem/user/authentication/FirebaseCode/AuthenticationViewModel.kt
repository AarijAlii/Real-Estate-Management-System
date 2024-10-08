package com.example.realestatemanagementsystem.user.authentication.FirebaseCode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

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
        }else{
            _authState.value = AuthState.Success
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
            }
        }
    }

    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "SignUp failed")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Failed
    }
}