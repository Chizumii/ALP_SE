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
import com.example.alp_se.view.ProfileScreen
import com.example.alp_se.view.TournamentDetailView
import com.example.alp_se.view.TournamentTeamSubmit
import com.example.alp_se.view.TournamentView
import com.example.alp_se.viewModels.TournamentViewModel


@Composable
fun AppRouting(
    tournamentViewModel: TournamentViewModel = viewModel(factory = TournamentViewModel.Factory)
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Tournament.route,
        Screen.Profile.route
    )
    Scaffold(
        // Tampilkan BottomNavigationBar hanya jika rute saat ini ada di bottomNavRoutes
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
            composable(Screen.Tournament.route) {
                TournamentView(navController, tournamentViewModel) // Screen to navigate to
            }
            composable(Screen.TournamentCreate.route) {
                CreateTournament(navController, tournamentViewModel) // Screen to navigate to
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
                // Get the tournament from your ViewModel's state using tournamentId
                val tournament =
                    tournamentViewModel.tounament.collectAsState().value.find { it.TournamentID == TournamentID }

                tournament?.let {
                    TournamentTeamSubmit(tournament = it, navController)
                }
            }
            composable(Screen.Home.route) {
                HomeView(navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
//            composable(Screen.TournamentDetail.route) {
//                TournamentDetailView(   tournamentViewModel) // Screen to navigate to
//            }
//            composable(
//                route = Screen.TournamentDetail.route + "/{Id}",
//                arguments = listOf(
//                    navArgument("Id") { type = NavType.IntType } // userId as an integer argument
//                )
//            ) { backStackEntry ->
//                val userId = backStackEntry.arguments?.getInt("Id")
//                requireNotNull(userId) { "Id is required to navigate to Tournament Detail" }
//
//                TournamentDetailView()
//            }
        }
    }
}