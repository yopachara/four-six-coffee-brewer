plugins {
    id("foursixmethod.android.library")
    id("foursixmethod.android.hilt")
    id("foursixmethod.android.room")
    id("kotlinx-serialization")
}

android {
    namespace = "com.yopachara.fourtosixmethod.core.database"
}

dependencies {
    implementation(project(":core-model"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}
