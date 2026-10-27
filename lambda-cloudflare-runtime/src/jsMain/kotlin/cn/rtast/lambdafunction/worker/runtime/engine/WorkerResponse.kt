/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-27
 */


package cn.rtast.lambdafunction.worker.runtime.engine

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.utils.io.*
import js.coroutines.asPromise
import js.iterable.toList
import js.typedarrays.toUint8Array
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import web.http.BodyInit
import web.http.Headers
import web.http.Response
import web.http.ResponseInit

internal class WorkerResponse(call: WorkerCall, private val response: CompletableDeferred<Response>) :
    BaseApplicationResponse(call) {
    override suspend fun respondUpgrade(upgrade: OutgoingContent.ProtocolUpgrade) {}

    private val scope: CoroutineScope = call.scope

    override suspend fun responseChannel(): ByteWriteChannel {
        val channel = ByteChannel(autoFlush = true)
        val stream = channel.toReadableStream(scope)
        val deferred = CompletableDeferred<Unit>()
        waitUntil(deferred.asPromise())
        val response = Response(stream, responseInit())
        this.response.complete(response)
        return channel
    }

    override suspend fun respondOutgoingContent(content: OutgoingContent) {
        val response = content.toResponse()
        if (response != null) {
            this.response.complete(response)
            return
        }
        return super.respondOutgoingContent(content)
    }

    fun responseInit(): ResponseInit {
        val status = status() ?: HttpStatusCode.OK
        val code = status.value.toShort()
        val des = status.description
        return ResponseInit(rawHeaders, code, des)
    }


    private fun OutgoingContent.toResponse(): Response? {
        fun responseInit(): ResponseInit {
            commitHeaders(this@toResponse)
            return this@WorkerResponse.responseInit()
        }

        fun BodyInit.response(): Response = Response(this, responseInit())
        return when (this) {
            is ByteArrayContent -> BodyInit(bytes().toUint8Array()).response()
            is TextContent -> BodyInit(text).response()
            is OutgoingContent.NoContent -> Response(init = responseInit())
            else -> null
        }
    }

    override fun setStatus(statusCode: HttpStatusCode) {}

    private val rawHeaders = Headers()

    override val headers: ResponseHeaders = object : ResponseHeaders() {
        override fun engineAppendHeader(name: String, value: String) = rawHeaders.append(name, value)
        override fun getEngineHeaderNames(): List<String> = rawHeaders.keys().toList()
        override fun getEngineHeaderValues(name: String): List<String> = rawHeaders.values().toList()
    }
}