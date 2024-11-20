package com.example.realestatemanagementsystem.Home.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.realestatemanagementsystem.Navigation.Screen
import com.example.realestatemanagementsystem.Navigation.getNavigationItems
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.util.BuyPropertyCards
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    email: String,
    authViewModel: AuthViewModel,
    navHostController: NavHostController,
    userProfileDao: UserProfileDao,
    viewModel: PropertyViewModel,
    profileViewModel: UserProfileViewModel
) {
    // Other states and logic remain the same...
    val currentRoute = navHostController.currentBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    val items = getNavigationItems()
    val authState = authViewModel.authState.collectAsState()
    val allProperties by viewModel.unsoldProperties.collectAsState()
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var profileErrorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val propertyErroMessage by viewModel.errorMessage.collectAsState()
    // Dropdown state
    var selectedIndex by remember { mutableStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf("None") }
    val sortOptions = listOf("Price: Low to High", "Price: High to Low", "Newest First", "Oldest First")

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Failed) {
            navHostController.navigate(Screen.LoginScreen.route)
        }
    }
    LaunchedEffect(viewModel){
        try{
            viewModel.getAllBuyingProperties()
        }catch (e:Exception){
            Toast.makeText(getApplicationContext(),propertyErroMessage,Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(email) {
        try {
            val profile = userProfileDao.getUserByEmail(email)
            userProfile = profile
            isLoading = false
        } catch (e: Exception) {
            profileErrorMessage = "Failed to load profile: ${e.message}"
            isLoading = false
        }
    }
    fun refreshBuyProperties() {
        scope.launch {
            viewModel.getAllBuyingProperties()
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
                                IconButton(onClick = {scope.launch { drawerState.open() } }) {
                                    Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = { /* Open Filters */ },colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Makes the button's background transparent
                                contentColor = Color.Black)) {
                                Text("Filters")
                            }

                            // Sort By Dropdown
                            Box {
                                Button(onClick = { isDropdownExpanded = true },colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent, // Makes the button's background transparent
                                    contentColor = Color.Black)) {
                                    Text("Sort By: $selectedSortOption")
                                }

                                DropdownMenu(
                                    expanded = isDropdownExpanded,
                                    onDismissRequest = { isDropdownExpanded = false }
                                ) {
                                    sortOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                selectedSortOption = option
                                                isDropdownExpanded = false

                                                // Update sorting logic
                                                viewModel.sortProperties(option)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Property list (sorted dynamically)
                        LazyColumn {
                            items(allProperties) { property ->
                                // Display each property (replace with your card implementation)
                                BuyPropertyCards(
                                    modifier = Modifier,
                                    property = property ,
                                    navHostController = navHostController,
                                    viewModel=viewModel,
                                    onBuy=:: refreshBuyProperties

                                )
                            }
                        }
                    }
                }
            }
        } else {
            Text("User profile not found.")
        }

        if (profileErrorMessage.isNotEmpty()) {
            Text(
                text = profileErrorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
