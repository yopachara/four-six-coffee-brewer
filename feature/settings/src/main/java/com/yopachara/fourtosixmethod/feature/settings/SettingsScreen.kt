package com.yopachara.fourtosixmethod.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yopachara.fourtosixmethod.core.data.model.AccentColor
import com.yopachara.fourtosixmethod.core.data.model.ThemeConfig
import com.yopachara.fourtosixmethod.core.data.model.UserSettings

@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settings by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        settings = settings,
        onThemeConfigChange = viewModel::setThemeConfig,
        onAccentColorChange = viewModel::setAccentColor,
        onStepsDefaultExpandedChange = viewModel::setStepsDefaultExpanded,
        modifier = modifier,
    )
}

@Composable
internal fun SettingsScreen(
    settings: UserSettings,
    onThemeConfigChange: (ThemeConfig) -> Unit,
    onAccentColorChange: (AccentColor) -> Unit,
    onStepsDefaultExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 48.dp)
    ) {
        Text(
            text = "4:6 METHOD",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
        )

        SettingsSectionLabel(text = "Theme")
        ThemeSelector(
            selected = settings.themeConfig,
            onSelect = onThemeConfigChange
        )
        Text(
            text = when (settings.themeConfig) {
                ThemeConfig.SYSTEM -> "Follows your device's light or dark setting"
                ThemeConfig.LIGHT -> "Always light, whatever the device says"
                ThemeConfig.DARK -> "Always dark, whatever the device says"
            },
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        SettingsSectionLabel(text = "Accent color", modifier = Modifier.padding(top = 24.dp))
        AccentColorSelector(
            selected = settings.accentColor,
            onSelect = onAccentColorChange
        )

        SettingsSectionLabel(text = "Brew steps", modifier = Modifier.padding(top = 24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Expand full schedule",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Show every pour step on the timer by default",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Switch(
                checked = settings.stepsDefaultExpanded,
                onCheckedChange = onStepsDefaultExpandedChange,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
private fun SettingsSectionLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun ThemeSelector(
    selected: ThemeConfig,
    onSelect: (ThemeConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ThemeConfig.entries.forEach { config ->
            val isSelected = config == selected
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.surface
                        else Color.Transparent
                    )
                    .clickable { onSelect(config) }
                    .padding(vertical = 10.dp, horizontal = 4.dp)
            ) {
                Text(
                    text = when (config) {
                        ThemeConfig.SYSTEM -> "System"
                        ThemeConfig.LIGHT -> "Light"
                        ThemeConfig.DARK -> "Dark"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AccentColorSelector(
    selected: AccentColor,
    onSelect: (AccentColor) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AccentColor.entries.forEach { accent ->
            val isSelected = accent == selected
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(accent.argb))
                    .then(
                        if (isSelected) Modifier.border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = CircleShape
                        ) else Modifier
                    )
                    .clickable { onSelect(accent) }
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "${accent.name} selected",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSettingsScreen() {
    SettingsScreen(
        settings = UserSettings(),
        onThemeConfigChange = {},
        onAccentColorChange = {},
        onStepsDefaultExpandedChange = {},
    )
}
