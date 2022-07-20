package com.yopachara.fourtosixmethod.core.data.model

enum class Level(
    val firstIndex: Float,
    val secondIndex: Float,
    val thirdIndex: Float? = null,
    val fourthIndex: Float? = null,
) {
    Basic(0.2f, 0.2f, 0.2f),
    Strong(0.15f, 0.15f, 0.15f, 0.15f),
    Week(0.3f, 0.3f)
}