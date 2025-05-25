//CreateTeamView.kt
package com.example.alp_se.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.alp_se.R
import com.example.alp_se.viewModels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamView(
    navController: NavController,
    teamViewModel: TeamViewModel = viewModel(),
    teamId: Int? = null // null for create, non-null for edit
) {
    val context = LocalContext.current
    val uiState by teamViewModel.uiState.collectAsState()

    var teamName by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var imageError by remember { mutableStateOf<String?>(null) }

    val isEditMode = teamId != null
    val title = if (isEditMode) "Edit Team" else "Create Team"
    val buttonText = if (isEditMode) "Update Team" else "Create Team"

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        imageError = null
    }

    // Load team data for editing
    LaunchedEffect(teamId) {
        if (isEditMode && teamId != null) {
            uiState.teams.find { it.teamId == teamId }?.let { team ->
                teamName = team.namatim
                // Note: For editing, you might want to handle existing image differently
            }
        }
    }

    // Handle success
    LaunchedEffect(uiState.createSuccess, uiState.updateSuccess) {
        if (uiState.createSuccess || uiState.updateSuccess) {
            navController.popBackStack()
        }
    }

    // Handle error
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            // Error is handled in the UI
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF2A2A2A)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Selection Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2A2A2A))
                        .border(
                            width = 2.dp,
                            color = if (imageError != null) Color.Red else Color(0xFFFFC107),
                            shape = CircleShape
                        )
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected team image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Add Photo",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                if (imageError != null) {
                    Text(
                        text = imageError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Text(
                    text = "Tap to select team image",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Team Name Input
            OutlinedTextField(
                value = teamName,
                onValueChange = {
                    teamName = it
                    nameError = null
                    teamViewModel.clearError()
                },
                label = { Text("Team Name", color = Color.Gray) },
                placeholder = { Text("Enter team name", color = Color.Gray) },
                isError = nameError != null,
                supportingText = {
                    if (nameError != null) {
                        Text(
                            text = nameError!!,
                            color = Color.Red
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFC107),
                    unfocusedBorderColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Error Message
            if (uiState.error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = uiState.error!!,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Create/Update Button
            Button(
                onClick = {
                    // Validate inputs
                    var hasError = false

                    if (teamName.isBlank()) {
                        nameError = "Team name is required"
                        hasError = true
                    } else if (teamName.length > 100) {
                        nameError = "Team name cannot exceed 100 characters"
                        hasError = true
                    }

                    if (selectedImageUri == null && !isEditMode) {
                        imageError = "Please select a team image"
                        hasError = true
                    }

                    if (!hasError) {
                        if (isEditMode && teamId != null) {
                            if (selectedImageUri != null) {
                                teamViewModel.updateTeam(teamId, teamName,
                                    selectedImageUri!!, context)
                            }
                        } else {
                            selectedImageUri?.let { uri ->
                                teamViewModel.createTeam(teamName, uri, context)
                            }
                        }
                    }
                },
                enabled = !uiState.isOperationInProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isOperationInProgress) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Black
                        )
                        Text(
                            text = if (isEditMode) "Updating..." else "Creating...",
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Text(
                        text = buttonText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}