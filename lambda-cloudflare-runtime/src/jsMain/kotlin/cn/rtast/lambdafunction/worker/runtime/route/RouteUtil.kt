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
import cn.rtast.lambdafunction.worker.runtime.route.type.RegexRoute
import cn.rtast.lambdafunction.worker.runtime.route.type.RouteType
import cn.rtast.lambdafunction.worker.runtime.route.type.StringRoute

public fun WorkerApplication.route(
    path: Regex,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    block: Handler,
): Unit = run { routes.add(RegexRoute(path, block, methods)) }

public fun WorkerApplication.route(
    path: String,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    block: Handler,
): Unit = run { routes.add(StringRoute(path, block, methods)) }

public fun WorkerApplication.route(
    path: String,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    authenticator: Authenticator<HttpCredential>,
    block: Handler,
): Unit = run { routes.add(StringRoute(path, block, methods, type = RouteType.String, authenticator = authenticator)) }

public fun WorkerApplication.route(
    path: Regex,
    methods: Set<HttpMethod> = HttpMethod.entries.toSet(),
    authenticator: Authenticator<HttpCredential>,
    block: Handler,
): Unit = run { routes.add(RegexRoute(path, block, methods, type = RouteType.Regex, authenticator = authenticator)) }