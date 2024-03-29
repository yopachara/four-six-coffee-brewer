plugins {
    id("foursixmethod.android.feature")
    id("foursixmethod.android.library.compose")
    id("foursixmethod.android.library.jacoco")
}

android {
    namespace = "com.yopachara.foursixmethod.feature.history"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation("com.patrykandpatrick.vico:compose-m3:1.6.5")
}
