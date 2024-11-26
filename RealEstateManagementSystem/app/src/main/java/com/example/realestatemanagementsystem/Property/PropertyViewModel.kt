package com.example.realestatemanagementsystem.Property

//import com.example.realestatemanagementsystem.image.uploadImageToImgur
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realestatemanagementsystem.image.ImageDao
import com.example.realestatemanagementsystem.image.ImageUploader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PropertyViewModel(private val propertyDao: PropertyDao, private val imageDao: ImageDao) : ViewModel() {
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

    private val _compareList=MutableStateFlow<List<Property?>>(emptyList())
    val compareList: StateFlow<List<Property?>> get()=_compareList


    //       private val _errorMessage = MutableLiveData<String>()
    //    val errorMessage: MutableLiveData<String> get() = _errorMessage

    fun addCompareList(property: Property?){
        if (_compareList.value.size < 3) {
            val tempList=_compareList.value.toMutableList()
            tempList.add(property)
            _compareList.value=tempList
        } else {
            var added = false
            for (i in 0..2) {
                if (_compareList.value[i] == null) {
                    Log.d("property", "property added at index $i by ${property.toString()}")
                    val tempList=_compareList.value.toMutableList()
                    tempList[i]=property
                    _compareList.value = tempList
                    added = true
                    break
                }
            }
            if (!added) {
                Log.d("property", "property not added - no null slots")
            }
        }
        }


    fun removeCompareList(propertyNum: Int){
        val tempCompareList= _compareList.value.toMutableList()
        tempCompareList[propertyNum]=null
        _compareList.value=tempCompareList
    }
    fun resetCompareList(){
            }
    suspend fun addProperty(property: Property, imageUris: List<Uri>, context: Context, clientId: String) {
        // Launch a coroutine to perform property insertion
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Add the property and retrieve the generated propertyId
                val propertyId = propertyDao.adddProperty(
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

                // List to store uploaded image URLs
                val imageUrls = mutableListOf<String>()

                // Upload images one by one
                imageUris.forEach { uri ->
                    ImageUploader.uploadImageToImgur(context, uri, clientId) { imageUrl ->
                        if (imageUrl != null) {
                            imageUrls.add(imageUrl)

                            // Once all images are uploaded, insert them into the database
                            if (imageUrls.size == imageUris.size) {
                                Log.d("Image dao","${imageUrls.size}")
                                insertImagesForProperty(propertyId, imageUrls)
                            }
                        } else {
                            _errorMessage.value = "Failed to upload image: $uri"
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AddProperty", "Error adding property: ${e.message}")
            }
        }
    }




    private fun insertImagesForProperty(propertyId: Long, imageUrls: List<String>) {
        Log.d("Before Insert", "Inserting image for propertyId: $propertyId,${imageUrls.toString()}")

                // Insert images one by one
                Log.d("AddProperty0", "Inserting image for propertyId: $propertyId,${imageUrls.toString()}")
                imageUrls.forEach { imageUrl ->
                    Log.d("AddProperty", "Inserting image for propertyId: $propertyId,$imageUrl")
                    imageDao.insertImage(propertyId, imageUrl)
                }
                Log.d("AddProperty", "Images added successfully for propertyId: $propertyId")




    }
    private val _imageUrls = MutableStateFlow<List<String>>(emptyList())
    val imageUrls: StateFlow<List<String>> get() = _imageUrls

    fun fetchImagesForProperty(propertyId: Int) {
        viewModelScope.launch {
            try {
                val urls = imageDao.getImageUrlsForProperty(propertyId) // Fetch multiple URLs
                _imageUrls.value=urls// Update LiveData with the list of URLs
            } catch (e: Exception) {
                Log.e("PropertyViewModel", "Error fetching image URLs: ${e.message}")
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
    fun getAllBuyingProperties(filter: PropertyFilter,email:String) {
        viewModelScope.launch {
            try {
                filterProperties(filter,email)

            } catch (e: Exception) {
                _errorMessage.value = "Error fetching properties: ${e.message}"
            }

        }
    }

    fun sortProperties(option: String,email:String) {
        viewModelScope.launch {
            try {
                val sortedProperties = when (option) {
                    "Price: Low to High" -> _unsoldProperties.value.sortedBy { it.price }
                    "Price: High to Low" -> _unsoldProperties.value.sortedByDescending { it.price }
                    else -> propertyDao.getAllBuyingProperties(email) // Default: unsorted or original list
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
       filter: PropertyFilter,
        email:String
        ) {
        viewModelScope.launch {
            try {
                // Apply filters using the DAO method
                propertyDao.filterProperties(
                    filter.city, filter.state, filter.minPrice, filter.maxPrice, filter.zipCode, filter.type, filter.noOfRooms, filter.bedrooms, filter.garage,email
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