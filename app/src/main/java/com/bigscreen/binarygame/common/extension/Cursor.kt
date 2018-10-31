package com.bigscreen.binarygame.common.extension

import android.database.Cursor

fun <T> Cursor.toObject(converter: (movedCursor: Cursor) -> T): T? {
    val entity = if (moveToFirst()) converter(this) else null
    close()
    return entity
}

fun <T> Cursor.toList(converter: (movedCursor: Cursor) -> T): List<T> {
    val list = generateSequence { if (moveToNext()) this else null }
            .map { converter(it) }
            .toList()
    close()
    return list
}