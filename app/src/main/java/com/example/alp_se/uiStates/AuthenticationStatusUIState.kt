package com.example.alp_se.uiStates

import com.example.alp_se.models.UserResponse

sealed interface AuthenticationStatusUIState {
    data class Success(val userModelData: UserResponse): AuthenticationStatusUIState
    object Loading: AuthenticationStatusUIState
    object Start: AuthenticationStatusUIState
    data class Failed(val errorMessage: String): AuthenticationStatusUIState
}