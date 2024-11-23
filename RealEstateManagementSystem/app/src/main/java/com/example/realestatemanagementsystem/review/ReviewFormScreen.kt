package com.example.realestatemanagementsystem.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SubmitReviewScreen(
    contractorId: Int,
    email: String,
    reviewViewModel: ReviewViewModel,
    onReviewSubmitted: () -> Unit
) {
    var rating by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Submit a Review", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = rating,
            onValueChange = { rating = it },
            label = { Text("Rating (1-5)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (rating.isNotEmpty() && rating.toIntOrNull() in 1..5) {
                    reviewViewModel.submitReview(
                        contractorId = contractorId,
                        userEmail = email,
                        rating = rating.toInt(),
                        comment = comment
                    )
                    onReviewSubmitted()
                } else {
                    // Handle invalid rating input
                }
            }
        ) {
            Text("Submit")
        }
    }
}
