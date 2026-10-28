/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-28
 */


@file:OptIn(InternalLambdaRuntimeApi::class, ExperimentalForeignApi::class)

package cn.rtast.lambdafunction.azure.runtime

import cn.rtast.ktor.h2.engine.NGHTTP2Engine
import cn.rtast.ktor.h2.engine.NativeH2ApplicationEngine
import cn.rtast.ktor.h2.engine.configuration.NativeH2EngineConfiguration
import io.ktor.server.application.*
import io.ktor.server.engine.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import platform.posix.getenv

public fun runAzureLambda(
    host: String = "127.0.0.1",
    port: Int = getenv("FUNCTIONS_CUSTOMHANDLER_PORT")?.toKString()?.toInt() ?: 8080,
    tlsContext: TLSContext = TLSContext.EmptyContext,
    signal: Boolean = true,
    module: suspend Application.(server: EmbeddedServer<NativeH2ApplicationEngine, NativeH2EngineConfiguration>) -> Unit,
): Unit = runBlocking {
    val server = embeddedServer(NGHTTP2Engine, configure = c@{
        this@c.host = host
        this@c.port = port
        tlsContext.certMaterial?.let { this@c.certMaterial = it }
        tlsContext.keyMaterial?.let { this@c.keyMaterial = it }
        tlsContext.certPassword?.let { this@c.certPassword }
    }, module = { install(RequestCallLoggingPlugin) })
    module.invoke(server.application, server)
    server.startSuspend(wait = false)
    if (signal) systemSignalDetector()
    shutdownSignal.await()
    server.stop(1000, 2000)
}