package com.example.alp_se.navigation

sealed class Screen(val route: String) {
    object News : Screen("News")
    object Home : Screen("Home")
    object Tournament : Screen("Tournament")
    object TournamentCreate : Screen("tournamentCreate")
    object TournamentDetail : Screen("tournamentDetail/{TournamentID}") {
        fun createRoute(tournamentId: Int) = "tournamentDetail/$tournamentId"
    }
    object TournamentSubmit : Screen("tournamentSubmit/{TournamentID}") {
        fun createRoute(tournamentId: Int) = "tournamentSubmit/$tournamentId"
    }
    object Team : Screen("Team")
    object TeamCreate : Screen("teamCreate")
    object TeamEdit : Screen("teamEdit/{teamId}") {
        fun createRoute(teamId: Int) = "teamEdit/$teamId"
    }
    object Profile : Screen("Profile")
}