package com.example.alp_se.utils

sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val exception: String): Result<Nothing>()
}