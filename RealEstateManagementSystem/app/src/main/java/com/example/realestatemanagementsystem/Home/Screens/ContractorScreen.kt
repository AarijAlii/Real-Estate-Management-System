package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.realestatemanagementsystem.contractor.Contractor
import com.example.realestatemanagementsystem.contractor.ContractorViewModel
import com.example.realestatemanagementsystem.contractor.ContractorWithUserProfile
import com.example.realestatemanagementsystem.review.ReviewViewModel
import com.example.realestatemanagementsystem.util.ContractorCard
import kotlinx.coroutines.launch

@Composable
fun ContractorScreen(modifier: Modifier = Modifier,contractorViewModel: ContractorViewModel,innerPadding:PaddingValues,navHostController: NavHostController,email:String,reviewViewModel: ReviewViewModel) {

    var showDialog by remember { mutableStateOf(false) }
    var showInfo by remember { mutableStateOf(false) }
    val allContractors by contractorViewModel.contractors.observeAsState(emptyList())
    var contractor by remember { mutableStateOf<Contractor?>(null) }
    var selectedContractor by remember { mutableStateOf<ContractorWithUserProfile?>(null) }
    val scope= rememberCoroutineScope()
    fun refreshContractor(){
        scope.launch {
        contractorViewModel.fetchAllContractorsWithDetails()
    }}
    LaunchedEffect(Unit) {
            contractorViewModel.fetchAllContractorsWithDetails()
            contractor=contractorViewModel.getContractorByEmail(email)

        }
    Column( modifier = Modifier
        .padding(innerPadding)
        ) {
        if (allContractors.isEmpty()) {
            // Show message if no contractors
            Box(contentAlignment = Alignment.Center,modifier = Modifier
                .weight(1f)
                .fillMaxWidth()){
           Text(text = "No contractors available")}
        } else {

                LazyColumn(modifier=Modifier.weight(1f)) {
                    items(allContractors) { contractor ->
                        // Display each property (replace with your card implementation)
                        ContractorCard(
                            contractor = contractor,
                            modifier = Modifier,
                            innerPadding = innerPadding,
                            reviewViewModel = reviewViewModel,
                            contractorViewModel = contractorViewModel,
                            onRefresh = {
                                contractorViewModel.fetchAllContractorsWithDetails()

                            },
                            onCardClick = {
                                showInfo=true
                                selectedContractor = contractor
                            }
                        )


                    }
                }


        }

        if(showInfo==true){
            selectedContractor?.let { ContractorInfo(onDismiss = {showInfo=false}, contractor = it, reviewViewModel = reviewViewModel) }
        }
        if (contractor == null) { // Show the button only if the user is not a contractor
            Button(
                onClick = {
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Red,
                    disabledContentColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Register")
            }
        }
    }
    if (showDialog) {
        Dialog (onDismissRequest = {showDialog=false})  {
          Box(modifier = Modifier

              .wrapContentHeight()
              .padding(16.dp)
              .background(
                  color = Color.White, // Set the background color
                  shape = RoundedCornerShape(12.dp)
              ) ){  ContractorFormScreen(email = email, contractorViewModel = contractorViewModel, onRegistrationComplete = {
                showDialog=false
                navHostController.popBackStack()
            })}
        }

    }
}

@Composable
fun ContractorInfo(modifier: Modifier = Modifier,onDismiss:()->Unit,contractor: ContractorWithUserProfile,reviewViewModel: ReviewViewModel) {
    val contractorReview by reviewViewModel.reviews.collectAsState(emptyList())
    LaunchedEffect(contractor.contractorId) {
        reviewViewModel.getReviewsForContractor(contractor.contractorId)
    }

    Dialog(onDismissRequest = { onDismiss()}) {
        Box(modifier = Modifier
            .height(800.dp)
            .padding(16.dp)
            .shadow(16.dp, RoundedCornerShape(16.dp))
            .background(
                color = Color.White,)){
            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .zIndex(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }
                Column(modifier=Modifier.padding(16.dp)){
                    if (contractor.imageUrl!=null) {


                        AsyncImage(
                            model=contractor.imageUrl,
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .padding(8.dp)
                                .size(100.dp), // Adjust size as needed
                            contentScale = ContentScale.Fit
                        )


                    }
                    else{
                        Box(modifier = Modifier.size(100.dp)){
                            Text(text = "No image available")
                        }
                    }
                    Text(text= "Contractor Info: ", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier=Modifier.height(8.dp))
                    Text("Name: ${contractor.firstName} ${contractor.lastName}")
                    Text("Email: ${contractor.email}")
                    Text("Phone: ${contractor.contact}")
                    Text("Rating: ${contractor.overallRating}")
                    Text("Deals in: ${contractor.speciality}")
                    Spacer(modifier=Modifier.height(16.dp))
                    Text(text= "Review: ", style = MaterialTheme.typography.titleLarge,modifier=Modifier.padding(bottom = 8.dp))
                    LazyColumn(){
                        items(contractorReview){review ->

                                Text(review.email)
                                Text("${review.rating}")
                                Text(review.comment)


                            Divider(modifier = Modifier.wrapContentHeight())
                            Spacer(modifier=Modifier.height(8.dp))

                    }}

                }
            } // Set the background color

    }
}

