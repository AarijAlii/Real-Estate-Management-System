package com.example.realestatemanagementsystem.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.realestatemanagementsystem.Home.Screens.BuyScreen
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.Home.Screens.HomeScreen
import com.example.realestatemanagementsystem.Home.Screens.SellScreen
import com.example.realestatemanagementsystem.property.AddPropertyScreen
import com.example.realestatemanagementsystem.property.Property
import com.example.realestatemanagementsystem.property.PropertyScreen
import com.example.realestatemanagementsystem.property.PropertyViewModel
import com.example.realestatemanagementsystem.property.PropertyViewModelFactory
import com.example.realestatemanagementsystem.property.SoldPropertiesScreen
import com.example.realestatemanagementsystem.property.UpdatePropertyScreen
import com.example.realestatemanagementsystem.user.UserProfile.AppDatabase
import com.example.realestatemanagementsystem.user.UserProfile.Screens.ProfileScreen
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserViewModelFactory
import com.example.realestatemanagementsystem.user.authentication.Screens.SignInScreen
import com.example.realestatemanagementsystem.user.authentication.Screens.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = (Screen.LoginScreen.route)
    ) {
//        composable(Screen.SignupScreen.route) {
//            SignUpScreen<Any>(
//                authViewModel,
//                navHostController=navController
//            )
//        }

//        composable(Screen.LoginScreen.route) {
//            LoginScreen(
//                authViewModel,
//              navHostController = navController
//            )
//        }

        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = viewModel(),
                navController = navController,
                appDatabase = AppDatabase.getDatabase(LocalContext.current) // Pass the appDatabase here
            )
        }

        composable(Screen.LoginScreen.route) {
                SignInScreen(
                    authViewModel = viewModel(),
                    navController = navController,
                    appDatabase = AppDatabase.getDatabase(LocalContext.current)
                )
            }

        composable(Screen.ProfileScreen.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val context = LocalContext.current
            val appDatabase = AppDatabase.getDatabase(context)
            val factory = UserViewModelFactory(appDatabase)  // Pass AppDatabase here
            val userProfileViewModel: UserProfileViewModel = viewModel(factory = factory)

            if (email != null) {
                ProfileScreen(
                    email = email,
                    userProfileDao = appDatabase.userProfileDao(),
                    profileViewModel = userProfileViewModel,
                    navController = navController
                )
            }
        }


        composable(Screen.HomeScreen.route) {
            HomeScreen(
            //authViewModel,
                navHostController=navController
            )
        }
        composable(Screen.BuyScreen.route){
            BuyScreen(
                modifier=Modifier,
                navHostController = navController
            )
        }
        composable(Screen.SellScreen.route){
            SellScreen(
                modifier=Modifier,
                navHostController = navController
            )
        }

        composable(Screen.PropertyScreen.route) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val context = LocalContext.current
            val appDatabase = AppDatabase.getDatabase(context)
            val factory = PropertyViewModelFactory(appDatabase.propertyDao())
            val propertyViewModel: PropertyViewModel = viewModel(factory = factory)
            if (email != null) {
                PropertyScreen(
                    email = email, propertyViewModel = propertyViewModel,
                    navController = navController
                )
            }
        }
        //TESTING PROPERTY NAVS
        composable("add_property_screen/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val context = LocalContext.current
            val appDatabase = AppDatabase.getDatabase(context)
            val factory = PropertyViewModelFactory(appDatabase.propertyDao())
            val propertyViewModel: PropertyViewModel = viewModel(factory = factory)
            if (email != null) {
                AddPropertyScreen(email = email, navController = navController, propertyViewModel = propertyViewModel)
            }
        }

//        composable("update_property_screen/{propertyId}") { backStackEntry ->
//            val propertyId = backStackEntry.arguments?.getString("propertyId")?.toIntOrNull()
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory = PropertyViewModelFactory(appDatabase.propertyDao())
//            val propertyViewModel: PropertyViewModel = viewModel(factory = factory)
//            if (propertyId != null) {
//                UpdatePropertyScreen(propertyId = propertyId, navController = navController, propertyViewModel = propertyViewModel)
//            }
//        }

//        composable(route = "update_property_screen",
//            arguments = listOf(navArgument("property") { type = NavType.ParcelableType(Property::class.java) })
//        ) { backStackEntry ->
//            val property = backStackEntry.arguments?.getParcelable<Property>("property")
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory = PropertyViewModelFactory(appDatabase.propertyDao())
//            val propertyViewModel: PropertyViewModel = viewModel(factory = factory)
//            if (property != null) {
//                UpdatePropertyScreen(property, propertyViewModel, navController)
//            }
//        }


        composable("sold_properties_screen/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val context = LocalContext.current
            val appDatabase = AppDatabase.getDatabase(context)
            val factory = PropertyViewModelFactory(appDatabase.propertyDao())
            val propertyViewModel: PropertyViewModel = viewModel(factory = factory)
            if (email != null) {
                SoldPropertiesScreen(email = email, propertyViewModel = propertyViewModel, navController = navController)
            }
        }

    }
}