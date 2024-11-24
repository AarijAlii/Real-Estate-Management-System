package com.example.realestatemanagementsystem.Home.Screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.realestatemanagementsystem.Property.Property
import com.example.realestatemanagementsystem.Property.PropertyFilter
import com.example.realestatemanagementsystem.Property.PropertyViewModel
import com.example.realestatemanagementsystem.R
import com.example.realestatemanagementsystem.favorites.FavoriteViewModel
import com.example.realestatemanagementsystem.user.UserProfile.UserProfileViewModel
import com.example.realestatemanagementsystem.util.BuyPropertyCards
import com.example.realestatemanagementsystem.util.FavoritePropertyCard
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: PropertyViewModel,
    navHostController: NavHostController,
    profileViewModel: UserProfileViewModel,
    innerPadding: PaddingValues,
    favoriteViewModel: FavoriteViewModel,
    email: String,

) {

    val tabs = listOf("Buy", "Favourites")
    val pagerState = rememberPagerState(initialPage = 0){tabs.size}
    val scope= rememberCoroutineScope()
    Column(modifier = Modifier.padding(innerPadding)) {
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
            state=pagerState,
        ){page ->
                when (page) {
                    0 -> BuyScreen(
                        viewModel = viewModel,
                        navHostController = navHostController,
                        profileViewModel = profileViewModel,
                        favoriteViewModel = favoriteViewModel,
                        email = email


                    )

                    1 -> FavoritesScreenContent(
                        viewModel = viewModel,
                        navHostController = navHostController,
                        profileViewModel = profileViewModel,
                        innerPadding = innerPadding,
                        favoriteViewModel=favoriteViewModel,
                        email=email
                    )
                }}



    }
}

@Composable
fun BuyScreen(
    modifier: Modifier = Modifier,
    viewModel: PropertyViewModel,
    navHostController: NavHostController,
    favoriteViewModel: FavoriteViewModel,
    profileViewModel: UserProfileViewModel,
    email: String,

) {


    val filter=PropertyFilter()
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf("None") }
    val sortOptions = listOf("Price: Low to High", "Price: High to Low")
    var selectedProperty by remember { mutableStateOf<Property?>(null) }
    var showPopup by remember { mutableStateOf(false) }
    val allProperties by viewModel.unsoldProperties.collectAsState()
    val scope = rememberCoroutineScope()
    val favorites = favoriteViewModel.favorites.collectAsState(initial = emptyList()).value
    favoriteViewModel.getFavoritessByEmail(email)
    fun refreshBuyProperties() {
        scope.launch {
            viewModel.getAllBuyingProperties(filter)
        }
    }

    Column(modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                FiltersExample(viewModel,filter)
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
                   Text(text = "Sort  By: $selectedSortOption")
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

        LazyColumn() {
            items(allProperties) { property ->
                // Display each property (replace with your card implementation)


                BuyPropertyCards(
                    modifier = Modifier,
                    property = property ,
                    navHostController = navHostController,
                    viewModel=viewModel,
                    onBuy=:: refreshBuyProperties,
                    email = email,
                    propertyId=property.propertyId,
                    favoriteViewModel=favoriteViewModel

                ){
                    showPopup=true
                    selectedProperty=property
                }


            }
        }

        if(showPopup && selectedProperty != null){
            Box(
                modifier = Modifier
            ) {
                PropertyDetailDialog(
                    property = selectedProperty!!,
                    onDismiss ={ showPopup = false },
                    viewModel = profileViewModel // Hide the popup
                )
            }
        }

    }
}
@Composable
fun FiltersExample(viewModel: PropertyViewModel,filter: PropertyFilter) {
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

                    filter.city = cityFilter.value.takeIf { it.isNotEmpty() }
                    filter.state = stateFilter.value.takeIf { it.isNotEmpty() }
                    filter.minPrice = minPrice.value.toDoubleOrNull()
                    filter.maxPrice = maxPrice.value.toDoubleOrNull()
                    filter.zipCode = null
                    filter.type = null
                    filter.noOfRooms = null
                    filter.bedrooms = null
                    filter.garage = null

                    viewModel.filterProperties(filter)

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
@Composable
fun PropertyDetailDialog(
    property: Property,
    onDismiss: () -> Unit,

    viewModel: UserProfileViewModel
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        PropertyDetailPopup(
            property = property,
            onClose = onDismiss,
            viewModel=viewModel
        )
    }
}
@Composable
fun PropertyDetailPopup(
    property: Property, // Replace with your data class for property
    onClose: () -> Unit,

    viewModel: UserProfileViewModel
) {
    viewModel.getUserProfileByEmail(property.email)
    val user by viewModel.sellerUserProfile.collectAsState()
    Box(
        modifier = Modifier
            .shadow(16.dp, RoundedCornerShape(16.dp))
            .background(Color.White)

    ) {
        IconButton(
            onClick = { onClose() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .zIndex(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
        }
        // Background layer with image
        Column {
            // Unscrollable Image Section
            Image(
                painter = painterResource(id = R.drawable.house_file), // Replace with your image loader
                contentDescription = "Property Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Set a fixed height for the image
                    .clip(MaterialTheme.shapes.medium)
            )

            // Scrollable Details Section
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                item {
                    // Property Title
                    Text(
                        text ="Property Number: ${property.propertyNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    // Property Description
                    Text(
                        text = "Property Type: ${property.type}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    // Property Price
                    val text = when {
                        property.price >= 10000000 -> {
                            val crore = (property.price / 10000000).toInt() // Get crore part and discard decimals
                            "$crore Crore" // Format price as Crore
                        }
                        property.price >= 100000 -> {
                            val lac = (property.price / 100000).toInt() // Get lac part and discard decimals
                            "$lac Lacs" // Format price as Lacs
                        }
                        else -> {
                            // For prices below 1 Lac, do not show the "Thousands" part.
                            property.price.toInt().toString() // Display the price as a whole number without decimals
                        }
                    }
                    Text(
                        text = "Price: $text",
                        style = MaterialTheme.typography.bodyLarge,

                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }


                item{
                    Text(
                        text = buildAnnotatedString {
                            append("${property.area}yd") // Regular text
                            withStyle(style = SpanStyle(
                                baselineShift = BaselineShift.Superscript, // Make "2" superscript
                                fontSize = 12.sp // Optional, you can change the font size of the superscript
                            )
                            ) {
                                append("2")
                            }},

                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item{
                    Text(
                        text = "Bedrooms: ${property.bedrooms}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item{
                    Text(
                        text = "Bathrooms: ${property.rooms}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item{
                    Text(
                        text = "Garage: ${property.garage}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item{
                    Text(
                        text = "City: ${property.city}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item {
                    Text(
                        text= "State: ${property.state}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item{
                    Text(
                        text = "Zip Code: ${property.zipCode}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                item{
                    Text(
                        text= "Seller email: ${property.email}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp,bottom = 8.dp)
                    )
                }
                item{
                    Text(
                        text = "Seller Contact: ${user?.contact}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp))
                }
                // Add more details here...
            }
        }

        // Close Button

    }
}

@Composable
fun FavoritesScreenContent(
    viewModel: PropertyViewModel,
    navHostController: NavHostController,
    profileViewModel: UserProfileViewModel,
    innerPadding: PaddingValues,
    favoriteViewModel: FavoriteViewModel,
    email :String
) {
    // Implement the Favorites logic here
    val favorites by favoriteViewModel.favoriteProperties.collectAsState()
    favoriteViewModel.getFavoritessByEmail(email)
    Log.d("Favorite", favorites.toString())
    LazyColumn(modifier=Modifier.fillMaxHeight()) {
        items(favorites) { property ->
            // Display each property (replace with your card implementation)


            FavoritePropertyCard (
                modifier = Modifier,
                property = property ,
                navHostController = navHostController,
                viewModel=viewModel,

                email = email,
                propertyId=property.propertyId,
            )


        }
    }

}

