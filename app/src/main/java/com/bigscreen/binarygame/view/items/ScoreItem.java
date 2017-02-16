package com.bigscreen.binarygame.view.items;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigscreen.binarygame.R;
import com.bigscreen.binarygame.entities.ScoreEntity;


public class ScoreItem extends LinearLayout {

    private static final String TAG = ScoreItem.class.getSimpleName();

    private TextView tvRank, tvScore, tvLevel, tvLines, tvTime;

    public ScoreItem(Context context) {
        super(context);
        inflateView(context);
    }

    private void inflateView(Context context) {
        inflate(context, R.layout.item_score, this);

        tvRank = (TextView) findViewById(R.id.tv_item_rank);
        tvScore = (TextView) findViewById(R.id.tv_item_score);
        tvLevel = (TextView) findViewById(R.id.tv_item_level);
        tvLines = (TextView) findViewById(R.id.tv_item_lines);
        tvTime = (TextView) findViewById(R.id.tv_item_time);
    }

    public void bind(ScoreEntity entity, int position) {

        String rank;
        if ((position + 1) < 10) {
            rank = "0" + (position + 1);
        } else {
            rank = "" + (position + 1);
        }

        tvRank.setText("" + rank);
        tvScore.setText("" + entity.getScore());
        tvLevel.setText("" + entity.getLevel());
        tvLines.setText("" + entity.getLines());
        tvTime.setText("" + entity.getFormattedTime());
    }
}
