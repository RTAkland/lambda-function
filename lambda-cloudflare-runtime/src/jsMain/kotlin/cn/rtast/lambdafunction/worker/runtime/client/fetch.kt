/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.lambdafunction.worker.runtime.client

import cn.rtast.lambdafunction.worker.runtime.util.toByteArray
import kotlinx.coroutines.await
import org.w3c.dom.url.URL
import org.w3c.fetch.Request
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import kotlin.js.Promise

public external fun fetch(input: String, init: RequestInit = definedExternally): Promise<Response>

public suspend fun Request.readBytes(): ByteArray =
    this.arrayBuffer().await().toByteArray()

public suspend fun Request.readText(): String = readBytes().decodeToString()

public val Request.asURL: URL get() = URL(this.url)