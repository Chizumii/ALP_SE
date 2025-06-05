package com.example.alp_se.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.alp_se.R
import com.example.alp_se.models.Team
import com.example.alp_se.navigation.Screen
import com.example.alp_se.viewModels.TeamViewModel

// Theme Elements (copied for this file, ideally in a separate Theme.kt)
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

    LaunchedEffect(uiState.createSuccess, uiState.updateSuccess, uiState.deleteSuccess) {
        if (uiState.createSuccess || uiState.updateSuccess || uiState.deleteSuccess) {
            teamViewModel.clearSuccessFlags()
            teamViewModel.loadTeams()
        }
    }

    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Snackbar can be shown here if needed for non-critical errors
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(pageBackgroundBrushTeamList)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TeamListHeader(
                onRefresh = {
                    teamViewModel.clearError()
                    teamViewModel.loadTeams()
                }
            )
            TeamListSearchBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = { query -> teamViewModel.searchTeams(query) },
                isSearching = uiState.isSearching
            )

            Box(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)) {
                when {
                    uiState.isLoading -> LoadingStateTeamList()
                    uiState.error != null && uiState.displayTeams.isEmpty() -> ErrorStateTeamList(
                        error = uiState.error!!,
                        onRetry = {
                            teamViewModel.clearError()
                            teamViewModel.loadTeams()
                        }
                    )

                    uiState.showEmptyState -> EmptyStateTeamList()
                    uiState.showNoSearchResults -> NoSearchResultsStateTeamList(searchQuery = uiState.searchQuery)
                    else -> TeamListBody(
                        teams = uiState.displayTeams,
                        onEditClick = { team ->
                            navController.navigate(
                                Screen.TeamEdit.createRoute(
                                    team.TeamId
                                )
                            )
                        },
                        onDeleteClick = { team -> showDeleteDialog = team },
                        getImageUrl = { imagePath -> teamViewModel.getImageUrl(imagePath) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.navigate(Screen.TeamCreate.route) },
            containerColor = primaryAppColorTeamList,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Create Team")
        }
    }

    showDeleteDialog?.let { team ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Team", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Are you sure you want to delete ${team.namatim}?",
                    color = secondaryTextColorTeamList
                )
            },
            containerColor = cardBackgroundColorTeamList,
            shape = defaultCornerShapeTeamList,
            confirmButton = {
                TextButton(onClick = {
                    teamViewModel.deleteTeam(team.TeamId)
                    showDeleteDialog = null
                }) { Text("Delete", color = errorColorTeamList, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text(
                        "Cancel",
                        color = primaryAppColorTeamList
                    )
                }
            }
        )
    }
}

@Composable
fun TeamListHeader(onRefresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBackgroundBrushTeamList)
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .zIndex(1f), // Ensure header is on top if there's any overlap potential
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image( // Using the example's logo resource
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
fun TeamListSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    isSearching: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = {
            Text(
                "Search teams...",
                color = secondaryTextColorTeamList.copy(alpha = 0.7f)
            )
        },
        leadingIcon = {
            if (isSearching) CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = primaryAppColorTeamList,
                strokeWidth = 2.dp
            )
            else Icon(
                Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = secondaryTextColorTeamList
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear Search",
                        tint = secondaryTextColorTeamList
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = textFieldBackgroundColorTeamList,
            unfocusedContainerColor = textFieldBackgroundColorTeamList,
            disabledContainerColor = textFieldBackgroundColorTeamList,
            cursorColor = primaryAppColorTeamList,
            focusedBorderColor = primaryAppColorTeamList,
            unfocusedBorderColor = cardBackgroundColorTeamList, // Or a subtle gray
        ),
        shape = defaultCornerShapeTeamList, // Consistent rounded corners
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun TeamListBody(
    teams: List<Team>,
    onEditClick: (Team) -> Unit,
    onDeleteClick: (Team) -> Unit,
    getImageUrl: (String) -> String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp), // Horizontal padding is on the parent Box
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(teams, key = { it.TeamId }) { team ->
            TeamCardItem(
                team = team,
                onEditClick = { onEditClick(team) },
                onDeleteClick = { onDeleteClick(team) },
                imageUrl = getImageUrl(team.image)
            )
        }
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
                    // Reminder: Replace R.drawable.img_placeholder_team with your actual placeholder
                    placeholder = painterResource(id = R.drawable.__t_33pnkrfv_vlnxkbrsnya),
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
fun ErrorState(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Add padding to the Box
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.ErrorOutline,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Oops! Something went wrong.", // More user-friendly
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error, // The specific error message
                color = Color.LightGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5A5AFF)
                ),
                shape = RoundedCornerShape(12.dp)
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Inbox, // Example Icon
                contentDescription = "No teams found",
                tint = Color.Gray,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Teams Yet",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap the '+' button to create your first team!",
                color = Color.Gray,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
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
fun NoSearchResultsStateTeamList(searchQuery: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Filled.SearchOff,
                contentDescription = "No results",
                tint = secondaryTextColorTeamList,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No results for \"$searchQuery\"",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Try different keywords or check spelling.",
                color = secondaryTextColorTeamList,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}