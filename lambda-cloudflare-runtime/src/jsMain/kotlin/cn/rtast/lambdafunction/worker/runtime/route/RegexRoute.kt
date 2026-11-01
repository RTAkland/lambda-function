/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/28 01:04
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */


package cn.rtast.lambdafunction.worker.runtime.route

import cn.rtast.lambdafunction.worker.runtime.HttpMethod
import cn.rtast.lambdafunction.worker.runtime.auth.credentials.HttpCredential
import cn.rtast.lambdafunction.worker.runtime.auth.provider.Authenticator
import org.w3c.fetch.Request
import org.w3c.fetch.Response

internal data class RegexRoute(
    val path: Regex,
    override val block: suspend (Request) -> Response,
    override val methods: Set<HttpMethod>,
    override val type: RouteType = RouteType.Regex,
    override val authenticator: Authenticator<HttpCredential>? = null,
) : AbstractRoute