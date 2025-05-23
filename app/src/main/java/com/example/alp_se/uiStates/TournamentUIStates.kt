package com.example.alp_se.uiStates

import com.example.alp_se.models.TournamentResponse

sealed interface TournamentDataStatusUIState {
    data class Success(val data: List<TournamentResponse>) : TournamentDataStatusUIState
    object Start : TournamentDataStatusUIState
    object Loading : TournamentDataStatusUIState
    data class Failed(val errorMessage: String) : TournamentDataStatusUIState
}
