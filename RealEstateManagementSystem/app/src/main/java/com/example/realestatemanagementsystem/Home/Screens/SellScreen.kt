package com.example.realestatemanagementsystem.Home.Screens

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import com.example.realestatemanagementsystem.Navigation.Screen
import com.example.realestatemanagementsystem.Navigation.getNavigationItems
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.util.PropertyCards
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellScreen(authViewModel:AuthViewModel,modifier: Modifier = Modifier,navHostController: NavHostController) {
    val currentRoute = navHostController.currentBackStackEntry?.destination?.route
    val items= getNavigationItems()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope= rememberCoroutineScope()
    var selectedIndex by remember {
        mutableStateOf(1)
    }
    ModalNavigationDrawer(drawerContent = {
        Spacer(modifier = Modifier.height(16.dp))
        ModalDrawerSheet {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Picture
                Image(
                    painter = painterResource(id = R.drawable.house_file), // Replace with your profile picture resource
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable {
                            navHostController.navigate(route = Screen.UserProfileScreen.route) //make an update prof screen for updating profile
                        }
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Profile Name
                Text(
                    text = "John Doe", // Replace with dynamic user name
                    style = MaterialTheme.typography.titleMedium
                )
                // Profile Email or Subtitle
                Text(
                    text = "john.doe@example.com", // Replace with dynamic email
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
            }
            items.forEachIndexed{
                    index, navigationItem ->
                NavigationDrawerItem(label = {
                    Row(){
                        Icon(painter = painterResource(navigationItem.icon), contentDescription = null,modifier= Modifier
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                            .size(18.dp))
                        Text(text = navigationItem.title)}

                }, selected = currentRoute == navigationItem.route || (currentRoute == null && index == 0)

                    , onClick = { if(index!=selectedIndex){
                        selectedIndex=index

                        scope.launch { drawerState.close() }
                        navHostController.navigate(navigationItem.route)

                    }
                    },modifier=Modifier.padding(2.dp))
            }



            Row(modifier= Modifier
                .padding(horizontal = 27.dp, vertical = 8.dp)
                .clickable {
                    authViewModel.signOut()
                    // Navigate to login screen
                    navHostController.navigate(Screen.LoginScreen.route)

                },
                verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Start) {
                Icon(painter = painterResource(R.drawable.baseline_logout_24), contentDescription = "Logout", modifier = Modifier.size(22.dp))
                Text(text = "  Logout", fontSize = 14.sp)
            }

        }
    }, drawerState = drawerState) {
        Scaffold(
            topBar = {
                TopAppBar(title ={ Text( "PropertyHub") },
                    navigationIcon = {
                        IconButton(onClick = {scope.launch { drawerState.open() }}) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                        }
                    }

                )
            }

        ){innerPadding->
            SellPropertyViews(modifier = Modifier.padding(innerPadding))
        }
    }
}
@Composable
fun SellPropertyViews(modifier: Modifier){
    Column(modifier = Modifier.fillMaxWidth().padding(top = 100.dp)) {
        PropertyCards()
        PropertyCards()

        Button(modifier=Modifier.fillMaxWidth().padding(16.dp),
            colors =  ButtonColors(
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                containerColor = Color.Red,
                disabledContentColor = Color.White,
            ),onClick = {  }) {
            Text(text = "Create Listing")
        }

}}


