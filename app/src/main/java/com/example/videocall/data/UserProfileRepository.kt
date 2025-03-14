package com.example.videocall.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.videocall.models.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserProfileRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        val JWT_TOKEN_KEY = stringPreferencesKey("user_token")
        const val TAG = "UserProfileRepo"
    }

    val userProfile: Flow<UserProfile> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            UserProfile(
                name = preferences[USER_NAME_KEY] ?: "",
                email = preferences[USER_EMAIL_KEY] ?: "",
                token = preferences[JWT_TOKEN_KEY] ?: ""
            )
        }

    suspend fun saveUserProfile(userProfile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = userProfile.name
            preferences[USER_EMAIL_KEY] = userProfile.email
            preferences[JWT_TOKEN_KEY] = userProfile.token
        }
    }
}