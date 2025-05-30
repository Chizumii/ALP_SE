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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.example.alp_se.view.CreateTournament
import com.example.alp_se.view.HomeView
<<<<<<< HEAD
import com.example.alp_se.view.NewsScreen
import com.example.alp_se.view.ProfileScreen
import com.example.alp_se.view.TeamView
=======
import com.example.alp_se.view.ProfileScreen
>>>>>>> parent of 9bcc9b4 (Complete TeamView)
import com.example.alp_se.view.TournamentDetailView
import com.example.alp_se.view.TournamentTeamSubmit
import com.example.alp_se.view.TournamentView
import com.example.alp_se.viewModels.TournamentViewModel


@Composable
fun AppRouting(
    tournamentViewModel: TournamentViewModel = viewModel(factory = TournamentViewModel.Factory)
<<<<<<< HEAD
            teamViewModel: TeamViewModel = viewModel(factory = TeamViewModel.Factory)
=======
>>>>>>> parent of 9bcc9b4 (Complete TeamView)
) {
    val token = "aa110648-5d97-4dad-926f-a076f295140f"
    val localContext = LocalContext.current
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val bottomNavRoutes = listOf(
<<<<<<< HEAD
        "News",
        "Home",
        "Tournament",
        "Profile"
    )
    Scaffold(
        containerColor = Color(0xFF222222),
=======
        Screen.Home.route,
        Screen.Tournament.route,
        Screen.Profile.route
    )
    Scaffold(
        // Tampilkan BottomNavigationBar hanya jika rute saat ini ada di bottomNavRoutes
>>>>>>> parent of 9bcc9b4 (Complete TeamView)
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier.padding(innerPadding)
        ) {
<<<<<<< HEAD
            composable("News") {
                NewsScreen(navController) // Screen to navigate to
            }

            composable("Tournament") {
                TournamentView(navController, tournamentViewModel, token) // Screen to navigate to
            }
            composable("tournamentCreate") {
                CreateTournament(navController, tournamentViewModel, localContext, token) // Screen to navigate to
=======
            composable(Screen.Tournament.route) {
                TournamentView(navController, tournamentViewModel) // Screen to navigate to
            }
            composable(Screen.TournamentCreate.route) {
                CreateTournament(navController, tournamentViewModel) // Screen to navigate to
>>>>>>> parent of 9bcc9b4 (Complete TeamView)
            }
            composable(
                "tournamentDetail/{TournamentID}"
            ) { backStackEntry ->
                val TournamentID = backStackEntry.arguments?.getString("TournamentID")
                val tournament =
                    tournamentViewModel.tounament.collectAsState().value.find { it.TournamentID == Integer.parseInt(TournamentID) }

                tournament?.let {
                    TournamentDetailView(tournament = it, navController= navController, tournamentViewModel)
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
<<<<<<< HEAD
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
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

=======
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
>>>>>>> parent of 9bcc9b4 (Complete TeamView)
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

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf( "News", "Tournament", "Team", "Profile")
    val icons = listOf(
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
