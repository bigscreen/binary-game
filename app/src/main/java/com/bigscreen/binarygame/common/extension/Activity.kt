package com.bigscreen.binarygame.common.extension

import android.app.Activity
import android.graphics.Point

fun Activity.getScreenSize(): Map<String, Int> {
    val point = Point()
    windowManager.defaultDisplay.getSize(point)
    return mapOf("width" to point.x, "height" to point.y)
}

fun Activity.getScreenWidth() = getScreenSize()["width"] ?: 0

fun Activity.getScreenHeight() = getScreenSize()["height"] ?: 0