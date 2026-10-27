import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("cn.rtast.lambdafunction") version "0.1.1"
}

repositories {
    mavenLocal()
}

kotlin {
    linuxX64()
    mingwX64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries { executable { entryPoint = "cn.rtast.azure.test.main" } }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":azure-runtime"))
        }
    }
}


lambdaFunction {
    customResources.addAll(*file("src/commonMain/resources").listFiles())
}

tasks.withType<PublishToMavenRepository>().configureEach {
    enabled = false
}