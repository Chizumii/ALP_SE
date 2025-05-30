package com.example.alp_se.navigation

sealed class Screen(val route: String) {
    object News : Screen("News")
    object Tournament : Screen("Tournament")
    object Home : Screen("Home")
    object TournamentCreate: Screen("tournamentCreate")
//    object TournamentDetail : Screen("tournament_detail/{TournamentID}") {
//        fun createRoute(TournamentID: Int) = "tournament_detail/$TournamentID"
//    }
    object TournamentSubmit : Screen("tournament_submit/{TournamentID}") {
        fun createRoute(TournamentID: Int) = "tournament_submit/$TournamentID"
    }
    object Team : Screen("team")
    object TeamCreate : Screen("team_create")
    object TeamEdit : Screen("team_edit/{teamId}") {
        fun createRoute(teamId: Int) = "team_edit/$teamId"
    }
    object Profile: Screen("Profile")
}