package com.example.alp_se.repositories

import com.example.alp_se.models.ListTeamsResponseWrapper
import com.example.alp_se.models.SingleTeamResponseWrapper
import com.example.alp_se.models.DeleteTeamResponseWrapper
import com.example.alp_se.services.TeamService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

interface TeamRepository {
    fun createTeam(
        namaTeam: String,
        imageFile: File
    ): Call<SingleTeamResponseWrapper>

    suspend fun getAllTeams(): Response<ListTeamsResponseWrapper>

    fun getTeamById(
        teamId: Int
    ): Call<SingleTeamResponseWrapper>

    fun updateTeam(
        teamId: Int,
        namaTeam: String,
        imageFile: File?
    ): Call<SingleTeamResponseWrapper>

    fun deleteTeam(
        teamId: Int
    ): Call<DeleteTeamResponseWrapper>
}

class NetworkTeamRepository(
    private val teamServiceApi: TeamService
) : TeamRepository {

    override fun createTeam(
        namaTeam: String,
        imageFile: File
    ): Call<SingleTeamResponseWrapper> {
        val namaTeamRequestBody = namaTeam.toRequestBody("text/plain".toMediaTypeOrNull())

        val imageFileRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageFileRequestBody)

        return teamServiceApi.createTeam(namaTeamRequestBody, imagePart)
    }

    override suspend fun getAllTeams(): Response<ListTeamsResponseWrapper> {
        return teamServiceApi.getAllTeams()
    }

    override fun getTeamById(
        teamId: Int
    ): Call<SingleTeamResponseWrapper> {
        return teamServiceApi.getTeamById(teamId)
    }

    override fun updateTeam(
        teamId: Int,
        namaTeam: String,
        imageFile: File?
    ): Call<SingleTeamResponseWrapper> {
        val namaTeamRequestBody = namaTeam.toRequestBody("text/plain".toMediaTypeOrNull())

        var imagePart: MultipartBody.Part? = null
        imageFile?.let {
            val imageFileRequestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            imagePart = MultipartBody.Part.createFormData("image", it.name, imageFileRequestBody)
        }

        return teamServiceApi.updateTeam(teamId, namaTeamRequestBody, imagePart)
    }

    override fun deleteTeam(
        teamId: Int
    ): Call<DeleteTeamResponseWrapper> {
        return teamServiceApi.deleteTeam(teamId)
    }
}