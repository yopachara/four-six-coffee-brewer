include(":core-domain")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "FourSixMethod"
include(":app")
include(":core-common")
include(":core-designsystem")
include(":core-data")
include(":core-database")
include(":core-model")
include(":feature:timer")
