import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("foursixmethod.kmp.feature")
}

kotlin {
    android {
        // This module owns the bottom-navigation label strings as composeResources, which Compose
        // Resources ships as Android assets - same reason core-designsystem needs the flag.
        androidResources.enable = true
    }

    // Direct framework export rather than CocoaPods: nothing in the dependency graph needs a
    // native iOS pod while Firebase-on-iOS stays deferred, so this avoids the Xcode-plugin toil.
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":feature:timer"))
            implementation(project(":feature:history"))
            implementation(project(":feature:settings"))
            implementation(project(":feature:about"))
            // MutableStateSerializer / SavedStateConfiguration for the explicit NavKey
            // polymorphism registration. Also arrives transitively via navigation3-runtime, but
            // this module imports it directly.
            implementation(libs.androidx.savedstate.compose)
        }
    }
}

compose.resources {
    packageOfResClass = "com.yopachara.fourtosixmethod.shared.generated.resources"
}
