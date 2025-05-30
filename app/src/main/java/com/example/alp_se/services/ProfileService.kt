package com.example.alp_se.services

import com.example.alp_se.models.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileService {

    @GET("users/{id}")
    fun getUserProfile(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: Int
    ): Call<UserResponse>

    @POST("users")
    fun createUserProfile(
        @Body userModel: UserResponse
    ): Call<UserResponse>

    @PUT("users/{id}")
    fun updateUserProfile(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: Int,
        @Body userModel: UserResponse
    ): Call<UserResponse>

    @DELETE("users/{id}")
    fun deleteUserProfile(
        @Header("X-API-TOKEN") token: String,
        @Path("id") id: Int
    ): Call<UserResponse>
}
