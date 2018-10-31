package com.bigscreen.binarygame.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.bigscreen.binarygame.BGApplication;
import com.bigscreen.binarygame.R;

import com.bigscreen.binarygame.misc.SoundService;
import com.bigscreen.binarygame.storage.DBHelper;
import java.util.List;
import javax.inject.Inject;


public class ScoreActivity extends Activity {

    private static final String TAG = ScoreActivity.class.getSimpleName();

    private boolean isPaused = false;

    private ListView listScores;
    private Button buttonShareScore;
    private ScoreAdapter scoreAdapter;
    private List<Score> scoreEntities;

    @Inject
    public DBHelper dbHelper;
    @Inject
    public SoundService soundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BGApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_scores);

        listScores = (ListView) findViewById(R.id.list_scores);
        buttonShareScore = (Button) findViewById(R.id.button_share_score);
        scoreAdapter = new ScoreAdapter(this);
        scoreEntities = dbHelper.getScores();

        listScores.setAdapter(scoreAdapter);
        scoreAdapter.setData(scoreEntities);

        buttonShareScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundService.playEffect(R.raw.effect_button_clicked);
                long score = scoreEntities.get(0).getScore();
                long level = scoreEntities.get(0).getLevel();
                String shareText = String.format(getString(R.string.share_score), score, level);
                share(shareText);
            }
        });

        soundService.playBackSound();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPaused) {
            soundService.playBackSound();
            isPaused = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundService.pauseBackSound();
        isPaused = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        soundService.playEffect(R.raw.effect_back);
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
