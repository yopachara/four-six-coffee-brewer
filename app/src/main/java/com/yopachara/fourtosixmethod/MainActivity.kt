package com.yopachara.fourtosixmethod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yopachara.fourtosixmethod.core.data.model.ThemeConfig
import com.yopachara.fourtosixmethod.core.designsystem.theme.FourSixMethodTheme
import com.yopachara.fourtosixmethod.ui.FlowSixApp
import org.koin.compose.viewmodel.koinViewModel

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainActivityViewModel = koinViewModel()
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
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
