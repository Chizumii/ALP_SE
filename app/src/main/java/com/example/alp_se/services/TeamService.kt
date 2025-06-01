package com.example.alp_se.services

import com.example.alp_se.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import android.content.Context
import android.net.Uri
import com.example.alp_se.repositories.TeamRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


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


class TeamService(
    private val teamRepository: TeamRepository

) {
    companion object {
        private const val BASE_URL = "http://192.168.253.69:3000/"
    }
    val token = "6d548eaf-e6bc-4b3b-bf07-60b20b7a1b02"
    suspend fun createTeam(namatim: String, imageUri: Uri, context: Context): Result<Team> {
        return try {
            val namatimBody = namatim.toRequestBody("text/plain".toMediaTypeOrNull())
            val imagePart = createImagePart(imageUri, context)
            val response = teamRepository.createTeam(namatimBody, imagePart, token)

            if (response.isSuccessful) {
                response.body()?.let { teamResponse ->
                    // Add this logging
                    println("API Response: ${teamResponse.data}")
                    println("Team ID from API: ${teamResponse.data.TeamId}")

                    Result.success(teamResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = parseErrorResponse(response)
                Result.failure(Exception("HTTP ${response.code()}: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllTeams(): Result<List<Team>> {
        return try {
            val response = teamRepository.getAllTeams(token)
            if (response.isSuccessful) {
                response.body()?.let { teamsResponse ->
                    Result.success(teamsResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = parseErrorResponse(response)
                Result.failure(Exception("HTTP ${response.code()}: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTeam(id: Int, namatim: String, imageUri: Uri, context: Context): Result<Team> {
        return try {
            val namatimBody = namatim.toRequestBody("text/plain".toMediaTypeOrNull())
            val imagePart = createImagePart(imageUri, context)

            val response = teamRepository.updateTeam(id, namatimBody, imagePart, token)
            if (response.isSuccessful) {
                response.body()?.let { teamResponse ->
                    Result.success(teamResponse.data)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                val errorMessage = parseErrorResponse(response)
                Result.failure(Exception("HTTP ${response.code()}: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTeam(id: Int): Result<Boolean> {
        return try {
            val response = teamRepository.deleteTeam(id, token)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                val errorMessage = parseErrorResponse(response)
                Result.failure(Exception("HTTP ${response.code()}: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchTeams(query: String): Result<List<Team>> {
        return try {
            val allTeamsResult = getAllTeams()
            allTeamsResult.fold(
                onSuccess = { teams ->
                    val filteredTeams = teams.filter { team ->
                        team.namatim.contains(query, ignoreCase = true)
                    }
                    Result.success(filteredTeams)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun createImagePart(imageUri: Uri, context: Context): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream: InputStream = contentResolver.openInputStream(imageUri)
            ?: throw Exception("Unable to open image stream")

        val mimeType = contentResolver.getType(imageUri)
        val extension = when (mimeType) {
            "image/jpeg" -> ".jpg"
            "image/png" -> ".png"
            "image/gif" -> ".gif"
            else -> ".jpg"
        }

        val tempFile = File.createTempFile("upload_image", extension, context.cacheDir)
        val outputStream = FileOutputStream(tempFile)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        val requestFile = tempFile.asRequestBody(mimeType?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", tempFile.name, requestFile)
    }

    private fun parseErrorResponse(response: retrofit2.Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (errorBody != null) {
                val gson = com.google.gson.Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                errorResponse.message
            } else {
                response.message()
            }
        } catch (e: Exception) {
            response.message()
        }
    }

    fun getImageUrl(imagePath: String): String {
        return when {
            imagePath.startsWith("http://") || imagePath.startsWith("https://") -> imagePath
            imagePath.startsWith("/") -> "$BASE_URL${imagePath.substring(1)}"
            else -> "$BASE_URL$imagePath"
        }
    }

    fun validateTeamName(namatim: String): String? {
        return when {
            namatim.isBlank() -> "Team name cannot be empty"
            namatim.length > 100 -> "Team name cannot exceed 100 characters"
            else -> null
        }
    }
}
