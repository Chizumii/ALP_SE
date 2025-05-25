package com.example.alp_se.models

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("TeamId")
    val teamId: Int,

    @SerializedName("namatim")
    val namatim: String,

    @SerializedName("image")
    val image: String
)

data class TeamResponse(
    val message: String,
    val data: Team
)

data class TeamsResponse(
    val message: String,
    val data: List<Team>
)

data class CreateTeamRequest(
    val namatim: String
    // Note: image will be handled as multipart in the repository
)

data class UpdateTeamRequest(
    val namatim: String
    // Note: image will be handled as multipart in the repository
)