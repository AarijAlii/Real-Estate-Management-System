package com.example.realestatemanagementsystem.Home.Screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.TextField
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
import com.example.realestatemanagementsystem.property.PropertyViewModel
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
    var showFilters by remember { mutableStateOf(false) }
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
    val sortOptions = listOf("Price: Low to High", "Price: High to Low")
    var searchText = remember { mutableStateOf("") }
    var cityFilter = remember { mutableStateOf("") }
    var stateFilter = remember { mutableStateOf("") }
    var minPrice = remember { mutableStateOf("") }
    var maxPrice = remember { mutableStateOf("") }


    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Failed) {
            navHostController.navigate(Screen.LoginScreen.route)
        }
    }
//    @Composable
//    fun sortProperties(option:String){
//        LaunchedEffect(option) {
//            viewModel.sortProperties(option)
//        }
//    }
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {
                                FiltersExample(viewModel, innerPadding)
                            }
                            // Sort By Dropdown
                            Box {
                                Button(
                                    onClick = { isDropdownExpanded = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent, // Makes the button's background transparent
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Text("Sort By: $selectedSortOption")
                                }
                            }}
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
@Composable
fun FiltersExample(viewModel: PropertyViewModel, innerPadding:PaddingValues) {
    // States to control filter visibility and values
    var showFilters by remember { mutableStateOf(false) }
    val searchText = remember { mutableStateOf("") }
    val cityFilter = remember { mutableStateOf("") }
    val stateFilter = remember { mutableStateOf("") }
    val minPrice = remember { mutableStateOf("") }
    val maxPrice = remember { mutableStateOf("") }

    var selectedSortOption by remember { mutableStateOf("None") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val sortOptions = listOf("Price: Low to High", "Price: High to Low")

    // "Filters" button to toggle filter visibility
    Button(
        onClick = { showFilters = !showFilters },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Makes the button's background transparent
                    contentColor = Color.Black
                )
            ) {
                Text("Filters")
    }

        // Sort By Dropdown
        // Filter section that toggles visibility based on showFilters state
        AnimatedVisibility(visible = showFilters) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .animateContentSize() // This will animate size changes smoothly
            ) {
                // Search TextField
                TextField(
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    label = { Text("Search Property ID or User Email") },
                    modifier = Modifier,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // City and State filters in a row
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = cityFilter.value,
                        onValueChange = { cityFilter.value = it },
                        label = { Text("City") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )

                    TextField(
                        value = stateFilter.value,
                        onValueChange = { stateFilter.value = it },
                        label = { Text("State") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Min and Max price filters in a row
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = minPrice.value,
                        onValueChange = { minPrice.value = it },
                        label = { Text("Min Price") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )

                    TextField(
                        value = maxPrice.value,
                        onValueChange = { maxPrice.value = it },
                        label = { Text("Max Price") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )

                }
                Button(onClick = {
                    if (searchText.value.isNotEmpty()) {
                        if (searchText.value.toIntOrNull() != null) {
                            viewModel.searchByPropertyId(searchText.value.toInt())
                        } else {
                            viewModel.searchByEmail(searchText.value)
                        }
                    } else {
                        viewModel.filterProperties(
                            city = cityFilter.value.takeIf { it.isNotEmpty() },
                            state = stateFilter.value.takeIf { it.isNotEmpty() },
                            minPrice = minPrice.value.toDoubleOrNull(),
                            maxPrice = maxPrice.value.toDoubleOrNull(),
                            zipCode = null,
                            type = null,
                            noOfRooms = null,
                            bedrooms = null,
                            garage = null,

                        )
                    }
                    showFilters=false
                },colors = ButtonColors(
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    containerColor = Color.Red,
                    disabledContentColor = Color.White,
                )
                ) {
                    Text("Done")
                }
            }
        }

}
