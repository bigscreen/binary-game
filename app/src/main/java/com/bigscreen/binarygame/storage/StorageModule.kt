package com.bigscreen.binarygame.storage

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun providesBGPreferences(context: Context): BGPreferences = BGPreferences(
            context)

    @Provides
    @Singleton
    fun providesDBService(context: Context): DBService = DBService(
            context)

    @Provides
    @Singleton
    fun providesDBHelper(context: Context): DBHelper = DBHelper(
            context)
}