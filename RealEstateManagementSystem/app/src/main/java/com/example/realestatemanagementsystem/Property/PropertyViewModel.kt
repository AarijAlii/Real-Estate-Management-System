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

    private val _searchResults = MutableStateFlow<List<Property>>(emptyList())
    val searchResults: StateFlow<List<Property>> = _searchResults

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _property = MutableStateFlow<Property?>(null)
    val property: StateFlow<Property?> = _property

    private val _filteredProperties = MutableStateFlow<List<Property>>(emptyList())
    val filteredProperties: StateFlow<List<Property>> = _filteredProperties


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

    fun getPropertyByID(propertyID:Int) {
        viewModelScope.launch {
            try {
                val property = propertyDao.getPropertyById(propertyID)
            }
            catch (e:Exception){
                _errorMessage.value = "Error fetching property: ${e.message}"
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
    fun getAllBuyingProperties() {
        viewModelScope.launch {
            try {
                val propertyList = propertyDao.getAllBuyingProperties()
                _unsoldProperties.value = propertyList
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching properties: ${e.message}"
            }

                     }
    }

    fun sortProperties(option: String) {
        viewModelScope.launch {
            when (option) {
                "Price: Low to High" -> _filteredProperties.value.sortedBy { it.price }
                "Price: High to Low" -> _filteredProperties.value.sortedByDescending { it.price }

                else -> properties.value
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
                        _filteredProperties.value =result
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error filtering properties: ${e.message}"
                _filteredProperties.value = emptyList() // Clear on error
            }
        }
    }
}