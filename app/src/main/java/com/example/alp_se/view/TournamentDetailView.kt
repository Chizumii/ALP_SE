// TournamentDetailView.kt
package com.example.alp_se.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.alp_se.R
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.navigation.Screen
import coil.compose.rememberAsyncImagePainter
import com.example.alp_se.viewModels.TournamentViewModel

@Composable
fun TournamentDetailView(
    tournament: TournamentResponse,
    navController: NavController,
    tournamentViewModel: TournamentViewModel // Click handler for "Register"
) {
    val fullImageUrl = "http://10.0.2.2:3000${tournament.image}"
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF222222))
    ) {
        // Top Navbar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(0XFF222222))
                .zIndex(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0XFF222222)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo on the left
                    Image(
                        painter = painterResource(R.drawable.community_removebg_preview),
                        contentDescription = "Logo",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f)) // Spacer to push content to the center
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clickable {
                                tournamentViewModel.openUpdate(navController, tournament)
                            }
                            ,
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit, // Use the ImageVector from Material Icons
                            contentDescription = "Edit icon", // Always provide a content description for accessibility
                            modifier = Modifier.size(24.dp), // Adjust size as needed
                            tint = Color(0xFF448AFF) // Adjust color as needed
                        )
                    }
                }

                Text(
                    text = "Tournament",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Main Content
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 90.dp, bottom = 80.dp, start = 16.dp, end = 16.dp)
        ) {
            // Title
            Text(
                text = tournament.nama_tournament,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
            )

            // Image
//            SubcomposeAsyncImage(
//                model = tournament.image, // This could be a URL, file path, or other input
//                contentDescription = "Tournament Image",
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier.fillMaxSize(),
//                loading = {
//                    // Show a loading indicator while the image is being loaded
//                    CircularProgressIndicator(
//                        color = Color.White,
//                        strokeWidth = 4.dp,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                }
//            )
            Image(
                painter = rememberAsyncImagePainter(model = fullImageUrl),
                contentDescription = "Tournament Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Set a fixed height or aspect ratio
                contentScale = ContentScale.Crop // Crop to fill bounds
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description Section
            Text(
                text = "Description",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = tournament.description,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Information Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Biaya Pendaftaran",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = tournament.biaya.toString(),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                Column {
                    Text(
                        text = "Lokasi",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = tournament.lokasi,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Rules",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = tournament.tipe,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                // Register Button
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF448AFF), // Blue background color
                            shape = RoundedCornerShape(8.dp) // Rounded corners
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            navController.navigate(Screen.TournamentSubmit.createRoute(tournament.TournamentID))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Register",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
