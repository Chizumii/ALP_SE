package com.example.alp_se.services

import com.example.alp_se.models.ListTeamsResponseWrapper
import com.example.alp_se.models.SingleTeamResponseWrapper
import com.example.alp_se.models.DeleteTeamResponseWrapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.DELETE

interface TeamService {

    @Multipart
    @POST("api/team")
    fun createTeam(
        @Part("namaTeam") namaTeam: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<SingleTeamResponseWrapper>

    @GET("api/team")
    suspend fun getAllTeams(): Response<ListTeamsResponseWrapper>

    @GET("api/team/{id}")
    fun getTeamById(
        @Path("id") teamId: Int
    ): Call<SingleTeamResponseWrapper>

    @Multipart
    @PUT("api/team/{id}")
    fun updateTeam(
        @Path("id") teamId: Int,
        @Part("namaTeam") namaTeam: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<SingleTeamResponseWrapper>

    @DELETE("api/team/{id}")
    fun deleteTeam(
        @Path("id") teamId: Int
    ): Call<DeleteTeamResponseWrapper>
}