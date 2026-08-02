plugins {
    id("foursixmethod.kmp.feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.vico.multiplatform)
        }
    }
}
