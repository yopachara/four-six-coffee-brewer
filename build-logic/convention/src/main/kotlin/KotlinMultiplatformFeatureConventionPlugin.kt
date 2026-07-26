import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * KMP feature module: [KotlinMultiplatformConventionPlugin] + Compose Multiplatform + shared
 * project dependencies wired into `commonMain` (screens/ViewModels/DI). Koin + Compose ViewModel
 * are common; navigation3 stays Android-only (`androidMain`) until the Phase 7 iOS shell moves the
 * shared nav host + polymorphic `NavKey` serialization to common.
 */
class KotlinMultiplatformFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("foursixmethod.kmp.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            val compose = ComposePlugin.Dependencies(target)

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(project(":core-model"))
                        implementation(project(":core-designsystem"))
                        implementation(project(":core-data"))
                        implementation(project(":core-database"))
                        implementation(project(":core-common"))
                        implementation(project(":core-domain"))

                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material3)
                        implementation(compose.materialIconsExtended)
                        implementation(compose.ui)
                        implementation(compose.components.resources)
                        // Supplies the multiplatform androidx.compose.ui.tooling.preview.Preview.
                        // The older components:components-ui-tooling-preview artifact (reachable
                        // as compose.components.uiToolingPreview) is deprecated as of CMP 1.11 and
                        // only carries the org.jetbrains.compose.* annotation. Declared by
                        // coordinate because the compose.preview accessor is itself deprecated.
                        implementation(libs.findLibrary("compose.multiplatform.uiToolingPreview").get())

                        implementation(target.dependencies.platform(libs.findLibrary("koin.bom").get()))
                        implementation(libs.findLibrary("koin.core").get())
                        implementation(libs.findLibrary("koin.compose.viewmodel").get())

                        implementation(libs.findLibrary("kotlinx.coroutines.core").get())
                        implementation(libs.findLibrary("kotlinx.serialization.json").get())
                        implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                        implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                    }

                    androidMain.dependencies {
                        implementation(libs.findLibrary("coil.kt").get())
                        implementation(libs.findLibrary("coil.kt.compose").get())
                        implementation(libs.findLibrary("androidx.activity.compose").get())
                        implementation(libs.findLibrary("androidx.navigation3.runtime").get())
                        implementation(libs.findLibrary("androidx.navigation3.ui").get())
                    }
                }
            }

            // The renderer Android Studio needs to actually draw this module's @Preview.
            // Per the Compose Multiplatform preview-setup docs it goes on the Android runtime
            // classpath rather than into a source set: the AGP KMP library plugin is
            // single-variant, so there is no debugImplementation to scope it to, and keeping
            // it off the compile classpath stops production code from referencing it.
            dependencies.add(
                "androidRuntimeClasspath",
                libs.findLibrary("compose.multiplatform.uiTooling").get(),
            )
        }
    }
}
