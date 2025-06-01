package com.example.alp_se.services

import com.example.alp_se.models.GeneralResponseModel
import com.example.alp_se.models.RegistrationStatusResponse
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.models.listTournament
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TournamentServiceApi {

    @Multipart
    @POST("/api/tournament")
    fun createTournament(
        @Header("X-API-TOKEN") token: String,
        @Part("nama_tournament") nama_tournament: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("tipe") tipe: RequestBody,
        @Part("biaya") biaya: RequestBody,
        @Part("lokasi") lokasi: RequestBody
    ): Call<GeneralResponseModel>

    @GET("/api/tournament")
    suspend fun getAllTournament(
        @Header("X-API-TOKEN") token: String
    ): Response<listTournament>

    @GET("/api/tournament/{id}")
    fun getTournamentById(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: Int
    ): Call<TournamentResponse>

    @Multipart
    @PUT("/api/tournament/{id}")
    fun updateTournament(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: String,
        @Part("nama_tournament") nama_tournament: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("tipe") tipe: RequestBody,
        @Part("biaya") biaya: RequestBody,
        @Part("lokasi") lokasi: RequestBody
    ): Call<GeneralResponseModel>

    @DELETE("/api/tournament/{id}")
    fun deleteTournament(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: String
    ): Call<GeneralResponseModel>
}
