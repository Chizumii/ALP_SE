package com.example.alp_se.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
fun TeamScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
    ) {
        // Top Bar
        TopBar()

        // Search Bar
        SearchBar()

        // Content Section
        TeamCard()

        // Bottom Navigation
        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar()
    }
}

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF333333))
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image on the left
            Image(
                painter = painterResource(R.drawable.community_removebg_preview),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Team",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}



@Composable
fun SearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF444444), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Search Team Name",
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.White
            )
        }
    }
}

@Composable
fun TeamCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF333333), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.__t_33pnkrfv_vlnxkbrsnya), // Replace with your team image
                contentDescription = "Team Image",
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Mobile Legend",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Tinju Bales Tinju",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_home_filled_24),
            contentDescription = "home",
            modifier = Modifier.size(40.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.baseline_search_24),
            contentDescription = "search",
            modifier = Modifier.size(40.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.champion),
            contentDescription = "champion",
            modifier = Modifier.size(40.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.baseline_groups_24),
            contentDescription = "team",
            modifier = Modifier.size(40.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.baseline_person_24),
            contentDescription = "profile",
            modifier = Modifier.size(40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TeamScreenPreview() {
    TeamScreen()
}