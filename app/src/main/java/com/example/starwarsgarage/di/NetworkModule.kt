package com.example.starwarsgarage.di

import com.example.starwarsgarage.data.remote.StarshipApi
import com.example.starwarsgarage.data.remote.StarshipVehicleDetailsApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StarshipRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StarshipVehicleDetailsRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL_STARSHIP = "https://starwars-databank-server.vercel.app/"
    private const val BASE_URL_STARSHIP_VEHICLE_DETAILS = "https://swapi.dev/api/"

    @Provides
    @Singleton
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    @StarshipRetrofit
    fun provideStarshipRetrofit(moshi: Moshi) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL_STARSHIP)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideStarshipApi (@StarshipRetrofit retrofit: Retrofit): StarshipApi
    = retrofit.create(StarshipApi::class.java)

    @Provides
    @Singleton
    @StarshipVehicleDetailsRetrofit
    fun provideStarshipVehicleDetailsRetrofit(moshi: Moshi) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_STARSHIP_VEHICLE_DETAILS)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideStarshipVehicleDetailsApi(@StarshipVehicleDetailsRetrofit retrofit: Retrofit): StarshipVehicleDetailsApi
    = retrofit.create(StarshipVehicleDetailsApi::class.java)
}
