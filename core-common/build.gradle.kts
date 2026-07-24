plugins {
    id("foursixmethod.android.library")
}

android {
    namespace = "com.yopachara.foursixmethod.core.common"
}

dependencies {
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.android)
}