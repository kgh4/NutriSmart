package com.example.nutrismart

import android.app.Application
import com.example.nutrismart.di.AppContainer
import com.example.nutrismart.di.AppDataContainer

class NutriSmartApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
