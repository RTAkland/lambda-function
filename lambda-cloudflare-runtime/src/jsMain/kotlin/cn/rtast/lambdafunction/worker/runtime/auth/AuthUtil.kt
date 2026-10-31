/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/1/3
 */

@file:Suppress("NOTHING_TO_INLINE")

package cn.rtast.lambdafunction.worker.runtime.auth

import cn.rtast.lambdafunction.worker.runtime.auth.credentials.BasicCredential
import cn.rtast.lambdafunction.worker.runtime.auth.credentials.HeaderCredential

internal inline fun String.packBasicCredential(): BasicCredential =
    with(this.split(":")) { BasicCredential(first(), last()) }

internal inline fun String.packBearerCredential(): HeaderCredential = HeaderCredential(this)