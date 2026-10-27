/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-27
 */


package cn.rtast.lambdafunction.worker.runtime.engine

import io.ktor.events.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import web.abort.asCoroutineScope
import web.http.*

public class WorkerApplicationEngine(
    environment: ApplicationEnvironment,
    monitor: Events, developmentMode: Boolean,
    public val configuration: ApplicationEngine.Configuration,
    private val applicationProvider: () -> Application,
) : BaseApplicationEngine(environment, monitor, developmentMode) {
    public class Configuration : BaseApplicationEngine.Configuration()

    private val startGate = CompletableDeferred<Unit>()
    private var started = false
    private lateinit var application: Application

    override fun start(wait: Boolean): ApplicationEngine {
        started = true
        application = applicationProvider()
        startGate.complete(Unit)
        return this
    }

    internal suspend fun handle(request: Request): Response {
//        startGate.await()
        if (!started) throw IllegalStateException("WorkerApplicationEngine is not started")
        val deferred = CompletableDeferred<Response>()
        val scope = request.asCoroutineScope()
        scope.launch {
            try {
                pipeline.execute(WorkerCall(request, deferred, application, scope))
            } catch (t: Throwable) {
                if (!deferred.isCompleted) {
                    val headers = Headers()
                    val body = BodyInit("Internal Server Error: ${t.message}")
                    deferred.complete(Response(body, ResponseInit(headers, 500, "Internal Server Error")))
                }
                throw t
            }
        }
        return deferred.await()
    }

    override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {}
}