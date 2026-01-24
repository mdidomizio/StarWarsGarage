package com.example.starwarsgarage

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class StarWarsGarageApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Log.d("HiltDebug", "Application onCreate - context: ${applicationContext.hashCode()}")
    }
}
