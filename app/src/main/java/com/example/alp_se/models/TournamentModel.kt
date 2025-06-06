package com.example.alp_se.models


data class listTournament(
    val data: List<TournamentResponse>
)

data class TournamentResponse(
    val TournamentID: Int = 0,
    val nama_tournament: String = "",
    val description: String = "",
    val image: String = "",
    val tipe: String = "",
    val biaya: Int = 0,
    val lokasi: String = ""
)


data class RegistrationStatusResponse(
    val message: String,
    val data: RegistrationData
)

data class RegistrationData(
    val isRegistered: Boolean,
    val message: String
)






