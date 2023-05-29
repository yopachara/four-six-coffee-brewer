plugins {
    id("foursixmethod.android.library")
    id("foursixmethod.android.hilt")
}

android {
    namespace = "com.yopachara.fourtosixmethod.core.data"
}

dependencies {
    implementation(project(":core-database"))
    implementation(project(":core-model"))
    implementation(project(":core-common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
}