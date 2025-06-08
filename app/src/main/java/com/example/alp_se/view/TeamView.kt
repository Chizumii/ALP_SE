package com.example.alp_se.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.alp_se.R
import com.example.alp_se.models.Team
import com.example.alp_se.navigation.Screen
import com.example.alp_se.uiStates.TeamDataStatusUIState
import com.example.alp_se.viewModels.TeamViewModel
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val pageBackgroundBrushTeamList = Brush.verticalGradient(
    colors = listOf(Color(0xFF0F0F23), Color(0xFF1A1A2E), Color(0xFF16213E))
)
val headerBackgroundBrushTeamList = Brush.horizontalGradient(
    colors = listOf(Color(0xFF6C63FF), Color(0xFF9C88FF), Color(0xFF6C63FF))
)
val primaryAppColorTeamList = Color(0xFF6C63FF)
val secondaryTextColorTeamList = Color(0xFFB0B3B8)
val cardBackgroundColorTeamList = Color(0xFF1F1F32)
val textFieldBackgroundColorTeamList = Color(0xFF2A2A3D)
val errorColorTeamList = Color(0xFFE53935)
val defaultCornerShapeTeamList = RoundedCornerShape(12.dp)

@Composable
fun TeamView(
    navController: NavController,
    teamViewModel: TeamViewModel
) {
    val uiState = teamViewModel.uiState
    val teams by teamViewModel.teamList.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<Team?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val token = "7a1ce296-ab8e-40ce-bce8-add67c22d965"

    LaunchedEffect(navController.currentBackStackEntry) {
        teamViewModel.loadTeams(token)
    }

    // Debounce search
    LaunchedEffect(searchQuery) {
        isSearching = true
        coroutineScope.launch {
            delay(300) // 300ms debounce
            isSearching = false
        }
    }

    val filteredTeams = remember(searchQuery, teams) {
        if (searchQuery.isBlank()) {
            teams
        } else {
            teams.filter {
                it.namatim.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(pageBackgroundBrushTeamList)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TeamListHeader(onRefresh = { teamViewModel.loadTeams(token) })

            // Enhanced Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search teams...", color = secondaryTextColorTeamList.copy(alpha = 0.7f)) },
                leadingIcon = { 
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = primaryAppColorTeamList,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Search, "Search", tint = secondaryTextColorTeamList)
                    }
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                Icons.Default.Clear,
                                "Clear Search",
                                tint = secondaryTextColorTeamList
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = textFieldBackgroundColorTeamList,
                    unfocusedContainerColor = textFieldBackgroundColorTeamList,
                    cursorColor = primaryAppColorTeamList,
                    focusedBorderColor = primaryAppColorTeamList,
                    unfocusedBorderColor = cardBackgroundColorTeamList,
                ),
                shape = defaultCornerShapeTeamList
            )

            // Search Results Count
            if (searchQuery.isNotEmpty() && filteredTeams.isNotEmpty()) {
                Text(
                    text = "Found ${filteredTeams.size} team${if (filteredTeams.size != 1) "s" else ""}",
                    color = secondaryTextColorTeamList,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            Box(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                when (uiState) {
                    is TeamDataStatusUIState.Loading -> LoadingStateTeamList()
                    is TeamDataStatusUIState.Failed -> ErrorStateTeamList(
                        error = uiState.errorMessage,
                        onRetry = { teamViewModel.loadTeams(token) }
                    )
                    is TeamDataStatusUIState.Success -> {
                        if (filteredTeams.isEmpty()) {
                            if (searchQuery.isNotBlank()) {
                                NoSearchResultsStateTeamList(
                                    searchQuery = searchQuery,
                                    onClearSearch = { searchQuery = "" }
                                )
                            } else {
                                EmptyStateTeamList()
                            }
                        } else {
                            TeamListBody(
                                teams = filteredTeams,
                                onEditClick = { team ->
                                    teamViewModel.initializeForEdit(team)
                                    navController.navigate(Screen.TeamEdit.createRoute(team.TeamId))
                                },
                                onDeleteClick = { team -> showDeleteDialog = team }
                            )
                        }
                    }
                    is TeamDataStatusUIState.Start -> {}
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Screen.TeamCreate.route) },
            containerColor = primaryAppColorTeamList,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Create Team")
        }
    }

    showDeleteDialog?.let { team ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Team", color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete ${team.namatim}?", color = secondaryTextColorTeamList) },
            containerColor = cardBackgroundColorTeamList,
            shape = defaultCornerShapeTeamList,
            confirmButton = {
                TextButton(onClick = {
                    teamViewModel.deleteTeam(team.TeamId, token)
                    showDeleteDialog = null
                }) { Text("Delete", color = errorColorTeamList, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancel", color = primaryAppColorTeamList) }
            }
        )
    }
}

@Composable
fun TeamListBody(
    teams: List<Team>,
    onEditClick: (Team) -> Unit,
    onDeleteClick: (Team) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(teams, key = { it.TeamId }) { team ->
            TeamCardItem(
                team = team,
                onEditClick = { onEditClick(team) },
                onDeleteClick = { onDeleteClick(team) },
                imageUrl = "http://192.168.81.69:3000/${team.image}"
            )
        }
    }
}

@Composable
fun TeamListHeader(onRefresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBackgroundBrushTeamList)
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.community_removebg_preview),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
                    .padding(6.dp)
            )
            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Teams",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Text(
            text = "Teams",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TeamCardItem(
    team: Team,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    imageUrl: String
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColorTeamList),
        shape = defaultCornerShapeTeamList,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.size(64.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(imageUrl).crossfade(true).build(),
                    placeholder = painterResource(id = R.drawable.baseline_groups_24),
                    error = painterResource(id = R.drawable.__t_33pnkrfv_vlnxkbrsnya),
                    contentDescription = team.namatim,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = team.namatim,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ID: ${team.TeamId}",
                    color = secondaryTextColorTeamList,
                    fontSize = 14.sp
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(primaryAppColorTeamList.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Team",
                        tint = primaryAppColorTeamList,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(errorColorTeamList.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Team",
                        tint = errorColorTeamList,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingStateTeamList() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = primaryAppColorTeamList)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading teams...", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun ErrorStateTeamList(error: String, onRetry: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.ErrorOutline,
                contentDescription = "Error",
                tint = errorColorTeamList,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Oops! Something went wrong.",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                error,
                color = secondaryTextColorTeamList,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = primaryAppColorTeamList),
                shape = defaultCornerShapeTeamList
            ) { Text("Retry", color = Color.White) }
        }
    }
}

@Composable
fun EmptyStateTeamList() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.Inbox,
                contentDescription = "No teams",
                tint = secondaryTextColorTeamList,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No Teams Yet",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Tap '+' to create your first team!",
                color = secondaryTextColorTeamList,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Composable
fun NoSearchResultsStateTeamList(
    searchQuery: String,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = "No Results",
            modifier = Modifier.size(64.dp),
            tint = secondaryTextColorTeamList
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No teams found for \"$searchQuery\"",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Try adjusting your search or create a new team",
            color = secondaryTextColorTeamList,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = onClearSearch,
            colors = ButtonDefaults.textButtonColors(
                contentColor = primaryAppColorTeamList
            )
        ) {
            Text("Clear Search")
        }
    }
}