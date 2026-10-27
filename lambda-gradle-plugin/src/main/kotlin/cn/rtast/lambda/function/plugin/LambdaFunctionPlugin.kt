/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-22
 */


package cn.rtast.lambda.function.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("Unused")
class LambdaFunctionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val ext = extensions.create("lambdaFunction", LambdaFunctionExtension::class.java)
        ext.enableAzureFunction.convention(true)
        ext.appName.convention("myazurefunction")
        ext.customResources.convention(emptyList())

        if (ext.enableAzureFunction.get()) AzureFunctionTask.registerExtension(target, ext)
    }
}