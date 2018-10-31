package com.bigscreen.binarygame.common.component;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bigscreen.binarygame.R;

public class GameTextView extends AppCompatTextView {

    public GameTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont(attrs);
    }

    private void initFont(AttributeSet attributeSet) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.GameTextView);
        int fontStyle = styledAttributes.getInt(R.styleable.GameTextView_textFont, 0);
        setTypeface(getSelectedTypeface(fontStyle));
        styledAttributes.recycle();
    }

    private Typeface getSelectedTypeface(int fontStyle) {
        switch (fontStyle) {
            case GameTextViewAttr.caveStory:
                return Typeface.createFromAsset(getContext().getAssets(), "fonts/cave_story.ttf");
            case GameTextViewAttr.pixelMix:
                return Typeface.createFromAsset(getContext().getAssets(), "fonts/pixel_mix.ttf");
            case GameTextViewAttr.pressStart:
                return Typeface.createFromAsset(getContext().getAssets(), "fonts/press_start_2p.ttf");
            default:
                return Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_regular.ttf");
        }
    }

    private static class GameTextViewAttr {
        static final int caveStory = 1;
        static final int pixelMix = 2;
        static final int pressStart = 3;
    }

}
