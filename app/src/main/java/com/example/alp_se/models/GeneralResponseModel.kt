package com.example.alp_se.models

data class GeneralResponseModel(
    val message: String? = null,
    val data: TournamentResponse? = null // ⭐ THIS MUST BE TournamentResponse? to match the JSON object ⭐
)