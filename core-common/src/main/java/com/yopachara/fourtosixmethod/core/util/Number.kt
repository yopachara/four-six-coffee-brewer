package com.yopachara.fourtosixmethod.util

import kotlin.math.pow
import kotlin.math.round

// Round numerically instead of formatting to a String and parsing it back:
// String.format uses the default locale, so in locales with a comma decimal
// separator "%.1f".format(18.7f) == "18,7" and toFloat() throws.
fun Float.roundTo(n: Int): Float {
    val factor = 10.0.pow(n)
    return (round(this * factor) / factor).toFloat()
}

fun Double.roundTo(n: Int): Double {
    val factor = 10.0.pow(n)
    return round(this * factor) / factor
}
