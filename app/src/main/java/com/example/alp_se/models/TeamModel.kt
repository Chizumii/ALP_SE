package com.example.alp_se.models

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("TeamId")
    val teamId: Int,

    @SerializedName("namatim")
    val namatim: String,

    @SerializedName("image")
    val image: String
)

data class TeamResponse(
    val message: String,
    val data: Team
)

data class TeamsResponse(
    val message: String,
    val data: List<Team>
)

data class CreateTeamRequest(
    val namatim: String
    // Note: image will be handled as multipart in the repository
)

<<<<<<< HEAD
data class TeamUIState(
    val teams: List<Team> = emptyList(),
    val filteredTeams: List<Team> = emptyList(),
    val searchQuery: String = "",
    val selectedTeam: Team? = null,
    val isLoading: Boolean = false,
    val isSearching: Boolean = false, // Crucial for search UX
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

    // Using the more comprehensive logic from the original models.TeamUIState
    val showEmptyState: Boolean
        get() = teams.isEmpty() && !isLoading && !isSearching && error == null // Consider !isSearching too

    val showNoSearchResults: Boolean
        get() = searchQuery.isNotBlank() && filteredTeams.isEmpty() && !isLoading && !isSearching && error == null

    // Optional additions from the uiStates version if you find them cleaner
    val hasTeams: Boolean
        get() = teams.isNotEmpty()

    val hasFilteredResults: Boolean
        get() = filteredTeams.isNotEmpty()
}
=======
data class UpdateTeamRequest(
    val namatim: String
    // Note: image will be handled as multipart in the repository
)
>>>>>>> parent of 9bcc9b4 (Complete TeamView)
