package com.example.videocall

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.videocall.data.UserProfileRepository
import com.example.videocall.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private const val USER_PROFILE_PREFERENCE_NAME = "user_profile_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PROFILE_PREFERENCE_NAME
)

class VideoCallApplication : Application() {
    companion object {
        lateinit var instance: VideoCallApplication
            private set
    }

    lateinit var userProfileRepository: UserProfileRepository

    private val baseUrl = "http://anywhereapptest.duckdns.org:8000"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        userProfileRepository = UserProfileRepository(dataStore)
    }
}