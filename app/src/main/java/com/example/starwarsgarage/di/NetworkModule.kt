package com.example.starwarsgarage.di

import com.example.starwarsgarage.data.remote.api.StarshipApi
import com.example.starwarsgarage.data.remote.api.StarshipVehicleDetailsApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
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
    private const val BASE_URL_STARSHIP_VEHICLE_DETAILS = "https://swapi.info/api/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @StarshipRetrofit
    fun provideStarshipRetrofit(okHttpClient: OkHttpClient, moshi: Moshi) : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL_STARSHIP)
            .client(okHttpClient)
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
    fun provideStarshipVehicleDetailsRetrofit(moshi: Moshi, okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_STARSHIP_VEHICLE_DETAILS)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideStarshipVehicleDetailsApi(@StarshipVehicleDetailsRetrofit retrofit: Retrofit): StarshipVehicleDetailsApi
    = retrofit.create(StarshipVehicleDetailsApi::class.java)
}
