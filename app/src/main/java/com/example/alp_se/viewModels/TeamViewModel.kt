package com.example.alp_se.viewModels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.alp_se.EshypeApplication
import com.example.alp_se.models.ErrorModel
import com.example.alp_se.models.Team
import com.example.alp_se.repositories.TeamRepository
import com.example.alp_se.uiStates.TeamDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class TeamViewModel(
    private val teamRepository: TeamRepository
) : ViewModel() {

    var uiState: TeamDataStatusUIState by mutableStateOf(TeamDataStatusUIState.Start)
        private set

    private val _teamList = MutableStateFlow<List<Team>>(emptyList())
    val teamList: StateFlow<List<Team>> = _teamList.asStateFlow()

    var nameTeamInput by mutableStateOf("")
    var imageUriInput by mutableStateOf<Uri?>(null)

    var editingTeamId by mutableStateOf<Int?>(null)

    init {
        val token = "7a1ce296-ab8e-40ce-bce8-add67c22d965"
        loadTeams(token)
    }

    fun loadTeams(token: String) {
        viewModelScope.launch {
            uiState = TeamDataStatusUIState.Loading
            try {
                val response = teamRepository.getAllTeams(token)
                if (response.isSuccessful) {
                    val teams = response.body()?.data ?: emptyList()
                    _teamList.value = teams
                    uiState = TeamDataStatusUIState.Success(teams)
                } else {
                    val errorMessage = "Error fetching teams: ${response.code()}"
                    uiState = TeamDataStatusUIState.Failed(errorMessage)
                }
            } catch (e: IOException) {
                uiState = TeamDataStatusUIState.Failed("Network Error: ${e.message}")
            } catch (e: Exception) {
                uiState = TeamDataStatusUIState.Failed("An unexpected error occurred: ${e.message}")
            }
        }
    }

    fun createTeam(context: Context, navController: NavController) {
        if (nameTeamInput.isBlank()) {
            uiState = TeamDataStatusUIState.Failed("Team name cannot be empty.")
            return
        }
        if (imageUriInput == null) {
            uiState = TeamDataStatusUIState.Failed("Team image is required.")
            return
        }

        viewModelScope.launch {
            uiState = TeamDataStatusUIState.Loading
            try {
                val response = teamRepository.createTeam(context, nameTeamInput, imageUriInput!!)
                if (response.isSuccessful) {
                    clearInputs()
                    navController.popBackStack()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorModel = Gson().fromJson(errorBody, ErrorModel::class.java)
                    uiState = TeamDataStatusUIState.Failed(errorModel.errors ?: "Failed to create team.")
                }
            } catch (e: IOException) {
                uiState = TeamDataStatusUIState.Failed("Network error: ${e.message}")
            }
        }
    }

    fun updateTeam(context: Context, navController: NavController) {
        val teamId = editingTeamId
        if (teamId == null) {
            uiState = TeamDataStatusUIState.Failed("No team selected for update.")
            return
        }
        if (nameTeamInput.isBlank()) {
            uiState = TeamDataStatusUIState.Failed("Team name cannot be empty.")
            return
        }

        viewModelScope.launch {
            uiState = TeamDataStatusUIState.Loading
            try {
                val response = teamRepository.updateTeam(context, teamId, nameTeamInput, imageUriInput)
                if (response.isSuccessful) {
                    clearInputs()
                    navController.popBackStack()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorModel = Gson().fromJson(errorBody, ErrorModel::class.java)
                    uiState = TeamDataStatusUIState.Failed(errorModel.errors ?: "Failed to update team.")
                }
            } catch (e: IOException) {
                uiState = TeamDataStatusUIState.Failed("Network error: ${e.message}")            }
        }
    }

    fun deleteTeam(id: Int, token: String) {
        viewModelScope.launch {
            uiState = TeamDataStatusUIState.Loading
            try {
                val response = teamRepository.deleteTeam(id, token)
                if (response.isSuccessful) {
                    // Muat ulang daftar tim setelah berhasil menghapus
                    loadTeams(token)
                } else {
                    val errorMessage = "Failed to delete team: ${response.code()}"
                    uiState = TeamDataStatusUIState.Failed(errorMessage)
                }
            } catch (e: IOException) {
                uiState = TeamDataStatusUIState.Failed("Network Error: ${e.message}")
            }
        }
    }
    fun initializeForEdit(team: Team?) {
        if (team != null) {
            editingTeamId = team.TeamId
            nameTeamInput = team.namatim

            imageUriInput = null
        } else {
            clearInputs()
        }
    }

    private fun clearInputs() {
        editingTeamId = null
        nameTeamInput = ""
        imageUriInput = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EshypeApplication)
                val teamRepository = application.container.teamRepository
                TeamViewModel(teamRepository = teamRepository)
            }
        }
    }
}