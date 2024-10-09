package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.NavGraph.Screen
import com.example.realestatemanagementsystem.NavGraph.getNavigationItems
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthState
import com.example.realestatemanagementsystem.user.authentication.FirebaseCode.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(authViewModel: AuthViewModel = viewModel(), navHostController: NavHostController)
{
    val authState = authViewModel.authState.observeAsState()

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
//        Spacer(modifier = Modifier.height(16.dp))
            ModalDrawerSheet {
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
                    .padding(innerPadding)){}
            }
        }}