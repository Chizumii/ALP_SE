//AppRouting.kt
package com.example.alp_se.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.alp_se.view.CreateTournament
import com.example.alp_se.view.HomeView
import com.example.alp_se.view.TeamView
import com.example.alp_se.view.CreateTeamView
import com.example.alp_se.view.TournamentDetailView
import com.example.alp_se.view.TournamentTeamSubmit
import com.example.alp_se.view.TournamentView
import com.example.alp_se.viewModels.TeamViewModel
import com.example.alp_se.viewModels.TournamentViewModel


@Composable
fun AppRouting(
    tournamentViewModel: TournamentViewModel = viewModel(factory = TournamentViewModel.Factory),
    teamViewModel: TeamViewModel = viewModel()
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Tournament.route,
        Screen.Team.route,
        Screen.Profile.route
    )

    Scaffold(
        // Show BottomNavigationBar only if current route is in bottomNavRoutes
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route, // Set the starting screen
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home Screen
            composable(Screen.Home.route) {
                HomeView(navController)
            }

            // Tournament Screens
            composable(Screen.Tournament.route) {
                TournamentView(navController, tournamentViewModel)
            }

            composable(Screen.TournamentCreate.route) {
                CreateTournament(navController, tournamentViewModel)
            }

            composable(
                route = Screen.TournamentDetail.route,
                arguments = listOf(
                    navArgument("TournamentID") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val TournamentID = backStackEntry.arguments?.getInt("TournamentID")
                val tournament =
                    tournamentViewModel.tounament.collectAsState().value.find { it.TournamentID == TournamentID }

                tournament?.let {
                    TournamentDetailView(tournament = it, navController)
                }
            }

            composable(
                route = Screen.TournamentSubmit.route,
                arguments = listOf(
                    navArgument("TournamentID") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val TournamentID = backStackEntry.arguments?.getInt("TournamentID")
                val tournament =
                    tournamentViewModel.tounament.collectAsState().value.find { it.TournamentID == TournamentID }

                tournament?.let {
                    TournamentTeamSubmit(tournament = it, navController)
                }
            }

            // Team Screens
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

            // Uncomment when ProfileScreen is implemented
            // composable(Screen.Profile.route) {
            //     ProfileScreen(navController)
            // }
        }
    }
}