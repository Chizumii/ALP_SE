package com.example.alp_se.repositories


import com.example.alp_se.models.UserResponse
import com.example.alp_se.services.ProfileService
import retrofit2.Call

interface ProfileRepository {
    fun getUserProfile(token: String, id: Int): Call<UserResponse>

    fun createUserProfile(token: String, username: String?, email: String?, password: String?, namaDepan: String?, namaBelakang: String?, nomorTelp: String?, nicknameGame: String?, teamID: Int?): Call<UserResponse>

    fun updateUserProfile(token: String, id: Int, username: String?, email: String?, password: String?, namaDepan: String?, namaBelakang: String?, nomorTelp: String?, nicknameGame: String?, teamID: Int?): Call<UserResponse>

    fun deleteUserProfile(token: String, id: Int): Call<UserResponse>
}

class NetworkProfileRepository(
    private val profileService: ProfileService
) : ProfileRepository {

    override fun getUserProfile(token: String, id: Int): Call<UserResponse> {
        return profileService.getUserProfile("Bearer $token", id)
    }

    override fun createUserProfile(
        token: String,
        username: String?,
        email: String?,
        password: String?,
        namaDepan: String?,
        namaBelakang: String?,
        nomorTelp: String?,
        nicknameGame: String?,
        teamID: Int?
    ): Call<UserResponse> {
        return profileService.createUserProfile(
            token,
            UserResponse(
                username = username ?: "",
                email = email ?: "",
//                password = password ?: "",
                nama_depan = namaDepan ?: "",
                nama_belakang = namaBelakang ?: "",
                nomor_telp = nomorTelp ?: "",
                nicknamegame = nicknameGame ?: "",
//                TeamID = teamID ?: 0,
//                token = null
            )
        )
    }

    override fun updateUserProfile(
        token: String,
        id: Int,
        username: String?,
        email: String?,
        password: String?,
        namaDepan: String?,
        namaBelakang: String?,
        nomorTelp: String?,
        nicknameGame: String?,
        teamID: Int?
    ): Call<UserResponse> {
        return profileService.updateUserProfile(
            token,
            id,
            UserResponse(
                username = username ?: "",
                email = email ?: "",
//                password = password ?: "",
                nama_depan = namaDepan ?: "",
                nama_belakang = namaBelakang ?: "",
                nomor_telp = nomorTelp ?: "",
                nicknamegame = nicknameGame ?: "",
//                TeamID = teamID ?: 0,
//                token = null
            )
        )
    }

    override fun deleteUserProfile(token: String, id: Int): Call<UserResponse> {
        return profileService.deleteUserProfile("Bearer $token", id)
    }
}
