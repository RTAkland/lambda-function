/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */


package cn.rtast.lambda.function.plugin

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import java.io.File

abstract class LambdaFunctionExtension {
    @get:Input
    @get:Optional
    abstract val hostJsonFile: Property<File>

    @get:Input
    @get:Optional
    abstract val localSettingsFile: Property<File>

    @get:Input
    @get:Optional
    abstract val functionJsonFile: Property<File>

    @get:Input
    abstract val enableAzureFunction: Property<Boolean>

    @get:Input
    abstract val appName: Property<String>

    @get:Input
    @get:Optional
    abstract val customResources: ListProperty<File>
}