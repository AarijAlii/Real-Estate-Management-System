//package com.example.realestatemanagementsystem.Navigation
//
//import android.util.Log
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.example.realestatemanagementsystem.Home.Screens.CreateListingScreen
//import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
//import com.example.realestatemanagementsystem.Home.Screens.HomeScreen
//import com.example.realestatemanagementsystem.Home.Screens.SellScreen
//import com.example.realestatemanagementsystem.Home.Screens.UpdateListingScreen
//import com.example.realestatemanagementsystem.property.PropertyViewModel
//import com.example.realestatemanagementsystem.property.PropertyViewModelFactory
//import com.example.realestatemanagementsystem.AppDatabase
//import com.example.realestatemanagementsystem.contractor.ContractorFormScreen
//import com.example.realestatemanagementsystem.contractor.ContractorViewModel
//import com.example.realestatemanagementsystem.contractor.ContractorViewModelFactory
//import com.example.realestatemanagementsystem.contractor.ContractorListScreen
//import com.example.realestatemanagementsystem.favorites.FavoriteViewModel
//import com.example.realestatemanagementsystem.review.ReviewViewModel
//import com.example.realestatemanagementsystem.review.ReviewViewModelFactory
//import com.example.realestatemanagementsystem.review.SubmitReviewScreen
//import com.example.realestatemanagementsystem.user.UserProfile.Screens.UserProfileScreen
//import com.example.realestatemanagementsystem.user.UserProfile.Screens.UserProfileUpdateScreen
//import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
//import com.example.realestatemanagementsystem.user.UserProfile.UserViewModelFactory
//import com.example.realestatemanagementsystem.user.authentication.AuthenticationViewModel
//import com.example.realestatemanagementsystem.user.authentication.Screens.AuthenticationScreen
//import com.example.realestatemanagementsystem.user.authentication.Screens.LoginScreen
//import com.example.realestatemanagementsystem.user.authentication.Screens.SignUpScreen
//
//@Composable
//fun NavigationGraph(
//    navController: NavHostController,
//    authViewModel: AuthViewModel
//) {
//    NavHost(
//        navController = navController,
//        startDestination = Screen.LoginScreen.route
//    ) {
//        composable(Screen.SignupScreen.route) {
//            SignUpScreen(
//                authViewModel = viewModel(),
//                navHostController =  navController,
//                appDatabase = AppDatabase.getDatabase(LocalContext.current) // Pass the appDatabase here
//            )
//        }
//        composable(Screen.LoginScreen.route) {
//            LoginScreen(
//                authViewModel = viewModel(),
//                navHostController = navController,
//                appDatabase = AppDatabase.getDatabase(LocalContext.current)
//            )
//        }
//        composable(Screen.HomeScreen.route) {
//                backStackEntry ->
//            val email = backStackEntry.arguments?.getString("email")
//            val favoriteViewModel = viewModel<FavoriteViewModel>()
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory = UserViewModelFactory(appDatabase)  // Pass AppDatabase here
//            val userProfileViewModel: UserProfileViewModel = viewModel(factory = factory)
//            val propertyFactory = PropertyViewModelFactory(
//                appDatabase.propertyDao(),   // propertyDao
//                appDatabase.imageDao()       // imageDao
//            )
//            val propertyViewModel: PropertyViewModel = viewModel(factory = propertyFactory)
//            val userProfileDao=appDatabase.userProfileDao()
//
//            if (email != null) {
//                Log.d("HomeScreen", "Email received: $email")
//                HomeScreen(
////                    email: String,
////                    authViewModel: AuthViewModel,
////                    navHostController: NavHostController,
////                    userProfileDao: UserProfileDao,
////                    viewModel: PropertyViewModel,
////                    profileViewModel: UserProfileViewModel
//                    email = email,
//                    authViewModel = AuthViewModel(),
//                    navHostController = navController,
//                    viewModel =propertyViewModel,
//                    userProfileDao=userProfileDao,
//                    profileViewModel = userProfileViewModel,
//                    favoriteViewModel = favoriteViewModel
//                )
//            }
//        }
//
//        composable(Screen.SellScreen.route){
//                backStackEntry ->
//            val email = backStackEntry.arguments?.getString("email")
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val userFactory = UserViewModelFactory(appDatabase)  // Pass AppDatabase here
//            val userProfileViewModel: UserProfileViewModel = viewModel(factory = userFactory)
//            val propertyFactory = PropertyViewModelFactory(
//                appDatabase.propertyDao(),   // propertyDao
//                appDatabase.imageDao()       // imageDao
//            )
//            val propertyViewModel: PropertyViewModel = viewModel(factory = propertyFactory)
//            val propertyDao=appDatabase.propertyDao()
//            if (email != null) {
//               SellScreen(
//                    email = email,
//                    userProfileDao = appDatabase.userProfileDao(),
//                    profileViewModel = userProfileViewModel,
//                    navHostController = navController,
//                   authViewModel = AuthViewModel(),
//                   propertyViewModel = propertyViewModel,
//                   propertyDao = propertyDao
//                )
//            }
//        }
//        composable(Screen.UserProfileUpdateScreen.route) { backStackEntry ->
//            val email = backStackEntry.arguments?.getString("email")
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory = UserViewModelFactory(appDatabase)  // Pass AppDatabase here
//            val userProfileViewModel: UserProfileViewModel = viewModel(factory = factory)
//
//            if (email != null) {
//                UserProfileUpdateScreen(
//                    email = email,
//                    userProfileDao = appDatabase.userProfileDao(),
//                    profileViewModel = userProfileViewModel,
//                    navHostController = navController
//                )
//            }
//        }
//
//        composable(Screen.UserProfileScreen.route) { backStackEntry ->
//            val email = backStackEntry.arguments?.getString("email")
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory = UserViewModelFactory(appDatabase)  // Pass AppDatabase here
//            val userProfileViewModel: UserProfileViewModel = viewModel(factory = factory)
//            if (email != null) {
//                userProfileViewModel.getUserProfile(email)
//            }
//            if (email != null) {
//                UserProfileScreen(
//                    email = email,
//
//                    profileViewModel = userProfileViewModel,
//                    navHostController = navController
//                )
//            }
//        }
//        composable(Screen.UpdateListingScreen.route){
//                backStackEntry ->
//            val propertyID = backStackEntry.arguments?.getString("email")
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory = PropertyViewModelFactory(
//                appDatabase.propertyDao(),   // propertyDao
//                appDatabase.imageDao()       // imageDao
//            )
//            val propertyViewModel: PropertyViewModel = viewModel(factory = factory)
//            val propertyDao = appDatabase.propertyDao()
//            if (propertyID != null) {
//
//                UpdateListingScreen(propertyid = propertyID, navController = navController, propertyViewModel = propertyViewModel, propertyDao = propertyDao)
//            }
//        }
//        composable(Screen.CreateListingScreen.route){
//                backStackEntry ->
//            val email = backStackEntry.arguments?.getString("email")
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory = PropertyViewModelFactory(
//                appDatabase.propertyDao(),   // propertyDao
//                appDatabase.imageDao()       // imageDao
//            )
//            val propertyViewModel: PropertyViewModel = viewModel(factory = factory)
//            if (email != null) {
//                CreateListingScreen(email = email, navController = navController, viewModel = propertyViewModel)
//            }
//        }
//
//        //CONTRACTOR
//        composable(Screen.ContractorFormScreen.route) {
//                backStackEntry ->
//            val email = backStackEntry.arguments?.getString("email")
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory = ContractorViewModelFactory(appDatabase.contractorDao())  // Pass AppDatabase here
//            val contractorViewModel: ContractorViewModel = viewModel(factory = factory)
//            if (email != null) {
//                ContractorFormScreen(
//                    email = email,
//                    contractorViewModel = contractorViewModel,
//                    onRegistrationComplete = { navController.popBackStack() }
//                )
//            }
//        }
//
//        composable(Screen.ContractorListScreen.route) {
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory = ContractorViewModelFactory(appDatabase.contractorDao())  // Pass AppDatabase here
//            val contractorViewModel: ContractorViewModel = viewModel(factory = factory)
//            ContractorListScreen(
//                contractorViewModel = contractorViewModel,
//                 navController = navController
//            )
//        }
//
//
//
//        composable(Screen.ReviewFormScreen.route) {
//                    backStackEntry ->
//            val contractorId = backStackEntry.arguments?.getInt("contractorId") ?: 0
//            val email = backStackEntry.arguments?.getString("email")
//            val context = LocalContext.current
//            val appDatabase = AppDatabase.getDatabase(context)
//            val factory =  ReviewViewModelFactory(appDatabase.reviewDao(),appDatabase.contractorDao())  // Pass AppDatabase here
//            val reviewViewModel: ReviewViewModel = viewModel(factory = factory)
//            if (email != null) {
//                SubmitReviewScreen(
//                    contractorId = contractorId,
//                    email = email, // Pass the user's email here
//                    reviewViewModel = reviewViewModel,
//                    onReviewSubmitted = { navController.popBackStack() }
//                )
//            }
//        }
//
////        composable(Screen.AuthenticationScreen.route) {
////            val authViewModel: AuthenticationViewModel = viewModel() // Get ViewModel
////            AuthenticationScreen(viewmodel = authViewModel)
////        }
//    }
//}
//
