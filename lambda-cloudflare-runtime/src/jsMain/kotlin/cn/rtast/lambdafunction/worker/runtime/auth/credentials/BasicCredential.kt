/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */


package cn.rtast.lambdafunction.worker.runtime.auth.credentials

/**
 * Basic credential
 */
public data class BasicCredential(val username: String, val password: String) : HttpCredential