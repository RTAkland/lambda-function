/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-28
 */


@file:OptIn(ExperimentalForeignApi::class, InternalLambdaRuntimeApi::class)

package cn.rtast.lambdafunction.azure.runtime

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.EngineConnectorBuilder
import io.ktor.server.engine.embeddedServer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import platform.posix.getenv

public fun runAzureLambda(
    host: String = "127.0.0.1",
    port: Int = getenv("FUNCTIONS_CUSTOMHANDLER_PORT")?.toKString()?.toInt() ?: 8080,
    signal: Boolean = true,
    module: suspend Application.(server: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>) -> Unit,
): Unit = runBlocking {
    val server = embeddedServer(CIO, configure = {
        connectors.add(EngineConnectorBuilder().apply ec@{
            reuseAddress = true
            this@ec.host = host
            this@ec.port = port
        })
    }, module = { install(RequestCallLoggingPlugin) })
    module.invoke(server.application, server)
    server.startSuspend(wait = false)
    if (signal) systemSignalDetector()
    shutdownSignal.await()
    server.stop(1000, 2000)
}