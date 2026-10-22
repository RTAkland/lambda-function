/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-22
 */


package cn.rtast.lambda.function.plugin

fun currentPlatform(): String {
    val os = System.getProperty("os.name").lowercase()
    return when {
        os.contains("win") -> "MingwX64"
        os.contains("mac") -> "MacosArm64"
        os.contains("linux") -> "LinuxX64"
        else -> error("Unsupported platform")
    }
}