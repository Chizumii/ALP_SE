package com.example.alp_se.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alp_se.R
import com.example.alp_se.view.templates.TournamentCard
import com.example.alp_se.viewModels.TournamentViewModel

@Composable
fun TournamentListView(
    navController: NavController,
    tournamentViewModel: TournamentViewModel,
    token: String
) {
    val tournamentListState by tournamentViewModel.tounament.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var showSearchSuggestions by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    // Debounced search query
    val debouncedSearchQuery by remember(searchQuery) {
        derivedStateOf {
            searchQuery
        }
    }

    // Search suggestions based on tournament names
    val searchSuggestions = remember(tournamentListState, searchQuery) {
        if (searchQuery.length >= 2) {
            tournamentListState
                .map { it.nama_tournament }
                .filter { it.contains(searchQuery, ignoreCase = true) }
                .take(5)
        } else {
            emptyList()
        }
    }

    // Enhanced search filtering
    val filteredTournaments = remember(tournamentListState, debouncedSearchQuery) {
        if (debouncedSearchQuery.isBlank()) {
            tournamentListState
        } else {
            tournamentListState.filter {
                val query = debouncedSearchQuery.lowercase()
                it.nama_tournament.lowercase().contains(query) ||
                it.description.lowercase().contains(query) ||
                it.lokasi.lowercase().contains(query) ||
                it.tipe.lowercase().contains(query)
            }
        }
    }

    LaunchedEffect(Unit) {
        tournamentViewModel.fetchTournaments(token)
    }

    // Debounce search
    LaunchedEffect(searchQuery) {
        isSearching = true
        coroutineScope.launch {
            delay(300) // 300ms debounce
            isSearching = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0F23),
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF6C63FF),
                                Color(0xFF9C88FF),
                                Color(0xFF6C63FF)
                            )
                        )
                    )
                    .padding(vertical = 20.dp),
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
                            .size(48.dp)
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                CircleShape
                            )
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Tournaments",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { tournamentViewModel.openCreate(navController) },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Tournament",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        showSearchSuggestions = it.isNotEmpty()
                    },
                    placeholder = {
                        Text(
                            text = "Search tournaments...",
                            color = Color(0xFFB0B3B8)
                        )
                    },
                    leadingIcon = {
                        if (isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF6C63FF),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Color(0xFF6C63FF)
                            )
                        }
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { 
                                searchQuery = ""
                                showSearchSuggestions = false
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Clear Search",
                                    tint = Color(0xFFB0B3B8)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { 
                            keyboardController?.hide()
                            showSearchSuggestions = false
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF6C63FF),
                        unfocusedBorderColor = Color(0xFF404040),
                        cursorColor = Color(0xFF6C63FF),
                        focusedContainerColor = Color(0xFF2D2D3D),
                        unfocusedContainerColor = Color(0xFF2D2D3D)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Search suggestions dropdown
                if (showSearchSuggestions && searchSuggestions.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF2D2D3D),
                                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                            )
                            .padding(vertical = 8.dp)
                    ) {
                        Column {
                            searchSuggestions.forEach { suggestion ->
                                Text(
                                    text = suggestion,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            searchQuery = suggestion
                                            showSearchSuggestions = false
                                            keyboardController?.hide()
                                        }
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                                if (suggestion != searchSuggestions.last()) {
                                    HorizontalDivider(
                                        color = Color(0xFF404040),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (filteredTournaments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(R.drawable.baseline_error_outline_24),
                            contentDescription = "No tournaments",
                            modifier = Modifier.size(80.dp),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFFB0B3B8))
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) {
                                "No tournaments found for \"$searchQuery\"."
                            } else {
                                "No tournaments available yet."
                            },
                            color = Color(0xFFB0B3B8),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                        if (searchQuery.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Try adjusting your search or create a new tournament!",
                                color = Color(0xFFB0B3B8).copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            )
                        }
                    }
                }
            } else {
                Column {
                    // Search results count
                    Text(
                        text = "Found ${filteredTournaments.size} tournament${if (filteredTournaments.size != 1) "s" else ""}",
                        color = Color(0xFFB0B3B8),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
                    ) {
                        items(filteredTournaments) { tournamentItem ->
                            val currentRegistrationStatusMap by tournamentViewModel.registrationStatusMap.collectAsState()
                            val isRegistered = currentRegistrationStatusMap[tournamentItem.TournamentID] ?: false

                            LaunchedEffect(tournamentItem.TournamentID) {
                                if (!currentRegistrationStatusMap.containsKey(tournamentItem.TournamentID)) {
                                    tournamentViewModel.checkRegistrationStatus(tournamentItem.TournamentID, token)
                                }
                            }
                            TournamentCard(
                                tournament = tournamentItem,
                                tournamentViewModel = tournamentViewModel,
                                navController = navController,
                                isRegistered = isRegistered
                            )
                        }
                    }
                }
            }
        }
    }
}