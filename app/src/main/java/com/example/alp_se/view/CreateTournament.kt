package com.example.alp_se.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.alp_se.R
import com.example.alp_se.viewModels.TournamentViewModel

@Composable
fun CreateTournament(
    navController: NavController,
    tournamentViewModel: TournamentViewModel
) {
//    val availableLocations = listOf(tournamentViewModel.lokasiInput)
    var imageInput by remember { mutableStateOf(tournamentViewModel.imageInput) }
    var namaInput by remember { mutableStateOf(tournamentViewModel.nameTournamentInput) }
    var deskripsiInput by remember { mutableStateOf(tournamentViewModel.descriptionInput) }
    var costInput by remember { mutableStateOf(tournamentViewModel.costInput) }
    var typeInput by remember { mutableStateOf(tournamentViewModel.typeInput) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { newValue ->
                imageInput = newValue.toString()
                tournamentViewModel.updateImageInput(imageInput)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // Enhanced Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D2D2D))
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.community_removebg_preview),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 16.dp)
                )
                Text(
                    text = "Create Tournament",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Tournament Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

//             Image Upload Section
            item {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            color = Color(0xFF2D2D2D),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageInput != null) {
                        Image(
                            painter = rememberAsyncImagePainter(tournamentViewModel.imageInput),
                            contentDescription = "Tournament Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.community_removebg_preview),
                                contentDescription = "Upload Icon",
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "Upload Tournament Banner",
                                color = Color.White,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = { launcher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2)),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Choose Image",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Form Fields
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    CustomTextField(
                        value = tournamentViewModel.nameTournamentInput,
                        onValueChange = {
                            namaInput = it
                            tournamentViewModel.updateNameTournamentInput(it)
                        },
                        label = "Tournament Name"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = tournamentViewModel.descriptionInput,
                        onValueChange = {
                            deskripsiInput = it
                            tournamentViewModel.updateDescriptionInput(it)
                        },
                        label = "Description",
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = tournamentViewModel.costInput,
                        onValueChange = {
                            costInput = it
                            tournamentViewModel.updateCostInput(it)
                        },
                        label = "Entry Cost"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = tournamentViewModel.typeInput,
                        onValueChange = {
                            typeInput = it
                            tournamentViewModel.updateTypeInput(it)
                        },
                        label = "Tournament Type"
                    )
                }
            }

            // Submit Button
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        tournamentViewModel.createTournament(
                            navController,
                            namaInput,
                            deskripsiInput,
                            costInput,
                            imageInput,
                            typeInput,
                            tournamentViewModel.lokasiInput
                        )
                        navController.navigate("Tournament")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Create Tournament",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2D2D2D), RoundedCornerShape(12.dp)),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            maxLines = maxLines,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF4A90E2),
                unfocusedBorderColor = Color(0xFF404040),
                cursorColor = Color(0xFF4A90E2)
            )
        )
    }
}

