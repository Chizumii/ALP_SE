package com.example.alp_se.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.alp_se.uiStates.TeamDataStatusUIState
import com.example.alp_se.viewModels.TeamViewModel

val pageBackgroundBrushCreateTeam = Brush.verticalGradient(
    colors = listOf(Color(0xFF0F0F23), Color(0xFF1A1A2E), Color(0xFF16213E))
)
val headerBackgroundBrushCreateTeam = Brush.horizontalGradient(
    colors = listOf(Color(0xFF6C63FF), Color(0xFF9C88FF), Color(0xFF6C63FF))
)
val primaryAppColorCreateTeam = Color(0xFF6C63FF)
val secondaryTextColorCreateTeam = Color(0xFFB0B3B8)
val cardBackgroundColorCreateTeam = Color(0xFF1F1F32)
val textFieldBackgroundColorCreateTeam = Color(0xFF2A2A3D)
val errorColorCreateTeam = Color(0xFFE53935)
val defaultCornerShapeCreateTeam = RoundedCornerShape(12.dp)


@Composable
fun CreateTeamView(
    navController: NavController,
    teamViewModel: TeamViewModel,
    teamId: Int? = null
) {
    val context = LocalContext.current
    val uiState = teamViewModel.uiState
    val teams by teamViewModel.teamList.collectAsState()

    val isEditMode = teamId != null
    val pageTitle = if (isEditMode) "Edit Team" else "Create New Team"
    val buttonText = if (isEditMode) "Update Team" else "Create Team"

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        teamViewModel.imageUriInput = uri
    }

    // Initialize ViewModel for edit mode
    LaunchedEffect(key1 = teamId) {
        if (isEditMode) {
            val teamToEdit = teams.find { it.TeamId == teamId }
            teamViewModel.initializeForEdit(teamToEdit)
        } else {
            teamViewModel.initializeForEdit(null)
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
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(textFieldBackgroundColorCreateTeam)
                        .border(
                            width = 2.dp,
                            color = primaryAppColorCreateTeam,
                            shape = CircleShape
                        )
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (teamViewModel.imageUriInput != null) {
                        AsyncImage(
                            model = teamViewModel.imageUriInput,
                            contentDescription = "Selected team image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Icon dan Teks placeholder
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                Icons.Default.AddAPhoto, "Add team image",
                                tint = secondaryTextColorCreateTeam, modifier = Modifier.size(48.dp)
                            )
                            Text("Add Photo", color = secondaryTextColorCreateTeam, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
                Text("Tap to select team image", color = secondaryTextColorCreateTeam, fontSize = 14.sp, modifier = Modifier.padding(top = 12.dp))

                Spacer(modifier = Modifier.height(32.dp))

                // Team Name Input
                OutlinedTextField(
                    value = teamViewModel.nameTeamInput,
                    onValueChange = { teamViewModel.nameTeamInput = it },
                    label = { Text("Team Name", color = secondaryTextColorCreateTeam) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        focusedContainerColor = textFieldBackgroundColorCreateTeam, unfocusedContainerColor = textFieldBackgroundColorCreateTeam,
                        cursorColor = primaryAppColorCreateTeam, focusedBorderColor = primaryAppColorCreateTeam,
                        unfocusedBorderColor = cardBackgroundColorCreateTeam
                    ),
                    shape = defaultCornerShapeCreateTeam,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // General Error Message & Loading
                when (uiState) {
                    is TeamDataStatusUIState.Failed -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = errorColorCreateTeam.copy(alpha = 0.15f)),
                            shape = defaultCornerShapeCreateTeam
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ErrorOutline, "Error", tint = errorColorCreateTeam, modifier = Modifier.padding(end = 8.dp))
                                Text(uiState.errorMessage, color = errorColorCreateTeam, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    else -> {}
                }

                Spacer(modifier = Modifier.weight(1f))

                // Create/Update Button
                Button(
                    onClick = {
                        if (isEditMode) {
                            teamViewModel.updateTeam(context, navController)
                        } else {
                            teamViewModel.createTeam(context, navController)
                        }
                    },
                    enabled = uiState !is TeamDataStatusUIState.Loading,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryAppColorCreateTeam, contentColor = Color.White,
                        disabledContainerColor = secondaryTextColorCreateTeam.copy(alpha = 0.5f)
                    ),
                    shape = defaultCornerShapeCreateTeam
                ) {
                    if (uiState is TeamDataStatusUIState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
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
    // ... (Isi fungsi ini sama seperti di file lama Anda, tidak perlu diubah)
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