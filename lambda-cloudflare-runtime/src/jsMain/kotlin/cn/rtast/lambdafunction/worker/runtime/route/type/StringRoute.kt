/*
 * Copyright © 2025 RTAkland
 * Date: 2025/12/28 01:04
 * Open Source Under Apache-2.0 License
 * https://www.apache.org/licenses/LICENSE-2.0
 */


package cn.rtast.lambdafunction.worker.runtime.route.type

import cn.rtast.lambdafunction.worker.runtime.HttpMethod
import cn.rtast.lambdafunction.worker.runtime.auth.credentials.HttpCredential
import cn.rtast.lambdafunction.worker.runtime.auth.provider.Authenticator
import cn.rtast.lambdafunction.worker.runtime.route.Handler

internal data class StringRoute(
    val path: String,
    override val block: Handler,
    override val methods: Set<HttpMethod>,
    override val type: RouteType = RouteType.String,
    override val authenticator: Authenticator<HttpCredential>? = null,
) : AbstractRoute