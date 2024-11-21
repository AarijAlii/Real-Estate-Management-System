package com.example.realestatemanagementsystem.image

import android.content.Context
import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import okhttp3.*

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

    val url = "https://api.imgur.com/3/image"
    val authorizationHeader = "68edc80df54e62f"
    val bearerToken = "42204b0e8ca8596e704a03a93d6718e7009096f5"
    val filePath =
        context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
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