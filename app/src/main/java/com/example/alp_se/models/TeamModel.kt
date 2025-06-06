package com.example.alp_se.models

data class Team(
    val TeamId: Int,
    val namatim: String,
    val image: String
)

data class TeamResponse(
    val success: Boolean,
    val message: String,
    val data: Team
)

data class TeamsResponse(
    val success: Boolean,
    val message: String,
    val data: List<Team>
)

data class ErrorResponse(
    val success: Boolean = false,
    val message: String
)

