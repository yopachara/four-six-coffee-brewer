plugins {
    id("foursixmethod.android.library")
}

android {
    namespace = "com.yopachara.fourtosixmethod.core.domain"
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-model"))
    implementation(project(":core-common"))
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
}