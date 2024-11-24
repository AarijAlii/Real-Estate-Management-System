package com.example.realestatemanagementsystem.user.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanagementsystem.user.UserProfile.UuserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class AuthenticationViewModel : ViewModel() {
    private val _signUpResult = MutableLiveData<String>()
    val signUpResult: LiveData<String> = _signUpResult

    private val _signInResult = MutableLiveData<String>()
    val signInResult: LiveData<String> = _signInResult

    private val authApi = RetrofitClientt.api // Your Retrofit Client instance

    // Sign Up
    fun signUp(user: UuserProfile) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authApi.signUp(user)
                if (response.isSuccessful) {
                    _signUpResult.postValue(response.body()?.message ?: "SignUp successful.")
                } else {
                    Log.e("SignUp", "Error: ${response.code()} - ${response.message()}")
                    val errorBody = response.errorBody()?.string()
                    Log.e("SignUp Error", errorBody ?: "Unknown error")
                    _signUpResult.postValue("SignUp failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _signUpResult.postValue("Error: ${e.message}")
                Log.e("SignUp Error", e.message.orEmpty())
            }
        }
    }

    // Sign In
    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authApi.signIn(SignInRequest(email, password))
                if (response.isSuccessful) {
                    _signInResult.postValue(response.body()?.message)
                } else {
                    _signInResult.postValue("SignIn failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _signInResult.postValue("Error: ${e.message}")
            }
        }
    }
}
