package com.example.realestatemanagementsystem.property

data class FireStoreProperty(
    val city: String = "",
    val state: String = "",
    val propertyNumber: String = "",
    val rooms: Int = 0,
    val bedrooms: Int = 0,
    val garage: Int = 0,
    val area: Double = 0.0,
    val type: String = "",
    val price: Double = 0.0,
    val zipCode: String = "",
    val email: String = "",
    val isSold: Boolean = false
)
