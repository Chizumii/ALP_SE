package com.example.alp_se.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PeopleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import coil.request.ImageRequest
import com.example.alp_se.R
import com.example.alp_se.models.Team
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.viewModels.TeamViewModel
import com.example.alp_se.viewModels.TournamentViewModel

val pageBackgroundBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0F0F23),
        Color(0xFF1A1A2E),
        Color(0xFF16213E)
    )
)

val headerBackgroundBrush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF6C63FF),
        Color(0xFF9C88FF),
        Color(0xFF6C63FF)
    )
)

val primaryAccentColor = Color(0xFF6C63FF)
val cardBackgroundColor = Color(0xFF2D2D3D) // For unselected cards
val secondaryTextColor = Color(0xFFB0B3B8) // For less prominent text

@Composable
fun TournamentTeamSubmit(
    tournament: TournamentResponse,
    navController: NavController,
    tournamentViewModel: TournamentViewModel,
    token: String,
    teamViewModel: TeamViewModel = viewModel()
) {
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val uiState by teamViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        teamViewModel.loadTeams()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackgroundBrush) // Use gradient background
    ) {
        // Top Navigation - Styled like TournamentView's header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(headerBackgroundBrush) // Use header gradient
                .padding(vertical = 16.dp), // Adjusted padding
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.community_removebg_preview),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color.White.copy(alpha = 0.1f),
                            CircleShape
                        )
                        .padding(6.dp) // Adjusted padding for logo
                )
                // Spacer can be added if an icon is on the right, for now title is centered below
            }
            Text(
                text = "Tournament Registration",
                fontSize = 22.sp, // Consistent font size
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 80.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ) // Adjusted top padding
        ) {
            // Tournament Title
            Text(
                text = tournament.nama_tournament,
                fontSize = 24.sp, // Slightly larger title
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Selected team display
            selectedTeam?.let { team ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = primaryAccentColor.copy(alpha = 0.8f)), // Use accent color
                    shape = RoundedCornerShape(12.dp) // Softer corners
                ) {
                    Text(
                        text = "Selected Team: ${team.namatim}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold, // Adjusted weight
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Team List Section
            Text(
                text = "Select Your Team:", // Changed text for clarity
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Content based on state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ensure this box takes available space
            ) {
                when {
                    uiState.isLoading -> {
                        LoadingStateTeamList() // Ensure this is styled consistently
                    }

                    uiState.error != null -> {
                        ErrorState( // Ensure this is styled consistently
                            error = uiState.error!!,
                            onRetry = {
                                teamViewModel.clearError()
                                teamViewModel.loadTeams()
                            }
                        )
                    }

                    uiState.teams.isEmpty() && !uiState.isLoading -> { // More specific check for empty
                        EmptyTeamListState { // Provide a way to perhaps create a team or refresh
                            teamViewModel.loadTeams()
                        }
                    }

                    else -> {
                        SelectableTeamList(
                            teams = uiState.displayTeams.takeIf { it.isNotEmpty() }
                                ?: uiState.teams, // Ensure displayTeams is used if available
                            selectedTeam = selectedTeam,
                            onTeamSelect = { team ->
                                selectedTeam = team
                            },
                            getImageUrl = { imagePath ->
                                teamViewModel.getImageUrl(imagePath)
                            }
                        )
                    }
                }
            }

            // Submit button
            Button(
                onClick = {
                    selectedTeam?.let { team ->
                        isSubmitting = true
                        tournamentViewModel.registerTeamWithId(
                            tournamentId = tournament.TournamentID,
                            teamId = team.TeamId,
                            token = token,
                            onSuccess = {
                                isSubmitting = false
                                Toast.makeText(
                                    context,
                                    "Successfully registered team '${team.namatim}'!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("Tournament") {
                                    popUpTo("Tournament") { inclusive = true }
                                }
                            },
                            onError = { errorMessage ->
                                isSubmitting = false
                                Toast.makeText(
                                    context,
                                    "Failed to register: $errorMessage",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                },
                enabled = selectedTeam != null && !isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryAccentColor,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 3.dp
                    )
                } else {
                    Text(
                        text = "Register",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun SelectableTeamList(
    teams: List<Team>,
    selectedTeam: Team?,
    onTeamSelect: (Team) -> Unit,
    getImageUrl: (String) -> String
) {
    if (teams.isEmpty()) {
        EmptyTeamListState(onRetry = {})
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(teams, key = { it.TeamId }) { team ->
                SelectableTeamCard(
                    team = team,
                    isSelected = selectedTeam?.TeamId == team.TeamId,
                    onTeamSelect = { onTeamSelect(team) },
                    imageUrl = getImageUrl(team.image)
                )
            }
        }
    }
}

@Composable
fun SelectableTeamCard(
    team: Team,
    isSelected: Boolean,
    onTeamSelect: () -> Unit,
    imageUrl: String
) {
    val context = LocalContext.current
    val cardContainerColor = if (isSelected) primaryAccentColor else cardBackgroundColor

    val placeholderPainter = rememberVectorPainter(image = Icons.Filled.Group)
    val errorPainter = rememberVectorPainter(image = Icons.Filled.BrokenImage)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTeamSelect() },
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) BorderStroke(
            2.dp,
            Color.White.copy(alpha = 0.7f)
        ) else BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Team Image",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop,
                placeholder = placeholderPainter,
                error = errorPainter
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = team.namatim,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Team ID: ${team.TeamId}",
                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else secondaryTextColor,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = if (isSelected) "Selected" else "Not Selected",
                tint = if (isSelected) Color.White else secondaryTextColor.copy(alpha = 0.7f),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Composable
fun EmptyTeamListState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                imageVector = Icons.Filled.PeopleOutline,
                contentDescription = "No teams available",
                modifier = Modifier.size(100.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(secondaryTextColor)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "No Teams Found",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You don't have any teams created yet, or none are available to select.",
                color = secondaryTextColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}