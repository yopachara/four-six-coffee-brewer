plugins {
    id("foursixmethod.android.library")
    id("foursixmethod.android.hilt")
}

android {
    namespace = "com.yopachara.foursixmethod.core.common"
}

dependencies {
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
}