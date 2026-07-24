package com.yopachara.fourtosixmethod.util

import kotlin.math.pow
import kotlin.math.round

fun Float.roundTo(n: Int): Float {
    val factor = 10f.pow(n)
    return round(this * factor) / factor
}

fun Double.roundTo(n: Int): Double {
    val factor = 10.0.pow(n)
    return round(this * factor) / factor
}