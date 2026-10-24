/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */


package cn.rtast.runtime

import io.ktor.server.application.*
import io.ktor.server.logging.*
import io.ktor.util.logging.*

public val LOGGER: Logger = KtorSimpleLogger("RequestCallLogging")

internal val RequestCallLoggingPlugin = createRouteScopedPlugin("RequestCallLoggingPlugin", { }) {
    onCall { with(it.request) { LOGGER.info(toLogString()) } }
}
