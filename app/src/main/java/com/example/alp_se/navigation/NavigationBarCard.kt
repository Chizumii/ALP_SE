package com.example.alp_se.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.alp_se.R
import com.example.alp_se.view.HomeView
import com.example.alp_se.view.ProfileScreen
import com.example.alp_se.view.TournamentView
import com.example.alp_se.viewModels.TournamentViewModel

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf("Home", "News", "Tournament", "Team", "Profile")
    val icons = listOf(
        R.drawable.baseline_home_filled_24,
        R.drawable.baseline_search_24,
        R.drawable.champion,
        R.drawable.baseline_groups_24,
        R.drawable.baseline_person_24
    )
    val activeColor = Color(0xFFFFC107) // Yellow color for active item
    val inactiveColor = Color(0xFF6B90B6) // Grey color for inactive item)

    var selectedItem by remember { mutableStateOf(0) }
    NavigationBar(containerColor = Color(0xFF222222)) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item) // Navigate to selected page
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = item
                    )
                },
                label = {
                    Text(
                        text = item,
                    )
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    tournamentViewModel: TournamentViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "Home",
        modifier = modifier
    ) {
        composable("Home") { HomeView(navController = navController) }
//        composable("News") { NewsScreen(navController = navController) }
        composable("Tournament") {
            TournamentView(
                navController = navController,
                tournamentViewModel
            )
        }
        composable("Profile") { ProfileScreen(navController = navController) }
    }
}