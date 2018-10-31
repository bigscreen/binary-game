package com.bigscreen.binarygame.storage

import android.content.Context

private const val PREFS_NAME = "binary_game"
private const val IS_MUSIC_ENABLED = "is_music_enabled"
private const val IS_SOUND_FX_ENABLED = "is_sound_fx_enabled"

class BGPreferences(context: Context) {

    private val preferences = context.getSharedPreferences(
            PREFS_NAME, 0)

    fun clearSession() = preferences.edit().clear().apply()

    fun setMusicEnabled(isMusicEnabled: Boolean) =
            preferences.edit().putBoolean(IS_MUSIC_ENABLED, isMusicEnabled).apply()

    fun isMusicEnabled(): Boolean = preferences.getBoolean(
            IS_MUSIC_ENABLED, true)

    fun setSoundFxEnabled(isSoundFxEnabled: Boolean) =
            preferences.edit().putBoolean(IS_SOUND_FX_ENABLED, isSoundFxEnabled).apply()

    fun isSoundFxEnabled(): Boolean = preferences.getBoolean(
            IS_SOUND_FX_ENABLED, true)

}
