package com.example.alp_se.view.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.alp_se.R
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.navigation.Screen
import com.example.alp_se.viewModels.TournamentViewModel

@Composable
fun TournamentCard(
    tournament: TournamentResponse,
    tournamentViewModel: TournamentViewModel,
    navController: NavController,
    isRegistered: Boolean = false
) {
    val context = LocalContext.current
    val fullImageUrl = "http://192.168.253.69:3000${tournament.image}"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.navigate(Screen.TournamentDetail.createRoute(tournament.TournamentID))
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF333333)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Tournament Image
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(fullImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Tournament Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.pppppppp),
                error = painterResource(id = R.drawable.pppppppp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tournament Title
            Text(
                text = tournament.nama_tournament,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tournament Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Fee: Rp ${tournament.biaya}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Type: ${tournament.tipe}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Registration Status
                if (isRegistered) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF4CAF50),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Registered",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}