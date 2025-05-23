package com.example.alp_se.uiStates

import com.example.alp_se.models.TournamentResponse


sealed interface TournamentSubmitUIState {
    data class Success(val data: List<TournamentResponse>) : TournamentSubmitUIState
    object Start : TournamentSubmitUIState
    object Loading : TournamentSubmitUIState
    data class Error(val errorMessage: String) : TournamentSubmitUIState
}