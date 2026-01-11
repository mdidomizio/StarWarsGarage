package com.example.starwarsgarage.di

import android.content.Context
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
        return Room.databaseBuilder(
            appContext,
            StarWarsDatabase::class.java,
            "starwars_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFavoriteStarshipDao(database: StarWarsDatabase): FavoriteStarshipDao {
        return database.favoriteStarshipDao()
    }
}
