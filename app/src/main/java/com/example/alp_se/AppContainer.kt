package com.example.alp_se

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.alp_se.repositories.*
import com.example.alp_se.services.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val tournamentRepository: TournamentRepository
    val teamRepository: TeamRepository
}

class DefaultAppContainer(
    private val userDataStore: DataStore<androidx.datastore.preferences.core.Preferences>
) : AppContainer {
    // change it to your own local ip please
    private val baseUrl = "http://192.168.249.69:3000/"

    private val retrofit: Retrofit by lazy {
        initRetrofit()
    }

    private val tournamentRetrofitService: TournamentServiceApi by lazy {
        retrofit.create(TournamentServiceApi::class.java)
    }

    private val teamRetrofitService: TeamApiService by lazy {
        retrofit.create(TeamApiService::class.java)
    }

    override val tournamentRepository: TournamentRepository by lazy {
        NetworkTournamentRepository(tournamentRetrofitService)
    }

    override val teamRepository: TeamRepository by lazy {
        NetworkTeamRepository(teamRetrofitService)
    }

    private fun initRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)

        return Retrofit
            .Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(client.build())
            .baseUrl(baseUrl)
            .build()
    }
}