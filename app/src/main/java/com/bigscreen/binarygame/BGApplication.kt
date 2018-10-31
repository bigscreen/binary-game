package com.bigscreen.binarygame

import android.app.Application
import com.bigscreen.binarygame.deps.AppComponent
import com.bigscreen.binarygame.deps.AppModule
import com.bigscreen.binarygame.deps.DaggerAppComponent
import kotlin.properties.Delegates

class BGApplication : Application() {

    var appComponent: AppComponent by Delegates.notNull()

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}