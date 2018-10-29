package com.bigscreen.binarygame.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bigscreen.binarygame.R;


public class PauseDialog extends Dialog implements View.OnClickListener {

    public static final int BUTTON_PLAY = 1;
    public static final int BUTTON_REPLAY = 2;
    public static final int BUTTON_SETTING = 3;
    public static final int BUTTON_EXIT = 4;

    private ImageView buttonPlay, buttonReplay, buttonSetting, buttonExit;

    private PauseDialogListener listener;

    public PauseDialog(Context context, PauseDialogListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pause);

        setCancelable(false);

        buttonPlay = (ImageView) findViewById(R.id.image_btn_play);
        buttonReplay = (ImageView) findViewById(R.id.image_btn_replay);
        buttonSetting = (ImageView) findViewById(R.id.image_btn_setting);
        buttonExit = (ImageView) findViewById(R.id.image_btn_exit);

        buttonPlay.setOnClickListener(this);
        buttonReplay.setOnClickListener(this);
        buttonSetting.setOnClickListener(this);
        buttonExit.setOnClickListener(this);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_btn_play : {
                listener.onPauseDialogClicked(BUTTON_PLAY);
                break;
            }
            case R.id.image_btn_replay : {
                listener.onPauseDialogClicked(BUTTON_REPLAY);
                break;
            }
            case R.id.image_btn_setting : {
                listener.onPauseDialogClicked(BUTTON_SETTING);
                break;
            }
            case R.id.image_btn_exit : {
                listener.onPauseDialogClicked(BUTTON_EXIT);
                break;
            }
            default: break;
        }
    }

    public interface PauseDialogListener {
        void onPauseDialogClicked(int clickedButton);
    }
}
