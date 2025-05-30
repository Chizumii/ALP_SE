package com.example.alp_se.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.alp_se.R
import com.example.alp_se.viewModels.TeamViewModel

@Composable
fun CreateTeamScreen(
    teamViewModel: TeamViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    var teamName by remember { mutableStateOf("") }
    var gameId by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var leaderPhone by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val uiState by teamViewModel.uiState.collectAsState()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // Handle success/error states
    LaunchedEffect(uiState.createSuccess) {
        if (uiState.createSuccess) {
            teamViewModel.clearSuccessFlags()
            onNavigateBack()
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            errorMessage = uiState.error!!
            showError = true
        }
    }

    // Error Snackbar
    if (showError) {
        LaunchedEffect(showError) {
            kotlinx.coroutines.delay(3000)
            showError = false
            teamViewModel.clearError()
        }
    }

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

                // Team Name Input (This will be saved to backend)
                CustomTextField(
                    label = "Team Name",
                    value = teamName,
                    onValueChange = { teamName = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // GameID Input (Won't be saved to backend - just for UI)
                CustomTextField(
                    label = "GameID",
                    value = gameId,
                    onValueChange = { gameId = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nickname Input (Won't be saved to backend - just for UI)
                CustomTextField(
                    label = "Nickname",
                    value = nickname,
                    onValueChange = { nickname = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Upload Logo Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
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
                                onClick = { imagePickerLauncher.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A5AFF))
                            ) {
                                Text(text = "Upload", color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Leader Phone Number Input (Won't be saved to backend - just for UI)
                CustomTextField(
                    label = "Leader Phone Number",
                    value = leaderPhone,
                    onValueChange = { leaderPhone = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Complete Button
                Button(
                    onClick = {
                        if (teamName.isBlank()) {
                            errorMessage = "Team name is required"
                            showError = true
                        } else if (selectedImageUri == null) {
                            errorMessage = "Please select an image"
                            showError = true
                        } else {
                            teamViewModel.createTeam(teamName, selectedImageUri!!, context)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A5AFF)),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !uiState.isCreating
                ) {
                    if (uiState.isCreating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "COMPLETE", color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation
        BottomNavigationsBar()
    }

    // Error Snackbar
    if (showError) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = errorMessage,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
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

@Preview(showBackground = true)
@Composable
fun CreateTeamScreenPreview() {
    CreateTeamScreen()
}