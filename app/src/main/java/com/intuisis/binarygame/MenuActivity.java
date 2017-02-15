package com.intuisis.binarygame;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.intuisis.binarygame.view.dialogs.BeautyDialog;

/**
 * Created by gallant on 15/03/15.
 */
public class MenuActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MenuActivity.class.getSimpleName();

    private boolean isPaused = false;

    private BGApplication app;
    private Button btnStart, btnHighScores, btnSettings, btnAbout;
    private boolean justShared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        app = (BGApplication) getApplication();

        btnStart = (Button) findViewById(R.id.btn_start);
        btnHighScores = (Button) findViewById(R.id.btn_high_scores);
        btnSettings = (Button) findViewById(R.id.btn_settings);
        btnAbout = (Button) findViewById(R.id.btn_about);

        btnStart.setOnClickListener(this);
        btnHighScores.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnAbout.setOnClickListener(this);

        app.initBackSound(R.raw.backsound);
        app.playBackSound();

        checkDeviceScreen(app.getScreenSize(this)[0], app.getScreenSize(this)[1]);
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
        app.stopAndReleaseBackSound();
        Log.e(TAG, "onDestroy");
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

    private void showConfirmExit() {
        BeautyDialog confirmDialogExit = new BeautyDialog(this);
        confirmDialogExit.setCancelable(true);
        confirmDialogExit.setMessage(R.string.confirm_exit_app);
        confirmDialogExit.setPositiveButton(new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                finish();
            }
        });
        confirmDialogExit.setNegativeButton(new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                dialog.cancel();
            }
        });
        confirmDialogExit.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                app.playEffect(R.raw.effect_back);
            }
        });
        confirmDialogExit.show();
    }

    private void showAbout() {
        BeautyDialog dialogAbout = new BeautyDialog(this);
        dialogAbout.setCancelable(true);
        dialogAbout.setTitle(R.string.about);
        dialogAbout.setMessage(R.string.txt_about);
        dialogAbout.setPositiveButton(R.string.share, new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                justShared = true;
                app.playEffect(R.raw.effect_button_clicked);
                share();
            }
        });
        dialogAbout.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!justShared)
                    app.playEffect(R.raw.effect_back);
                justShared = false;
            }
        });
        dialogAbout.show();
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
            case R.id.btn_start : {
                app.playEffect(R.raw.effect_button_clicked);
                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.btn_high_scores : {
                app.playEffect(R.raw.effect_button_clicked);
                startActivity(new Intent(this, ScoresActivity.class));
                break;
            }
            case R.id.btn_settings : {
                app.playEffect(R.raw.effect_button_clicked);
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }
            case R.id.btn_about : {
                app.playEffect(R.raw.effect_button_clicked);
                showAbout();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        app.playEffect(R.raw.effect_button_clicked);
        showConfirmExit();
    }
}
