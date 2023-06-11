import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("foursixmethod.android.library")
    id("foursixmethod.android.hilt")
    kotlin("kapt")
}

android {
    namespace = "com.yopachara.fourtosixmethod.core.domain"
}

dependencies {
    implementation(project(":core-data"))
    implementation(project(":core-model"))
    implementation(project(":core-common"))
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    kapt(libs.hilt.compiler)

}