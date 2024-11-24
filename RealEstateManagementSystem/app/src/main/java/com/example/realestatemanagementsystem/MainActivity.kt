package com.example.realestatemanagementsystem

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.realestatemanagementsystem.Navigation.NavigationGraph
import com.example.realestatemanagementsystem.ui.theme.RealEstateManagementSystemTheme
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            RealEstateManagementSystemTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val db = FirebaseFirestore.getInstance()
                    db.collection("test").add(mapOf("message" to "Hello Firebase xd!"))
                        .addOnSuccessListener {
                            Log.d("FirebaseSetup", "Connection successful")
                        }
                        .addOnFailureListener {
                            Log.e("FirebaseSetup", "Connection failed: ${it.message}")
                        }

                    NavigationGraph(navController, authViewModel)
                }
            }
        }
    }
}