package com.example.starwarsgarage.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "starship_showstopper_prefs")

@Singleton
class ShowstopperDataStoreManager @Inject constructor(
    @ApplicationContext context: Context
){
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val LAST_FETCH_TIMESTAMP = longPreferencesKey("last_fetch_timestamp")
        val DAILY_STARSHIP_ID = stringPreferencesKey("daily_starship_id")
    }

    private companion object {
        const val TWENTY_FOUR_HOURS_MS = 24 * 60 * 60 * 1000
    }

    suspend fun saveDailyStarship(starshipId: String, timestamp: Long){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_FETCH_TIMESTAMP] = timestamp
            preferences[PreferencesKeys.DAILY_STARSHIP_ID] = starshipId
        }
    }

    private suspend fun getLastFetchTimeStamp(): Long {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.LAST_FETCH_TIMESTAMP] ?: 0L
        }.first()
    }

    suspend fun getDailyStarshipId(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.DAILY_STARSHIP_ID]
        }.first()
    }

    suspend fun isNewStarshipNeeded(): Boolean {
        val lastFetchTime = getLastFetchTimeStamp()
        val currentTime = System.currentTimeMillis()
        return lastFetchTime == 0L || (currentTime - lastFetchTime) > TWENTY_FOUR_HOURS_MS
    }
}
