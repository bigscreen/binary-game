package com.bigscreen.binarygame.menu;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bigscreen.binarygame.BGApplication;
import com.bigscreen.binarygame.MainActivity;
import com.bigscreen.binarygame.R;
import com.bigscreen.binarygame.misc.SoundService;
import com.bigscreen.binarygame.common.extension.ActivityKt;
import com.bigscreen.binarygame.setting.SettingActivity;
import com.bigscreen.binarygame.score.ScoreActivity;
import com.bigscreen.binarygame.common.dialog.BeautyDialog;
import javax.inject.Inject;


public class MenuActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MenuActivity.class.getSimpleName();

    private boolean isPaused = false;

    private Button buttonStart, buttonHighScores, buttonSettings, buttonAbout;
    private boolean justShared = false;

    @Inject
    public SoundService soundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BGApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_menu);


        buttonStart = (Button) findViewById(R.id.button_start);
        buttonHighScores = (Button) findViewById(R.id.button_high_scores);
        buttonSettings = (Button) findViewById(R.id.button_settings);
        buttonAbout = (Button) findViewById(R.id.button_about);

        buttonStart.setOnClickListener(this);
        buttonHighScores.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
        buttonAbout.setOnClickListener(this);

        soundService.initBackSound(R.raw.backsound);
        soundService.playBackSound();

        checkDeviceScreen();
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
        soundService.stopAndReleaseBackSound();
        super.onDestroy();
    }

    private void checkDeviceScreen() {
        final int width = ActivityKt.getScreenWidth(this);
        final int height = ActivityKt.getScreenHeight(this);
        if (width < 300 || height < 450) {
            BeautyDialog warningDialog = new BeautyDialog(this);
            warningDialog.setCancelable(false);
            warningDialog.setTitle(R.string.warning);
            warningDialog.setMessage(R.string.warning_screen_resolution);
            warningDialog.setPositiveButton(new BeautyDialog.OnClickListener() {
                @Override
                public void onClick(Dialog dialog, int which) {
                    dialog.cancel();
                }
            });
            warningDialog.show();
        } else {
            Log.i(TAG, "Supported device screen resolution.");
        }
    }

    private void showExitConfirmation() {
        BeautyDialog confirmExitDialog = new BeautyDialog(this);
        confirmExitDialog.setCancelable(true);
        confirmExitDialog.setMessage(R.string.confirm_exit_app);
        confirmExitDialog.setPositiveButton(new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                finish();
            }
        });
        confirmExitDialog.setNegativeButton(new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                dialog.cancel();
            }
        });
        confirmExitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                soundService.playEffect(R.raw.effect_back);
            }
        });
        confirmExitDialog.show();
    }

    private void showAbout() {
        BeautyDialog aboutDialog = new BeautyDialog(this);
        aboutDialog.setCancelable(true);
        aboutDialog.setTitle(R.string.about);
        aboutDialog.setMessage(R.string.txt_about);
        aboutDialog.setPositiveButton(R.string.share, new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                justShared = true;
                soundService.playEffect(R.raw.effect_button_clicked);
                share();
            }
        });
        aboutDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!justShared)
                    soundService.playEffect(R.raw.effect_back);
                justShared = false;
            }
        });
        aboutDialog.show();
    }

    private void share() {
        String playStoreUri = String.format(getString(R.string.play_store_uri_format), getPackageName());
        String shareMessage = String.format("%s\n%s", getString(R.string.share_message), playStoreUri);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start : {
                soundService.playEffect(R.raw.effect_button_clicked);
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.button_high_scores : {
                soundService.playEffect(R.raw.effect_button_clicked);
                startActivity(new Intent(this, ScoreActivity.class));
                break;
            }
            case R.id.button_settings : {
                soundService.playEffect(R.raw.effect_button_clicked);
                startActivity(new Intent(this, SettingActivity.class));
                break;
            }
            case R.id.button_about : {
                soundService.playEffect(R.raw.effect_button_clicked);
                showAbout();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        soundService.playEffect(R.raw.effect_button_clicked);
        showExitConfirmation();
    }
}
