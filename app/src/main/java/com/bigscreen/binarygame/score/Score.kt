package com.bigscreen.binarygame.score

import android.text.format.DateFormat
import java.util.Date

data class Score(
        var id: Int = 0,
        var score: Long = 0,
        var level: Int = 0,
        var lines: Int = 0,
        var time: Long = 0
) {

    fun getFormattedTime(): String {
        val date = Date(time * 1000)
        return "" + DateFormat.format("dd/MM/yy, HH:mm", date)
    }
}