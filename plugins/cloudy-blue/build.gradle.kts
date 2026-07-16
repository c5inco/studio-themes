import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform
import studio.themes.build.StageStudioThemesResourcesTask
import studio.themes.build.StudioThemesCatalogLoader

plugins {
    java
    id("org.jetbrains.intellij.platform")
}

val catalog = StudioThemesCatalogLoader.load(rootProject.rootDir)
val metadata = catalog.themes.getValue("cloudy-blue")

group = "com.c5inco.studiothemes"
version = metadata.version

dependencies {
    intellijPlatform {
        intellijIdeaCommunity(providers.gradleProperty("platformVersion"))
        pluginVerifier()
        zipSigner()
    }
}

java {
    toolchain {
        languageVersion.set(providers.gradleProperty("javaVersion").map(String::toInt).map(JavaLanguageVersion::of))
    }
}

val stagePluginResources = tasks.register("stagePluginResources", StageStudioThemesResourcesTask::class) {
    distributionName.set("cloudy-blue")
    themesDirectory.set(rootProject.layout.projectDirectory.dir("themes"))
    aggregateMetadataFile.set(rootProject.layout.projectDirectory.file("plugins/studio-themes/plugin.properties.json"))
    aggregateDescriptionFile.set(rootProject.layout.projectDirectory.file("plugins/studio-themes/description.html"))
    aggregateChangeNotesFile.set(rootProject.layout.projectDirectory.file("plugins/studio-themes/change-notes.html"))
    aggregatePluginIconFile.set(rootProject.layout.projectDirectory.file("plugins/studio-themes/pluginIcon.svg"))
    outputDirectory.set(layout.buildDirectory.dir("generated/studioThemesResources"))
    sharedSinceBuild.set(providers.gradleProperty("pluginSinceBuild"))
    sharedVendorName.set(providers.gradleProperty("pluginVendorName"))
    sharedVendorEmail.set(providers.gradleProperty("pluginVendorEmail"))
    sharedVendorUrl.set(providers.gradleProperty("pluginVendorUrl"))
}

extensions.configure<SourceSetContainer>("sourceSets") {
    named("main") {
        resources.srcDir(stagePluginResources.map { it.outputDirectory })
    }
}

tasks.named("processResources") {
    dependsOn(stagePluginResources)
}

tasks.named("patchPluginXml") {
    dependsOn(stagePluginResources)
}

tasks.matching { it.name == "buildSearchableOptions" }.configureEach {
    enabled = false
}
