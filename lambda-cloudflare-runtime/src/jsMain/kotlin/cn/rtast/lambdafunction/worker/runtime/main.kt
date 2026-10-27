/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-27
 */


@file:OptIn(ExperimentalJsExport::class, DelicateCoroutinesApi::class)

package cn.rtast.lambdafunction.worker.runtime

import cn.rtast.lambdafunction.worker.runtime.engine.CloudflareWorker
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.w3c.dom.events.EventListener
import web.http.Request
import web.http.Response

public fun main() {
    val eventListener = EventListener { event ->
        val dyn = event.asDynamic()
        GlobalScope.promise {
            event.asDynamic().respondWith(handleRequest(dyn.request as Request))
            js("addEventListener('fetch', eventListener)")
        }
    }
}

private val server = embeddedServer(CloudflareWorker) {
    serverConfig {
        watchPaths = emptyList()
    }

    routing {
        get("/") {
            call.respond("Hello")
        }
    }
}

public var started: Boolean = false

public suspend fun ensuredStarted() {
    if (!started) server.startSuspend(wait = true)
    started = true
}

@JsExport
public suspend fun handleRequest(request: Request): Response = server.engine.handle(request)