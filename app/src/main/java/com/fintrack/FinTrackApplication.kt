package com.fintrack

import android.app.Application
import com.fintrack.di.AppContainer

class FinTrackApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
