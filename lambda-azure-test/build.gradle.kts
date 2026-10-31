import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("cn.rtast.lambdafunction") version "0.1.4"
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
            implementation(project(":azure-runtime-cio"))
//            implementation(project(":azure-runtime-nghttp2"))
        }
    }

    compilerOptions.freeCompilerArgs.addAll("-Xcompanion-blocks-and-extensions")
}


lambdaFunction {
    enableAzureFunction = true
    customResources.addAll(*file("src/commonMain/resources").listFiles(), file("certs"))
}