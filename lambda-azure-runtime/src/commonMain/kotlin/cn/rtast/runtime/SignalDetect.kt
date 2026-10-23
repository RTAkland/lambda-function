/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */


@file:OptIn(ExperimentalForeignApi::class, UnsafeNumber::class)

package cn.rtast.runtime

import kotlinx.cinterop.*
import kotlinx.coroutines.CompletableDeferred
import platform.posix.STDERR_FILENO
import platform.posix.write

private const val EXIT_MSG = "SIGINT/SIGTERM detected, exiting...\n"
internal const val messageLen = EXIT_MSG.length
internal val messagePtr: CPointer<ByteVar> = nativeHeap.allocArray<ByteVar>(EXIT_MSG.length).apply {
    EXIT_MSG.forEachIndexed { i, c -> this[i] = c.code.toByte() }
}

internal val shutdownSignal = CompletableDeferred<Unit>()

internal val onSignalCallback = staticCFunction<Int, Unit> { _ ->
    write(STDERR_FILENO, messagePtr, messageLen.convert())
    shutdownSignal.complete(Unit)
}

internal expect fun systemSignalDetector()

