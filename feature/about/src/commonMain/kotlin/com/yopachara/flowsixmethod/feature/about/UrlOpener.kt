package com.yopachara.flowsixmethod.feature.about

import androidx.compose.runtime.Composable

/** Returns a callback that opens [url] in the platform browser. */
@Composable
expect fun rememberUrlOpener(): (String) -> Unit
