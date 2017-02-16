package com.bigscreen.binarygame;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bigscreen.binarygame.view.dialogs.BeautyDialog;


public class MenuActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MenuActivity.class.getSimpleName();

    private boolean isPaused = false;

    private BGApplication application;
    private Button buttonStart, buttonHighScores, buttonSettings, buttonAbout;
    private boolean justShared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        application = (BGApplication) getApplication();

        buttonStart = (Button) findViewById(R.id.button_start);
        buttonHighScores = (Button) findViewById(R.id.button_high_scores);
        buttonSettings = (Button) findViewById(R.id.button_settings);
        buttonAbout = (Button) findViewById(R.id.button_about);

        buttonStart.setOnClickListener(this);
        buttonHighScores.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
        buttonAbout.setOnClickListener(this);

        application.initBackSound(R.raw.backsound);
        application.playBackSound();

        checkDeviceScreen(application.getScreenSize(this)[0], application.getScreenSize(this)[1]);
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
        application.stopAndReleaseBackSound();
        super.onDestroy();
    }

    private void checkDeviceScreen(int width, int height) {
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
                application.playEffect(R.raw.effect_back);
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
                application.playEffect(R.raw.effect_button_clicked);
                share();
            }
        });
        aboutDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!justShared)
                    application.playEffect(R.raw.effect_back);
                justShared = false;
            }
        });
        aboutDialog.show();
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message) + "\n"
                + getString(R.string.share_uri));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start : {
                application.playEffect(R.raw.effect_button_clicked);
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.button_high_scores : {
                application.playEffect(R.raw.effect_button_clicked);
                startActivity(new Intent(this, ScoresActivity.class));
                break;
            }
            case R.id.button_settings : {
                application.playEffect(R.raw.effect_button_clicked);
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }
            case R.id.button_about : {
                application.playEffect(R.raw.effect_button_clicked);
                showAbout();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        application.playEffect(R.raw.effect_button_clicked);
        showExitConfirmation();
    }
}
