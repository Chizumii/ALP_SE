package com.example.alp_se.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alp_se.R
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

// Main Composable
@Composable
fun CreateTeamScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Create Team Form
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
                    text = "Create Team",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Input Fields
                CustomTextField("Team Name")
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField("GameID")
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField("Nickname")
                Spacer(modifier = Modifier.height(8.dp))

                // Upload Logo
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
                            text = "Upload Logo Team",
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

                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField("Leader Phone Number")

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

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(label: String) {
    OutlinedTextField(
        value = "",
        onValueChange = { /* Handle Text Change */ },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White), // Set text color here
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF5A5AFF),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.Gray,
            cursorColor = Color.White

        )
    )
}

@Composable
fun BottomNavigationsBar() {
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

// Fix: Place Preview Function at the Top Level
@Preview(showBackground = true)
@Composable
fun CreateTeamScreenPreview() {
    CreateTeamScreen()
}