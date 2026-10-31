/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.lambdafunction.worker.runtime.auth.provider

import cn.rtast.lambdafunction.worker.runtime.auth.AuthResult
import cn.rtast.lambdafunction.worker.runtime.auth.credentials.HeaderCredential
import org.w3c.fetch.Request

/**
 * Header auth provider
 */
public abstract class HeaderAuthenticator(public val header: String = "authorization") :
    Authenticator<HeaderCredential> {
    abstract override suspend fun authenticate(request: Request, credential: HeaderCredential?): AuthResult
}