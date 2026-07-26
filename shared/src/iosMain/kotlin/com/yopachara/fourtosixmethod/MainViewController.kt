package com.yopachara.fourtosixmethod

import androidx.compose.ui.window.ComposeUIViewController
import com.yopachara.fourtosixmethod.di.initKoin
import platform.UIKit.UIViewController

/**
 * Entry point for the Xcode project: the Swift side wraps this in a `UIViewControllerRepresentable`.
 */
fun MainViewController(): UIViewController = ComposeUIViewController { FlowSixRoot() }

/**
 * Starts the shared Koin graph. iOS needs no platform declaration, but Swift cannot call a Kotlin
 * function through its default argument, so it gets its own entry point.
 */
fun initKoinIos() {
    initKoin()
}
