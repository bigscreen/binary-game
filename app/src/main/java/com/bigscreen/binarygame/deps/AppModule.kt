package com.bigscreen.binarygame.deps

import android.content.Context
import dagger.Module
import javax.inject.Singleton
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = context

}