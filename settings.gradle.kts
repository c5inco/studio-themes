import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

plugins {
    id("org.jetbrains.intellij.platform.settings") version "2.18.1"
}

rootProject.name = "studio-themes"

include(
    ":plugins:cloudy-blue",
    ":plugins:in-bed-by-7pm",
    ":plugins:islands-darcula",
    ":plugins:new-darcula",
    ":plugins:studio-themes",
)

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS

    repositories {
        mavenCentral()
        intellijPlatform {
            defaultRepositories()
        }
    }
}
