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
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.util.logging.*
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import platform.posix.*

public val LOGGER: Logger = KtorSimpleLogger("cn.rtast.kazure.custom.handler.RequestTracePlugin")

private val RequestTracePlugin = createRouteScopedPlugin("RequestTracePlugin", { }) {
    onCall { call ->
        val method = call.request.httpMethod.value
        val uri = call.request.uri
        val remote = call.request.origin.remoteHost
        LOGGER.info("$remote : $method $uri")
    }
}

private val onSigintFunc = staticCFunction<Int, Unit> { _ ->
    LOGGER.info("SIGINT detected, exiting")
    _exit(130)
}

private fun onSignal() {
    signal(SIGINT, onSigintFunc)
    signal(SIGTERM, onSigintFunc)
}

public fun runAzureLambda(block: suspend Application.() -> Unit) {
    onSignal()
    embeddedServer(CIO, configure = {
        connectors.add(EngineConnectorBuilder().apply {
            reuseAddress = true
            port = getenv("FUNCTIONS_CUSTOMHANDLER_PORT")?.toKString()?.toInt() ?: 8080
            host = "127.0.0.1"
        })
    }, module = {
        install(RequestTracePlugin)
        block()
    }).start(wait = true)
}