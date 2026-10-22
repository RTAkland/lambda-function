/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-22
 */


package cn.rtast.azure.test

import cn.rtast.runtime.runAzureLambda
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(): Unit = runAzureLambda {
    routing {
        get("/") {
            call.respond("Hello")
        }
    }
}
