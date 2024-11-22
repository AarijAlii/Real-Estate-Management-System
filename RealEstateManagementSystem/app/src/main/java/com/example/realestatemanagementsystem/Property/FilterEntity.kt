package com.example.realestatemanagementsystem.Property

data class PropertyFilter(
    var city: String? = null,
    var state: String? = null,
    var minPrice: Double? = null,
    var maxPrice: Double? = null,
    var zipCode: String? = null,
    var type: String? = null,  // e.g., "Apartment", "House", etc.
    var noOfRooms: Int? = null,
    var bedrooms: Int? = null,
    var garage: Boolean? = null  // `true` for required, `false` for no garage, `null` for any
)
