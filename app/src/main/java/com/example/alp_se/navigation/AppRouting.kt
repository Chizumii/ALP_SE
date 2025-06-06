package com.example.alp_se.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alp_se.R
import com.example.alp_se.view.*
import com.example.alp_se.viewModels.TeamViewModel
import com.example.alp_se.viewModels.TournamentViewModel

@Composable
fun AppRouting(
    tournamentViewModel: TournamentViewModel = viewModel(factory = TournamentViewModel.Factory),
    teamViewModel: TeamViewModel = viewModel(factory = TeamViewModel.Factory)
) {
    val token = "7a1ce296-ab8e-40ce-bce8-add67c22d965"
    val localContext = LocalContext.current
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        Screen.News.route,
        Screen.Home.route,
        Screen.Tournament.route,
        Screen.Team.route,
        Screen.Profile.route
    )

    Scaffold(
        containerColor = Color(0xFF222222),
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.News.route) {
                NewsScreen(navController)
            }

            composable(Screen.Home.route) {
                HomeView(navController)
            }

            composable(Screen.Tournament.route) {
                TournamentListView(navController, tournamentViewModel, token)
            }

            composable(Screen.TournamentCreate.route) {
                CreateTournament(navController, tournamentViewModel, localContext, token)
            }

            composable(
                route = Screen.TournamentDetail.route,
                arguments = listOf(
                    navArgument("TournamentID") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val tournamentIdString = backStackEntry.arguments?.getString("TournamentID")
                val tournamentId = tournamentIdString?.toIntOrNull()

                if (tournamentId != null) {
                    val tournament = tournamentViewModel.tounament.collectAsState().value
                        .find { it.TournamentID == tournamentId }

                    tournament?.let {
                        TournamentDetailView(
                            tournament = it,
                            navController = navController,
                            tournamentViewModel = tournamentViewModel,
                            token = token
                        )
                    }
                }
            }

            composable(
                route = Screen.TournamentSubmit.route,
                arguments = listOf(
                    navArgument("TournamentID") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val tournamentIdString = backStackEntry.arguments?.getString("TournamentID")
                val tournamentId = tournamentIdString?.toIntOrNull()

                if (tournamentId != null) {
                    val tournament = tournamentViewModel.tounament.collectAsState().value
                        .find { it.TournamentID == tournamentId }

                    tournament?.let {
                        TournamentTeamSubmit(
                            tournament = it,
                            navController = navController,
                            tournamentViewModel = tournamentViewModel,
                            token = token,
                            teamViewModel = teamViewModel
                        )
                    }
                }
            }

            composable(Screen.Team.route) {
                TeamView(
                    navController = navController,
                    teamViewModel = teamViewModel
                )
            }

            composable(Screen.TeamCreate.route) {
                CreateTeamView(
                    navController = navController,
                    teamViewModel = teamViewModel
                )
            }

            composable(
                route = Screen.TeamEdit.route,
                arguments = listOf(
                    navArgument("teamId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val teamId = backStackEntry.arguments?.getInt("teamId")
                CreateTeamView(
                    navController = navController,
                    teamViewModel = teamViewModel,
                    teamId = teamId
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
        }
    }
}





@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val containerColor = Color(0xFF222222)
    val selectedItemColor = Color(0xFFFFC107)
    val unselectedItemColor = Color(0xFF9E9E9E)
    val selectedIndicatorColor = selectedItemColor.copy(alpha = 0.15f)

    val itemsWithRoutes = listOf(
        Screen.News.route to "News",
        Screen.Home.route to "Home",
        Screen.Tournament.route to "Tournament",
        Screen.Team.route to "Team",
        Screen.Profile.route to "Profile"
    )


    val icons = listOf(
        R.drawable.baseline_search_24,
        R.drawable.baseline_home_filled_24,
        R.drawable.champion,
        R.drawable.baseline_groups_24,
        R.drawable.baseline_person_24
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = containerColor,
    ) {
        itemsWithRoutes.forEachIndexed { index, (route, label) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = icons[index]),
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                        fontSize = 10.sp
                    )
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedItemColor,
                    selectedTextColor = selectedItemColor,
                    unselectedIconColor = unselectedItemColor,
                    unselectedTextColor = unselectedItemColor,
                    indicatorColor = selectedIndicatorColor
                )
            )
        }
    }
}