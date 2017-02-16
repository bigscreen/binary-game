package com.bigscreen.binarygame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;


public class SettingsActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private boolean isPaused = false;

    private BGApplication application;
    private LinearLayout layoutMusicBg, layoutSoundFx;
    private CheckBox checkboxMusicBg, checkboxSoundFx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        application = (BGApplication) getApplication();

        layoutMusicBg = (LinearLayout) findViewById(R.id.layout_music_bg);
        layoutSoundFx = (LinearLayout) findViewById(R.id.layout_sound_fx);
        checkboxMusicBg = (CheckBox) findViewById(R.id.checkbox_music_bg);
        checkboxSoundFx = (CheckBox) findViewById(R.id.checkbox_sound_fx);

        layoutMusicBg.setOnClickListener(this);
        layoutSoundFx.setOnClickListener(this);
        checkboxMusicBg.setOnCheckedChangeListener(this);
        checkboxSoundFx.setOnCheckedChangeListener(this);

        checkboxMusicBg.setChecked(application.getSession().isMusicEnabled());
        checkboxSoundFx.setChecked(application.getSession().isSoundFxEnabled());

        application.playBackSound();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused) {
            application.playBackSound();
            isPaused = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        application.pauseBackSound();
        isPaused = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_music_bg : {
                if (checkboxMusicBg.isChecked()) {
                    checkboxMusicBg.setChecked(false);
                } else {
                    checkboxMusicBg.setChecked(true);
                }
                break;
            }
            case R.id.layout_sound_fx : {
                if (checkboxSoundFx.isChecked()) {
                    checkboxSoundFx.setChecked(false);
                } else {
                    checkboxSoundFx.setChecked(true);
                }
                break;
            }
            default: break;
        }
        application.playEffect(R.raw.effect_button_clicked);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_music_bg : {
                if (isChecked) {
                    application.getSession().setMusicEnabled(true);
                    application.playBackSound();
                } else {
                    application.pauseBackSound();
                    application.getSession().setMusicEnabled(true);
                }
                break;
            }
            case R.id.checkbox_sound_fx : {
                application.getSession().setSoundFxEnabled(isChecked);
                break;
            }
            default: break;
        }
    }

    @Override
    public void onBackPressed() {
        application.playEffect(R.raw.effect_back);
        super.onBackPressed();
    }
}
