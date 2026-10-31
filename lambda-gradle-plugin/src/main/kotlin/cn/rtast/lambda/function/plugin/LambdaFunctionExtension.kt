/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */


package cn.rtast.lambda.function.plugin

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import java.io.File

public abstract class LambdaFunctionExtension {
    @get:Input
    @get:Optional
    public abstract val hostJsonFile: Property<File>

    @get:Input
    @get:Optional
    public abstract val localSettingsFile: Property<File>

    @get:Input
    @get:Optional
    public abstract val functionJsonFile: Property<File>

    @get:Input
    public abstract val appName: Property<String>

    @get:Input
    @get:Optional
    public abstract val customResources: ListProperty<File>

    @get:Input
    @get:Optional
    public abstract val wranglerFile: RegularFileProperty

    @get:Input
    @get:Optional
    public abstract val wranglerDevVars: RegularFileProperty

    @get:Input
    @get:Optional
    public abstract val wranglerPort: Property<Int>

    @get:Input
    @get:Optional
    public abstract val enableAzureFunction: Property<Boolean>

    @get:Input
    @get:Optional
    public abstract val enableCloudflareWorker: Property<Boolean>
}