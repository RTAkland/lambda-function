/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-23
 */


package cn.rtast.lambda.function.plugin

import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Zip
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.io.File

object AzureFunctionTask {
    fun registerExtension(target: Project, ext: LambdaFunctionExtension) = with(target) {
        plugins.withId("org.jetbrains.kotlin.multiplatform") {
            val kmp = extensions.getByType(KotlinMultiplatformExtension::class.java)

            kmp.targets.configureEach { target ->
                if (target is KotlinNativeTarget) {
                    registerPackageTask(project, target, ext)
                    registerDeployTasks(project, target.name, ext.appName.get())
                }
            }

            tasks.register("azureFunctionRun", Exec::class.java) { task ->
                val currentPlatform = currentPlatform()
                task.doFirst {
                    copyConfigFiles(project, ext, currentPlatform)
                    copyExecutable(project, currentPlatform.replaceFirstChar { it.lowercase() }, "debugExecutable")
                    copyResources(project, ext.customResources.get())
                }
                task.group = "azure-function"
                task.description = "Package Azure Function for $currentPlatform"
                task.dependsOn(tasks.named("linkDebugExecutable${currentPlatform}"))
                val outputDir = project.layout.buildDirectory.dir("run-azure-function").get().asFile.apply { mkdirs() }
                task.workingDir(outputDir)
                task.commandLine("func", "start")
            }
        }
    }

    private fun registerPackageTask(project: Project, target: KotlinNativeTarget, ext: LambdaFunctionExtension) {
        val targetName = target.name
        val capName = targetName.replaceFirstChar { it.uppercase() }
        val taskName = "package$capName"

        project.tasks.register(taskName) { task ->
            task.group = "azure-function"
            task.description = "Package Azure Function for $targetName"
            task.doFirst { copyConfigFiles(project, ext, targetName) }
            val linkTaskName = "linkReleaseExecutable$capName"
            task.dependsOn(project.tasks.matching { it.name == linkTaskName })
            task.doLast {
                copyExecutable(project, targetName, "releaseExecutable")
                copyResources(project, ext.customResources.get())
            }
        }

        val zipTaskName = "zip$capName"
        project.tasks.register(zipTaskName, Zip::class.java) { zip ->
            zip.group = "azure-function"
            zip.description = "Zip Azure Function package for $targetName"
            zip.dependsOn(taskName)

            zip.from(project.layout.buildDirectory.dir("run-azure-function")) {
                it.exclude("local.settings.json")
            }
            zip.archiveFileName.set(
                "${project.name}-${project.version}-$targetName.zip"
            )
            zip.destinationDirectory.set(project.layout.buildDirectory.dir("dist"))
        }
        project.tasks.named(taskName).configure { it.finalizedBy(zipTaskName) }
    }

    private fun registerDeployTasks(project: Project, target: String, appName: String) {
        project.tasks.register("deploy${target.replaceFirstChar { it.uppercase() }}", Exec::class.java) { task ->
            val currentPlatform = currentPlatform()
            task.group = "azure-function"
            task.description = "Deploy Azure Function for $currentPlatform"
            task.dependsOn(project.tasks.named("package${currentPlatform}"))
            val outputDir = project.layout.buildDirectory.dir("run-azure-function").get().asFile.apply { mkdirs() }
            task.workingDir(outputDir)
            task.commandLine("func", "azure", "functionapp", "publish", appName)
        }
    }

    private fun copyResources(project: Project, included: List<File>) {
        val outputDir = project.layout.buildDirectory
            .get().asFile
            .resolve("run-azure-function")
            .apply { mkdirs() }
        included.forEach { src ->
            if (src.exists()) {
                val destFile = outputDir.resolve(src.name)
                if (src.isDirectory) {
                    src.copyRecursively(destFile, overwrite = true)
                } else {
                    destFile.parentFile.mkdirs()
                    src.copyTo(destFile, overwrite = true)
                }
            }
        }
    }

    private fun copyExecutable(project: Project, targetName: String, buildTypeDir: String) {
        val exeName = buildExecutableName(project.name, targetName)
        val buildDir = project.layout.buildDirectory.get().asFile
        val src = buildDir.resolve("bin/$targetName/$buildTypeDir/$exeName")
        val outputDir = buildDir.resolve("run-azure-function").apply { mkdirs() }
        if (!src.exists()) error("Executable not found at $src; did the link task run?")
        val dest = outputDir.resolve(exeName)
        src.copyTo(dest, overwrite = true)
        if (!targetName.startsWith("mingw")) dest.setExecutable(true, false)
    }

    private fun buildExecutableName(moduleName: String, targetName: String): String {
        val ext = if (targetName.startsWith("mingw")) "exe" else "kexe"
        return "$moduleName.$ext"
    }

    private fun copyConfigFiles(project: Project, ext: LambdaFunctionExtension, targetName: String) {
        val outputDir = project.layout.buildDirectory.dir("run-azure-function").get().asFile.apply { mkdirs() }
        fun copyOrWrite(source: File?, relativePath: String, transform: (String) -> String = { it }) {
            val dest = outputDir.resolve(relativePath)
            dest.parentFile.mkdirs()
            val content = source?.readText() ?: readResource(relativePath)
            dest.writeText(transform(content))
        }

        val exeName = buildExecutableName(project.name, targetName)
        copyOrWrite(ext.hostJsonFile.orNull, "host.json") { it.replace("{{AUTO_DETECTED}}", exeName) }
        copyOrWrite(ext.functionJsonFile.orNull, "delegate_/function.json")
        copyOrWrite(ext.localSettingsFile.orNull, "local.settings.json")
    }
}