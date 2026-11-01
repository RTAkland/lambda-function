/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/25 10:49
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */

package cn.rtast.lambdafunction.worker.runtime.route

import cn.rtast.lambdafunction.worker.runtime.HttpMethod
import cn.rtast.lambdafunction.worker.runtime.WorkerApplication
import cn.rtast.lambdafunction.worker.runtime.auth.credentials.HttpCredential
import cn.rtast.lambdafunction.worker.runtime.auth.provider.Authenticator
import org.w3c.fetch.Request
import org.w3c.fetch.Response

public fun WorkerApplication.route(
    path: Regex,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    block: suspend (Request) -> Response,
): Unit = run { routes.add(RegexRoute(path, block, methods)) }

public fun WorkerApplication.route(
    path: String,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    block: suspend (Request) -> Response,
): Unit = run { routes.add(StringRoute(path, block, methods)) }

public fun WorkerApplication.route(
    path: String,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = run { routes.add(StringRoute(path, block, methods, RouteType.String, authenticator)) }

public fun WorkerApplication.route(
    path: Regex,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = run { routes.add(RegexRoute(path, block, methods, RouteType.Regex, authenticator)) }

public fun WorkerApplication.get(path: String, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.GET), block)

public fun WorkerApplication.get(
    path: String,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.GET), authenticator, block)

public fun WorkerApplication.get(path: Regex, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.GET), block)

public fun WorkerApplication.get(
    path: Regex,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.GET), authenticator, block)

public fun WorkerApplication.post(path: String, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.POST), block)

public fun WorkerApplication.post(
    path: String,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.POST), authenticator, block)

public fun WorkerApplication.post(path: Regex, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.POST), block)

public fun WorkerApplication.post(
    path: Regex,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.POST), authenticator, block)

public fun WorkerApplication.head(path: String, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.HEAD), block)

public fun WorkerApplication.head(
    path: String,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.HEAD), authenticator, block)

public fun WorkerApplication.head(path: Regex, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.HEAD), block)

public fun WorkerApplication.head(
    path: Regex,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.HEAD), authenticator, block)

public fun WorkerApplication.put(path: String, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.PUT), block)

public fun WorkerApplication.put(
    path: String,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.PUT), authenticator, block)

public fun WorkerApplication.put(path: Regex, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.PUT), block)

public fun WorkerApplication.put(
    path: Regex,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.PUT), authenticator, block)

public fun WorkerApplication.delete(path: String, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.DELETE), block)

public fun WorkerApplication.delete(
    path: String,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.DELETE), authenticator, block)

public fun WorkerApplication.delete(path: Regex, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.DELETE), block)

public fun WorkerApplication.delete(
    path: Regex,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.DELETE), authenticator, block)

public fun WorkerApplication.patch(path: String, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.PATCH), block)

public fun WorkerApplication.patch(
    path: String,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.PATCH), authenticator, block)

public fun WorkerApplication.patch(path: Regex, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.PATCH), block)

public fun WorkerApplication.patch(
    path: Regex,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.PATCH), authenticator, block)

public fun WorkerApplication.options(path: String, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.OPTIONS), block)

public fun WorkerApplication.options(
    path: String,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.OPTIONS), authenticator, block)

public fun WorkerApplication.options(path: Regex, block: suspend (Request) -> Response): Unit =
    route(path, setOf(HttpMethod.OPTIONS), block)

public fun WorkerApplication.options(
    path: Regex,
    authenticator: Authenticator<HttpCredential>,
    block: suspend (Request) -> Response,
): Unit = route(path, setOf(HttpMethod.OPTIONS), authenticator, block)