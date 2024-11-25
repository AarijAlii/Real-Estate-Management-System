package com.example.realestatemanagementsystem.Home.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.realestatemanagementsystem.R

@Composable
fun AppointmentScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "Upcoming Appointments", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.padding(4.dp))
        AppointmentCard(modifier=Modifier)


    }
}

@Composable
fun AppointmentCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(8.dp)){
        Column(modifier = modifier.padding(8.dp)) {
        Image(painter = painterResource(id = R.drawable.house_file), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(400.dp, 200.dp)  // Make image take up the full width of the card
            .clip(RoundedCornerShape(16.dp))
            .graphicsLayer {
                scaleX = 1.5f
            })
            Spacer(modifier=Modifier.padding(4.dp))
            Text(text = "30-11-2024", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.padding(4.dp))
            Row {

                Text(text = "ARK SAN", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "k224155@nu.edu.pk",style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "031254698729",style = MaterialTheme.typography.bodyMedium)

    }}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewAppointment() {
    AppointmentScreen(modifier=Modifier)
}