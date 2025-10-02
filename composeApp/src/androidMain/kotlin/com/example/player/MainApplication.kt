package com.example.player

import android.app.Application
import com.example.player.platform.initializePlatformContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializePlatformContext(this)
    }
}