package com.bigscreen.binarygame.misc

import android.content.Context
import com.bigscreen.binarygame.storage.BGPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MiscModule {

    @Provides
    @Singleton
    fun providesSoundService(
            context: Context,
            preferences: BGPreferences
    ): SoundService = SoundService(context, preferences)

}