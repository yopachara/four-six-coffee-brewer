plugins {
    id("foursixmethod.android.library")
    id("foursixmethod.android.hilt")
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

    ksp(libs.hilt.compiler)

}