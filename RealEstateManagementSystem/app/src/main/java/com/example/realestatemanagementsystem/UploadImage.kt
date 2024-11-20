package com.example.realestatemanagementsystem

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.ContentResolverCompat
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
//
//object ImageUploader {
//
//    // Function to upload image to Imgur
//    fun uploadImageToImgur(context: Context, uri: Uri, clientId: String, callback: (String?) -> Unit) {
//        // Convert URI to File
//        val file = getFileFromUri(context, uri)
//
//        if (file != null) {
//            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
//            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//            // Call Imgur API to upload the image
//            RetrofitClient.imgurApiService.uploadImage("Client-ID $clientId", body).enqueue(object : retrofit2.Callback<ImgurResponse> {
//                override fun onResponse(call: Call<ImgurResponse>, response: retrofit2.Response<ImgurResponse>) {
//                    if (response.isSuccessful) {
//                        // Get the image URL
//                        val imageUrl = response.body()?.data?.link
//                        callback(imageUrl) // Return the image URL
//                    } else {
//                        callback(null) // Handle failure
//                    }
//                }
//
//                override fun onFailure(call: Call<ImgurResponse>, t: Throwable) {
//                    callback(null) // Handle error
//                }
//            })
//        } else {
//            callback(null) // Handle invalid file
//        }
//    }
//
//    // Helper function to convert URI to File
//    private fun getFileFromUri(context: Context, uri: Uri): File? {
//        // Create a temp file to store the image
//        val file = File(context.cacheDir, UUID.randomUUID().toString() + ".jpg")
//        try {
//            context.contentResolver.openInputStream(uri)?.use { inputStream ->
//                FileOutputStream(file).use { outputStream ->
//                    inputStream.copyTo(outputStream)
//                }
//            }
//            return file
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return null
//    }
//}

suspend fun uploadImageToImgur(context: Context, uri: Uri, clientId: String, callback: (String?) -> Unit) {
    try {
        val file = uri.path?.let { File(it) }  // Get the file from URI
        val requestFile =
            file?.asRequestBody("image/*".toMediaTypeOrNull())  // Create request body for file
        val body = requestFile?.let { MultipartBody.Part.createFormData("image", file?.name, it) }

        // Create Retrofit instance and call the API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.imgur.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ImgurApiService::class.java)
        val response = body?.let { service.uploadImage("Client-ID $clientId", it) }

        // Check the response
        if (response != null) {
            if (response.isSuccessful) {
                val imageUrl = response.body()?.data?.link
                Log.d("ImgurUpload", "Image uploaded successfully: $imageUrl")
                callback(imageUrl)  // Return the image URL if upload is successful
            } else {
                Log.e("ImgurUpload", "Failed to upload: ${response.message()}")
                callback(null)  // If upload fails, return null
            }
        }
    } catch (e: Exception) {
        Log.e("ImgurUpload", "Error during upload: ${e.message}")
        callback(null)  // In case of error, return null
    }
}

//fun uploadImageToImgur(context: Context, uri: Uri, clientId: String, callback: (String?) -> Unit) {
//    try {
//        val file = File(uri.path)  // Get the file from URI
//        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())  // Create request body for file
//        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//        // Make Retrofit API call
//        val response = RetrofitClient.imgurService.uploadImage("Client-ID $clientId", body)
//
//        // Handle the response
//        if (response.isSuccessful) {
//            val imageUrl = response.body()?.data?.link
//            Log.d("ImgurUpload", "Image uploaded successfully: $imageUrl")
//            callback(imageUrl)  // Return the image URL if upload is successful
//        } else {
//            Log.e("ImgurUpload", "Failed to upload: ${response.message()}")
//            callback(null)  // If upload fails, return null
//        }
//    } catch (e: Exception) {
//        Log.e("ImgurUpload", "Error during upload: ${e.message}")
//        callback(null)  // In case of error, return null
//    }
//}

