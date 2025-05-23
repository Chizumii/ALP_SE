package com.example.alp_se.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alp_se.R

@Composable
fun CreateNewsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
    ) {
        // Top Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF333333))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "News Creation",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create News Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Color(0xFF333333), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create News",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Upload Logo Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Upload News Image",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { /* Handle Upload */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A5AFF))
                        ) {
                            Text(text = "Upload", color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Complete Button
                Button(
                    onClick = { /* Handle Completion */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A5AFF)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "COMPLETE", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val icons = listOf(
                R.drawable.baseline_home_filled_24 to "Home",
                R.drawable.baseline_search_24 to "Search",
                R.drawable.champion to "Champion",
                R.drawable.baseline_groups_24 to "Team",
                R.drawable.baseline_person_24 to "Profile"
            )

            icons.forEach { (iconRes, description) ->
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = description,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { /* Handle Navigation */ }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateNewsScreenPreview() {
    CreateNewsScreen()
}