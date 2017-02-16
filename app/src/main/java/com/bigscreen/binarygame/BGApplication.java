package com.bigscreen.binarygame;

import android.app.Activity;
import android.app.Application;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.Display;

import com.bigscreen.binarygame.helpers.DBHelper;
import com.bigscreen.binarygame.helpers.SessionManager;


public class BGApplication extends Application {

    private SessionManager sessionManager;
    private DBHelper dbHelper;
    private MediaPlayer mpSoundFX, mpBackSound;

    /**
     * Get screen size current activity.
     * Use [0] to get screen width or [1] to get screen height.
     * @param activity {@link android.app.Activity}
     * @return int[2], index 0 for width and 1 for height
     */
    public int[] getScreenSize(Activity activity) {
        int width, height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            width = size.x;
            height = size.y;
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            width = display.getWidth();
            height = display.getHeight();
        }
        Log.i(activity.getClass().getSimpleName(), "Get screen size!, width= " + width
                + ", height= " + height);
        return new int[] {width, height};
    }

    /**
     * Get session manager of application.
     * Session manager is class to manage data on {@link android.content.SharedPreferences}.
     * @return {@link com.bigscreen.binarygame.helpers.SessionManager}
     */
    public SessionManager getSession() {
        if (sessionManager == null)
            sessionManager = new SessionManager(this);

        return sessionManager;
    }

    /**
     * Get database helper of application.
     * @return {@link com.bigscreen.binarygame.helpers.DBHelper}
     */
    public DBHelper getDatabase() {
        if (dbHelper == null)
            dbHelper = new DBHelper(this);

        return dbHelper;
    }

    public void playEffect(int soundResId) {
        if (!getSession().isSoundFxEnabled())
            return;
        releaseMPSoundFX();
        mpSoundFX = MediaPlayer.create(this, soundResId);
        mpSoundFX.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                releaseMPSoundFX();
            }
        });
        mpSoundFX.start();
    }

    private void releaseMPSoundFX() {
        if (mpSoundFX != null) {
            mpSoundFX.release();
            mpSoundFX = null;
        }
    }

    public void initBackSound(int soundResId) {
        releaseMPBackSound();
        mpBackSound = MediaPlayer.create(this, soundResId);
        mpBackSound.setLooping(true);
    }

    public void playBackSound() {
        if (!getSession().isMusicEnabled() || mpBackSound == null)
            return;
        if (!mpBackSound.isPlaying())
            mpBackSound.start();
    }

    public void pauseBackSound() {
        if (!getSession().isMusicEnabled() || mpBackSound == null)
            return;
        if (mpBackSound.isPlaying())
            mpBackSound.pause();
    }

    public void stopBackSound() {
        if (!getSession().isMusicEnabled() || mpBackSound == null)
            return;
        if (mpBackSound.isPlaying())
            mpBackSound.stop();
    }

    public void stopAndReleaseBackSound() {
        if (mpBackSound != null) {
            if (mpBackSound.isPlaying()) {
                mpBackSound.stop();
                releaseMPBackSound();
            } else {
                releaseMPBackSound();
            }
        }
    }

    private void releaseMPBackSound() {
        if (mpBackSound != null) {
            mpBackSound.release();
            mpBackSound = null;
        }
    }

}
