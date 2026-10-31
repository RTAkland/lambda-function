import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("cn.rtast.lambdafunction") version "0.1.4"
}

repositories {
    mavenLocal()
}

kotlin {
    js {
        nodejs()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":cloudflare-runtime"))
        }
    }

    compilerOptions.freeCompilerArgs.addAll("-Xcompanion-blocks-and-extensions")
}

lambdaFunction {
    enableCloudflareWorker = true
    wranglerFile.set(file("wrangler.toml"))
}