package com.example.starwarsgarage.di

import com.example.starwarsgarage.data.repository.FavoritesRepositoryImpl
import com.example.starwarsgarage.data.repository.StarshipRepositoryImpl
import com.example.starwarsgarage.domain.repository.FavoritesRepository
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindStarshipRepository(
        starshipRepositoryImpl: StarshipRepositoryImpl
    ): StarshipRepository

    @Binds
    abstract fun bindFavoritesRepository(
        favoritesRepositoryImpl: FavoritesRepositoryImpl
    ): FavoritesRepository
}
