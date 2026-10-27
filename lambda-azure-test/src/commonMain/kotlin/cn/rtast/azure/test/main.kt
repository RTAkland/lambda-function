/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-22
 */


package cn.rtast.azure.test

import cn.rtast.lambdafunction.azure.runtime.runAzureLambda
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

fun main(): Unit = runAzureLambda {
    routing {
        get("/") {
            call.respondSource(SystemFileSystem.source(Path("the-text-content.txt")))
        }

        get("/{name}") {
            call.respond("Hello ${call.pathParameters["name"]}")
        }
    }
}
