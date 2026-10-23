/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-22
 */


@file:OptIn(ExperimentalForeignApi::class)

package cn.rtast.runtime

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import platform.posix.getenv

public fun runAzureLambda(
    host: String = "127.0.0.1",
    port: Int = getenv("FUNCTIONS_CUSTOMHANDLER_PORT")?.toKString()?.toInt() ?: 8080,
    module: suspend Application.() -> Unit,
): Unit = runBlocking {
    val server = embeddedServer(CIO, configure = {
        connectors.add(EngineConnectorBuilder().apply ec@{
            reuseAddress = true
            this@ec.host = host
            this@ec.port = port
        })
    }, module = m@{
        install(RequestCallLoggingPlugin)
        module.invoke(this@m)
    }).startSuspend(wait = false)
    systemSignalDetector()
    shutdownSignal.await()
    server.stop(1000, 2000)
}