package com.bigscreen.binarygame.common.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	
	private SharedPreferences pref;
    private Editor editor;

    private final int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "binary_game";
    private static final String IS_MUSIC_ENABLED = "is_music_enabled";
    private static final String IS_SOUND_FX_ENABLED = "is_sound_fx_enabled";
    
    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
    
    public void setMusicEnabled(boolean isMusicEnabled) {
        editor.putBoolean(IS_MUSIC_ENABLED, isMusicEnabled);
        editor.commit();
    }

    public boolean isMusicEnabled() {
        return pref.getBoolean(IS_MUSIC_ENABLED, true);
    }
    
    public void setSoundFxEnabled(boolean isSoundFxEnabled) {
        editor.putBoolean(IS_SOUND_FX_ENABLED, isSoundFxEnabled);
        editor.commit();
    }

    public boolean isSoundFxEnabled() {
        return pref.getBoolean(IS_SOUND_FX_ENABLED, true);
    }

}
