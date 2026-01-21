package com.example.starwarsgarage.data.local

import android.content.Context
import android.icu.util.Calendar
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
        val LAST_FETCH_DAY = longPreferencesKey("last_fetch_day")
        val DAILY_STARSHIP_ID = stringPreferencesKey("daily_starship_id")
    }

    suspend fun saveDailyStarship(starshipId: String){
        val currentDay =  getCurrentDayTimestamp()
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_FETCH_DAY] = currentDay
            preferences[PreferencesKeys.DAILY_STARSHIP_ID] = starshipId
        }
    }

    private fun getCurrentDayTimestamp(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private suspend fun getLastFetchTimeStamp(): Long {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.LAST_FETCH_DAY] ?: 0L
        }.first()
    }

    suspend fun getDailyStarshipId(): String? {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.DAILY_STARSHIP_ID]
        }.first()
    }

    suspend fun isNewStarshipNeeded(): Boolean {
        val lastFetchTime = getLastFetchTimeStamp()
        val currentDay = getCurrentDayTimestamp()
        return lastFetchTime == 0L || currentDay > lastFetchTime
    }
}
