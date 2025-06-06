package com.example.alp_se.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.alp_se.EshypeApplication
import com.example.alp_se.enums.PagesEnum
import com.example.alp_se.models.ErrorModel
import com.example.alp_se.models.GeneralResponseModel
import com.example.alp_se.models.TournamentResponse
import com.example.alp_se.navigation.Screen
import com.example.alp_se.repositories.TournamentRepository
import com.example.alp_se.uiStates.StringDataStatusUIState
import com.example.alp_se.uiStates.TournamentDataStatusUIState
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.math.log

class TournamentViewModel(
    private val tournamentRepository: TournamentRepository
) : ViewModel() {

    var submissionStatus: StringDataStatusUIState by mutableStateOf(StringDataStatusUIState.Start)
        private set

    var nameTournamentInput by mutableStateOf("")

    var descriptionInput by mutableStateOf("")

    var imageInput by mutableStateOf("")

    var typeInput by mutableStateOf("")

    var costInput by mutableStateOf(0)

    var lokasiInput by mutableStateOf("")

    var role by mutableStateOf("")

    var currentTournament: TournamentResponse? by mutableStateOf(null)

    // Registration status management
    private val _registrationStatusMap = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val registrationStatusMap: StateFlow<Map<Int, Boolean>> = _registrationStatusMap.asStateFlow()

    var registrationLoading by mutableStateOf(false)
        private set

    private val _tournament = MutableStateFlow<MutableList<TournamentResponse>>(mutableListOf())

    val tounament: StateFlow<List<TournamentResponse>>
        get() {
            return _tournament.asStateFlow()
        }

    fun updateCurrentTournament(input: TournamentResponse){
        currentTournament = input
    }

    fun updateNameTournamentInput(input: String) {
        nameTournamentInput = input
    }

    fun updateDescriptionInput(input: String) {
        descriptionInput = input
    }

    fun updateImageInput(input: String) {
        imageInput = input
    }

    fun updateLokasiInput(input: String) {
        lokasiInput = input
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as EshypeApplication)
                val tournamentRepository = application.container.tournamentRepository
                TournamentViewModel(tournamentRepository)
            }
        }
    }

    fun fetchTournaments(
        token: String
    ) {
        viewModelScope.launch {
            try {
                val response = tournamentRepository.getALLTournament(
                    token
                )
                if (response.isSuccessful) {
                    val tournament = response.body()?.data
                    if (tournament!= null) {
                        _tournament.value = tournament.toMutableList()
                    } else {
                        _tournament.value = mutableListOf()
                    }

                } else {
                    Log.e("TournamentPageViewModel", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TournamentPageViewModel", "Error fetching activities: ${e.message}")
                // Handle network or unexpected errors
            }
        }
    }

    fun openCreate(
        navController: NavController
    ){
        currentTournament = null
        initializeForEdit(null)
        navController.navigate("tournamentCreate")
    }

    fun openUpdate(
        navController: NavController,
        tournament: TournamentResponse
    ){
        Log.d("TournamentViewModel", "Opening update for tournament: ${tournament.nama_tournament}")
        updateCurrentTournament(tournament)
        initializeForEdit(tournament)
        navController.navigate("tournamentCreate")

    }

    fun initializeForEdit(tournament: TournamentResponse?) {
        tournament?.let {
            nameTournamentInput = it.nama_tournament
            descriptionInput = it.description
            imageInput = it.image // Assuming this is the relative path from backend
            typeInput = it.tipe
            costInput = it.biaya.toInt() // ⭐ Convert Double from backend to Int for ViewModel ⭐
            lokasiInput = it.lokasi
        } ?: run {
            nameTournamentInput = ""
            descriptionInput = ""
            imageInput = ""
            typeInput = ""
            costInput = 0
            lokasiInput = ""
        }
    }


    fun checkRegistrationStatus(tournamentId: Int, token: String) {
        viewModelScope.launch {
            try {
                registrationLoading = true
                val response = tournamentRepository.checkRegistrationStatus(tournamentId, token)

                if (response.isSuccessful) {
                    val registrationData = response.body()?.data
                    registrationData?.let {
                        val currentMap = _registrationStatusMap.value.toMutableMap()
                        currentMap[tournamentId] = it.isRegistered
                        _registrationStatusMap.value = currentMap
                        Log.d("TournamentViewModel", "Registration status for tournament $tournamentId: ${it.isRegistered}")
                    }
                } else {
                    Log.e("TournamentViewModel", "Failed to check registration status: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TournamentViewModel", "Error checking registration status: ${e.message}")
            } finally {
                registrationLoading = false
            }
        }
    }

    // ADD THIS NEW METHOD that your TournamentTeamSubmit screen is calling
    fun registerTeamWithId(
        tournamentId: Int,
        teamId: Int,
        token: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                registrationLoading = true
                val response = tournamentRepository.registerTeamWithId(tournamentId, teamId, token)

                if (response.isSuccessful) {
                    // Update local state
                    val currentMap = _registrationStatusMap.value.toMutableMap()
                    currentMap[tournamentId] = true
                    _registrationStatusMap.value = currentMap

                    Log.d("TournamentViewModel", "Successfully registered team $teamId for tournament $tournamentId")
                    onSuccess()
                } else {
                    val errorMessage = try {
                        val errorBody = response.errorBody()?.string()
                        val errorModel = Gson().fromJson(errorBody, ErrorModel::class.java)
                        errorModel.errors ?: "Failed to register team for tournament"
                    } catch (e: Exception) {
                        "Failed to register team for tournament"
                    }
                    Log.e("TournamentViewModel", "Team registration failed: $errorMessage")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Network error occurred"
                Log.e("TournamentViewModel", "Error registering team for tournament: $errorMessage")
                onError(errorMessage)
            } finally {
                registrationLoading = false
            }
        }
    }


    fun createTournament(
        navController: NavController,
        token: String,
        context: Context

    ) {
        viewModelScope.launch {
            try {
                val imageUri = if (imageInput.isNotEmpty()) Uri.parse(imageInput) else null

                val call = tournamentRepository.createTournament(
                    context = context, // Pass context to repository
                    nama_tournament = nameTournamentInput,
                    description = descriptionInput,
                    imageUri = imageUri, // Pass Uri directly
                    tipe = typeInput,
                    biaya = costInput,
                    lokasi = lokasiInput,
                    token = token
                )

                call.enqueue(object : Callback<GeneralResponseModel> {
                    override fun onResponse(
                        call: Call<GeneralResponseModel>,
                        response: Response<GeneralResponseModel>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("TournamentViewModel", "Tournament created successfully")
                            initializeForEdit(null)
                            navController.navigate("Tournament") {
                                popUpTo("tournamentCreate") { inclusive = true }
                            }
                        }else{
                            val errorMessage = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ErrorModel::class.java
                            )
                            Log.e("API Response", "Error: ${response.errorBody()}")
                            submissionStatus = StringDataStatusUIState.Failed(errorMessage.errors)
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                        submissionStatus = StringDataStatusUIState.Failed(t.localizedMessage)
                    }
                })



            } catch (error: IOException) {
                submissionStatus = StringDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun updateTournament(
        navController: NavController,
        tournamentId: Int, // The ID of the tournament to update
        token: String,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                // If the imageInput is a URL (from backend) or empty, we don't convert it to Uri
                // Only convert to Uri if it's a new image selected from gallery (content:// URI)
                val imageUri =
                    if (imageInput.startsWith("http://192.168.88.32:3000/")) Uri.parse(imageInput) else null

                val call = tournamentRepository.updateTournament(
                    context = context,
                    tournamentId = tournamentId,
                    nama_tournament = nameTournamentInput,
                    description = descriptionInput,
                    imageUri = imageUri, // Pass null if no new image selected
                    tipe = typeInput,
                    biaya = costInput,
                    lokasi = lokasiInput,
                    token = token
                )

                val response = suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<GeneralResponseModel> {
                        override fun onResponse(
                            call: Call<GeneralResponseModel>,
                            response: Response<GeneralResponseModel>
                        ) {
                            if (response.isSuccessful) {
                                currentTournament = null
                                initializeForEdit(null)
                                navController.navigate("Tournament") {
                                    popUpTo("tournamentCreate") { inclusive = true }
                                }
                            } else {
                                submissionStatus = StringDataStatusUIState.Failed(
                                    response.errorBody()?.string() ?: "Failed to update tournament."
                                )
                            }
                            continuation.resume(response)
                        }

                        override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                            submissionStatus = StringDataStatusUIState.Failed(t.localizedMessage)
                            continuation.resumeWith(Result.failure(t))
                        }
                    })
                    continuation.invokeOnCancellation {
                        call.cancel()
                    }
                }

                if (response.isSuccessful) {
                    navController.navigate("Tournament") {
//                        popUpTo("CreateTournament") { inclusive = true } // Or popUpTo TournamentDetail if you have one
                    }
                }
            } catch (error: IOException) {
                submissionStatus = StringDataStatusUIState.Failed(error.localizedMessage)
            } catch (e: IllegalArgumentException) {
                submissionStatus = StringDataStatusUIState.Failed(e.localizedMessage)
            } catch (e: SecurityException) {
                submissionStatus = StringDataStatusUIState.Failed(e.localizedMessage)
            }
        }
    }

    fun deleteTournament(
        navController: NavController,
        tournamentId: Int,
        token: String
    ){
        viewModelScope.launch {
            try {
                val call = tournamentRepository.deleteTournament(
                    id = tournamentId,
                    token = token
                )

                val response = suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<GeneralResponseModel> {
                        override fun onResponse(
                            call: Call<GeneralResponseModel>,
                            response: Response<GeneralResponseModel>
                        ) {
                            if (response.isSuccessful) {
                                currentTournament = null
                                initializeForEdit(null)
                                navController.navigate("Tournament") {
                                    popUpTo("tournamentCreate") { inclusive = true }
                                }
                            } else {
                                submissionStatus = StringDataStatusUIState.Failed(
                                    response.errorBody()?.string() ?: "Failed to update tournament."
                                )
                            }
                            continuation.resume(response)
                        }

                        override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                            submissionStatus = StringDataStatusUIState.Failed(t.localizedMessage)
                            continuation.resumeWith(Result.failure(t))
                        }
                    })
                    continuation.invokeOnCancellation {
                        call.cancel()
                    }
                }

                if (response.isSuccessful) {
                    navController.navigate("Tournament") {
                    }
                }
            } catch (error: IOException) {

            }
        }
    }

}
