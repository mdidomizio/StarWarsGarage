package com.example.starwarsgarage.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.starwarsgarage.data.local.FavoriteStarshipDao
import com.example.starwarsgarage.data.local.StarWarsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): StarWarsDatabase {

        Log.d("HiltDebug", "Database path: ${appContext.getDatabasePath("starwars_database").absolutePath}")

        // Log everything about the context
        Log.d("HiltDebug", "=== DATABASE CREATION ===")
        Log.d("HiltDebug", "Context class: ${appContext.javaClass.name}")
        Log.d("HiltDebug", "Context hashCode: ${appContext.hashCode()}")
        Log.d("HiltDebug", "Package name: ${appContext.packageName}")
        Log.d("HiltDebug", "Application context: ${appContext.applicationContext.hashCode()}")

        val dbPath = appContext.getDatabasePath("starwars_database")
        Log.d("HiltDebug", "Database path: ${dbPath.absolutePath}")
        Log.d("HiltDebug", "Database exists: ${dbPath.exists()}")
        Log.d("HiltDebug", "Database parent exists: ${dbPath.parentFile?.exists()}")

        // List all databases
        appContext.databaseList().forEach {
            Log.d("HiltDebug", "Existing database: $it")
        }
        return Room.databaseBuilder(

            appContext,
            StarWarsDatabase::class.java,
            "starwars_database"
        )
            .fallbackToDestructiveMigration()
            .build()
            .also {
                Log.d("HiltDebug", "Database instance created: ${it.hashCode()}")
            }
    }

    @Provides
    fun provideFavoriteStarshipDao(database: StarWarsDatabase): FavoriteStarshipDao {
        Log.d("HiltDebug", "ProvideFavoritesDao called")
        Log.d("HiltDebug", "Database instance: ${database.hashCode()}")
        return database.favoriteStarshipDao()
    }
}
