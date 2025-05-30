package com.example.alp_se.services

import android.content.Context
import android.net.Uri
import com.example.alp_se.models.Team
import com.example.alp_se.repositories.TeamRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

<<<<<<< HEAD

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


class TeamService(
    private val teamRepository: TeamRepository
) {
    companion object {
        private const val BASE_URL = "http://192.168.105.69:3000/"
    }
=======
class TeamService(private val repository: TeamRepository = TeamRepository()) {
>>>>>>> parent of 9bcc9b4 (Complete TeamView)

    suspend fun createTeam(namatim: String, imageUri: Uri, context: Context): Result<Team> {
        return try {
            val imagePart = createImagePart(imageUri, context)
            repository.createTeam(namatim, imagePart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllTeams(): Result<List<Team>> {
        return repository.getAllTeams()
    }

    suspend fun updateTeam(id: Int, namatim: String, imageUri: Uri, context: Context): Result<Team> {
        return try {
            val imagePart = createImagePart(imageUri, context)
            repository.updateTeam(id, namatim, imagePart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTeam(id: Int): Result<Boolean> {
        return repository.deleteTeam(id)
    }

    suspend fun searchTeams(query: String): Result<List<Team>> {
        return try {
            val allTeamsResult = repository.getAllTeams()
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

        // Create a temporary file
        val tempFile = File.createTempFile("upload_image", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)

        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        // Fixed: Use asRequestBody() extension function instead of RequestBody.create()
        val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("image", tempFile.name, requestFile)
    }

    fun getImageUrl(imagePath: String, baseUrl: String = "http://your-api-base-url/"): String {
        return if (imagePath.startsWith("http")) {
            imagePath
        } else {
            "${baseUrl}${imagePath}"
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