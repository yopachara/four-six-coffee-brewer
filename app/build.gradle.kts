import java.util.Properties

plugins {
    id("foursixmethod.android.application")
    id("foursixmethod.android.application.compose")
    id("foursixmethod.android.application.flavors")
    id("foursixmethod.android.application.firebase")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val hasReleaseSigning = keystorePropertiesFile.exists()
val keystoreProperties = Properties().apply {
    if (hasReleaseSigning) load(keystorePropertiesFile.inputStream())
}

android {
    defaultConfig {
        applicationId = "com.yopachara.fourtosixmethod"
        versionCode = 5
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = com.yopachara.foursixmethod.FSMBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            ndk {
                debugSymbolLevel = "FULL"
            }
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    namespace = "com.yopachara.fourtosixmethod"
}

dependencies {

    // The whole UI, navigation and Koin graph live in :shared; this module is only the Android
    // entry point (Application + Activity), so it needs nothing else from the module graph.
    implementation(project(":shared"))

    implementation(libs.androidx.activity.compose)
    // ThemeOverlay.AppCompat.* in res/values/themes.xml.
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    // Configuration.Provider / WorkerFactory for the timer's foreground worker.
    implementation(libs.androidx.work.ktx)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.workmanager)
    debugImplementation(libs.androidx.compose.ui.tooling)

}