package com.example.alp_se.services

import com.example.alp_se.models.GeneralResponseModel
import com.example.alp_se.models.TournamentRequest
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.models.listTournament
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TournamentServiceApi {


    @POST("/api/tournament")
    fun createTournament(
        @Body request: TournamentRequest
    ): Call<GeneralResponseModel>


    @GET("/api/tournament")
    suspend fun getAllTournament(
//        @Header("X-API-TOKEN") token: String,
        @Query("LokasiID") lokasiID: Int
    ): Response<listTournament>


    @GET("/api/tournament/{id}")
    fun getTournamentById(
        @Header("X-API-TOKEN") token: String, @Path("id") id: Int
    ): Call<TournamentResponse>

    @PUT("/api/tournament/{id}")
    fun updateTournament(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: String,
        @Body request: String,
        image: String,
        tipe: String,
        biaya: String,
        LokasiID: Int
    ): Call<TournamentResponse>

    @DELETE("/api/tournaments/{id}")
    fun deleteTournament(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: String,
        description: String,
        image: String,
        tipe: String,
        biaya: String,
        lokasiID: Int
    ): Call<TournamentResponse>
}
