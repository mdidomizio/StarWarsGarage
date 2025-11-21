package com.example.starwarsgarage.di

import com.example.starwarsgarage.data.remote.StarshipApi
import com.example.starwarsgarage.data.repository.StarshipRepository
import com.example.starwarsgarage.data.repository.StarshipRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {

    private const val BASE_URL = "https://swapi.dev/api/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val starshipApi: StarshipApi = retrofit.create(StarshipApi::class.java)

    val starshipRepository: StarshipRepository = StarshipRepositoryImpl(starshipApi)
}
