/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */

@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.runtime

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import platform.windows.*

private val ctrlHandlerCallback = staticCFunction<DWORD, BOOL> { ctrlType ->
    when (ctrlType.toInt()) {
        CTRL_C_EVENT, CTRL_CLOSE_EVENT, CTRL_BREAK_EVENT, CTRL_SHUTDOWN_EVENT -> {
            shutdownSignal.complete(Unit)
            TRUE
        }

        else -> FALSE
    }
}

internal actual fun systemSignalDetector(): Unit = run { SetConsoleCtrlHandler(ctrlHandlerCallback, TRUE) }