    package com.example.alp_se.repositories

<<<<<<< HEAD
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
=======
import com.example.alp_se.models.Team
import com.example.alp_se.models.TeamResponse
import com.example.alp_se.models.TeamsResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface TeamApiService {
    @Multipart
    @POST("api/team")
    suspend fun createTeam(
        @Part("namatim") namatim: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<TeamResponse>

    @GET("api/team")
    suspend fun getAllTeams(): Response<TeamsResponse>

    @Multipart
    @PATCH("api/team/{id}")
    suspend fun updateTeam(
        @Path("id") id: Int,
        @Part("namatim") namatim: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<TeamResponse>

    @DELETE("api/team/{id}")
    suspend fun deleteTeam(@Path("id") id: Int): Response<TeamResponse>
}

class TeamRepository {
    private val apiService: TeamApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://your-api-base-url/") // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(TeamApiService::class.java)
    }

    suspend fun createTeam(namatim: String, image: MultipartBody.Part): Result<Team> {
        return try {
            // Fixed: Use toRequestBody() extension function instead of RequestBody.create()
            val namatimBody = namatim.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.createTeam(namatimBody, image)
            if (response.isSuccessful) {
                response.body()?.let { teamResponse ->
                    Result.success(teamResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllTeams(): Result<List<Team>> {
        return try {
            val response = apiService.getAllTeams()
            if (response.isSuccessful) {
                response.body()?.let { teamsResponse ->
                    Result.success(teamsResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTeam(id: Int, namatim: String, image: MultipartBody.Part): Result<Team> {
        return try {
            // Fixed: Use toRequestBody() extension function
            val namatimBody = namatim.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.updateTeam(id, namatimBody, image)
            if (response.isSuccessful) {
                response.body()?.let { teamResponse ->
                    Result.success(teamResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTeam(id: Int): Result<Boolean> {
        return try {
            val response = apiService.deleteTeam(id)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
>>>>>>> parent of 9bcc9b4 (Complete TeamView)
