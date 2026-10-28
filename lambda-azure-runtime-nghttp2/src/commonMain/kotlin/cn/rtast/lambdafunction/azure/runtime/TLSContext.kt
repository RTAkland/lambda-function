/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-28
 */


package cn.rtast.lambdafunction.azure.runtime

import cn.rtast.ktor.h2.engine.ssl.KeyMaterial

public interface TLSContext {
    public val certMaterial: KeyMaterial?
    public val keyMaterial: KeyMaterial?
    public val certPassword: KeyMaterial?

    public companion object {
        public val EmptyContext: TLSContext = object : TLSContext {
            override val certMaterial: KeyMaterial? = null
            override val keyMaterial: KeyMaterial? = null
            override val certPassword: KeyMaterial? = null
        }
    }
}