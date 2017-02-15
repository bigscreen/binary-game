package com.intuisis.binarygame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

/**
 * Created by gallant on 16/03/15.
 */
public class SettingsActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private boolean isPaused = false;

    private BGApplication app;
    private LinearLayout llMusicBg, llSoundFx;
    private CheckBox cbMusicBg, cbSoundFx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        app = (BGApplication) getApplication();

        llMusicBg = (LinearLayout) findViewById(R.id.ll_music_bg);
        llSoundFx = (LinearLayout) findViewById(R.id.ll_sound_fx);
        cbMusicBg = (CheckBox) findViewById(R.id.cb_music_bg);
        cbSoundFx = (CheckBox) findViewById(R.id.cb_sound_fx);

        llMusicBg.setOnClickListener(this);
        llSoundFx.setOnClickListener(this);
        cbMusicBg.setOnCheckedChangeListener(this);
        cbSoundFx.setOnCheckedChangeListener(this);

        cbMusicBg.setChecked(app.getSession().isMusicEnabled());
        cbSoundFx.setChecked(app.getSession().isSoundFxEnabled());

        app.playBackSound();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if (isPaused) {
            app.playBackSound();
            isPaused = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        app.pauseBackSound();
        isPaused = true;
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_music_bg : {
                if (cbMusicBg.isChecked()) {
                    cbMusicBg.setChecked(false);
                } else {
                    cbMusicBg.setChecked(true);
                }
                break;
            }
            case R.id.ll_sound_fx : {
                if (cbSoundFx.isChecked()) {
                    cbSoundFx.setChecked(false);
                } else {
                    cbSoundFx.setChecked(true);
                }
                break;
            }
            default: break;
        }
        app.playEffect(R.raw.effect_button_clicked);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_music_bg : {
                if (isChecked) {
                    app.getSession().setMusicEnabled(isChecked);
                    app.playBackSound();
                } else {
                    app.pauseBackSound();
                    app.getSession().setMusicEnabled(isChecked);
                }
                break;
            }
            case R.id.cb_sound_fx : {
                app.getSession().setSoundFxEnabled(isChecked);
                break;
            }
            default: break;
        }
    }

    @Override
    public void onBackPressed() {
        app.playEffect(R.raw.effect_back);
        super.onBackPressed();
    }
}
