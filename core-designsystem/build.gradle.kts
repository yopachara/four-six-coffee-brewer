plugins {
    id("foursixmethod.kmp.library")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    android {
        // Required for composeResources to reach the APK. Compose Resources packages its
        // files as Android *assets*, wiring them via `variant.sources.assets`, which the KMP
        // Android library plugin leaves null while resource processing is disabled (its
        // default). Without this the copy task is still registered but never connected, and
        // every painterResource() throws MissingResourceException at runtime.
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            api(compose.foundation)
            api(compose.material3)
            api(compose.materialIconsExtended)
            api(compose.ui)
            api(compose.components.resources)
        }
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.yopachara.fourtosixmethod.core.designsystem.generated.resources"
}
