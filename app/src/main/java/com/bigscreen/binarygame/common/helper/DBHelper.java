package com.bigscreen.binarygame.common.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bigscreen.binarygame.score.Score;
import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "db_binary_game";

    private static final String TB_SCORES = "tb_scores";

    private static final String KEY_ID = "id";
    private static final String KEY_SCORE = "score";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_LINES = "lines";
    private static final String KEY_TIME = "time";

    private static final String[] COLUMNS_SCORES = {KEY_ID, KEY_SCORE, KEY_LEVEL, KEY_LEVEL, KEY_LINES, KEY_TIME};

    private static final String CREATE_TB_SCORES = "CREATE TABLE " +
            TB_SCORES + " ( " +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_SCORE + " INTEGER NOT NULL, " +
            KEY_LEVEL + " INTEGER NOT NULL, " +
            KEY_LINES + " INTEGER NOT NULL, " +
            KEY_TIME + " INTEGER NOT NULL );";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TB_SCORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_SCORES);
        this.onCreate(db);
    }

    public boolean insertScore(Score entity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(entity);

        Log.i(TAG, "Insert score, " + entity.toString());

        try {
            db.insert(TB_SCORES, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Insert score failed!");
            db.close();
            return false;
        }
        Log.i(TAG, "Insert score success!");
        db.close();
        return true;
    }

    public boolean updateScore(Score entity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(entity);

        try {
            db.update(TB_SCORES, values, KEY_ID + " = ?", new String[] { ""
                    + entity.getId() });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Update score failed!");
            db.close();
            return false;
        }
        Log.i(TAG, "Update score success!");
        db.close();
        return true;
    }

    public boolean deleteScore(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TB_SCORES, KEY_ID + " = ?",
                    new String[] { "" + id });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Delete score failed!");
            db.close();
            return false;
        }
        Log.i(TAG, "Delete score success!");
        db.close();
        return true;
    }

    public boolean deleteScores() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TB_SCORES, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Delete scores failed!");
            db.close();
            return false;
        }
        Log.i(TAG, "Delete scores success!");
        db.close();
        return true;
    }

    public List<Score> getScores() {
        List<Score> entities = new ArrayList<>();
        String query = "SELECT  * FROM " + TB_SCORES + " ORDER BY " + KEY_SCORE
                + " DESC LIMIT 10";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Score entity = getScoreEntity(cursor);
                entities.add(entity);
            } while (cursor.moveToNext());
        }
        Log.i(TAG, "Select scores, " + entities.toString());

        cursor.close();
        db.close();

        return entities;
    }

    public Score getScore(int id) {
        Score entity = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TB_SCORES, COLUMNS_SCORES, KEY_ID + " = ?",
                new String[] { "" + id }, null, null, null, null);

        if (cursor.moveToFirst()) {
            entity = getScoreEntity(cursor);
            Log.d(TAG, "Select score, " + entity.toString());
        }

        cursor.close();
        db.close();

        return entity;
    }

    public Score getHighestScore() {
        Score entity = new Score();
        String query = "SELECT  * FROM " + TB_SCORES + " ORDER BY " + KEY_SCORE
                + " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            entity = getScoreEntity(cursor);
            Log.d(TAG, "Select score, " + entity.toString());
        }

        cursor.close();
        db.close();

        return entity;
    }

    private ContentValues getContentValues(Score entity) {
        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, entity.getScore());
        values.put(KEY_LEVEL, entity.getLevel());
        values.put(KEY_LINES, entity.getLines());
        values.put(KEY_TIME, entity.getTime());

        return values;
    }

    private Score getScoreEntity(Cursor cursor) {
        Score entity = new Score();
        entity.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        entity.setScore(cursor.getLong(cursor.getColumnIndex(KEY_SCORE)));
        entity.setLevel(cursor.getInt(cursor.getColumnIndex(KEY_LEVEL)));
        entity.setLines(cursor.getInt(cursor.getColumnIndex(KEY_LINES)));
        entity.setTime(cursor.getLong(cursor.getColumnIndex(KEY_TIME)));

        return entity;
    }
}
