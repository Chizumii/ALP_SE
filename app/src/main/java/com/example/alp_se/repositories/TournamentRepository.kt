package com.example.alp_se.repositories

import android.content.Context
import android.net.Uri
import com.example.alp_se.models.GeneralResponseModel
import com.example.alp_se.models.TournamentRequest
import com.example.alp_se.models.TournamentResponse
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
        context: Context, // Add context to access content resolver for Uri
        nama_tournament: String,
        description: String,
        imageUri: Uri?, // Change to Uri?
        tipe: String,
        biaya: Int,
        lokasi: String,
        token: String
    ): Call<GeneralResponseModel>

    fun updateTournament(
        context: Context,
        tournamentId: Int, // New: Pass the ID of the tournament to update
        nama_tournament: String,
        description: String,
        imageUri: Uri?, // Changed to Uri? for multipart update
        tipe: String,
        biaya: Int,
        lokasi: String,
        token: String
    ): Call<GeneralResponseModel>

    fun deleteTournament(
        nama_tournament: String,
        description: String,
        image: String,
        tipe: String,
        biaya: Double,
        lokasi: String
    ): Call<TournamentResponse>

   suspend fun getALLTournament(
        token: String,
    ): Response<listTournament>
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
        context: Context, // Added context
        nama_tournament: String,
        description: String,
        imageUri: Uri?, // Changed to Uri?
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
            // Get the file path from the Uri
            val filePath = getPathFromUri(context, uri)
            if (filePath != null) {
                val file = File(filePath)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imageFilePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
        }

        // Ensure imageFilePart is not null. If it can be null, your API needs to handle it.
        // For now, I'm assuming image is always sent.
        if (imageFilePart == null) {
            // Handle case where image URI is null or file path couldn't be resolved
            // You might throw an error or return a failed call
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
        tournamentId: Int, // Pass the ID
        nama_tournament: String,
        description: String,
        imageUri: Uri?,
        tipe: String,
        biaya: Int,
        lokasi: String,
        token: String
    ): Call<GeneralResponseModel> { // Changed to GeneralResponseModel
        val namaTournamentPart = nama_tournament.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val tipePart = tipe.toRequestBody("text/plain".toMediaTypeOrNull())
        val biayaPart = biaya.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val lokasiPart = lokasi.toRequestBody("text/plain".toMediaTypeOrNull())

        // Handle image update: only send if a new image Uri is provided
        // Otherwise, your backend update API should handle keeping the old image
        // or a separate API for image update. For now, assuming imageUri means new image.
        var imageFilePart: MultipartBody.Part? = null
        imageUri?.let { uri ->
            // Get the file path from the Uri
            val filePath = getPathFromUri(context, uri)
            if (filePath != null) {
                val file = File(filePath)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                imageFilePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            }
        }


        return tournamentServiceApi.updateTournament(
            token = token,
            id = tournamentId.toString(), // Convert Int ID to String for @Path
            nama_tournament = namaTournamentPart,
            description = descriptionPart,
            image = imageFilePart, // This will be null if no new image, backend must handle
            tipe = tipePart,
            biaya = biayaPart,
            lokasi = lokasiPart
        )
    }

    override fun deleteTournament(
        nama_tournament: String,
        description: String,
        image: String,
        tipe: String,
        biaya: Double,
        lokasi: String
    ): Call<TournamentResponse> {
        val token = "X-API-SERVICE"
        return tournamentServiceApi.deleteTournament(
            token,
            nama_tournament,
            description,
            image,
            tipe,
            biaya,
            lokasi
        )
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

