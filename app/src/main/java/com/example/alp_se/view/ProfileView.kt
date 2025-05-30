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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alp_se.R

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF222222))
    ) {
        // Header section
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
                Image(
                    painter = painterResource(R.drawable.community_removebg_preview),
                    contentDescription = "Logo",
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.weight(1f))

                Box(contentAlignment = Alignment.BottomEnd) {

                    IconButton(
                        onClick = { /* Handle edit photo */ },
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFF3B82F6), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Photo",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            Text(
                text = "Profile",
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

        // Content below divider - centered
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Header - Centered
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profileimg),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)  // Menambahkan clip untuk membuat gambar bulat
                            .background(Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop  // Menambahkan contentScale agar gambar mengisi area dengan baik
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Text(
                            text = "Delvincent",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Patricio",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "089690387887",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Team Section
            Text(
                text = "Team",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = { /* Handle team creation/joining */ },
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Makes button width 80% of screen width
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Create Team or Join Team")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Profile Game Section
            Text(
                text = "Profile Game",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(0.8f), // Makes card width 80% of screen width
                colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "Mobile Legend",
                        fontSize = 16.sp,
                        color = Color(0xFF60A5FA),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.padding(start = 20.dp)) {
                            Text(
                                text = "In-Game Nickname",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Chizumi",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                        Column(modifier = Modifier.padding(end = 20.dp)) {

                            Text(
                                text = "Game Id",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "427877263",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}