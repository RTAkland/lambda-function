/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */


@file:OptIn(ExperimentalForeignApi::class, InternalLambdaRuntimeApi::class)

package cn.rtast.lambdafunction.azure.runtime

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.CompletableDeferred

@InternalLambdaRuntimeApi
public val shutdownSignal: CompletableDeferred<Unit> = CompletableDeferred()

internal val onSignalCallback = staticCFunction<Int, Unit> { _ ->
    shutdownSignal.complete(Unit)
}

@InternalLambdaRuntimeApi
public expect fun systemSignalDetector()

