/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-22
 */


package cn.rtast.lambda.function.plugin

fun readResource(path: String): String = LambdaFunctionExtension::class.java
    .classLoader
    .getResourceAsStream(path)!!
    .readAllBytes()
    .decodeToString()