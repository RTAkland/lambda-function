/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */

package cn.rtast.lambdafunction.worker.runtime.auth.provider

import cn.rtast.lambdafunction.worker.runtime.auth.AuthResult
import cn.rtast.lambdafunction.worker.runtime.auth.credentials.BasicCredential
import org.w3c.fetch.Request

/**
 * Basic auth provider
 * Schema: Basic <Base64-encoded username:password string>
 */
public abstract class BasicAuthenticator : Authenticator<BasicCredential> {
    abstract override suspend fun authenticate(request: Request, credential: BasicCredential?): AuthResult
}