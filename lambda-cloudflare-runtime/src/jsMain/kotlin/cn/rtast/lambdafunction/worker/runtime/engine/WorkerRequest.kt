/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-27
 */


@file:OptIn(ExperimentalWasmJsInterop::class)

package cn.rtast.lambdafunction.worker.runtime.engine

import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.utils.io.*
import web.http.Request


internal class WorkerRequest(
    call: WorkerCall,
    private val request: Request,
) : BaseApplicationRequest(call) {
    override val engineHeaders: Headers by lazy {
        Headers.build { request.headers.forEach { value, key -> this.append(key, value) } }
    }
    override val engineReceiveChannel: ByteReadChannel =
        request.body?.toByteChannel(call.scope) ?: ByteReadChannel.Empty

    override val cookies: RequestCookies by lazy {
        RequestCookies(this)
    }

    private val url = Url(request.url)


    override val local: RequestConnectionPoint by lazy {
        object : RequestConnectionPoint {
            override val scheme: String
                get() = url.protocol.name
            override val version: String by lazy {
                request.asDynamic().cf?.httpVersion
            }


            @Deprecated("Use localPort or serverPort instead", level = DeprecationLevel.ERROR)
            override val port: Int
                get() = url.port
            override val localPort: Int
                get() = url.port
            override val serverPort: Int
                get() = url.port

            @Deprecated("Use localHost or serverHost instead", level = DeprecationLevel.ERROR)
            override val host: String
                get() = url.host
            override val localHost: String
                get() = url.host
            override val serverHost: String
                get() = url.host
            override val localAddress: String = ""

            override val uri: String
                get() = url.fullPath

            override val method: HttpMethod by lazy {
                HttpMethod.parse(request.method.toString())
            }

            override val remoteHost: String get() = remoteAddress
            override val remotePort: Int = 0
            override val remoteAddress: String = request.headers.get("CF-Connecting-IP") ?: ""

        }
    }
    override val queryParameters: Parameters = url.parameters
    override val rawQueryParameters: Parameters = url.parameters
}