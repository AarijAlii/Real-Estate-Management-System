package com.example.realestatemanagementsystem.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
fun ReviewFormScreen(
    email: String,
    contractorId: Int,
    reviewViewModel: ReviewViewModel,
    onReviewSubmitted: () -> Unit
) {
    var rating by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Submit Review", style = MaterialTheme.typography.h6)

        OutlinedTextField(
            value = rating,
            onValueChange = { rating = it },
            label = { Text("Rating (1-5)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (rating.toFloatOrNull()!! in 1f..5f) {
                reviewViewModel.submitReview(
                    contractorId = contractorId,
                    userEmail = email, // Replace with logged-in user email
                    rating = rating.toFloat(),
                    comment = comment
                )
                onReviewSubmitted()
            }
        }) {
            Text("Submit Review")
        }
    }
}
