package com.example.alp_se.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.alp_se.R
import com.example.alp_se.models.Team
import com.example.alp_se.navigation.Screen
import com.example.alp_se.viewModels.TeamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamView(
    navController: NavController,
    teamViewModel: TeamViewModel = viewModel()
) {
    val uiState by teamViewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<Team?>(null) }

    LaunchedEffect(Unit) {
        teamViewModel.loadTeams()
    }

    // Handle success messages
    LaunchedEffect(uiState.createSuccess, uiState.updateSuccess, uiState.deleteSuccess) {
        if (uiState.createSuccess || uiState.updateSuccess || uiState.deleteSuccess) {
            teamViewModel.clearSuccessFlags()
            teamViewModel.loadTeams()
        }
    }

    // Show error messages
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // You can show a snackbar or toast here
            teamViewModel.clearError()
        }
    }

    Scaffold(
        containerColor = Color(0xFF222222),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.TeamCreate.route)
                },
                containerColor = Color(0xFF5A5AFF),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Team"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF222222))
        ) {
            // Top Bar
            TopBar(
                onRefresh = {
                    teamViewModel.clearError()
                    teamViewModel.loadTeams()
                }
            )

            // Search Bar
            SearchBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = { query ->
                    teamViewModel.searchTeams(query)
                },
                isSearching = uiState.isSearching
            )

            // Content Section
            Box(modifier = Modifier.weight(1f)) {
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
                    uiState.showNoSearchResults -> {
                        NoSearchResultsState(searchQuery = uiState.searchQuery)
                    }
                    else -> {
                        TeamList(
                            teams = uiState.displayTeams,
                            onEditClick = { team ->
                                navController.navigate(Screen.TeamEdit.createRoute(team.teamId))
                            },
                            onDeleteClick = { team ->
                                showDeleteDialog = team
                            },
                            getImageUrl = { imagePath ->
                                teamViewModel.getImageUrl(imagePath)
                            }
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    showDeleteDialog?.let { team ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Team") },
            text = { Text("Are you sure you want to delete ${team.namatim}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        teamViewModel.deleteTeam(team.teamId)
                        showDeleteDialog = null
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TopBar(
    onRefresh: () -> Unit = {}
) {
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
            // Refresh button
            IconButton(
                onClick = onRefresh,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearching: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = {
            Text(
                text = "Search Team Name",
                color = Color.Gray
            )
        },
        leadingIcon = {
            if (isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun TeamList(
    teams: List<Team>,
    onEditClick: (Team) -> Unit,
    onDeleteClick: (Team) -> Unit,
    getImageUrl: (String) -> String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(teams) { team ->
            TeamCard(
                team = team,
                onEditClick = { onEditClick(team) },
                onDeleteClick = { onDeleteClick(team) },
                imageUrl = getImageUrl(team.image)
            )
        }
    }
}

@Composable
fun TeamCard(
    team: Team,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    imageUrl: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF333333)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Team Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.__t_33pnkrfv_vlnxkbrsnya),
                error = painterResource(id = R.drawable.__t_33pnkrfv_vlnxkbrsnya)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Team ID: ${team.teamId}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = team.namatim,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color(0xFF5A5AFF),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color.Red,
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading teams...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ErrorState(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5A5AFF)
                )
            ) {
                Text("Retry", color = Color.White)
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No teams found",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Create your first team to get started",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NoSearchResultsState(searchQuery: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No teams found for \"$searchQuery\"",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Try searching with different keywords",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}