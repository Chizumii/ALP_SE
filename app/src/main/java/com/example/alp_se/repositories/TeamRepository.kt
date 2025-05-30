    package com.example.alp_se.repositories

    import com.example.alp_se.models.*
    import com.example.alp_se.services.TeamApiService
    import okhttp3.MultipartBody
    import okhttp3.RequestBody
    import retrofit2.Call
    import retrofit2.Response

    interface TeamRepository {
        suspend fun createTeam(
            namatim: RequestBody,
            image: MultipartBody.Part
        ): Response<TeamResponse>

        suspend fun getAllTeams(): Response<TeamsResponse>

        suspend fun updateTeam(
            id: Int,
            namatim: RequestBody,
            image: MultipartBody.Part
        ): Response<TeamResponse>

        suspend fun deleteTeam(id: Int): Response<TeamResponse>
    }

    class NetworkTeamRepository(
        private val teamApiService: TeamApiService
    ) : TeamRepository {

        override suspend fun createTeam(
            namatim: RequestBody,
            image: MultipartBody.Part
        ): Response<TeamResponse> {
            return teamApiService.createTeam(namatim, image)
        }

        override suspend fun getAllTeams(): Response<TeamsResponse> {
            return teamApiService.getAllTeams()
        }

        override suspend fun updateTeam(
            id: Int,
            namatim: RequestBody,
            image: MultipartBody.Part
        ): Response<TeamResponse> {
            return teamApiService.updateTeam(id, namatim, image)
        }

        override suspend fun deleteTeam(id: Int): Response<TeamResponse> {
            return teamApiService.deleteTeam(id)
        }
    }
