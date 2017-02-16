package com.bigscreen.binarygame.view.items;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigscreen.binarygame.R;
import com.bigscreen.binarygame.entities.ScoreEntity;


public class ScoreItem extends LinearLayout {

    private static final String TAG = ScoreItem.class.getSimpleName();

    private TextView textRank, textScore, textLevel, textLines, textTime;

    public ScoreItem(Context context) {
        super(context);
        inflateView(context);
    }

    private void inflateView(Context context) {
        inflate(context, R.layout.item_score, this);

        textRank = (TextView) findViewById(R.id.text_item_rank);
        textScore = (TextView) findViewById(R.id.text_item_score);
        textLevel = (TextView) findViewById(R.id.text_item_level);
        textLines = (TextView) findViewById(R.id.text_item_lines);
        textTime = (TextView) findViewById(R.id.text_item_time);
    }

    public void bind(ScoreEntity entity, int position) {

        String rank;
        if ((position + 1) < 10) {
            rank = "0" + (position + 1);
        } else {
            rank = "" + (position + 1);
        }

        textRank.setText("" + rank);
        textScore.setText("" + entity.getScore());
        textLevel.setText("" + entity.getLevel());
        textLines.setText("" + entity.getLines());
        textTime.setText("" + entity.getFormattedTime());
    }
}
