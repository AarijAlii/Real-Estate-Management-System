package com.example.realestatemanagementsystem.user.authentication

sealed class AuthState{
    data object Success: AuthState()
    data object Failed: AuthState()
    data object Loading: AuthState()
    data class  Error(val message: String): AuthState()
}