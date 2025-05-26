package com.example.alp_se.models

data class TeamResponse(
    val teamID: Int,
    val namaTeam: String,
    val image: String
)

data class CreateTeamRequestPayload(
    val namaTeam: String
)

data class UpdateTeamRequestPayload(
    val namaTeam: String?
)

data class ListTeamsResponseWrapper(
    val message: String,
    val data: List<TeamResponse>
)

data class SingleTeamResponseWrapper(
    val message: String,
    val data: TeamResponse
)

data class DeleteTeamResponseWrapper(
    val message: String
)