/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */


package cn.rtast.runtime

import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.util.logging.*

public val LOGGER: Logger = KtorSimpleLogger("cn.rtast.lambdafunction.RequestCallLoggingPlugin")

internal val RequestCallLoggingPlugin = createRouteScopedPlugin("RequestCallLoggingPlugin", { }) {
    onCall { with(it.request) { LOGGER.info("${origin.remoteHost} : ${httpMethod.value} $uri") } }
}
