package com.example.starwarsgarage.di

import com.example.starwarsgarage.data.repository.StarshipRepositoryImpl
import com.example.starwarsgarage.domain.repository.StarshipRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStarshipRepository(
        starshipRepositoryImpl: StarshipRepositoryImpl
    ): StarshipRepository
}
