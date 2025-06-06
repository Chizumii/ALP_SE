package com.example.alp_se.uiStates

import com.example.alp_se.models.Team

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
        get() = teams.isEmpty() && !isLoading && !isSearching && error == null // Consider !isSearching too

    val showNoSearchResults: Boolean
        get() = searchQuery.isNotBlank() && filteredTeams.isEmpty() && !isLoading && !isSearching && error == null

    val hasTeams: Boolean
        get() = teams.isNotEmpty()

    val hasFilteredResults: Boolean
        get() = filteredTeams.isNotEmpty()
}