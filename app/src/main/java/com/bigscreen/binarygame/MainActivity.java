package com.bigscreen.binarygame;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigscreen.binarygame.adapters.LineAdapter;
import com.bigscreen.binarygame.models.Line;
import com.bigscreen.binarygame.extras.CustomCountDownTimer;
import com.bigscreen.binarygame.fragments.Keyboard;
import com.bigscreen.binarygame.helpers.AppHelper;
import com.bigscreen.binarygame.models.Score;
import com.bigscreen.binarygame.view.dialogs.BeautyDialog;
import com.bigscreen.binarygame.view.dialogs.PauseDialog;
import com.bigscreen.binarygame.view.items.LineItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LineItem.OnLineItemClickListener,
        Keyboard.OnKeyboardItemClickListener, View.OnClickListener, PauseDialog.PauseDialogListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PREFIX_NUMBER = "%d";
    private static final int LINE_LENGTH = 8;
    private static final int MAX_LINES = 7;
    private static final int MAX_LINES_PER_LEVEL = 15;
    private static final int MAX_LEVEL = 10;

    private BGApplication application;

    private int canvasHeight, lineHeight, decimalHintHeight, canvasMargin;

    private int[] decimal = {128, 64, 32, 16, 8, 4, 2, 1};
    private int[] timePerLevel = {12000, 11000, 10000, 9000, 8000, 7000, 6000, 5000, 4000, 3000};
    private int linesLength = 0, flagGameState = 0, linePerLevel = 0, lineState = 0, levelState = 1;
    private int timeLimit, selectedPosition;
    private long score = 0, highestScore;
    private boolean isPaused = false, isDialogShowed = false, readyToExit = false;

    private Animation lineAddedAnim, lineRemovedAnim, keyboardAttachedAnim, keyboardDetachedAnim;

    private LinearLayout layoutCanvas;
    private LineAdapter lineAdapter;
    private FrameLayout frameKeyboard;
    private ImageView imageBtnPause;
    private TextView selectedTextView;
    private TextView textScore, textLevel, textLines;
    private Line selectedLine;
    private CustomCountDownTimer lineCountDown;
    private PauseDialog pauseDialog;
    private BeautyDialog confirmDialogRestart;
    private PowerManager.WakeLock wakeLockManager;
    private MediaPlayer mpBackSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = (BGApplication) getApplication();

        decimalHintHeight = getResources().getDimensionPixelSize(R.dimen.decimal_hint_height);
        canvasMargin = getResources().getDimensionPixelSize(R.dimen.game_canvas_horizontal_margin);
        canvasHeight = application.getScreenSize(this)[1] - ((2 * decimalHintHeight));
        lineHeight = canvasHeight / MAX_LINES;

        lineAddedAnim = AnimationUtils.loadAnimation(this, R.anim.line_added);
        lineRemovedAnim = AnimationUtils.loadAnimation(this, R.anim.line_removed);
        keyboardAttachedAnim = AnimationUtils.loadAnimation(this, R.anim.keyboard_attached);
        keyboardDetachedAnim = AnimationUtils.loadAnimation(this, R.anim.keyboard_detached);

        layoutCanvas = (LinearLayout) findViewById(R.id.layout_game_canvas);
        frameKeyboard = (FrameLayout) findViewById(R.id.frame_keyboard);
        textScore = (TextView) findViewById(R.id.text_score);
        textLevel = (TextView) findViewById(R.id.text_level);
        textLines = (TextView) findViewById(R.id.text_lines);
        imageBtnPause = (ImageView) findViewById(R.id.image_btn_pause);

        lineAdapter = new LineAdapter(this, layoutCanvas, lineHeight);
        pauseDialog = new PauseDialog(this, this);

        lineAdapter.setData(new ArrayList<Line>());
        highestScore = application.getDatabase().getHighestScore().getScore();

        imageBtnPause.setOnClickListener(this);
        pauseDialog.setOnShowListener(onShowListener);
        pauseDialog.setOnDismissListener(onDialogDismissListener);

        initKeyboard();
        initWakeLock();
        initBackSound();

        playBackSound();
        startGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "OnResume, lineAdapter.getList()= " + lineAdapter.getList());
        if (isPaused && !isDialogShowed) {
            playBackSound();
            if (flagGameState > 2) {
                lineCountDown.resume();
                lineCountDown.start();
            }
            isPaused = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "OnPause, lineAdapter.getList()= " + lineAdapter.getList());
        isPaused = true;
        pauseBackSound();
        if (flagGameState > 2)
            lineCountDown.pause();
        if (!readyToExit)
            pauseDialog.show();
    }

    @Override
    protected void onDestroy() {
        lineCountDown.cancel();
        stopAndReleaseBackSound();
        wakeLockManager.release();
        Log.i(TAG, "onDestroy, lineAdapter.getList()= " + lineAdapter.getList());
        super.onDestroy();
    }

    private void populateData() {
        List<Line> lineEntities = new ArrayList<>();
        for(int i=0; i<MAX_LINES; i++) {
            Line line = new Line();
            int[] lines = new int[LINE_LENGTH];
            List<Integer> randomIndex = getRandomLineIndex();
            for(int j=0; j<LINE_LENGTH; j++) {
                lines[j] = 0;
                for (int index : randomIndex) {
                    if (j == index) {
                        lines[j] = 1;
                    }
                }
            }
            Log.i(TAG, "line[" + i + "]=" + Arrays.toString(lines));
            line.setLines(lines);
            int randomType = AppHelper.getRandomNumber(1, 2);
            line.setType(randomType);
            if (randomType == Line.GAME_MODE_ONE) {
                line.setResult(getRandomDecimal());
            } else if (randomType == Line.GAME_MODE_TWO) {
                line.setResult(0);
            }
            lineEntities.add(line);
        }
        lineAdapter.setData(lineEntities);
        linesLength = lineEntities.size();
    }

    private int getRandomDecimal() {
        int customDecimal = 0;
        int max = 0;
        if (levelState == 1) {
            max = 1;
        } else if (levelState == 2) {
            max = AppHelper.getRandomNumber(1, 2);
        } else if (levelState >= 3 && levelState <= 4) {
            max = AppHelper.getRandomNumber(2, 3);
        } else if (levelState >= 5 && levelState <= 7) {
            max = AppHelper.getRandomNumber(2, 4);
        } else if (levelState >= 8 && levelState <= 10) {
            max = AppHelper.getRandomNumber(3, 4);
        }
        List<Integer> randomIndex = AppHelper.getUniqueIndexList(max);
        for (int index : randomIndex) {
            customDecimal = customDecimal + decimal[index];
        }
        return customDecimal;
    }

    private List<Integer> getRandomLineIndex() {
        List<Integer> randomList = new ArrayList<>();
        int limit = 0;
        if (levelState == 1) {
            limit = 1;
        } else if (levelState >= 2 && levelState <= 4) {
            limit = AppHelper.getRandomNumber(1, 3);
        } else if (levelState >= 5 && levelState <= 7) {
            limit = AppHelper.getRandomNumber(3, 4);
        } else if (levelState >= 8 && levelState <=10) {
            limit = AppHelper.getRandomNumber(4, 5);
        }
        for (int length=0; length<limit; length++) {
            int temp = AppHelper.getRandomIndex();
            while (!AppHelper.isValueExist(temp, randomList)) {
                randomList.add(temp);
            }
        }
        return randomList;
    }

    /**
     * Start the game.
     */
    private void startGame() {
        addFirstData(Line.GAME_MODE_ONE);
        timeLimit = timePerLevel[0];
        initCountDown(timeLimit);
    }

    /**
     * Restart dan remove game progress.
     */
    private void restartGame() {
        if (pauseDialog != null && pauseDialog.isShowing()) {
            pauseDialog.dismiss();
        }
        lineCountDown.cancel();
        linesLength = 0;
        flagGameState = 0;
        linePerLevel = 0;
        lineState = 0;
        levelState = 1;
        score = 0;
        lineAdapter.clearData();
        startGame();
        playBackSound();
        isPaused = false;
    }

    /**
     * Setup countdown for automatically adding data periodically.
     * @param limit countdown time limit in milli second.
     */
    private void initCountDown(final int limit) {
        lineCountDown = new CustomCountDownTimer(limit, 1000, true) {
            int tick = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                tick++;
                Log.e(TAG, "onTick, " + tick);
                if (tick == (limit/1000)) {
                    tick = 0;
                }
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "Check linesLength = " + linesLength);
                if (linesLength > (MAX_LINES-1)) {
                    onGameOver();
                } else {
                    addData();
                    restart(limit);
                }
            }
        };
    }

    /**
     * Add line data to list adapter, but it's only ran on first line and the second line to be added
     * when the game is played.
     * @param gameMode game mode 1 or mode 2
     */
    private void addFirstData(int gameMode) {
        Line line = new Line();
        int[] lines = new int[LINE_LENGTH];
        List<Integer> randomIndex = getRandomLineIndex();
        for(int j=0; j<LINE_LENGTH; j++) {
            lines[j] = 0;
            for (int index : randomIndex) {
                if (j == index) {
                    lines[j] = 1;
                }
            }
        }
        Log.i(TAG, "add line=" + Arrays.toString(lines));
        line.setLines(lines);
        line.setType(gameMode);
        if (gameMode == Line.GAME_MODE_ONE) {
            line.setResult(getRandomDecimal());
        } else if (gameMode == Line.GAME_MODE_TWO) {
            line.setResult(0);
        }

        if (!isAnswerTrue(line)) {
            lineAdapter.appendSingleData(line);
            layoutCanvas.getChildAt(lineAdapter.getCount() - 1).startAnimation(lineAddedAnim);
            application.playEffect(R.raw.effect_line_added);
            linesLength++;
            flagGameState++;
        } else {
            if (flagGameState == 0) {
                addFirstData(Line.GAME_MODE_ONE);
            } else {
                addFirstData(Line.GAME_MODE_TWO);
            }
        }
    }

    /**
     * Add line data to list adapter.
     */
    private void addData() {
        Line line = new Line();
        int[] lines = new int[LINE_LENGTH];
        List<Integer> randomIndex = getRandomLineIndex();
        for(int j=0; j<LINE_LENGTH; j++) {
            lines[j] = 0;
            for (int index : randomIndex) {
                if (j == index) {
                    lines[j] = 1;
                }
            }
        }
        Log.i(TAG, "add line=" + Arrays.toString(lines));
        line.setLines(lines);
        int randomType = AppHelper.getRandomNumber(1, 2);
        line.setType(randomType);
        if (randomType == Line.GAME_MODE_ONE) {
            line.setResult(getRandomDecimal());
        } else if (randomType == Line.GAME_MODE_TWO) {
            line.setResult(0);
        }

        if (!isAnswerTrue(line)) {
            lineAdapter.appendSingleData(line);
            layoutCanvas.getChildAt(lineAdapter.getCount() - 1).startAnimation(lineAddedAnim);
            application.playEffect(R.raw.effect_line_added);
            linesLength++;
            flagGameState++;
            if (frameKeyboard.getVisibility() == View.VISIBLE) {
                selectedTextView = lineAdapter.getTvResult(selectedPosition);
            }
            Log.i(TAG, "linesLength = " + linesLength);
        } else {
            addData();
        }
    }

    /**
     * Remove line data on list adapter. Removing data is only triggered when the answer is correct.
     * @param position index on list.
     */
    private void removeData(final int position) {
        layoutCanvas.getChildAt(position).startAnimation(lineRemovedAnim);
        application.playEffect(R.raw.effect_line_removed);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                lineAdapter.removeData(position);
                linesLength--;
                updateScore();
            }
        }, lineRemovedAnim.getDuration());
    }

    /**
     * Check the answer after one line is done.
     * @param position index on list.
     */
    private void checkAnswer(int position) {
        if (isAnswerTrue(lineAdapter.getItem(position))) {
            removeData(position);
            linePerLevel++;
            lineState++;
            if (linePerLevel <= MAX_LINES_PER_LEVEL)
                Log.i(TAG, "Lines left per level = " + (MAX_LINES_PER_LEVEL - linePerLevel));
        } else {
            Log.i(TAG, "Answer is false...");
        }
    }

    /**
     * Check line item count if it is already same with result line or not.
     * @param line {@link com.bigscreen.binarygame.models.Line} which will be checked.
     * @return true if its count is same, dan false if not.
     */
    private boolean isAnswerTrue(Line line) {
        int tempCount = 0;
        int itemIndex = 0;
        for (int item : line.getLines()) {
            if (item == 1) {
                tempCount = tempCount + decimal[itemIndex];
            }
            itemIndex++;
        }
        return tempCount == line.getResult();
    }

    /**
     * Update game score.
     */
    private void updateScore() {
        if (flagGameState < 2) {
            addFirstData(Line.GAME_MODE_TWO);
        } else if (flagGameState == 2) {
            addData();
            lineCountDown.start();
        }
        if (linesLength == 0 && flagGameState > 2) {
            score = score + 1000;
            for (int i=0; i<3; i++) {
                addData();
            }
        } else {
            score = score + 100;
        }
        textScore.setText(getTextFromNumber(score));
        textLines.setText(getTextFromNumber(lineState));
        setLevelState();
    }

    /**
     * Check and update the level if it should be level up or not.
     */
    private void setLevelState() {
        if (linePerLevel > MAX_LINES_PER_LEVEL) {
            if (levelState >= MAX_LEVEL) {
                Log.i(TAG, "Game was ended!");
                onGameOver();
            } else {
                linePerLevel = 1;
                levelState++;
                lineCountDown.cancel();
                timeLimit = timePerLevel[levelState - 1];
                initCountDown(timeLimit);
                lineCountDown.start();
                textLevel.setText(getTextFromNumber(lineState));
                Log.i(TAG, "Lines left per level = " + MAX_LINES_PER_LEVEL);
            }
        }
        Log.i(TAG, "Current line/level = " + linePerLevel + "/" + levelState);
    }

    /**
     * Save the score to database.
     */
    private void saveScore() {
        if (this.score == 0)
            return;

        Score score = new Score();
        score.setScore(this.score);
        score.setLevel(levelState);
        score.setLines(lineState);
        score.setTime(System.currentTimeMillis() / 1000);

        if (application.getDatabase().insertScore(score))
            Log.i(TAG, "Game score saved!");
        else
            Log.e(TAG, "Game score could not saved!");
    }

    /**
     * Show confirmation dialog when game will be restarted.
     */
    private void showConfirmRestart() {
        if (confirmDialogRestart == null) {
            confirmDialogRestart = new BeautyDialog(this);
            confirmDialogRestart.setCancelable(false);
            confirmDialogRestart.setMessage(R.string.confirm_restart_game);
            confirmDialogRestart.setPositiveButton(new BeautyDialog.OnClickListener() {
                @Override
                public void onClick(Dialog dialog, int which) {
                    application.playEffect(R.raw.effect_button_clicked);
                    restartGame();
                }
            });
            confirmDialogRestart.setNegativeButton(new BeautyDialog.OnClickListener() {
                @Override
                public void onClick(Dialog dialog, int which) {
                    application.playEffect(R.raw.effect_back);
                    dialog.dismiss();
                }
            });
        }
        confirmDialogRestart.show();
    }

    /**
     * Show exit confirmation dialog.
     * @param isBackPressed set dialog will be shown after back key pressed or not.
     */
    private void showConfirmExit(final boolean isBackPressed) {
        BeautyDialog confirmDialogExit = new BeautyDialog(this);
        confirmDialogExit.setCancelable(false);
        confirmDialogExit.setMessage(R.string.confirm_exit_game);
        confirmDialogExit.setPositiveButton(new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                onExitGame();
            }
        });
        confirmDialogExit.setNegativeButton(new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                application.playEffect(R.raw.effect_back);
                dialog.cancel();
            }
        });
        if (isBackPressed) {
            confirmDialogExit.setOnDismissListener(onDialogDismissListener);
        }
        confirmDialogExit.show();
    }

    /**
     * Triggered when exit the game.
     */
    private void onExitGame() {
        readyToExit = true;
        if (pauseDialog != null && pauseDialog.isShowing()) {
            pauseDialog.dismiss();
        }
        saveScore();
        application.playEffect(R.raw.effect_back);
        finish();
    }

    /**
     * Triggered when the game is completed.
     */
    private void onGameOver() {
        lineCountDown.cancel();
        saveScore();
        String goMessage;
        if (score > highestScore) {
            highestScore = score;
            goMessage = getString(R.string.score) + ":\n" + score + "\n" + getString(R.string.txt_highest_score);
        } else {
            goMessage = getString(R.string.score) + ":\n" + score;
        }

        application.playEffect(R.raw.effect_game_over);

        BeautyDialog gameOverDialog;
        gameOverDialog = new BeautyDialog(this);
        gameOverDialog.setCancelable(false);
        gameOverDialog.setTitle(R.string.game_over);
        gameOverDialog.setMessage(goMessage);
        gameOverDialog.setPositiveButton(R.string.play_again, new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                application.playEffect(R.raw.effect_button_clicked);
                restartGame();
            }
        });
        gameOverDialog.setNegativeButton(R.string.exit, new BeautyDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog, int which) {
                application.playEffect(R.raw.effect_back);
                readyToExit = true;
                finish();
            }
        });
        gameOverDialog.show();
    }

    /**
     * Setup {@link android.os.PowerManager.WakeLock}, it will make screen keep awake.
     */
    private void initWakeLock() {
        wakeLockManager = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        wakeLockManager.acquire();
    }

    /**
     * Setup game keyboard fragment.
     */
    private void initKeyboard() {
        Fragment content = new Keyboard();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_keyboard, content, content.getTag())
                .commit();
    }

    /**
     * Show game keyboard within its animation.
     */
    private void showKeyboard() {
        imageBtnPause.setVisibility(View.GONE);
        frameKeyboard.setVisibility(View.VISIBLE);
        frameKeyboard.startAnimation(keyboardAttachedAnim);
    }

    /**
     * Hide game keyboard within its animation.
     */
    private void hideKeyboard() {
        frameKeyboard.startAnimation(keyboardDetachedAnim);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                frameKeyboard.setVisibility(View.GONE);
                imageBtnPause.setVisibility(View.VISIBLE);
            }
        }, keyboardDetachedAnim.getDuration());
    }

    /**
     * Setup game back sound.
     */
    private void initBackSound() {
        releaseMPBackSound();
        mpBackSound = MediaPlayer.create(this, R.raw.backsound_2);
        mpBackSound.setLooping(true);
    }

    /**
     * Play game back sound music.
     */
    private void playBackSound() {
        if (!application.getSession().isMusicEnabled() || mpBackSound == null)
            return;
        if (!mpBackSound.isPlaying())
            mpBackSound.start();
    }

    /**
     * Pause game back sound music.
     */
    private void pauseBackSound() {
        if (mpBackSound == null)
            return;
        if (mpBackSound.isPlaying())
            mpBackSound.pause();
    }

    /**
     * Stop game back sound music.
     */
    private void stopBackSound() {
        if (mpBackSound == null)
            return;
        if (mpBackSound.isPlaying())
            mpBackSound.stop();
    }

    /**
     * Stop and release back sound music {@link android.media.MediaPlayer}.
     */
    public void stopAndReleaseBackSound() {
        if (mpBackSound != null) {
            if (mpBackSound.isPlaying()) {
                mpBackSound.stop();
                releaseMPBackSound();
            } else {
                releaseMPBackSound();
            }
        }
    }

    /**
     * Release back sound music {@link android.media.MediaPlayer}.
     */
    private void releaseMPBackSound() {
        if (mpBackSound != null) {
            mpBackSound.release();
            mpBackSound = null;
        }
    }

    DialogInterface.OnShowListener onShowListener = new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialog) {
            isDialogShowed = true;
        }
    };

    DialogInterface.OnDismissListener onDialogDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            isDialogShowed = false;
            onResume();
        }
    };

    @Override
    public void onLineItemClick(View v, int parentPosition, int itemPosition, Line line) {
        application.playEffect(R.raw.effect_button_clicked);
        selectedPosition = parentPosition;
        selectedLine = line;
        if (itemPosition == LineItem.RESULT_CLICK) {
            selectedTextView = (TextView) v;
            showKeyboard();
        } else {
            if (frameKeyboard.getVisibility() == View.VISIBLE)
                hideKeyboard();
            lineAdapter.updateItem(parentPosition, line);
            checkAnswer(parentPosition);
        }
    }

    @Override
    public void onKeyboardItemClick(View v, int key) {
        application.playEffect(R.raw.effect_button_clicked);
        CharSequence strTemp = selectedTextView.getText().toString();
        if (key == Keyboard.KEY_DELETE) {
            CharSequence strDeleted = "";
            if (strTemp.length() > 1) {
                strDeleted = strTemp.subSequence(0, strTemp.length() - 1);
            }
            selectedTextView.setText(strDeleted);
            if (strDeleted.equals(""))
                selectedLine.setResult(0);
            else
                selectedLine.setResult(Integer.parseInt(""+strDeleted));
            lineAdapter.updateItem(selectedPosition, selectedLine);
        } else if (key == Keyboard.KEY_OK) {
            hideKeyboard();
            lineAdapter.updateItem(selectedPosition, selectedLine);
            checkAnswer(selectedPosition);
        } else {
            if (key == 0 && strTemp.equals("")) {
                Log.i("Keyboard", "0 at char[0] is useless");
            } else {
                String strEdited = strTemp + String.valueOf(key);
                selectedTextView.setText(strEdited);
                selectedLine.setResult(Integer.parseInt(strEdited));
                lineAdapter.updateItem(selectedPosition, selectedLine);
            }
        }
    }

    @Override
    public void onClick(View v) {
        application.playEffect(R.raw.effect_button_clicked);
        switch (v.getId()) {
            case R.id.image_btn_pause : {
                onPause();
                break;
            }
            default: break;
        }
    }

    @Override
    public void onPauseDialogClicked(int clickedButton) {
        application.playEffect(R.raw.effect_button_clicked);
        switch (clickedButton) {
            case PauseDialog.BUTTON_PLAY : {
                pauseDialog.dismiss();
                onResume();
                break;
            }
            case PauseDialog.BUTTON_REPLAY : {
                showConfirmRestart();
                break;
            }
            case PauseDialog.BUTTON_SETTING : {
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }
            case PauseDialog.BUTTON_EXIT : {
                showConfirmExit(false);
                break;
            }
            default: break;
        }
    }

    @Override
    public void onBackPressed() {
        application.playEffect(R.raw.effect_button_clicked);
        showConfirmExit(true);
        lineCountDown.pause();
        isDialogShowed = true;
        isPaused = true;
        pauseBackSound();
    }

    private String getTextFromNumber(long number) {
        return String.format(Locale.getDefault(), PREFIX_NUMBER, number);
    }
}
