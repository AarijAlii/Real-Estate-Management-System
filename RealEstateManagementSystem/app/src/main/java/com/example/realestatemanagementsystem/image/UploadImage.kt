package com.example.realestatemanagementsystem.image

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.realestatemanagementsystem.property.Property
import com.example.realestatemanagementsystem.property.PropertyDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject

suspend fun uploadImageToImgur(context: Context, uri: Uri, clientId: String, callback: (String?) -> Unit) {

    val url = "https://api.imgur.com/3/image"
    val authorizationHeader = "68edc80df54e62f"
    val bearerToken = "42204b0e8ca8596e704a03a93d6718e7009096f5"
    val filePath = context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
    // Replace with the actual file path
    val title = "abc"
    val mediaType = "image"

    // Create the file
    val tempFile = File.createTempFile("temp", null, context.cacheDir)
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }

        // Create multipart body
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", tempFile.name, tempFile.asRequestBody())
            .addFormDataPart("title", title)
            .addFormDataPart("type", mediaType)
            .build()

        // Build the request
        val request = Request.Builder()
            .url(url)
            .header("Authorization", authorizationHeader)
            .header("Bearer", bearerToken)
            .post(requestBody)
            .build()

        // Create OkHttpClient
        val client = OkHttpClient()
        // Make the request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                println("Request failed: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        println("Response: ${it.body?.string()}")
                    } else {
                        println("Request failed with code: ${it.code} and message: ${it.message}")
                    }
                }
            }
        })
}

//suspend fun uploadImageToImgurAndSave(
//    context: Context,
//    uri: Uri,
//    clientId: String,
//    property: Property,
//    callback: (Boolean) -> Unit
//) {
//    val url = "https://api.imgur.com/3/image"
//    val authorizationHeader = "68edc80df54e62f" // Replace with your Imgur client ID or token
//    val bearerToken = "42204b0e8ca8596e704a03a93d6718e7009096f5" // Your Bearer Token
//
//    val tempFile = File.createTempFile("temp", null, context.cacheDir)
//
//    // Create file from URI
//    context.contentResolver.openInputStream(uri)?.use { inputStream ->
//        tempFile.outputStream().use { outputStream ->
//            inputStream.copyTo(outputStream)
//        }
//    }
//
//    // Create multipart body
//    val requestBody = MultipartBody.Builder()
//        .setType(MultipartBody.FORM)
//        .addFormDataPart("image", tempFile.name, tempFile.asRequestBody())
//        .build()
//
//    // Build the request
//    val request = Request.Builder()
//        .url(url)
//        .header("Authorization", authorizationHeader)
//        .header("Bearer", bearerToken)
//        .post(requestBody)
//        .build()
//
//    // Create OkHttpClient and make the request
//    val client = OkHttpClient()
//
//    client.newCall(request).enqueue(object : Callback {
//        override fun onFailure(call: okhttp3.Call, e: IOException) {
//            println("Request failed: ${e.message}")
//            callback(false)  // Callback on failure
//        }
//
//        override fun onResponse(call: okhttp3.Call, response: Response) {
//            response.use {
//                if (it.isSuccessful) {
//                    val responseBody = it.body?.string()
//                    val imageUrl = parseImageUrlFromResponse(responseBody)  // Assuming you have a response parsing method
//
//                    if (imageUrl != null) {
//                        // Save property to local database
//                        val propertyId = savePropertyToDatabase(property)
//
//                        // Save image URL to image table in local database
//                        saveImageToDatabase(propertyId, imageUrl)
//
//                        callback(true)  // Callback on success
//                    } else {
//                        // Handle error if no URL was returned
//                        println("Failed to get image URL")
//                        callback(false)
//                    }
//                } else {
//                    println("Request failed with code: ${it.code} and message: ${it.message}")
//                    callback(false)  // Callback on failure
//                }
//            }
//        }
//    })
//}
//fun parseImageUrlFromResponse(responseBody: String?): String? {
//    // Parse the response body to extract the image URL (assuming JSON structure)
//    return try {
//        val jsonResponse = JSONObject(responseBody)
//        val data = jsonResponse.getJSONObject("data")
//        data.getString("link") // Assuming the response includes a 'link' field for the image URL
//    } catch (e: Exception) {
//        null
//    }
//}