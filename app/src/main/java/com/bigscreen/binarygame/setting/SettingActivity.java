package com.bigscreen.binarygame.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.bigscreen.binarygame.BGApplication;
import com.bigscreen.binarygame.R;
import com.bigscreen.binarygame.storage.BGPreferences;
import com.bigscreen.binarygame.misc.SoundService;
import javax.inject.Inject;


public class SettingActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = SettingActivity.class.getSimpleName();

    private boolean isPaused = false;

    private LinearLayout layoutMusicBg, layoutSoundFx;
    private CheckBox checkboxMusicBg, checkboxSoundFx;

    @Inject
    public BGPreferences preferences;
    @Inject
    public SoundService soundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BGApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_settings);

        layoutMusicBg = (LinearLayout) findViewById(R.id.layout_music_bg);
        layoutSoundFx = (LinearLayout) findViewById(R.id.layout_sound_fx);
        checkboxMusicBg = (CheckBox) findViewById(R.id.checkbox_music_bg);
        checkboxSoundFx = (CheckBox) findViewById(R.id.checkbox_sound_fx);

        layoutMusicBg.setOnClickListener(this);
        layoutSoundFx.setOnClickListener(this);
        checkboxMusicBg.setOnCheckedChangeListener(this);
        checkboxSoundFx.setOnCheckedChangeListener(this);

        checkboxMusicBg.setChecked(preferences.isMusicEnabled());
        checkboxSoundFx.setChecked(preferences.isSoundFxEnabled());

        soundService.playBackSound();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused) {
            soundService.playBackSound();
            isPaused = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundService.pauseBackSound();
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
        soundService.playEffect(R.raw.effect_button_clicked);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_music_bg : {
                if (isChecked) {
                    preferences.setMusicEnabled(true);
                    soundService.playBackSound();
                } else {
                    soundService.pauseBackSound();
                    preferences.setMusicEnabled(true);
                }
                break;
            }
            case R.id.checkbox_sound_fx : {
                preferences.setSoundFxEnabled(isChecked);
                break;
            }
            default: break;
        }
    }

    @Override
    public void onBackPressed() {
        soundService.playEffect(R.raw.effect_back);
        super.onBackPressed();
    }
}
