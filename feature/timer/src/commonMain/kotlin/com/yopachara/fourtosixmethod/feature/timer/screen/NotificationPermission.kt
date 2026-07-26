package com.yopachara.fourtosixmethod.feature.timer.screen

import androidx.compose.runtime.Composable

/**
 * Returns a callback that asks the platform for permission to post brew notifications.
 * Invoked right before a brew starts; the timer runs either way - only the notification
 * is lost when permission is denied.
 */
@Composable
expect fun rememberNotificationPermissionRequester(): () -> Unit
