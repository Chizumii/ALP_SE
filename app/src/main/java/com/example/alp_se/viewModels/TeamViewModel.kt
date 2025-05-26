package com.example.alp_se.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alp_se.models.Team
import com.example.alp_se.services.TeamService
import com.example.alp_se.repositories.NetworkTeamRepository
import com.example.alp_se.services.TeamApiService
import com.example.alp_se.uiStates.TeamUIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class TeamViewModel : ViewModel() {

    private val teamService: TeamService by lazy {
        initializeTeamService()
    }

    private val _uiState = MutableStateFlow(TeamUIState())
    val uiState: StateFlow<TeamUIState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadTeams()
    }


    private fun initializeTeamService(): TeamService {
        val baseUrl = "http://192.168.88.43:3000/"

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(baseUrl)
            .build()

        val teamApiService = retrofit.create(TeamApiService::class.java)
        val teamRepository = NetworkTeamRepository(teamApiService)

        return TeamService(teamRepository)
    }
    fun selectTeam(team: Team) {
        _uiState.value = _uiState.value.copy(selectedTeam = team)
    }

    fun clearSelectedTeam() {
        _uiState.value = _uiState.value.copy(selectedTeam = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearSuccessFlags() {
        _uiState.value = _uiState.value.copy(
            createSuccess = false,
            updateSuccess = false,
            deleteSuccess = false
        )
    }

    fun refresh() {
        loadTeams()
    }

    fun getImageUrl(imagePath: String): String {
        return teamService.getImageUrl(imagePath)
    }
    fun loadTeams() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            teamService.getAllTeams().fold(
                onSuccess = { teams ->
                    _uiState.value = _uiState.value.copy(
                        teams = teams,
                        filteredTeams = teams,
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    fun searchTeam(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    filteredTeams = _uiState.value.teams,
                    isSearching = false
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isSearching = true)

            // Add a small delay to avoid too many API calls while typing
            delay(300)

            teamService.searchTeam(query).fold(
                onSuccess = { filteredTeams ->
                    _uiState.value = _uiState.value.copy(
                        filteredTeams = filteredTeams,
                        isSearching = false
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        error = error.message ?: "Search failed"
                    )
                }
            )
        }
    }

    fun createTeam(namatim: String, imageUri: Uri, context: Context) {
        viewModelScope.launch {
            val validationError = teamService.validateTeamName(namatim)
            if (validationError != null) {
                _uiState.value = _uiState.value.copy(error = validationError)
                return@launch
            }

            _uiState.value = _uiState.value.copy(isCreating = true, error = null)

            teamService.createTeam(namatim, imageUri, context).fold(
                onSuccess = { newTeam ->
                    val updatedTeams = _uiState.value.teams + newTeam
                    _uiState.value = _uiState.value.copy(
                        teams = updatedTeams,
                        filteredTeams = updatedTeams,
                        isCreating = false,
                        createSuccess = true,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isCreating = false,
                        error = error.message ?: "Failed to create team"
                    )
                }
            )
        }
    }

    fun updateTeam(id: Int, namatim: String, imageUri: Uri, context: Context) {
        viewModelScope.launch {
            val validationError = teamService.validateTeamName(namatim)
            if (validationError != null) {
                _uiState.value = _uiState.value.copy(error = validationError)
                return@launch
            }

            _uiState.value = _uiState.value.copy(isUpdating = true, error = null)

            teamService.updateTeam(id, namatim, imageUri, context).fold(
                onSuccess = { updatedTeam ->
                    val updatedTeams = _uiState.value.teams.map { team ->
                        if (team.teamId == id) updatedTeam else team
                    }
                    _uiState.value = _uiState.value.copy(
                        teams = updatedTeams,
                        filteredTeams = updatedTeams,
                        isUpdating = false,
                        updateSuccess = true,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isUpdating = false,
                        error = error.message ?: "Failed to update team"
                    )
                }
            )
        }
    }

    fun deleteTeam(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeleting = true, error = null)

            teamService.deleteTeam(id).fold(
                onSuccess = {
                    val updatedTeams = _uiState.value.teams.filter { it.teamId != id }
                    _uiState.value = _uiState.value.copy(
                        teams = updatedTeams,
                        filteredTeams = updatedTeams,
                        isDeleting = false,
                        deleteSuccess = true,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        error = error.message ?: "Failed to delete team"
                    )
                }
            )
        }
    }


}
