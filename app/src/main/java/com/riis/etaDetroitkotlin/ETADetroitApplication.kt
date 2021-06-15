package com.riis.etaDetroitkotlin

import android.app.Application
import com.riis.etaDetroitkotlin.database.BusRepository

private const val TAG = "ETADetroitApplication"

class ETADetroitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        BusRepository.initialize(this)
    }
}