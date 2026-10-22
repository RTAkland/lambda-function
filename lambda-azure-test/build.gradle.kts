import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("cn.rtast.lambdafunction") version "0.0.1"
}

repositories {
    mavenLocal()
}

kotlin {
    linuxX64()
    mingwX64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries {
            executable {
                entryPoint = "cn.rtast.kazure.test.main"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":azure-runtime"))
        }
    }
}

azureFunction {
    hostJsonFile = rootProject.layout.projectDirectory
        .dir("kazure-gradle-plugin/src/main/resources/host.json").asFile
    functionJsonFile = rootProject.layout.projectDirectory
        .dir("kazure-gradle-plugin/src/main/resources/function.json").asFile
    localSettingsFile = rootProject.layout.projectDirectory
        .dir("kazure-gradle-plugin/src/main/resources/local.settings.json").asFile
}

tasks.withType<PublishToMavenRepository>().configureEach {
    enabled = false
}