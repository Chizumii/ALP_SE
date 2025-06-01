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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun TournamentView(
    navController: NavController,
    tournamentViewModel: TournamentViewModel,
    token: String
) {

    val tournament by tournamentViewModel.tounament.collectAsState()
    val searchText = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        tournamentViewModel.fetchTournaments(
            token
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF222222))
    ) {
        // Navbar atas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color(0XFF222222))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF222222)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo di kiri
                    Image(
                        painter = painterResource(R.drawable.community_removebg_preview),
                        contentDescription = "Logo",
                        modifier = Modifier.size(80.dp)
                    )

                    // Spacer untuk mengambil ruang kosong antara logo kiri dan tombol kanan
                    Spacer(modifier = Modifier.weight(1f))

                    // Box untuk tombol di kanan
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clickable { tournamentViewModel.openCreate(navController) }, // Handle click action here
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.baseline_add_circle_outline_24),
                            contentDescription = "Logo Button"
                        )
                    }
                }

                // Menampilkan teks di tengah bawah
                Text(
                    text = "Tournament",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center) // Pastikan teks berada di tengah
                )
            }
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }



        // LazyColumn dengan padding untuk menyesuaikan posisi
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 82.dp,
                    start = 16.dp,
                    end = 16.dp
                ) // Beri ruang untuk navbar atas dan bawah
        ) {
            item {
                SearchBar(searchText.value) { newText ->
                    searchText.value = newText
                }

            }
            val filteredTournaments = tournament.filter {
                it.nama_tournament.contains(searchText.value, ignoreCase = true)
            }

            if (filteredTournaments.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth() // Takes the full width of the LazyColumn item
                            .padding(vertical = 32.dp), // Add some vertical padding for spacing
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchText.value.isNotEmpty()) {
                                "No tournaments found matching \"${searchText.value}\"."
                            } else if (tournament.isEmpty()) { // Check original list if search is empty
                                "There are no tournaments available at the moment."
                            } else { // Original list has items, but filter (even if empty search) results in none
                                "No tournaments found."
                            },
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(
                    items = filteredTournaments,
                    // key = { tournament -> tournament.id } // Optional: if your tournament object has a stable ID
                ) { tournamentItem ->
                    TournamentCard(
                        tournament = tournamentItem, // Pass the individual tournament item
                        tournamentViewModel = tournamentViewModel,
                        navController = navController
                    )
                }
            }

        }
    }
}

@Composable

fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = 20.dp, horizontal = 10.dp)
            .width(380.dp)
            .height(47.dp)

            .background(Color(0xFF2D2D2D), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                // Handle search action (currently placeholder)
            })
        )
        if (searchText.isEmpty()) {
            Text(
                text = "Search",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(400),
                    color = Color.White,
                )
            )
        }
    }
}


