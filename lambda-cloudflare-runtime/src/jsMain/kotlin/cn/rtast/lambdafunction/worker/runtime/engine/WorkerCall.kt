/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-27
 */


package cn.rtast.lambdafunction.worker.runtime.engine

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import web.http.Request
import web.http.Response

internal class WorkerCall(
    jsRequest: Request,
    jsResponse: CompletableDeferred<Response>,
    application: Application,
    val scope: CoroutineScope,
) : BaseApplicationCall(application) {
    override val request = WorkerRequest(this, jsRequest)
    override val response = WorkerResponse(this, jsResponse)

    init {
        putResponseAttribute()
    }

    override val parameters: Parameters = request.queryParameters
    override val coroutineContext get() = scope.coroutineContext
}