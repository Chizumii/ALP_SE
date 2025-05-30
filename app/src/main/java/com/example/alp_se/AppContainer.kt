package com.example.alp_se

import androidx.datastore.core.DataStore
import com.example.alp_se.repositories.NetworkTournamentRepository
import com.example.alp_se.repositories.TournamentRepository
import com.example.alp_se.services.TournamentServiceApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface AppContainer {
    val tournamentRepository: TournamentRepository
}

class DefaultAppContainer(
    private val userDataStore: DataStore<androidx.datastore.preferences.core.Preferences>
) : AppContainer {
    // change it to your own local ip please
<<<<<<< HEAD
    private val baseUrl = "http://192.168.105.69:3000/"
=======
    private val baseUrl = "http://192.168.88.201:3000/"
>>>>>>> parent of 9bcc9b4 (Complete TeamView)

    private val tournamentRetrofitService: TournamentServiceApi by lazy {
        val retrofit = initRetrofit()
        retrofit.create(TournamentServiceApi::class.java)
    }

    override val tournamentRepository: TournamentRepository by lazy {
        NetworkTournamentRepository(tournamentRetrofitService)
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

