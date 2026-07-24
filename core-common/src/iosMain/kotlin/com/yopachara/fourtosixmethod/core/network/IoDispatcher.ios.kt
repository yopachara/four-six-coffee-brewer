package com.yopachara.fourtosixmethod.core.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// Kotlin/Native has no public Dispatchers.IO; Default is the standard iOS substitute for
// the app's light disk work (Room + DataStore). Room's own bundled driver serializes writes.
actual val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
