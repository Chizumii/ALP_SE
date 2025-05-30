// TorunamentCard.kt
package com.example.alp_se.view.templates

import android.provider.SyncStateContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.alp_se.R
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.navigation.Screen
import com.example.alp_se.viewModels.TournamentViewModel

@Composable
fun TournamentCard(
    tournament: TournamentResponse,
    tournamentViewModel: TournamentViewModel,
    navController: NavController
) {
    val fullImageUrl = "http://10.0.2.2:3000/images${tournament.image.substringAfter("/uploads")}"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("tournamentDetail/${tournament.TournamentID}")} // Handle click
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
//                SubcomposeAsyncImage(
//                    model = , // This could be a URL, file path, or other input
//                    contentDescription = "Tournament Image",
//                    contentScale = ContentScale.FillWidth,
//                    modifier = Modifier.fillMaxSize(),
//                    loading = {
//                        // Show a loading indicator while the image is being loaded
//                        CircularProgressIndicator(
//                            color = Color.White,
//                            strokeWidth = 4.dp,
//                            modifier = Modifier.fillMaxSize()
//                        )
//                    }
//                )
                Image(
                    painter = rememberAsyncImagePainter(model = fullImageUrl),
                    contentDescription = "Tournament Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), // Set a fixed height or aspect ratio
                    contentScale = ContentScale.Crop // Crop to fill bounds
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = tournament.nama_tournament,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = tournament.description,
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}

