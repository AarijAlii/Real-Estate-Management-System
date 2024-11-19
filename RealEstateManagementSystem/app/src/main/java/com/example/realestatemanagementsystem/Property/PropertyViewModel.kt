package com.example.realestatemanagementsystem.Property

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PropertyViewModel(private val propertyDao: PropertyDao) : ViewModel() {

    private val _soldProperties = MutableStateFlow<List<Property>>(emptyList())
    val soldProperties: StateFlow<List<Property>> = _soldProperties

    private val _unsoldProperties = MutableStateFlow<List<Property>>(emptyList())
    val unsoldProperties: StateFlow<List<Property>> = _unsoldProperties

    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties: StateFlow<List<Property>> = _properties

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage


    fun loadSoldListings(email: String) {
        viewModelScope.launch {
            try {
                val soldList = propertyDao.getSoldListings(email)
                _soldProperties.value = soldList
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching sold properties: ${e.message}"
            }
        }
    }


    fun loadCurrentListings(email: String) {
        viewModelScope.launch {
            try {
                val unsoldList = propertyDao.getCurrentListings(email)
                _unsoldProperties.value = unsoldList
                //_properties.value = propertyDao.getCurrentListings(email)
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching properties: ${e.message}"
            }
        }
    }


    fun adddProperty(property: Property) {
        viewModelScope.launch {
            try {
                propertyDao.adddProperty(
                    city = property.city,
                    state = property.state,
                    propertyNumber = property.propertyNumber,
                    rooms = property.rooms,
                    bedrooms = property.bedrooms,
                    garage = property.garage,
                    area = property.area,
                    type = property.type,
                    price = property.price,
                    zipCode = property.zipCode,
                    email = property.email,
                    isSold = property.isSold
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error adding property: ${e.message}"
            }
        }
    }

    fun updateeProperty(property: Property) {
        viewModelScope.launch {
            try {
                propertyDao.updateeProperty(
                    propertyId = property.propertyId,
                    city = property.city,
                    state = property.state,
                    propertyNumber = property.propertyNumber,
                    rooms = property.rooms,
                    bedrooms = property.bedrooms,
                    garage = property.garage,
                    area = property.area,
                    type = property.type,
                    price = property.price,
                    zipCode = property.zipCode,
                    isSold = property.isSold
                )
            } catch (e: Exception) {
                _errorMessage.value = "Error updating property: ${e.message}"
            }
        }
    }

    fun deleteeProperty(propertyId: Int) {
        viewModelScope.launch {
            try {
                propertyDao.deleteeProperty(propertyId)
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting property: ${e.message}"
            }
        }
    }

    fun markAsSold(propertyId: Int) {
        viewModelScope.launch {
            try {
                propertyDao.markPropertyAsSold(propertyId)
            } catch (e: Exception) {
                _errorMessage.value = "Error marking property as sold: ${e.message}"
            }
        }
    }


//    fun addProperty(property: Property) {
//        viewModelScope.launch {
//            try {
//                propertyDao.addProperty(property)
//            } catch (e: Exception) {
//                _errorMessage.value = "Error adding property: ${e.message}"
//            }
//        }
//    }
//
//    fun updateProperty(property: Property) {
//        viewModelScope.launch {
//            try {
//                propertyDao.updateProperty(property)
//            } catch (e: Exception) {
//                _errorMessage.value = "Error updating property: ${e.message}"
//            }
//        }
//    }
//
//    fun deleteProperty(property: Property) {
//        viewModelScope.launch {
//            try {
//                propertyDao.deleteProperty(property)
//            } catch (e: Exception) {
//                _errorMessage.value = "Error deleting property: ${e.message}"
//            }
//        }
//    }


//BUYSCREEN VIEWMODEL

    private val _filteredProperties = MutableStateFlow<List<Property>>(emptyList())
    val filteredProperties: StateFlow<List<Property>> = _filteredProperties

    private val _searchResults = MutableStateFlow<List<Property>>(emptyList())
    val searchResults: StateFlow<List<Property>> = _searchResults


    //Display All Porperties
    fun getAllProperties() {
        viewModelScope.launch {
            try {
                propertyDao.getAllProperties().collect { result ->
                    _properties.value = result
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching all properties: ${e.message}"
            }
        }
    }

    // Filter Properties for BuyScreen
//    fun filterProperties(
//        city: String?,
//        state: String?,
//        minPrice: Double?,
//        maxPrice: Double?,
//        zipCode: String?,
//        type: String?,
//        noOfRooms: Int?,
//        bedrooms: Int?,
//        garage: Boolean?,
//        sortOrder: String?
//    ) {
//        viewModelScope.launch {
//            try {
//                propertyDao.filterProperties(
//                    city, state, minPrice, maxPrice, zipCode, type, noOfRooms, bedrooms, garage, sortOrder
//                ).collect { result ->
//                    _filteredProperties.value = if (sortOrder == "Descending") {
//                        result.sortedByDescending { it.price }
//                    } else {
//                        result.sortedBy { it.price }
//                    }
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "Error filtering properties: ${e.message}"
//                //_filteredProperties.value = emptyList() // Clear on error
//            }
//        }
//    }

    fun filterProperties(
        city: String?,
        state: String?,
        minPrice: Double?,
        maxPrice: Double?,
        zipCode: String?,
        type: String?,
        noOfRooms: Int?,
        bedrooms: Int?,
        garage: Boolean?,
        sortOrder: String?
    ) {
        viewModelScope.launch {
            try {
                // Apply filters using the DAO method
                propertyDao.filterProperties(
                    city, state, minPrice, maxPrice, zipCode, type, noOfRooms, bedrooms, garage, sortOrder
                ).collect { result ->
                    // If no properties match the filter, set an empty list
                    if (result.isEmpty()) {
                        _filteredProperties.value = emptyList() // Explicitly clear the filtered properties list
                    } else {
                        // Sort the result based on the selected sortOrder
                        _filteredProperties.value = if (sortOrder == "Descending") {
                            result.sortedByDescending { it.price }
                        } else {
                            result.sortedBy { it.price }
                        }
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error filtering properties: ${e.message}"
                _filteredProperties.value = emptyList() // Clear on error
            }
        }
    }




    // Search Property by ID
    fun searchByPropertyId(propertyId: Int) {
        viewModelScope.launch {
            try {
                propertyDao.searchByPropertyId(propertyId).collect { property ->
                    _searchResults.value = if (property != null) listOf(property) else emptyList()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error searching property by ID: ${e.message}"
            }
        }
    }

    // Search Properties by Email
    fun searchByEmail(userEmail: String) {
        viewModelScope.launch {
            try {
                propertyDao.searchByEmail(userEmail).collect { result ->
                    _searchResults.value = result
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error searching properties by email: ${e.message}"
            }
        }
    }
}