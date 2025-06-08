package com.example.alp_se.services

import com.example.alp_se.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface TeamApiService {
    @Multipart
    @POST("api/team")
    suspend fun createTeam(
        @Header("X-API-TOKEN") token: String,
        @Part("namatim") namatim: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<TeamResponse>

    @GET("api/team")
    suspend fun getAllTeams(
        @Header("X-API-TOKEN") token: String
    ): Response<TeamsResponse>

    @Multipart
    @PATCH("api/team/{id}")
    suspend fun updateTeam(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: Int,
        @Part("namatim") namatim: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<TeamResponse>

    @DELETE("api/team/{id}")
    suspend fun deleteTeam(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: Int
    ): Response<TeamResponse>
}
