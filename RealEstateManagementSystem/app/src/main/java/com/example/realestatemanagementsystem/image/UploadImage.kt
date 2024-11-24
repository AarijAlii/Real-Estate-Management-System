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

//suspend fun uploadImageToImgur(context: Context, uri: Uri, clientId: String, callback: (String?) -> Unit) {
//
//    val url = "https://api.imgur.com/3/image"
//    val authorizationHeader = "68edc80df54e62f"
//    val bearerToken = "42204b0e8ca8596e704a03a93d6718e7009096f5"
//    val filePath = context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
//    // Replace with the actual file path
//    val title = "abc"
//    val mediaType = "image"
//
//    // Create the file
//    val tempFile = File.createTempFile("temp", null, context.cacheDir)
//    context.contentResolver.openInputStream(uri)?.use { inputStream ->
//        tempFile.outputStream().use { outputStream ->
//            inputStream.copyTo(outputStream)
//        }
//    }
//
//        // Create multipart body
//        val requestBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("image", tempFile.name, tempFile.asRequestBody())
//            .addFormDataPart("title", title)
//            .addFormDataPart("type", mediaType)
//            .build()
//
//        // Build the request
//        val request = Request.Builder()
//            .url(url)
//            .header("Authorization", authorizationHeader)
//            .header("Bearer", bearerToken)
//            .post(requestBody)
//            .build()
//
//        // Create OkHttpClient
//        val client = OkHttpClient()
//        // Make the request
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: okhttp3.Call, e: IOException) {
//                println("Request failed: ${e.message}")
//            }
//
//            override fun onResponse(call: okhttp3.Call, response: Response) {
//                response.use {
//                    if (it.isSuccessful) {
//                        println("Response: ${it.body?.string()}")
//                    } else {
//                        println("Request failed with code: ${it.code} and message: ${it.message}")
//                    }
//                }
//            }
//        })
//}

object ImageUploader {
    private val uploadedImageUrls = mutableListOf<String>() // List to store uploaded image URLs

    suspend fun uploadImageToImgur(context: Context, uri: Uri, clientId: String, callback: (String?) -> Unit) {
        val url = "https://api.imgur.com/3/image"
        val authorizationHeader = "Client-ID $clientId"

        // Create a temporary file from the image URI
        val tempFile = File.createTempFile("temp", null, context.cacheDir)
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        // Create the multipart request body
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", tempFile.name, tempFile.asRequestBody())
            .addFormDataPart("title", "Uploaded Image")
            .addFormDataPart("type", "image")
            .build()

        // Build the HTTP request
        val request = Request.Builder()
            .url(url)
            .header("Authorization", authorizationHeader)
            .post(requestBody)
            .build()

        // Create OkHttpClient and send the request
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ImageUploader", "Upload failed: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        val responseBody = it.body?.string()
                        val imageUrl = parseImageUrl(responseBody) // Extract the URL from the response
                        imageUrl?.let { url ->
                            uploadedImageUrls.add(url) // Add the URL to the list
                            Log.d("ImageUploader", "Uploaded image URL: $url") // Log the URL
                        }
                        callback(imageUrl)
                    } else {
                        Log.e("ImageUploader", "Upload failed with code: ${response.code}")
                        callback(null)
                    }
                }
            }
        })
    }

    private fun parseImageUrl(responseBody: String?): String? {
        // Example parsing logic (adjust for actual Imgur API response)
        // Extract "link" from the JSON response
        val regex = """"link":"(https?://[^"]+)"""".toRegex()
        val match = regex.find(responseBody ?: "")
        return match?.groups?.get(1)?.value
    }

    fun logAllUploadedUrls() {
        Log.d("ImageUploader", "All Uploaded URLs: $uploadedImageUrls")
    }
}
