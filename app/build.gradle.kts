plugins {
    id("foursixmethod.android.application")
    id("foursixmethod.android.application.compose")
    id("foursixmethod.android.application.flavors")
    id("foursixmethod.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    defaultConfig {
        applicationId = "com.yopachara.fourtosixmethod"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = com.yopachara.foursixmethod.FSMBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    namespace = "com.yopachara.fourtosixmethod"
}

dependencies {


    implementation(project(":core-common"))
    implementation(project(":core-data"))
    implementation(project(":core-designsystem"))
    implementation(project(":core-model"))
    implementation(project(":core-database"))
    implementation(project(":feature:timer"))
    implementation(project(":feature:history"))
    implementation(project(":feature:about"))
    implementation(project(":feature:settings"))

    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)
    implementation(libs.coil.kt)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.kotlinx.serialization.json)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)

}