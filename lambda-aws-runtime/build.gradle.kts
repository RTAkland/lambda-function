plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    linuxX64()

    explicitApi()
    withSourcesJar()

    sourceSets {
        commonMain.dependencies {
        }
    }
}

tasks.withType<PublishToMavenRepository>().configureEach {
    enabled = false
}