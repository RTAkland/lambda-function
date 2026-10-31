/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.lambdafunction.worker.runtime.websocket

import cn.rtast.lambdafunction.worker.runtime.WorkerApplication

internal data class WebsocketRoute(
    val stringPath: String? = null,
    val regexPath: Regex? = null,
    val block: WebsocketHandler,
)

public fun WorkerApplication.webSocket(
    path: Regex,
    block: WebsocketHandler,
): Unit = run { websocketRoutes.add(WebsocketRoute(regexPath = path, block = block)) }

public fun WorkerApplication.webSocket(
    path: String,
    block: WebsocketHandler,
): Unit = run { websocketRoutes.add(WebsocketRoute(stringPath = path, block = block)) }
