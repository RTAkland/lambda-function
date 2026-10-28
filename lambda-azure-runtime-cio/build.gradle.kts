plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    linuxX64()
    mingwX64()

    explicitApi()
    withSourcesJar()

    sourceSets {
        commonMain.dependencies {
            api(project(":azure-runtime-core"))
            api(libs.ktor.server.cio)
        }
    }
}