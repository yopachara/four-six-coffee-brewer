import androidx.room.gradle.RoomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Room 2.8.4 on KMP: commonMain gets room-runtime + sqlite-bundled, KSP generates the
 * `actual` RoomDatabaseConstructor for every target (android + all iOS targets) from the
 * `expect object` + `@ConstructedBy` declared in the consuming module's commonMain.
 */
class KmpRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.google.devtools.ksp")
                apply("androidx.room")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(libs.findLibrary("room.runtime").get())
                        implementation(libs.findLibrary("sqlite.bundled").get())
                    }
                }
            }

            dependencies {
                add("kspAndroid", libs.findLibrary("room.compiler").get())
                add("kspIosArm64", libs.findLibrary("room.compiler").get())
                add("kspIosSimulatorArm64", libs.findLibrary("room.compiler").get())
            }
        }
    }
}
