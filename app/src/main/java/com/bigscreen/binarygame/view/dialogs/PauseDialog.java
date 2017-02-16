package com.bigscreen.binarygame.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bigscreen.binarygame.R;


public class PauseDialog extends Dialog implements View.OnClickListener {

    public static final int BUTTON_PLAY = 1;
    public static final int BUTTON_REPLAY = 2;
    public static final int BUTTON_SETTING = 3;
    public static final int BUTTON_EXIT = 4;

    private ImageView btnPlay, btnReplay, btnSetting, btnExit;

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

        btnPlay = (ImageView) findViewById(R.id.iv_btn_play);
        btnReplay = (ImageView) findViewById(R.id.iv_btn_replay);
        btnSetting = (ImageView) findViewById(R.id.iv_btn_setting);
        btnExit = (ImageView) findViewById(R.id.iv_btn_exit);

        btnPlay.setOnClickListener(this);
        btnReplay.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        Log.i(PauseDialog.class.getSimpleName(), "Dialog dismissed");
        super.setOnDismissListener(listener);
    }

//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        try {
//            listener = (PauseDialogListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException("" + getContext()
//                    + " must implement PauseDialogListener");
//        }
//    }

//    @Override
//    public void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        listener = null;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_btn_play : {
                listener.onPauseDialogClicked(BUTTON_PLAY);
                break;
            }
            case R.id.iv_btn_replay : {
                listener.onPauseDialogClicked(BUTTON_REPLAY);
                break;
            }
            case R.id.iv_btn_setting : {
                listener.onPauseDialogClicked(BUTTON_SETTING);
                break;
            }
            case R.id.iv_btn_exit : {
                listener.onPauseDialogClicked(BUTTON_EXIT);
                break;
            }
            default: break;
        }
    }

    public interface PauseDialogListener {
        public void onPauseDialogClicked(int clickedButton);
    }
}
