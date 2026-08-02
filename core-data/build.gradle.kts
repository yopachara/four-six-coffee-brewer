plugins {
    id("foursixmethod.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core-database"))
            implementation(project(":core-model"))
            implementation(project(":core-common"))

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.dataStore.preferences.core)
            implementation(libs.okio)
        }
    }
}
