/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.runtime

import kotlinx.cinterop.*
import platform.posix.*

internal actual fun systemSignalDetector(): Unit = memScoped {
    val sa = alloc<sigaction>()
    memset(sa.ptr, 0, sizeOf<sigaction>().convert())
    sa.__sigaction_handler.sa_handler = onSignalCallback
    sigemptyset(sa.sa_mask.ptr)
    sa.sa_flags = 0
    sigaction(SIGINT, sa.ptr, null)
    sigaction(SIGTERM, sa.ptr, null)
}