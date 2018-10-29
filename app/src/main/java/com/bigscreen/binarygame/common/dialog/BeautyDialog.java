package com.bigscreen.binarygame.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigscreen.binarygame.R;


public class BeautyDialog extends Dialog implements View.OnClickListener {

    public static final int BUTTON_POSITIVE = 1;
    public static final int BUTTON_NEGATIVE= 2;

    private LinearLayout layoutTitle;
    private TextView textTitle, textMessage;
    private Button buttonPositive, buttonNegative;

    private OnClickListener positiveButtonOnClickListener, negativeButtonOnClickListener;

    private CharSequence title, message, positiveButtonText, negativeButtonText;
    private int titleVisibility, messageVisibility, positiveButtonVisibility, negativeButtonVisibility;

    public BeautyDialog(Context context) {
        super(context);
        title = getContext().getText(R.string.title);
        message = getContext().getString(R.string.message);
        positiveButtonText = getContext().getText(R.string.ok);
        negativeButtonText = getContext().getText(R.string.cancel);
        titleVisibility = View.GONE;
        messageVisibility = View.INVISIBLE;
        positiveButtonVisibility = View.GONE;
        negativeButtonVisibility = View.GONE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);

        layoutTitle = (LinearLayout) findViewById(R.id.layout_dlg_title);
        textTitle = (TextView) findViewById(R.id.text_dlg_title);
        textMessage = (TextView) findViewById(R.id.text_dlg_message);
        buttonPositive = (Button) findViewById(R.id.button_dlg_positive);
        buttonNegative = (Button) findViewById(R.id.button_dlg_negative);

        buttonPositive.setOnClickListener(this);
        buttonNegative.setOnClickListener(this);

        postView();
    }

    private void postView() {
        textTitle.setText(title);
        textMessage.setText(message);
        buttonPositive.setText(positiveButtonText);
        buttonNegative.setText(negativeButtonText);
        layoutTitle.setVisibility(titleVisibility);
        textMessage.setVisibility(messageVisibility);
        buttonPositive.setVisibility(positiveButtonVisibility);
        buttonNegative.setVisibility(negativeButtonVisibility);
    }

    public void setTitle(int textId) {
        title = getContext().getText(textId);
        titleVisibility = View.VISIBLE;
    }

    public void setTitle(CharSequence text) {
        title = text;
        titleVisibility = View.VISIBLE;
    }

    public void setMessage(int textId) {
        message = getContext().getText(textId);
        messageVisibility = View.VISIBLE;
    }

    public void setMessage(CharSequence text) {
        message = text;
        messageVisibility = View.VISIBLE;
    }

    public void setPositiveButton(int textId, OnClickListener onClickListener) {
        CharSequence text = getContext().getText(textId);
        positiveButtonOnClickListener = onClickListener;
        positiveButtonText = text;
        positiveButtonVisibility = View.VISIBLE;
    }

    public void setPositiveButton(CharSequence text, OnClickListener onClickListener) {
        positiveButtonOnClickListener = onClickListener;
        positiveButtonText = text;
        positiveButtonVisibility = View.VISIBLE;
    }

    public void setPositiveButton(OnClickListener onClickListener) {
        positiveButtonOnClickListener = onClickListener;
        positiveButtonVisibility = View.VISIBLE;
    }

    public void setNegativeButton(int textId, OnClickListener onClickListener) {
        CharSequence text = getContext().getText(textId);
        negativeButtonOnClickListener = onClickListener;
        negativeButtonText = text;
        negativeButtonVisibility = View.VISIBLE;
    }

    public void setNegativeButton(CharSequence text, OnClickListener onClickListener) {
        negativeButtonOnClickListener = onClickListener;
        negativeButtonText = text;
        negativeButtonVisibility = View.VISIBLE;
    }

    public void setNegativeButton(OnClickListener onClickListener) {
        negativeButtonOnClickListener = onClickListener;
        negativeButtonVisibility = View.VISIBLE;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_dlg_positive : {
                positiveButtonOnClickListener.onClick(this, BUTTON_POSITIVE);
                dismiss();
                break;
            }
            case R.id.button_dlg_negative : {
                negativeButtonOnClickListener.onClick(this, BUTTON_NEGATIVE);
                dismiss();
                break;
            }
            default: break;
        }
    }

    public interface OnClickListener {
        void onClick(Dialog dialog, int which);
    }
}
