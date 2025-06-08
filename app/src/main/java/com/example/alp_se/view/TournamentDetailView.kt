package com.example.alp_se.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.alp_se.R
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.navigation.Screen
import com.example.alp_se.viewModels.TournamentViewModel

val pageBackgroundBrushDetail = Brush.verticalGradient(
    colors = listOf(Color(0xFF0F0F23), Color(0xFF1A1A2E), Color(0xFF16213E))
)
val headerBackgroundBrushDetail = Brush.horizontalGradient(
    colors = listOf(Color(0xFF6C63FF), Color(0xFF9C88FF), Color(0xFF6C63FF))
)
val primaryAccentColorDetail = Color(0xFF6C63FF)
val secondaryTextColorDetail = Color(0xFFB0B3B8)
val successColorDetail = Color(0xFF4CAF50)
@Composable
fun TournamentDetailView(
    tournament: TournamentResponse,
    navController: NavController,
    tournamentViewModel: TournamentViewModel,
    token: String
) {
    val context = LocalContext.current
    val fullImageUrl = "http://192.168.81.69:3000/${tournament.image}"
    val registrationStatus by tournamentViewModel.registrationStatusMap.collectAsState()
    val isRegistered = registrationStatus[tournament.TournamentID] ?: false

    LaunchedEffect(tournament.TournamentID) {
        tournamentViewModel.checkRegistrationStatus(tournament.TournamentID, token)
    }

    val placeholderPainter = rememberVectorPainter(image = Icons.Filled.ImageSearch)
    val errorPainter = rememberVectorPainter(image = Icons.Filled.BrokenImage)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackgroundBrushDetail) // Apply themed background
    ) {
        // Top Navbar - Themed
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(headerBackgroundBrushDetail) // Apply themed header background
                .padding(vertical = 12.dp) // Adjusted padding
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // Adjusted horizontal padding
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.community_removebg_preview),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(48.dp) // Consistent logo size
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                        .padding(6.dp)
                )
                IconButton( // Styled Edit Button
                    onClick = { tournamentViewModel.openUpdate(navController, tournament) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Tournament",
                        tint = Color.White, // White icon on gradient
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Text(
                text = "Tournament Details", // Specific title
                fontSize = 22.sp, // Consistent title size
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize() // Fill the available space
                .verticalScroll(rememberScrollState())
                .padding(
                    top = 80.dp,
                    bottom = 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ) // Adjusted top for header, bottom for content
        ) {
            // Registration Status Banner
            if (isRegistered) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = successColorDetail // Use defined success color
                    ),
                    shape = RoundedCornerShape(12.dp) // Consistent corner radius
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Registered",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "You are registered for this tournament!", // Slightly friendlier text
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Tournament Title
            Text(
                text = tournament.nama_tournament,
                fontSize = 26.sp, // Adjusted size
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
            )

            // Image with proper AsyncImage and vector placeholders
            Card( // Wrap image in a card for better shadow/elevation if desired (optional)
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp) // Slightly adjusted height
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(fullImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Tournament Banner",
                    modifier = Modifier.fillMaxSize(), // Fill the card
                    contentScale = ContentScale.Crop,
                    placeholder = placeholderPainter,
                    error = errorPainter
                )
            }


            Spacer(modifier = Modifier.height(24.dp)) // Increased spacing

            // Description Section
            Text(
                text = "Description",
                fontSize = 20.sp, // Slightly larger section title
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = tournament.description,
                fontSize = 16.sp,
                color = secondaryTextColorDetail, // Use themed secondary text color
                lineHeight = 24.sp, // Improve readability
                modifier = Modifier.padding(bottom = 24.dp) // Increased spacing
            )

            // Information Section
            // Grouping info items for better visual structure
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoRow(title = "Registration Fee", value = "Rp ${tournament.biaya}")
                InfoRow(title = "Location", value = tournament.lokasi)
                InfoRow(title = "Game Rules / Type", value = tournament.tipe) // Clarified title
            }


            Spacer(modifier = Modifier.height(24.dp))

            // Register Button - Themed
            Button(
                onClick = {
                    if (!isRegistered) {
                        navController.navigate(
                            Screen.TournamentSubmit.createRoute(tournament.TournamentID)
                        )
                    }
                },
                enabled = !isRegistered, // Button is enabled if not registered
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryAccentColorDetail,
                    disabledContainerColor = secondaryTextColorDetail.copy(alpha = 0.5f) // Themed disabled color
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp) // Consistent shape
            ) {
                Text(
                    text = if (isRegistered) "Already Registered" else "Register for Tournament",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun InfoRow(title: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 16.sp, // Consistent size
            color = secondaryTextColorDetail, // Themed secondary text color
            fontWeight = FontWeight.SemiBold // Adjusted weight
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 18.sp, // Slightly larger value text
            color = Color.White,
            fontWeight = FontWeight.Medium // Adjusted weight
        )
    }
}