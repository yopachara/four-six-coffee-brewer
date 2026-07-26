package com.yopachara.fourtosixmethod

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yopachara.fourtosixmethod.core.data.model.ThemeConfig
import com.yopachara.fourtosixmethod.core.designsystem.theme.FourSixMethodTheme
import com.yopachara.fourtosixmethod.ui.FlowSixApp
import org.koin.compose.viewmodel.koinViewModel

/**
 * The whole UI, from the theme down. Both platform entry points mount this and nothing else, so
 * they only have to differ in how they get a native view out of it.
 */
@Composable
fun FlowSixRoot() {
    val viewModel: AppViewModel = koinViewModel()
    val settings by viewModel.userSettings.collectAsStateWithLifecycle()
    val darkTheme = when (settings.themeConfig) {
        ThemeConfig.LIGHT -> false
        ThemeConfig.DARK -> true
        ThemeConfig.SYSTEM -> isSystemInDarkTheme()
    }

    FourSixMethodTheme(
        darkTheme = darkTheme,
        accentColor = Color(settings.accentColor.argb),
    ) {
        FlowSixApp()
    }
}
