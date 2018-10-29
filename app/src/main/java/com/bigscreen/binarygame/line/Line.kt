package com.bigscreen.binarygame.line

import java.util.*

const val GAME_MODE_ONE = 1
const val GAME_MODE_TWO = 2

data class Line(
        var type: Int = 0,
        var lines: IntArray = intArrayOf(),
        var result: Int = 0
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Line

        if (type != other.type) return false
        if (!Arrays.equals(lines, other.lines)) return false
        if (result != other.result) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = type
        result1 = 31 * result1 + Arrays.hashCode(lines)
        result1 = 31 * result1 + result
        return result1
    }
}