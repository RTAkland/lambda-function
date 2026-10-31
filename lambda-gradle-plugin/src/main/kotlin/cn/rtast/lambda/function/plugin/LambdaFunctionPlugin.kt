/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-22
 */


package cn.rtast.lambda.function.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("Unused")
public class LambdaFunctionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val ext = extensions.create("lambdaFunction", LambdaFunctionExtension::class.java).apply {
            appName.convention("myazurefunction")
            customResources.convention(emptyList())
            wranglerPort.convention(8787)
            enableAzureFunction.convention(false)
            enableCloudflareWorker.convention(false)
        }
        afterEvaluate {
            if (ext.enableAzureFunction.get()) AzureFunctionTask.registerExtension(it, ext)
            if (ext.enableCloudflareWorker.get()) CloudflareWorkerTask.registerExtension(it, ext)
        }
    }
}