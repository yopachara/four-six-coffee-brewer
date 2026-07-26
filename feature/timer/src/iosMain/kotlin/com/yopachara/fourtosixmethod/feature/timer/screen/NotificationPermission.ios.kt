package com.yopachara.fourtosixmethod.feature.timer.screen

import androidx.compose.runtime.Composable
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

@Composable
actual fun rememberNotificationPermissionRequester(): () -> Unit = {
    // iOS shows the system prompt only on the first request; later calls resolve
    // silently against the stored answer, so this is safe to call on every start.
    UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
        UNAuthorizationOptionAlert or UNAuthorizationOptionSound,
    ) { _, _ -> }
}
