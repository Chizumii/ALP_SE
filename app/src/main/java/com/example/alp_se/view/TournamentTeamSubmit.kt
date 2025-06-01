package com.example.alp_se.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import coil.request.ImageRequest
import com.example.alp_se.R
import com.example.alp_se.models.Team
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.viewModels.TeamViewModel
import com.example.alp_se.viewModels.TournamentViewModel
import kotlinx.coroutines.launch
import android.widget.Toast

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
            .background(Color(0XFF222222))
    ) {
        // Top Navigation
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(0XFF222222))
        ) {
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
                }

                Text(
                    text = "Tournament Registration",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 90.dp, bottom = 80.dp, start = 16.dp, end = 16.dp)
        ) {
            // Tournament Title
            Text(
                text = tournament.nama_tournament,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Selected team display
            selectedTeam?.let { team ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF448AFF))
                ) {
                    Text(
                        text = "Selected Team: ${team.namatim}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Team List Section
            Text(
                text = "Select a Team:",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Content based on state
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    uiState.isLoading -> {
                        LoadingState()
                    }
                    uiState.error != null -> {
                        ErrorState(
                            error = uiState.error!!,
                            onRetry = {
                                teamViewModel.clearError()
                                teamViewModel.loadTeams()
                            }
                        )
                    }
                    uiState.showEmptyState -> {
                        EmptyState()
                    }
                    else -> {
                        SelectableTeamList(
                            teams = uiState.displayTeams,
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

            // Submit button - now actually calls the registration API
            Button(
                onClick = {
                    selectedTeam?.let { team ->
                        isSubmitting = true
                        // Call the actual registration API with the selected team
                        tournamentViewModel.registerTeamWithId(
                            tournamentId = tournament.TournamentID,
                            teamId = team.TeamId,
                            token = token,
                            onSuccess = {
                                isSubmitting = false
                                Toast.makeText(context, "Successfully registered team '${team.namatim}'!", Toast.LENGTH_SHORT).show()
                                // Navigate back to tournament list or detail
                                navController.navigate("Tournament") {
                                    popUpTo("Tournament") { inclusive = true }
                                }
                            },
                            onError = { errorMessage ->
                                isSubmitting = false
                                Toast.makeText(context, "Failed to register: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                enabled = selectedTeam != null && !isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF448AFF),
                    disabledContainerColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Submit Registration",
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
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(teams) { team ->
            SelectableTeamCard(
                team = team,
                isSelected = selectedTeam?.TeamId == team.TeamId,
                onTeamSelect = { onTeamSelect(team) },
                imageUrl = getImageUrl(team.image)
            )
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTeamSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF448AFF) else Color(0xFF333333)
        ),
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF448AFF)) else null
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Team Image",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.__t_33pnkrfv_vlnxkbrsnya),
                error = painterResource(id = R.drawable.__t_33pnkrfv_vlnxkbrsnya)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Team ID: ${team.TeamId}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    text = team.namatim,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = if (isSelected) "Selected" else "Not Selected",
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}