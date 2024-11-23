package com.example.realestatemanagementsystem.Home.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.example.realestatemanagementsystem.Property.PropertyFilter
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.contractor.ContractorViewModel
import com.example.realestatemanagementsystem.favorites.FavoriteViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserProfile
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileDao
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    email: String,
    authViewModel: AuthViewModel,
    navHostController: NavHostController,
    userProfileDao: UserProfileDao,
    viewModel: PropertyViewModel,
    profileViewModel: UserProfileViewModel,
    favoriteViewModel: FavoriteViewModel,
    contractorViewModel: ContractorViewModel
) {
    // Other states and logic remain the same...


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
    val filter=PropertyFilter()


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
            viewModel.getAllBuyingProperties(filter)
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
                                selected = selectedIndex ==index,
                                onClick = {
                                   selectedIndex=index
                                    scope.launch { drawerState.close() }
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

                       when(selectedIndex) {
                           0 -> MainScreen(

                               viewModel = viewModel,
                               navHostController = navHostController,
                               innerPadding = innerPadding,
                               profileViewModel = profileViewModel,
                               favoriteViewModel = favoriteViewModel,
                               email = email,

                               )

                           1 -> SellScreen(
                               userProfileDao = userProfileDao,
                               email = email,
                               propertyViewModel = viewModel,
                               navHostController = navHostController,
                               authViewModel = authViewModel,
                               profileViewModel = profileViewModel,
                               innerPadding = innerPadding
                           )

                           2 -> ContractorScreen(
                               innerPadding = innerPadding,
                               contractorViewModel = contractorViewModel,
                               navHostController = navHostController,
                               email = email
                           )

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





