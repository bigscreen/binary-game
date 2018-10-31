package com.bigscreen.binarygame.storage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bigscreen.binarygame.common.extension.toList
import com.bigscreen.binarygame.common.extension.toObject
import com.bigscreen.binarygame.score.Score
import io.reactivex.Observable

private const val DB_VERSION = 1
private const val DB_NAME = "db_binary_game"

class DBService(context: Context) : SQLiteOpenHelper(context,
        DB_NAME, null, DB_VERSION) {

    companion object {
        private const val TB_SCORES = "tb_scores"
        private const val KEY_ID = "id"
        private const val KEY_SCORE = "score"
        private const val KEY_LEVEL = "level"
        private const val KEY_LINES = "lines"
        private const val KEY_TIME = "time"
        private const val CREATE_TB_SCORES = "CREATE TABLE " +
                "$TB_SCORES ( " +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_SCORE INTEGER NOT NULL, " +
                "$KEY_LEVEL INTEGER NOT NULL, " +
                "$KEY_LINES INTEGER NOT NULL, " +
                "$KEY_TIME INTEGER NOT NULL );"
    }

    private val columnsScore = arrayOf(KEY_ID,
            KEY_SCORE,
            KEY_LEVEL,
            KEY_LEVEL,
            KEY_LINES,
            KEY_TIME)

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TB_SCORES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TB_SCORES")
        onCreate(db)
    }

    fun insertScore(entity: Score): Observable<Boolean> = Observable
            .fromCallable {
                writableDatabase.insert(
                        TB_SCORES, null, getScoreContentValues(entity))
            }
            .map { it != -1L }
            .doOnSubscribe { writableDatabase.close() }

    fun updateScore(newScore: Score): Observable<Boolean> = Observable
            .fromCallable {
                writableDatabase.update(
                        TB_SCORES,
                        getScoreContentValues(newScore),
                        "$KEY_ID = ?",
                        arrayOf(newScore.id.toString())
                )
            }
            .map { it > 0 }
            .doOnSubscribe { writableDatabase.close() }

    fun deleteScore(scoreId: Int): Observable<Boolean> = Observable
            .fromCallable {
                writableDatabase.delete(
                        TB_SCORES, "$KEY_ID = ?",
                        arrayOf(scoreId.toString())
                )
            }.map { it > 0 }
            .doOnSubscribe { writableDatabase.close() }

    fun deleteAllScores(): Observable<Boolean> = Observable
            .fromCallable {
                writableDatabase.delete(
                        TB_SCORES, null, null)
            }.map { it > 0 }
            .doOnSubscribe { writableDatabase.close() }

    fun getScores(): Observable<List<Score>> = Observable
            .fromCallable {
                writableDatabase.rawQuery(
                        "SELECT  * FROM $TB_SCORES ORDER BY $KEY_SCORE DESC LIMIT 10",
                        null
                )
            }
            .map { it.toList { movedCursor -> getScoreFromCursor(movedCursor) } }
            .doOnSubscribe { writableDatabase.close() }

    fun getScore(scoreId: Int): Observable<Score?> = Observable
            .fromCallable {
                writableDatabase.query(
                        TB_SCORES, columnsScore, "$KEY_ID = ?",
                        arrayOf(scoreId.toString()), null, null,
                        null, null)
            }
            .map { it.toObject { movedCursor -> getScoreFromCursor(movedCursor) } }
            .doOnSubscribe { writableDatabase.close() }

    fun getHighestScore(): Observable<Score> = Observable
            .fromCallable {
                writableDatabase.rawQuery(
                        "SELECT  * FROM $TB_SCORES ORDER BY $KEY_SCORE DESC LIMIT 1",
                        null
                )
            }
            .map { it.toObject { movedCursor -> getScoreFromCursor(movedCursor) } ?: Score() }
            .doOnSubscribe { writableDatabase.close() }

    private fun getScoreContentValues(score: Score): ContentValues = ContentValues().apply {
        put(KEY_SCORE, score.score)
        put(KEY_LEVEL, score.level)
        put(KEY_LINES, score.lines)
        put(KEY_TIME, score.time)
    }

    private fun getScoreFromCursor(cursor: Cursor): Score = Score(
            cursor.getInt(cursor.getColumnIndex(
                    KEY_ID)),
            cursor.getLong(cursor.getColumnIndex(
                    KEY_SCORE)),
            cursor.getInt(cursor.getColumnIndex(
                    KEY_LEVEL)),
            cursor.getInt(cursor.getColumnIndex(
                    KEY_LINES)),
            cursor.getLong(cursor.getColumnIndex(
                    KEY_TIME))
    )

}
