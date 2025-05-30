package com.example.alp_se.models

data class RegisterUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val IDgame: Int,
    val role: String,
)

data class LoginUserRequest(
    val email: String,
    val password: String
)

data class UpdateUserRequest(
    val nama_depan: String,
    val nama_belakang: String,
    val nomor_telp: String,
    val nicknamegame: String
)

data class UserResponse(
    val username: String,
    val email: String,
    val nama_depan: String,
    val nama_belakang: String,
    val nomor_telp: String,
    val nicknamegame: String
)