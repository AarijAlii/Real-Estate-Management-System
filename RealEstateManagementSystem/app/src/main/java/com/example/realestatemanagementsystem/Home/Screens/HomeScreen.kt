package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.realestatemanagementsystem.Navigation.Screen
import com.example.realestatemanagementsystem.Navigation.getNavigationItems
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import com.example.realestatemanagementsystem.util.PropertyCards
import kotlinx.coroutines.launch
import com.example.realestatemanagementsystem.util.PropertyCards

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(authViewModel: AuthViewModel = viewModel(),navHostController: NavHostController)
{
    val authState = authViewModel.authState.collectAsState()

    LaunchedEffect(authState.value){
        if (authState.value == AuthState.Failed) {
            navHostController.navigate(route = Screen.LoginScreen.route)
        }
    }


        val items= getNavigationItems()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope= rememberCoroutineScope()
        var selectedIndex by remember {
            mutableStateOf(-1)
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
                    Divider()}
                items.forEachIndexed{
                        index, navigationItem ->
                    NavigationDrawerItem(label = {
                        Row(){
                            Icon(painter = painterResource(navigationItem.icon), contentDescription = null,modifier= Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                .size(18.dp))
                            Text(text = navigationItem.title)}
                    }, selected = index==selectedIndex, onClick = { selectedIndex=index
                        navHostController.navigate(navigationItem.route)
                        scope.launch {
                            drawerState.close()
                        }
                    },modifier=Modifier.padding(2.dp))
                }
            }
        }, drawerState = drawerState) {
            Scaffold(
                topBar = {
                    TopAppBar(title ={ Text( "PropertyHub") },
                        navigationIcon = {
                            IconButton(onClick = {scope.launch { drawerState.open() }}) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                            }}

                    )
                         }

                ){innerPadding->
                Column (modifier = Modifier
                    .padding(innerPadding).fillMaxWidth().padding(16.dp)){
                        PropertyCards(modifier = Modifier)

                }
            }
        }}
@Preview (showBackground = true)
@Composable
private fun PreviewHome()    {

}