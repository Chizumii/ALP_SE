package com.example.alp_se.models

data class Team(
    val teamId: Int,
    val namatim: String,
    val image: String
)

data class TeamResponse(
    val success: Boolean,
    val message: String,
    val data: Team
)

data class TeamsResponse(
    val success: Boolean,
    val message: String,
    val data: List<Team>
)

data class ErrorResponse(
    val success: Boolean = false,
    val message: String
)

data class TeamUIState(
    val teams: List<Team> = emptyList(),
    val filteredTeams: List<Team> = emptyList(),
    val searchQuery: String = "",
    val selectedTeam: Team? = null,
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val createSuccess: Boolean = false,
    val updateSuccess: Boolean = false,
    val deleteSuccess: Boolean = false,
    val error: String? = null
) {
    val isOperationInProgress: Boolean
        get() = isCreating || isUpdating || isDeleting

    val displayTeams: List<Team>
        get() = if (searchQuery.isBlank()) teams else filteredTeams

    val showEmptyState: Boolean
        get() = teams.isEmpty() && !isLoading && error == null

    val showNoSearchResults: Boolean
        get() = searchQuery.isNotBlank() && filteredTeams.isEmpty() && !isSearching && error == null
}