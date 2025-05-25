//NavigationBarCard.kt
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alp_se.R
import com.example.alp_se.view.HomeView
//import com.example.alp_se.view.ProfileScreen
import com.example.alp_se.view.TournamentView
import com.example.alp_se.view.TeamView
import com.example.alp_se.view.CreateTeamView
import com.example.alp_se.viewModels.TournamentViewModel
import com.example.alp_se.viewModels.TeamViewModel

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf("Home", "News", "Tournament", "Team", "Profile")
    val routes = listOf(
        Screen.Home.route,
        "News", // Keep as string until NewsScreen is implemented
        Screen.Tournament.route,
        Screen.Team.route,
        Screen.Profile.route
    )
    val icons = listOf(
        R.drawable.baseline_home_filled_24,
        R.drawable.baseline_search_24,
        R.drawable.champion,
        R.drawable.baseline_groups_24,
        R.drawable.baseline_person_24
    )
    val activeColor = Color(0xFFFFC107) // Yellow color for active item
    val inactiveColor = Color(0xFF6B90B6) // Grey color for inactive item

    // Get current route to determine selected item
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedItem = routes.indexOfFirst { it == currentRoute }.takeIf { it >= 0 } ?: 0

    NavigationBar(containerColor = Color(0xFF222222)) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    val targetRoute = routes[index]
                    navController.navigate(targetRoute) {
                        // Pop up to the start destination to avoid building up a large stack
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = item,
                        tint = if (selectedItem == index) activeColor else inactiveColor
                    )
                },
                label = {
                    Text(
                        text = item,
                        color = if (selectedItem == index) activeColor else inactiveColor
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
    tournamentViewModel: TournamentViewModel,
    teamViewModel: TeamViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "Home",
        modifier = modifier
    ) {
        composable("Home") {
            HomeView(navController = navController)
        }

        // Uncomment when NewsScreen is implemented
        // composable("News") {
        //     NewsScreen(navController = navController)
        // }

        composable("Tournament") {
            TournamentView(
                navController = navController,
                tournamentViewModel = tournamentViewModel
            )
        }

        composable("Team") {
            TeamView(
                navController = navController,
                teamViewModel = teamViewModel
            )
        }

//        composable("Profile") {
//            ProfileScreen(navController = navController)
//        }
    }
}