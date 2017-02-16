package com.bigscreen.binarygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.bigscreen.binarygame.adapters.ScoreAdapter;
import com.bigscreen.binarygame.models.Score;

import java.util.List;


public class ScoresActivity extends Activity {

    private static final String TAG = ScoresActivity.class.getSimpleName();

    private boolean isPaused = false;

    private BGApplication app;
    private ListView listScores;
    private Button buttonShareScore;
    private ScoreAdapter scoreAdapter;
    private List<Score> scoreEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        app = (BGApplication) getApplication();

        listScores = (ListView) findViewById(R.id.list_scores);
        buttonShareScore = (Button) findViewById(R.id.button_share_score);
        scoreAdapter = new ScoreAdapter(this);
        scoreEntities = app.getDatabase().getScores();

        listScores.setAdapter(scoreAdapter);
        scoreAdapter.setData(scoreEntities);

        buttonShareScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.playEffect(R.raw.effect_button_clicked);
                long score = scoreEntities.get(0).getScore();
                long level = scoreEntities.get(0).getLevel();
                String shareText = String.format(getString(R.string.share_score), score, level);
                share(shareText);
            }
        });

        app.playBackSound();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused) {
            app.playBackSound();
            isPaused = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        app.pauseBackSound();
        isPaused = true;
    }

    @Override
    protected void onDestroy() {
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
