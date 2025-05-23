package com.example.alp_se.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alp_se.R
import com.example.alp_se.models.TournamentResponse
import kotlinx.coroutines.launch

@Composable
fun TournamentTeamSubmit(
    tournament: TournamentResponse,
    navController: NavController
) {
    var isSelected by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF222222))
    ) {
        // Navbar atas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(0XFF222222))
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
                    // Logo di kiri
                    Image(
                        painter = painterResource(R.drawable.community_removebg_preview),
                        contentDescription = "Logo",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f)) // Spacer untuk merenggangkan konten
                }

                Text(
                    text = "Tournament",
                    fontSize = 20.sp,
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
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 90.dp, bottom = 80.dp, start = 16.dp, end = 16.dp)
        ) {
            // Title
            Text(
                text = tournament.nama_tournament,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Image
            Image(
                painter = painterResource(id = R.drawable.pppppppp), // Replace with your drawable ID
                contentDescription = "Tournament Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
                    .padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFF424242), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = "Search Team Name",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter = painterResource(R.drawable.baseline_search_24),
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Search bar // State untuk kontrol outline
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF333333), RoundedCornerShape(12.dp))
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected) Color(0xFF448AFF) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            isSelected = !isSelected // Toggle outline

                        }
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background), // Replace with your image
                            contentDescription = "Team Logo",
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.Gray, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Tinju Bales Tinju",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit button
                if (showConfirmation) {
                    androidx.compose.material3.AlertDialog(
                        onDismissRequest = {
                            showConfirmation = false
                            navController.navigate("tournament_view") {
                                popUpTo("tournament_view") { inclusive = true }
                            }
                        },
                        title = {
                            Text(
                                text = "Success!",
                                color = Color.White
                            )
                        },
                        text = {
                            Text(
                                text = "Your team has been successfully registered for the tournament.",
                                color = Color.White
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showConfirmation = false
                                    navController.navigate("Tournament"){
                                        popUpTo("Tournament") { inclusive = true }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF448AFF
                                    )
                                )
                            ) {
                                Text("OK")
                            }
                        },
                        containerColor = Color(0xFF333333)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0XFF222222))
                ) {
                    // Previous code remains the same...

                    // Update the submit button's onClick handler
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                // Show confirmation dialog
                                showConfirmation = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF448AFF)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Submit",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

