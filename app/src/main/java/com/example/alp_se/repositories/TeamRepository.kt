package com.example.alp_se.repositories

import com.example.alp_se.models.*
import com.example.alp_se.services.TeamApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface TeamRepository {
    suspend fun createTeam(
        namatim: RequestBody,
        image: MultipartBody.Part,
        token: String
    ): Response<TeamResponse>

    suspend fun getAllTeams(token: String): Response<TeamsResponse>

    suspend fun updateTeam(
        id: Int,
        namatim: RequestBody,
        image: MultipartBody.Part,
        token: String
    ): Response<TeamResponse>

    suspend fun deleteTeam(id: Int, token: String
    ): Response<TeamResponse>

}

class NetworkTeamRepository(
    private val teamApiService: TeamApiService
) : TeamRepository {

    override suspend fun createTeam(
        namatim: RequestBody,
        image: MultipartBody.Part,
        token: String
    ): Response<TeamResponse> {
        return teamApiService.createTeam(token, namatim, image)
    }


    override suspend fun getAllTeams(token: String): Response<TeamsResponse> {
        return teamApiService.getAllTeams(token)
    }

    override suspend fun updateTeam(
        id: Int,
        namatim: RequestBody,
        image: MultipartBody.Part,
        token: String
    ): Response<TeamResponse> {
        return teamApiService.updateTeam(token, id, namatim, image)
    }

    override suspend fun deleteTeam(id: Int, token: String): Response<TeamResponse> {
        return teamApiService.deleteTeam(token, id)
    }
}
