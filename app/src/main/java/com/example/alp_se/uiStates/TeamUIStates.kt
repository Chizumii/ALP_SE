package com.example.alp_se.uiStates

import com.example.alp_se.models.Team

data class TeamUIState(
    val teams: List<Team> = emptyList(),
    val filteredTeams: List<Team> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val selectedTeam: Team? = null,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val createSuccess: Boolean = false,
    val updateSuccess: Boolean = false,
    val deleteSuccess: Boolean = false
) {
    val displayTeams: List<Team>
        get() = if (searchQuery.isBlank()) teams else filteredTeams

    val hasTeams: Boolean
        get() = teams.isNotEmpty()

    val hasFilteredResults: Boolean
        get() = filteredTeams.isNotEmpty()

    val showEmptyState: Boolean
        get() = !isLoading && teams.isEmpty() && error == null

    val showNoSearchResults: Boolean
        get() = !isLoading && searchQuery.isNotBlank() && filteredTeams.isEmpty()

    val isOperationInProgress: Boolean
        get() = isCreating || isUpdating || isDeleting
}