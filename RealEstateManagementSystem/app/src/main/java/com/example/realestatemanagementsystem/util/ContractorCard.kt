package com.example.realestatemanagementsystem.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.contractor.ContractorViewModel
import com.example.realestatemanagementsystem.contractor.ContractorWithUserProfile
import com.example.realestatemanagementsystem.review.ReviewViewModel

@Composable
fun ContractorCard(modifier: Modifier = Modifier,innerPadding:PaddingValues,contractor: ContractorWithUserProfile,reviewViewModel: ReviewViewModel,contractorViewModel: ContractorViewModel,onRefresh:()->Unit,onCardClick:()->Unit) {

    var showReviewForm by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .background(color = Color.White)
            .padding()
            .padding(8.dp)
            .clickable { onCardClick() }
    ) {
        Row(
            modifier = Modifier
        ) {
            // Image Section
            Image(
                painter = painterResource(id = R.drawable.house_file), // Replace with your image resource
                contentDescription = "Card Image",
                contentScale = ContentScale.Crop,

                modifier = Modifier
                    .weight(1f)

                    .padding(2.dp)
                    .shadow(16.dp, RoundedCornerShape(16.dp))
            )

            // Separator

            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp)
                    .width(1.dp)
                    .height(100.dp)


            )

            // Information Section
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${contractor.firstName} ${contractor.lastName}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.Star, contentDescription = "Star Icon",Modifier.size(16.dp),tint= Color.Yellow)
                    Text(
                        text = " ${contractor.overallRating}/5",
                        style= MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = contractor.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = contractor.contact,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )

                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Deals in: ${contractor.speciality}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { showReviewForm=true },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Color.Red,
                            disabledContentColor = Color.Gray
                        ),
                    ) {
                        Text(text = "Hire")
                    }
                }
            }
        }
    }
    if(showReviewForm){
        Dialog(onDismissRequest = { showReviewForm=false }) {
            SubmitReviewScreen(contractorId = contractor.contractorId, email = contractor.email, reviewViewModel = reviewViewModel, onReviewSubmitted = {showReviewForm=false
            onRefresh()})
        }
    }
}
@Composable
fun SubmitReviewScreen(
    contractorId: Int,
    email: String,
    reviewViewModel: ReviewViewModel,
    onReviewSubmitted: () -> Unit
) {
    var rating by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    Box(contentAlignment = Alignment.Center,modifier = Modifier.wrapContentHeight().background(color = Color.White).clip( RoundedCornerShape(16.dp))) {
        Column(
            modifier = Modifier

                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
    Column(
        modifier = Modifier

            .padding(16.dp)
    ) {
        Text(text = "Submit a Review", style = MaterialTheme.typography.titleLarge)
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
            modifier=Modifier.height(100.dp),
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
                        rating = rating.toFloat(),
                        comment = comment
                    )

                    onReviewSubmitted()
                }


            },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Red,
                disabledContentColor = Color.Gray
            )
        ) {
            Text("Submit")
        }
    }
    }
}
}
