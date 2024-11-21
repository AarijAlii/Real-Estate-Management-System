package com.example.realestatemanagementsystem.property

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanagementsystem.image.ImageDao
import com.example.realestatemanagementsystem.image.ImageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.realestatemanagementsystem.image.uploadImageToImgur


class PropertyViewModel(private val propertyDao: PropertyDao, private val imageDao: ImageDao) : ViewModel() {

 //       private val _errorMessage = MutableLiveData<String>()
    //    val errorMessage: MutableLiveData<String> get() = _errorMessage
        suspend fun addProperty(property: Property, imageUris: List<Uri>, context: Context, clientId: String) {

            val imageUrls = mutableListOf<String>()

            // Upload images to Imgur
            imageUris.forEach { uri ->
                uploadImageToImgur(context, uri, clientId) { imageUrl ->
                    if (imageUrl != null) {
                        imageUrls.add(imageUrl)

                        // Once all images are uploaded, insert the property and image URLs into the database
                        if (imageUrls.size == imageUris.size) {
                            insertPropertyWithImages(property, imageUrls)
                        }
                    } else {
                        _errorMessage.value = "Failed to upload some images"
                    }
                }
            }
        }


        private fun insertPropertyWithImages(
            property: Property,
            imageUrls: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Insert the property using the custom adddProperty query
                val propertyId = propertyDao.adddProperty(
                    property.city, property.state, property.propertyNumber,
                    property.rooms, property.bedrooms, property.garage,
                    property.area, property.type, property.price,
                    property.zipCode, property.email, property.isSold
                )

                // Prepare the images to insert
                val images = imageUrls.map { imageUrl ->
                    ImageEntity(propertyId = propertyId, imageUrl = imageUrl)  // No need to call toInt() here
                }

                // Insert the images into the database
                imageDao.insertImages(images)
            } catch (e: Exception) {
                _errorMessage.value = "Error adding property: ${e.message}"
            }
        }
    }

//        // LiveData to observe uploaded image URLs
//        private val _uploadedImageUrls = MutableLiveData<List<String>>()
//        val uploadedImageUrls: LiveData<List<String>> = _uploadedImageUrls
//
//        fun addProperty(property: Property, imageUris: List<Uri>, context: Context, clientId: String) {
//            viewModelScope.launch(Dispatchers.IO) {
//                try {
//                    // Insert property into the local database
//                    val propertyId = propertyDao.insertProperty(property)
//
//                    // Upload images to Imgur (or another cloud service)
//                    val imageUrls = uploadImagesToCloud(imageUris, clientId)
//
//                    // Save the image URLs into the database
//                    val images = imageUrls.map { ImageEntity(propertyId = propertyId, imageUrl = it) }
//                    imageDao.insertImages(images)
//
//                    // Return the image URLs to the UI
//                    _uploadedImageUrls.postValue(imageUrls)
//
//                } catch (e: Exception) {
//                    // Handle errors
//                }
//            }
//        }
//
//        private suspend fun uploadImagesToCloud(imageUris: List<Uri>, clientId: String): List<String> {
//            val imageUrls = mutableListOf<String>()
//            for (uri in imageUris) {
//                val imageUrl = uploadImageToImgur(uri, clientId)
//                imageUrls.add(imageUrl)
//            }
//            return imageUrls
//        }
//
//        private suspend fun uploadImageToImgur(uri: Uri, clientId: String): String {
//            // Logic to upload the image to Imgur and return the image URL
//            val requestBody = // Prepare the image data for upload
//            val response = imgurService.uploadImage(clientId, requestBody)
//            if (response.isSuccessful) {
//                return response.body()?.link ?: ""
//            } else {
//                throw Exception("Image upload failed")
//            }
//        }



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

    private val _imageUploadStatus = MutableStateFlow<String>("")
    val imageUploadStatus: StateFlow<String> = _imageUploadStatus


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
            try {
                val sortedProperties = when (option) {
                    "Price: Low to High" -> propertyDao.getPropertiesByPriceAsc()
                    "Price: High to Low" -> propertyDao.getPropertiesByPriceDesc()
                    else -> propertyDao.getAllBuyingProperties() // Default: unsorted or original list
                }
                _unsoldProperties.value = sortedProperties
            } catch (e: Exception) {
                _errorMessage.value = "Error sorting properties: ${e.message}"
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

    ) {
        viewModelScope.launch {
            try {
                // Apply filters using the DAO method
                propertyDao.filterProperties(
                    city, state, minPrice, maxPrice, zipCode, type, noOfRooms, bedrooms, garage
                ).collect { result ->
                    // If no properties match the filter, set an empty list
                    if (result.isEmpty()) {
                        _unsoldProperties.value = emptyList() // Explicitly clear the filtered properties list
                    } else {
                        // Sort the result based on the selected sortOrder
                        _unsoldProperties.value =result
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
