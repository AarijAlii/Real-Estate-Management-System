package com.example.realestatemanagementsystem.Home.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.Navigation.Screen
import com.example.realestatemanagementsystem.Navigation.getNavigationItems
import com.example.realestatemanagementsystem.Property.PropertyDao
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.util.PropertyCards
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
    propertyDao: PropertyDao
) {
    val currentRoute = navHostController.currentBackStackEntry?.destination?.route
    val items = getNavigationItems()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val authState = authViewModel.authState.collectAsState()
    var selectedIndex by remember { mutableStateOf(1) }
    val unsoldProperties by propertyViewModel.unsoldProperties.collectAsState()
    val soldProperties by propertyViewModel.soldProperties.collectAsState()
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

            ModalNavigationDrawer(
                drawerContent = {
                    Spacer(modifier = Modifier.height(16.dp))
                    ModalDrawerSheet {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.house_file),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    .clickable {
                                        navHostController.navigate("update_profile_screen/${email}")
                                    }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "$firstName $lastName",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = email,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider()
                        }
                        items.forEachIndexed { index, navigationItem ->
                            NavigationDrawerItem(
                                label = {
                                    Row {
                                        Icon(
                                            painter = painterResource(navigationItem.icon),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                                .size(18.dp)
                                        )
                                        Text(text = navigationItem.title)
                                    }
                                },
                                selected = currentRoute == navigationItem.route || (currentRoute == null && index == 0),
                                onClick = {
                                    if (index != selectedIndex) {
                                        selectedIndex = index
                                        scope.launch { drawerState.close() }
                                        val finalRoute = navigationItem.route.replace("{email}", email)
                                        navHostController.navigate(finalRoute)
                                    }
                                },
                                modifier = Modifier.padding(2.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 27.dp, vertical = 8.dp)
                                .clickable {
                                    authViewModel.signOut()
                                    navHostController.navigate(Screen.LoginScreen.route)
                                },
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.baseline_logout_24),
                                contentDescription = "Logout",
                                modifier = Modifier.size(22.dp)
                            )
                            Text(text = "  Logout", fontSize = 14.sp)
                        }
                    }
                },
                drawerState = drawerState
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("PropertyHub") },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        // Toggle Button
                        ToggleSoldUnsoldButton(
                            showSold = showSold,
                            onToggle = { showSold = it }
                        )

                        // LazyColumn to show properties
                        LazyColumn(modifier = Modifier.weight(1f)) {

                            items( if (showSold) soldProperties else unsoldProperties) { property ->
                                PropertyCards(
                                    modifier = Modifier,
                                    property.area.toString(),
                                    property.city,
                                    property.state,
                                    property.rooms.toString(),
                                    property.bedrooms.toString(),
                                    property.price.toString(),
                                    property.propertyId.toString(),
                                    navHostController = navHostController,
                                    propertyDao,
                                    onDeleted = ::refreshProperties
                                )
                            }
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                            onClick = {
                                navHostController.navigate("create_listing_screen/$email")
                            }
                        ) {
                            Text(text = "Create Listing")
                        }
                    }
                }
            }
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
fun ToggleSoldUnsoldButton(showSold: Boolean, onToggle: (Boolean) -> Unit) {
    IconToggleButton(
        checked = showSold,
        onCheckedChange = { onToggle(it) },
        modifier = Modifier
            .padding(16.dp)
            .size(48.dp)
    ) {
        val text=if(showSold) "Sold" else "Unsold"
        Text(text, fontSize = 12.sp)
    }
}




