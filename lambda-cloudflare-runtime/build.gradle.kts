/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-27
 */

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    js {
        nodejs()
        binaries.executable()
    }

    explicitApi()
    withSourcesJar()

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines)
        }
    }
}