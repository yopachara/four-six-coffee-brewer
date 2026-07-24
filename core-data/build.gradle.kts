plugins {
    id("foursixmethod.android.library")
}

android {
    namespace = "com.yopachara.fourtosixmethod.core.data"
}

dependencies {
    implementation(project(":core-database"))
    implementation(project(":core-model"))
    implementation(project(":core-common"))

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.kotlinx.coroutines.android)
}