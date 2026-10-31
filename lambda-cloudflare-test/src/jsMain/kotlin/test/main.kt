/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-31
 */


@file:OptIn(DelicateCoroutinesApi::class)

package test

import cn.rtast.lambdafunction.worker.runtime.WorkerApplication
import cn.rtast.lambdafunction.worker.runtime.response.respondText
import cn.rtast.lambdafunction.worker.runtime.route.route
import cn.rtast.lambdafunction.worker.runtime.runWorkerLambda
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.w3c.fetch.Request
import org.w3c.fetch.Response
import kotlin.js.Promise

fun main() = runWorkerLambda {
    handleRequest(it)
}

fun WorkerApplication.handleRequest(request: Request): Promise<Response> = GlobalScope.promise {
    route("/") {
        respondText("Hello World")
    }
    handle(request)
}