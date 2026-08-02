plugins {
    id("foursixmethod.kmp.feature")
}

kotlin {
    android {
        // The notification channel/title/action strings and the notification small icon are
        // Android platform resources (NotificationCompat takes an @DrawableRes Int, not a
        // Compose DrawableResource), so this module keeps a real res/ folder - Android
        // resource processing is off by default in KMP library modules.
        androidResources.enable = true
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.work.ktx)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.workmanager)
        }
        commonMain.dependencies {
//            compose-multiplatform-uiToolingPreview = { group = "org.jetbrains.compose.ui", name = "ui-tooling-preview", version.ref = "composeMultiplatform" }
            implementation(libs.compose.multiplatform.uiToolingPreview)
        }
    }
}
