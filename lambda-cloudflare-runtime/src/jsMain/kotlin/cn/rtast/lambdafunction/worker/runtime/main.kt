/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


/**
 * This file is for internally use
 */

@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)

package cn.rtast.lambdafunction.worker.runtime

import cn.rtast.lambdafunction.worker.runtime.auth.AuthResult
import cn.rtast.lambdafunction.worker.runtime.auth.credentials.HeaderCredential
import cn.rtast.lambdafunction.worker.runtime.auth.provider.HeaderAuthenticator
import cn.rtast.lambdafunction.worker.runtime.response.respondText
import cn.rtast.lambdafunction.worker.runtime.route.route
import cn.rtast.lambdafunction.worker.runtime.websocket.readText
import cn.rtast.lambdafunction.worker.runtime.websocket.webSocket
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.w3c.fetch.Request
import org.w3c.fetch.Response
import kotlin.js.Promise

public fun WorkerApplication.handleRequest(request: Request): Promise<Response> = GlobalScope.promise {
    route("/") {
        respondText("Hello kotlin cloudflare worker")
    }

    webSocket("/ws") {
        onMessage {
            println(it.readText())
        }

        onClose {
        }
    }

    route("/protected", setOf(HttpMethod.GET), object : HeaderAuthenticator() {
        override suspend fun authenticate(request: Request, credential: HeaderCredential?): AuthResult {
            return if (credential?.value != null) AuthResult.OK else AuthResult.UNAUTHORIZED
        }
    }) {
        respondText("Success")
    }
    handle(request)
}


public fun main(): Unit = runWorkerLambda {
    handleRequest(it)
}