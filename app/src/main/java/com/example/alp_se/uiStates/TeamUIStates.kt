package com.example.alp_se.uiStates

import com.example.alp_se.models.TeamResponse

sealed interface TeamDataStatusUIState {
    data class Success(val data: List<TeamResponse>) : TeamDataStatusUIState
    object Start : TeamDataStatusUIState
    object Loading : TeamDataStatusUIState
    data class Failed(val errorMessage: String) : TeamDataStatusUIState
}