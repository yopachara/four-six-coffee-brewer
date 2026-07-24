import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Base KMP module setup: androidTarget (via AGP 9's single-variant
 * `com.android.kotlin.multiplatform.library` plugin, which cannot coexist with the classic
 * `com.android.library` plugin) + iOS targets. Namespace is left to each consuming module.
 */
class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                // The AGP-registered "android" target is a dynamically added extension
                // (ExtensionContainer.add), not a real DSL function, so it needs `configure<T>`
                // rather than the `android { }` sugar available only inside .gradle.kts scripts.
                configure<KotlinMultiplatformAndroidLibraryTarget> {
                    compileSdk = 37
                    minSdk = 23

                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }
                }

                iosX64()
                iosArm64()
                iosSimulatorArm64()

                sourceSets.apply {
                    commonTest.dependencies {
                        implementation(kotlin("test"))
                    }
                }
            }
        }
    }
}
