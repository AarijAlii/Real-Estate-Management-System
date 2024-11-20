package com.example.realestatemanagementsystem.image

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

object FirebaseStorageUtil {

    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference

    // Modify the method to accept Context
    fun uploadImages(context: Context, imageUriList: List<Uri>, onComplete: (List<String>) -> Unit) {
        val imageUrls = mutableListOf<String>()

        imageUriList.forEach { uri ->
            val imageRef = storageReference.child("images").child(UUID.randomUUID().toString())
            imageRef.putFile(uri).addOnSuccessListener {
                imageRef.downloadUrl.addOnCompleteListener { task ->
                    val imageUrl = task.result.toString()
                    imageUrls.add(imageUrl)

                    // Check if all images are uploaded and trigger the callback
                    if (imageUrls.size == imageUriList.size) {
                        onComplete(imageUrls)
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
