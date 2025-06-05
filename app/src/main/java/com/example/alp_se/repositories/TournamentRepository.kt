package com.example.alp_se.repositories

import android.content.Context
import android.net.Uri
import com.example.alp_se.models.GeneralResponseModel
import com.example.alp_se.models.RegistrationStatusResponse
import com.example.alp_se.models.listTournament
import com.example.alp_se.services.TournamentServiceApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File


interface TournamentRepository {
    fun createTournament(
        context: Context,
        nama_tournament: String,
        description: String,
        imageUri: Uri?,
        tipe: String,
        biaya: Int,
        lokasi: String,
        token: String
    ): Call<GeneralResponseModel>

    fun updateTournament(
        context: Context,
        tournamentId: Int,
        nama_tournament: String,
        description: String,
        imageUri: Uri?,
        tipe: String,
        biaya: Int,
        lokasi: String,
        token: String
    ): Call<GeneralResponseModel>

    fun deleteTournament(
        id: Int,
        token: String
    ): Call<GeneralResponseModel>

    suspend fun getALLTournament(
        token: String,
    ): Response<listTournament>

    suspend fun checkRegistrationStatus(
        tournamentId: Int,
        token: String
    ): Response<RegistrationStatusResponse>

    suspend fun registerTeam(
        tournamentId: Int,
        token: String
    ): Response<GeneralResponseModel>

    // ADD THIS NEW METHOD to the interface
    suspend fun registerTeamWithId(
        tournamentId: Int,
        teamId: Int,
        token: String
    ): Response<GeneralResponseModel>

    suspend fun unregisterTeam(
        tournamentId: Int,
        token: String
    ): Response<GeneralResponseModel>
}


class NetworkTournamentRepository(
    private val tournamentServiceApi: TournamentServiceApi
) : TournamentRepository {

    override suspend fun getALLTournament(
        token: String
    ): Response<listTournament> {
        return tournamentServiceApi.getAllTournament(
            token,
        )
    }

    override fun createTournament(
        context: Context,
        nama_tournament: String,
        description: String,
        imageUri: Uri?,
        tipe: String,
        biaya: Int,
        lokasi: String,
        token: String
    ): Call<GeneralResponseModel> {

        val namaTournamentPart = nama_tournament.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val tipePart = tipe.toRequestBody("text/plain".toMediaTypeOrNull())
        val biayaPart = biaya.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lokasiPart = lokasi.toRequestBody("text/plain".toMediaTypeOrNull())

        var imageFilePart: MultipartBody.Part? = null
        imageUri?.let { uri ->
            val filePath = getPathFromUri(context, uri)
            if (filePath != null) {
                val file = File(filePath)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imageFilePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
        }

        if (imageFilePart == null) {
            throw IllegalArgumentException("Image file could not be prepared for upload.")
        }

        return tournamentServiceApi.createTournament(
            token = token,
            nama_tournament = namaTournamentPart,
            description = descriptionPart,
            image = imageFilePart!!,
            tipe = tipePart,
            biaya = biayaPart,
            lokasi = lokasiPart
        )
    }

    override fun updateTournament(
        context: Context,
        tournamentId: Int,
        nama_tournament: String,
        description: String,
        imageUri: Uri?,
        tipe: String,
        biaya: Int,
        lokasi: String,
        token: String
    ): Call<GeneralResponseModel> {
        val namaTournamentPart = nama_tournament.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val tipePart = tipe.toRequestBody("text/plain".toMediaTypeOrNull())
        val biayaPart = biaya.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lokasiPart = lokasi.toRequestBody("text/plain".toMediaTypeOrNull())

        var imageFilePart: MultipartBody.Part? = null
        imageUri?.let { uri ->
            val filePath = getPathFromUri(context, uri)
            if (filePath != null) {
                val file = File(filePath)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imageFilePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
        }

        return tournamentServiceApi.updateTournament(
            token = token,
            id = tournamentId.toString(),
            nama_tournament = namaTournamentPart,
            description = descriptionPart,
            image = imageFilePart,
            tipe = tipePart,
            biaya = biayaPart,
            lokasi = lokasiPart
        )
    }

    override fun deleteTournament(
        id : Int,
        token: String
    ): Call<GeneralResponseModel> {
        return tournamentServiceApi.deleteTournament(
            token = token,
            id = id.toString()
        )
    }

    override suspend fun checkRegistrationStatus(
        tournamentId: Int,
        token: String
    ): Response<RegistrationStatusResponse> {
        return tournamentServiceApi.checkRegistrationStatus(token, tournamentId)
    }

    override suspend fun registerTeam(
        tournamentId: Int,
        token: String
    ): Response<GeneralResponseModel> {
        return tournamentServiceApi.registerTeam(token, tournamentId)
    }

    // IMPLEMENT THE NEW METHOD
    override suspend fun registerTeamWithId(
        tournamentId: Int,
        teamId: Int,
        token: String
    ): Response<GeneralResponseModel> {
        return tournamentServiceApi.registerTeamWithId(token, tournamentId, teamId)
    }

    override suspend fun unregisterTeam(
        tournamentId: Int,
        token: String
    ): Response<GeneralResponseModel> {
        return tournamentServiceApi.unregisterTeam(token, tournamentId)
    }
}

private fun getPathFromUri(context: Context, uri: Uri): String? {
    var filePath: String? = null
    val scheme = uri.scheme
    if (scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex("_data")
                if (columnIndex != -1) {
                    filePath = it.getString(columnIndex)
                }
            }
        }
    } else if (scheme == "file") {
        filePath = uri.path
    }
    return filePath
}