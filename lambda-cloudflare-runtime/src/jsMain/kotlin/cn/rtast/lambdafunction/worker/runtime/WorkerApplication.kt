/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/28 01:03
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */


package cn.rtast.lambdafunction.worker.runtime

import cn.rtast.lambdafunction.worker.runtime.auth.AuthResult
import cn.rtast.lambdafunction.worker.runtime.auth.packBasicCredential
import cn.rtast.lambdafunction.worker.runtime.auth.packBearerCredential
import cn.rtast.lambdafunction.worker.runtime.auth.provider.BasicAuthenticator
import cn.rtast.lambdafunction.worker.runtime.auth.provider.HeaderAuthenticator
import cn.rtast.lambdafunction.worker.runtime.config.CORSConfig
import cn.rtast.lambdafunction.worker.runtime.response.respondEmpty
import cn.rtast.lambdafunction.worker.runtime.response.respondText
import cn.rtast.lambdafunction.worker.runtime.route.type.AbstractRoute
import cn.rtast.lambdafunction.worker.runtime.route.type.RegexRoute
import cn.rtast.lambdafunction.worker.runtime.route.type.StringRoute
import cn.rtast.lambdafunction.worker.runtime.util.decodeBase64String
import cn.rtast.lambdafunction.worker.runtime.websocket.WebsocketEventHandler
import cn.rtast.lambdafunction.worker.runtime.websocket.WebsocketRoute
import org.w3c.dom.events.EventListener
import org.w3c.dom.url.URL
import org.w3c.fetch.Request
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import kotlin.js.Promise

/**
 * Kotlin cloudflare worker logic entrypoint class
 */
public class WorkerApplication(
    public val corsConfig: CORSConfig = CORSConfig(),
) {
    internal val routes: MutableList<AbstractRoute> = mutableListOf()
    internal val websocketRoutes: MutableList<WebsocketRoute> = mutableListOf()

    public suspend fun handle(request: Request): Response {
        val url = URL(request.url)
        val path = url.pathname
        val method = HttpMethod.fromString(request.method) ?: HttpMethod.GET
        if (method == HttpMethod.OPTIONS && corsConfig.enabled) return respondEmpty()
        if (request.headers.get("upgrade") != null) {
            val route = websocketRoutes.firstOrNull { r ->
                r.stringPath?.let { it == url.pathname } ?: r.regexPath?.matches(url.pathname) ?: false
            } ?: return Response("Not Found", ResponseInit(404))
            val handler = WebsocketEventHandler(request = request)
            route.block(handler)
            return handler.handle()
        }
        val matchedRoutes = routes.filter {
            when (it) {
                is StringRoute -> it.path == path
                is RegexRoute -> it.path.matches(path)
                else -> false
            }
        }

        if (matchedRoutes.isEmpty()) return respondText("Not Found", 404)
        val methodMatchedRoute = matchedRoutes.firstOrNull { method in it.methods }
        if (methodMatchedRoute == null) {
            val allowMethods = matchedRoutes.flatMap { it.methods }.distinct().joinToString(", ")
            val headers: dynamic = js("{}")
            headers["Allow"] = allowMethods
            return respondText("Method Not Allowed", 405)
        }

        val auth = methodMatchedRoute.authenticator
        val responsePromise = if (auth != null) {
            val credential = when (auth) {
                is BasicAuthenticator -> request.headers.get("authorization")
                    ?.removePrefix("Basic ")
                    ?.decodeBase64String
                    ?.packBasicCredential()

                is HeaderAuthenticator -> request.headers.get(auth.header.lowercase())
                    ?.packBearerCredential()
            }
            when (auth.authenticate(request, credential)) {
                AuthResult.OK -> methodMatchedRoute.handle(request)
                AuthResult.UNAUTHORIZED -> respondText("UNAUTHORIZED", 401)
                AuthResult.FORBIDDEN -> respondText("FORBIDDEN", 403)
            }
        } else methodMatchedRoute.handle(request)
        return responsePromise
    }
}

public inline fun runWorkerLambda(crossinline block: WorkerApplication.(Request) -> Promise<Response>) {
    @Suppress("unused_expression")
    val eventListener = EventListener { event ->
        val dyn = event.asDynamic()
        event.asDynamic().respondWith(block(WorkerApplication(), dyn.request as Request))
        Unit
    }
    js("addEventListener('fetch', eventListener)")
}
