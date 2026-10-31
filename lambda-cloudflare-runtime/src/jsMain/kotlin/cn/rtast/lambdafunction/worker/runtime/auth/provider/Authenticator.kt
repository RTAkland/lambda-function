/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */

package cn.rtast.lambdafunction.worker.runtime.auth.provider

import cn.rtast.lambdafunction.worker.runtime.auth.AuthResult
import cn.rtast.lambdafunction.worker.runtime.auth.credentials.HttpCredential
import org.w3c.fetch.Request

/**
 * An interface for authenticating
 */
public sealed interface Authenticator<out C : HttpCredential> {
    public suspend fun authenticate(request: Request, credential: @UnsafeVariance C?): AuthResult
}