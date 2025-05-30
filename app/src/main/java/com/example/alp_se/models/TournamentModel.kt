package com.example.alp_se.models

// TournamentModel.kt
// Data class untuk membuat tournament baru
data class TournamentModel(
    val id: Int,
    val nama_tournament: String,
    val description: String,
    val image: String,
    val tipe: String,
    val biaya: Double,
)

data class CreateTournament(
    val nama_tournament: String,
    val description: String,
    val image: String,
    val tipe: String,
    val biaya: Int,
    val lokasi: String
)


data class listTournament(
    val data: List<TournamentResponse>
)

// Data class untuk respons tournament
data class TournamentResponse(
    val TournamentID: Int = 0,
    val nama_tournament: String = "",
    val description: String = "",
    val image: String = "",
    val tipe: String = "",
    val biaya: Int = 0,
    val lokasi: String = ""
)

// Data class untuk permintaan tournament (misalnya, saat mengambil detail tertentu)
data class TournamentRequest(
    val nama_tournament: String,
    val description: String,
    val image: String,
    val tipe: String,
    val biaya: Double,
    val lokasi: String
)

// Data class untuk memperbarui tournament
data class UpdateTournament(
    val namaTournament: String = "",
    val description: String = "",
    val image: String = "",
    val tipe: String = "",
        val biaya: Double = 0.0,
    val lokasi: String = ""
)

// Data class untuk menghapus tournament
data class DeleteTournament(
    val namaTournament: String = "",
    val description: String = "",
    val image: String = "",
    val tipe: String = "",
    val biaya: Double = 0.0,
    val lokasi: String = ""
)

// Data class untuk mendapatkan semua tournament
data class GetAllTournament(
    val namaTournament: String = "",
    val description: String = "",
    val image: String = "",
    val tipe: String = "",
    val biaya: Double = 0.0,
    val lokasi: String = ""
)






