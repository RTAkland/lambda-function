/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-27
 */


package cn.rtast.lambdafunction.worker.runtime.engine

import io.ktor.events.*
import io.ktor.server.application.*
import io.ktor.server.engine.*

public object CloudflareWorker : ApplicationEngineFactory<WorkerApplicationEngine,
        WorkerApplicationEngine.Configuration> {
    override fun configuration(configure: WorkerApplicationEngine.Configuration.() -> Unit): WorkerApplicationEngine.Configuration =
        WorkerApplicationEngine.Configuration().apply(configure)

    override fun create(
        environment: ApplicationEnvironment,
        monitor: Events,
        developmentMode: Boolean,
        configuration: WorkerApplicationEngine.Configuration,
        applicationProvider: () -> Application,
    ): WorkerApplicationEngine =
        WorkerApplicationEngine(environment, monitor, developmentMode, configuration, applicationProvider)
}