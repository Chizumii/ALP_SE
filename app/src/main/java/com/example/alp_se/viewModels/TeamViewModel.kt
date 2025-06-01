    package com.example.alp_se.viewModels

    import android.content.Context
    import android.net.Uri
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.viewModelScope
    import androidx.lifecycle.viewmodel.initializer
    import androidx.lifecycle.viewmodel.viewModelFactory
    import com.example.alp_se.EshypeApplication
    import com.example.alp_se.models.*
    import com.example.alp_se.services.TeamService
    import kotlinx.coroutines.Job
    import kotlinx.coroutines.delay
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.launch

    class TeamViewModel(
        private val teamService: TeamService
    ) : ViewModel() {

        private val _uiState = MutableStateFlow(TeamUIState())
        val uiState: StateFlow<TeamUIState> = _uiState.asStateFlow()

        private var searchJob: Job? = null

        init {
            loadTeams()
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

        fun searchTeams(query: String) {
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

                teamService.searchTeams(query).fold(
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
                            if (team.TeamId == id) updatedTeam else team
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
                        val updatedTeams = _uiState.value.teams.filter { it.TeamId != id }
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

        companion object {
            val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EshypeApplication)
                    val teamService = TeamService(application.container.teamRepository)
                    TeamViewModel(teamService = teamService)
                }
            }
        }
    }