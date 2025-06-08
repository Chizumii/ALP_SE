package com.example.alp_se.repositories

import com.example.alp_se.models.*
import com.example.alp_se.services.TeamApiService
import okhttp3.MultipartBody
import retrofit2.Response
import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

interface TeamRepository {
    suspend fun createTeam(
        context: Context,
        namatim: String,
        image: Uri
    ): Response<TeamResponse>

    suspend fun getAllTeams(token: String): Response<TeamsResponse>

    suspend fun updateTeam(
        context: Context,
        id: Int,
        namatim: String,
        image: Uri?
    ): Response<TeamResponse>

    suspend fun deleteTeam(id: Int, token: String): Response<TeamResponse>
}

class NetworkTeamRepository(
    private val teamApiService: TeamApiService
) : TeamRepository {

    override suspend fun createTeam(
        context: Context,
        namatim: String,
        image: Uri
    ): Response<TeamResponse> {
        val namatimBody = namatim.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = createImagePart(image, context, "image")
        val token = "7a1ce296-ab8e-40ce-bce8-add67c22d965"
        return teamApiService.createTeam(token, namatimBody, imagePart)
    }

    override suspend fun getAllTeams(token: String): Response<TeamsResponse> {
        val apiToken = "7a1ce296-ab8e-40ce-bce8-add67c22d965"
        return teamApiService.getAllTeams(apiToken)
    }

    override suspend fun updateTeam(
        context: Context,
        id: Int,
        namatim: String,
        image: Uri?
    ): Response<TeamResponse> {
        val namatimBody = namatim.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = image?.let { createImagePart(it, context, "image") }
        val token = "7a1ce296-ab8e-40ce-bce8-add67c22d965"
        return teamApiService.updateTeam(token, id, namatimBody, imagePart!!)
    }

    override suspend fun deleteTeam(id: Int, token: String): Response<TeamResponse> {
        val apiToken = "7a1ce296-ab8e-40ce-bce8-add67c22d965"
        return teamApiService.deleteTeam(apiToken, id)
    }

    private fun createImagePart(uri: Uri, context: Context, partName: String): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream: InputStream = contentResolver.openInputStream(uri)
            ?: throw Exception("Unable to open image stream")

        val mimeType = contentResolver.getType(uri)
        val extension = when (mimeType) {
            "image/jpeg" -> ".jpg"
            "image/png" -> ".png"
            "image/gif" -> ".gif"
            else -> ".jpg"
        }

        val tempFile = File.createTempFile("upload_image", extension, context.cacheDir)
        FileOutputStream(tempFile).use { outputStream ->
            inputStream.use { it.copyTo(outputStream) }
        }

        val requestFile = tempFile.asRequestBody(mimeType?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
    }
}