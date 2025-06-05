package com.example.alp_se.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.alp_se.viewModels.TeamViewModel

// Theme Elements (copied for this file, ideally in a separate Theme.kt)
val pageBackgroundBrushCreateTeam = Brush.verticalGradient(
    colors = listOf(Color(0xFF0F0F23), Color(0xFF1A1A2E), Color(0xFF16213E))
)
val headerBackgroundBrushCreateTeam = Brush.horizontalGradient(
    colors = listOf(Color(0xFF6C63FF), Color(0xFF9C88FF), Color(0xFF6C63FF))
)
val primaryAppColorCreateTeam = Color(0xFF6C63FF)
val secondaryTextColorCreateTeam = Color(0xFFB0B3B8)
val cardBackgroundColorCreateTeam = Color(0xFF1F1F32) // For error card, etc.
val textFieldBackgroundColorCreateTeam = Color(0xFF2A2A3D)
val errorColorCreateTeam = Color(0xFFE53935)
val defaultCornerShapeCreateTeam = RoundedCornerShape(12.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamView(
    navController: NavController,
    teamViewModel: TeamViewModel = viewModel(),
    teamId: Int? = null
) {
    val context = LocalContext.current
    val uiState by teamViewModel.uiState.collectAsState()

    var teamName by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var imageError by remember { mutableStateOf<String?>(null) }

    val isEditMode = teamId != null
    val pageTitle = if (isEditMode) "Edit Team" else "Create New Team"
    val buttonText = if (isEditMode) "Update Team" else "Create Team"

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        imageError = null
        teamViewModel.clearError()
    }

    LaunchedEffect(teamId) {
        if (isEditMode && teamId != null) {
            uiState.teams.find { it.TeamId == teamId }?.let { team ->
                teamName = team.namatim
            }
        }
    }

    LaunchedEffect(uiState.createSuccess, uiState.updateSuccess) {
        if (uiState.createSuccess || uiState.updateSuccess) {
            navController.popBackStack()
            teamViewModel.clearSuccessFlags()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackgroundBrushCreateTeam)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CreateTeamHeader(title = pageTitle, onBackClick = { navController.popBackStack() })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image Selection
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(textFieldBackgroundColorCreateTeam)
                            .border(
                                width = 2.dp,
                                color = if (imageError != null) errorColorCreateTeam else primaryAppColorCreateTeam,
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
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddAPhoto,
                                    contentDescription = "Add team image",
                                    tint = secondaryTextColorCreateTeam,
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    "Add Photo",
                                    color = secondaryTextColorCreateTeam,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                    if (imageError != null) {
                        Text(
                            imageError!!,
                            color = errorColorCreateTeam,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        Text(
                            "Tap to select team image",
                            color = secondaryTextColorCreateTeam,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }

                // Team Name Input
                OutlinedTextField(
                    value = teamName,
                    onValueChange = {
                        teamName = it
                        nameError = null
                        teamViewModel.clearError()
                    },
                    label = { Text("Team Name", color = secondaryTextColorCreateTeam) },
                    placeholder = {
                        Text(
                            "Enter team name",
                            color = secondaryTextColorCreateTeam.copy(alpha = 0.7f)
                        )
                    },
                    isError = nameError != null,
                    supportingText = {
                        if (nameError != null) {
                            Text(
                                nameError!!,
                                color = errorColorCreateTeam,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = textFieldBackgroundColorCreateTeam,
                        unfocusedContainerColor = textFieldBackgroundColorCreateTeam,
                        cursorColor = primaryAppColorCreateTeam,
                        focusedBorderColor = primaryAppColorCreateTeam,
                        unfocusedBorderColor = cardBackgroundColorCreateTeam,
                        errorBorderColor = errorColorCreateTeam
                    ),
                    shape = defaultCornerShapeCreateTeam,
                    singleLine = true
                )

                // General Error Message
                if (uiState.error != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = errorColorCreateTeam.copy(
                                alpha = 0.15f
                            )
                        ),
                        shape = defaultCornerShapeCreateTeam
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.ErrorOutline,
                                contentDescription = "Error",
                                tint = errorColorCreateTeam,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                uiState.error!!,
                                color = errorColorCreateTeam,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Create/Update Button
                Button(
                    onClick = {
                        var hasError = false
                        teamViewModel.clearError()
                        if (teamName.isBlank()) {
                            nameError = "Team name is required"
                            hasError = true
                        } else if (teamName.length < 3) {
                            nameError = "Team name at least 3 characters"
                            hasError = true
                        } else if (teamName.length > 50) {
                            nameError = "Team name max 50 characters"
                            hasError = true
                        }
                        if (selectedImageUri == null && !isEditMode) {
                            imageError = "Team image is required"
                            hasError = true
                        }
                        if (!hasError) {
                            if (isEditMode && teamId != null) {
                                selectedImageUri?.let {
                                    teamViewModel.updateTeam(
                                        teamId, teamName,
                                        it, context
                                    )
                                }
                            } else {
                                selectedImageUri?.let {
                                    teamViewModel.createTeam(
                                        teamName,
                                        it,
                                        context
                                    )
                                }
                            }
                        }
                    },
                    enabled = !uiState.isOperationInProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryAppColorCreateTeam,
                        contentColor = Color.White,
                        disabledContainerColor = secondaryTextColorCreateTeam.copy(alpha = 0.5f)
                    ),
                    shape = defaultCornerShapeCreateTeam
                ) {
                    if (uiState.isOperationInProgress) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Text(
                                if (isEditMode) "Updating..." else "Creating...",
                                modifier = Modifier.padding(start = 12.dp),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Text(buttonText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun CreateTeamHeader(title: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBackgroundBrushCreateTeam)
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

