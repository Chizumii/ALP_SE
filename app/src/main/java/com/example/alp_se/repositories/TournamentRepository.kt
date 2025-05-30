package com.example.alp_se.repositories

import com.example.alp_se.models.GeneralResponseModel
import com.example.alp_se.models.TournamentRequest
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.models.listTournament
import com.example.alp_se.services.TournamentServiceApi
import retrofit2.Call
import retrofit2.Response


interface TournamentRepository {
    fun createTournament(
        nama_tournament: String,
        description: String,
        image: String,
        tipe: String,
        biaya: String,
        LokasiID: Int
    ): Call<GeneralResponseModel>

    fun updateTournament(
        nama_tournament: String,
        description: String,
        image: String,
        tipe: String,
        biaya: String,
        lokasiID: Int
    ): Call<TournamentResponse>

    fun deleteTournament(
        nama_tournament: String,
        description: String,
        image: String,
        tipe: String,
        biaya: String,
        LokasiID: Int
    ): Call<TournamentResponse>

   suspend fun getALLTournament(
//        token: String,
        lokasiID: Int
    ): Response<listTournament>
}


class NetworkTournamentRepository(
    private val tournamentServiceApi: TournamentServiceApi
) : TournamentRepository {

    override suspend fun getALLTournament(
//        token: String,
        lokasiID: Int
    ): Response<listTournament> {
//        val token = "X-API-SERVICE"
        return tournamentServiceApi.getAllTournament(
//            token,
            lokasiID
        )
    }

    override fun createTournament(
        nama_tournament: String,
        description: String,
        image: String,
        tipe: String,
        biaya: String,
        LokasiID: Int
    ): Call<GeneralResponseModel> {
        val request = TournamentRequest(
            nama_tournament = nama_tournament,
            description = description,
            image = image,
            tipe = tipe,
            biaya = biaya,
            LokasiID = LokasiID
        )
        return tournamentServiceApi.createTournament(request)
    }

    override fun updateTournament(
        nama_tournament: String,
        description: String,
        image: String,
        tipe: String,
        biaya: String,
        LokasiID: Int
    ): Call<TournamentResponse> {
        val token = "X-API-SERVICE"
        return tournamentServiceApi.updateTournament(
            token,
            nama_tournament,
            description,
            image,
            tipe,
            biaya,
            LokasiID
        )

    }

    override fun deleteTournament(
        nama_tournament: String,
        description: String,
        image: String,
        tipe: String,
        biaya: String,
        LokasiID: Int
    ): Call<TournamentResponse> {
        val token = "X-API-SERVICE"
        return tournamentServiceApi.deleteTournament(
            token,
            nama_tournament,
            description,
            image,
            tipe,
            biaya,
            LokasiID
        )
    }
}

