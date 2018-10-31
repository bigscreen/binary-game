package com.bigscreen.binarygame.misc

import android.content.Context
import android.media.MediaPlayer
import com.bigscreen.binarygame.storage.BGPreferences
import javax.inject.Inject

class SoundService @Inject constructor(
        private val context: Context,
        private val preferences: BGPreferences
) {

    private var mpSoundFX: MediaPlayer? = null
    private var mpBackSound: MediaPlayer? = null

    fun playEffect(soundResId: Int) {
        if (preferences.isSoundFxEnabled().not()) return
        releaseSoundFX()
        mpSoundFX = MediaPlayer.create(context, soundResId)
        mpSoundFX?.setOnCompletionListener { releaseSoundFX() }
        mpSoundFX?.start()
    }

    private fun releaseSoundFX() {
        mpSoundFX?.release()
        mpSoundFX = null
    }

    fun initBackSound(soundResId: Int) {
        releaseBackSound()
        mpBackSound = MediaPlayer.create(context, soundResId)
        mpBackSound?.isLooping = true
    }

    fun playBackSound() {
        if (preferences.isMusicEnabled().not() || mpBackSound == null) return
        if (mpBackSound?.isPlaying == false) mpBackSound?.start()
    }

    fun pauseBackSound() {
        if (preferences.isMusicEnabled().not() || mpBackSound == null) return
        if (mpBackSound?.isPlaying == true) mpBackSound?.pause()
    }

    fun stopBackSound() {
        if (preferences.isMusicEnabled().not() || mpBackSound == null) return
        if (mpBackSound?.isPlaying == true) mpBackSound?.stop()
    }

    fun stopAndReleaseBackSound() {
        mpBackSound?.let {
            if (it.isPlaying) {
                it.stop()
                releaseBackSound()
            } else {
                releaseBackSound()
            }
        }
    }

    private fun releaseBackSound() {
        mpBackSound?.release()
        mpBackSound = null
    }

}