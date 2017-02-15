package com.intuisis.binarygame.entities;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by gallant on 16/03/15.
 */
public class ScoreEntity {

    private static final String DEFAULT_FORMAT = "dd/MM/yy, HH:mm";

    private int id;
    private long score;
    private int level;
    private int lines;
    private long time;

    public ScoreEntity() {
        id = 0;
        score = 0;
        level = 0;
        lines = 0;
        time = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFormattedTime() {
        Date date = new Date(time * 1000);
        return "" + DateFormat.format(DEFAULT_FORMAT, date);
    }

    @Override
    public String toString() {
        return "ScoresEntity{" +
                "score=" + score +
                ", level=" + level +
                ", lines=" + lines +
                ", time=" + time +
                ", formatted_time=" + getFormattedTime() +
                '}';
    }
}
