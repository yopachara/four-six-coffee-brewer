package com.yopachara.fourtosixmethod.core.data.model

enum class ThemeConfig {
    SYSTEM,
    LIGHT,
    DARK,
}

enum class AccentColor(val argb: Long) {
    CLAY(0xFFB5622B),
    EMBER(0xFF8C3A2E),
    OCEAN(0xFF3B7A93),
    OLIVE(0xFF5B6B45),
}

data class UserSettings(
    val themeConfig: ThemeConfig = ThemeConfig.SYSTEM,
    val accentColor: AccentColor = AccentColor.CLAY,
    val stepsDefaultExpanded: Boolean = false,
)
