package com.intuisis.binarygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.intuisis.binarygame.adapters.ScoreAdapter;
import com.intuisis.binarygame.entities.ScoreEntity;

import java.util.List;

/**
 * Created by gallant on 16/03/15.
 */
public class ScoresActivity extends Activity {

    private static final String TAG = ScoresActivity.class.getSimpleName();

    private boolean isPaused = false;

    private BGApplication app;
    private ListView lvScores;
    private Button shareScore;
    private ScoreAdapter scoreAdapter;
    private List<ScoreEntity> scoreEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        app = (BGApplication) getApplication();

        lvScores = (ListView) findViewById(R.id.lv_scores);
        shareScore = (Button) findViewById(R.id.btn_share_score);
        scoreAdapter = new ScoreAdapter(this);
        scoreEntities = app.getDatabase().getScores();

        lvScores.setAdapter(scoreAdapter);
        scoreAdapter.setData(scoreEntities);

        shareScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.playEffect(R.raw.effect_button_clicked);
                String score = "" + scoreEntities.get(0).getScore();
                String level = "" + scoreEntities.get(0).getLevel();
                String shareText = String.format(getString(R.string.share_score), score, level);
                share(shareText);
            }
        });

        app.playBackSound();
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
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        app.playEffect(R.raw.effect_back);
        super.onBackPressed();
    }

    private void share(String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(intent);
    }

}
