/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.lambdafunction.azure.runtime

import kotlinx.cinterop.*
import platform.posix.*

internal actual fun systemSignalDetector() {
    signal(SIGINT, onSignalCallback)
    signal(SIGTERM, onSignalCallback)
}