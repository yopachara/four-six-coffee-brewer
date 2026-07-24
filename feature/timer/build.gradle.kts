plugins {
    id("foursixmethod.android.feature")
    id("foursixmethod.android.library.compose")
    id("foursixmethod.android.library.jacoco")
}

android {
    namespace = "com.yopachara.foursixmethod.feature.timer"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.work.ktx)
    implementation(libs.koin.androidx.workmanager)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
}
