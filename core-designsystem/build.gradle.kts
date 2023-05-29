plugins {
    id("foursixmethod.android.library")
    id("foursixmethod.android.library.compose")
}

android {
    namespace = "com.yopachara.fourtosixmethod.core.designsystem"
}

dependencies {

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.kt.compose)
}