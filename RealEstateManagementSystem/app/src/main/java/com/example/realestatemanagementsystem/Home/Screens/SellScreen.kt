package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.Navigation.Screen
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.util.SellPropertyCards
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellScreen(
    email: String,
    userProfileDao: UserProfileDao,
    propertyViewModel: PropertyViewModel,
    navHostController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: UserProfileViewModel,

    innerPadding: PaddingValues
) {

    val scope = rememberCoroutineScope()
    val authState = authViewModel.authState.collectAsState()
    val unsoldProperties by propertyViewModel.unsoldProperties.collectAsState()

    val propertyErrorMessage by propertyViewModel.errorMessage.collectAsState()

    // Toggle state to show sold or unsold properties
    var showSold by remember { mutableStateOf(false) }

    // Load the unsold and sold properties when the screen is displayed
    LaunchedEffect(email) {
        propertyViewModel.loadCurrentListings(email)
        propertyViewModel.loadSoldListings(email) // Add this method in your ViewModel to load sold properties
    }
    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Failed) {
            navHostController.navigate(Screen.LoginScreen.route)
        }
    }

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Load the user profile when the screen is shown
    LaunchedEffect(email) {
        try {
            val profile = userProfileDao.getUserByEmail(email)
            userProfile = profile
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Failed to load profile: ${e.message}"
            isLoading = false
        }
    }

    if (propertyErrorMessage.isNotEmpty()) {
        Text(text = propertyErrorMessage, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
    }
    fun refreshProperties() {
        scope.launch {
            if (showSold) {
                propertyViewModel.loadSoldListings(email)
            } else {
                propertyViewModel.loadCurrentListings(email)
            }
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        if (userProfile != null) {
            val firstName by remember { mutableStateOf(userProfile?.firstName ?: "") }
            val lastName by remember { mutableStateOf(userProfile?.lastName ?: "") }

            val tabs = listOf("Unsold", "Sold")
            val pagerState = rememberPagerState(initialPage = 0){tabs.size}

                Column(modifier=Modifier.padding(innerPadding),horizontalAlignment = Alignment.CenterHorizontally){
                Column(modifier = Modifier.weight(1f)) {
                    // TabRow to display tabs
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,

                        contentColor = Color.White,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = Color.Red
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title, color = Color.Black) },
                                selected = pagerState.currentPage == index,
                                onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                            )
                        }
                    }

                    // HorizontalPager for swiping between tabs
                    HorizontalPager(
                        state = pagerState,
                    ) { page ->
                        when (page) {
                            1 -> SoldScreen(
                                propertyViewModel = propertyViewModel,
                                navHostController = navHostController,

                                email = email,
                                onDeleted = ::refreshProperties

                            )

                            0 -> UnSoldScreen(
                                propertyViewModel = propertyViewModel,
                                navHostController = navHostController,
                                onDeleted = ::refreshProperties,
                                email = email
                            )
                        }
                    }


                }

                    Button (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                        , colors = ButtonColors(
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            containerColor = Color.Red,
                            disabledContentColor = Color.White,
                        ),
                        onClick = {
                            navHostController.navigate("create_listing_screen/$email")
                        }
                    ) {
                        Text(text = "Create Listing")
                    }
                }



            // LazyColumn to show properties




        } else {
            Text("User profile not found.")
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
}
}


@Composable
fun SoldScreen(modifier: Modifier = Modifier,propertyViewModel: PropertyViewModel,email:String,navHostController: NavHostController,onDeleted: () -> Unit) {
    val soldProperties by propertyViewModel.soldProperties.collectAsState()
    // Load the unsold and sold properties when the screen is displayed
    LaunchedEffect(email) {

        propertyViewModel.loadSoldListings(email) // Add this method in your ViewModel to load sold properties
    }

    Column {
        LazyColumn(/*modifier=Modifier.weight(1f)*/) {

            items(soldProperties) { property ->
                SellPropertyCards(
                    modifier = Modifier,
                    property.area.toString(),
                    property.city,
                    property.state,
                    property.rooms.toString(),
                    property.bedrooms.toString(),
                    property.price,
                    property.propertyId.toString(),
                    navHostController = navHostController,
                    propertyViewModel = propertyViewModel,
                    onDeleted = onDeleted
                )
            }
        }
    }
}

@Composable
fun UnSoldScreen(modifier: Modifier = Modifier,propertyViewModel: PropertyViewModel,navHostController: NavHostController,email: String,onDeleted: () -> Unit) {
    val soldProperties by propertyViewModel.unsoldProperties.collectAsState()
    // Load the unsold and sold properties when the screen is displayed
    LaunchedEffect(email) {

        propertyViewModel.loadCurrentListings(email) // Add this method in your ViewModel to load sold properties
    }

    Column {
        LazyColumn(/*modifier=Modifier.weight(1f)*/) {

            items(soldProperties) { property ->
                SellPropertyCards(
                    modifier = Modifier,
                    property.area.toString(),
                    property.city,
                    property.state,
                    property.rooms.toString(),
                    property.bedrooms.toString(),
                    property.price,
                    property.propertyId.toString(),
                    navHostController = navHostController,
                    propertyViewModel = propertyViewModel,
                    onDeleted = onDeleted
                )
            }
        }
    }
}

