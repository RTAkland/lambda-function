/*
 * Copyright © 2026 rtakland
 * Author: rtakland
 * Date: 2026-10-31
 */


package cn.rtast.lambda.function.plugin

import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import java.io.File

public object CloudflareWorkerTask {
    public fun registerExtension(project: Project, ext: LambdaFunctionExtension) {
        registerDevTasks(project, ext)
        registerDeployTasks(project, ext)
    }

    private fun registerDevTasks(project: Project, ext: LambdaFunctionExtension) {
        val wranglerRunDir = project.layout.buildDirectory.dir("wrangler-run")
        val compileDevTaskName = "compileDevelopmentExecutableKotlinJs"

        val prepareWranglerRun = project.tasks.register("prepareWranglerRun") { task ->
            task.group = "wrangler"
            task.description = "Prepare development output for wrangler"
            task.dependsOn(project.tasks.matching { it.name == compileDevTaskName })
            task.doLast {
                val buildOutputDir = project.layout.buildDirectory
                    .dir("compileSync/js/main/developmentExecutable/kotlin")
                    .get().asFile
                val targetDir = wranglerRunDir.get().asFile.apply { mkdirs() }
                if (buildOutputDir.exists()) {
                    buildOutputDir.copyRecursively(targetDir, overwrite = true)
                } else {
                    project.logger.warn("Kotlin/JS dev output not found at $buildOutputDir")
                }
                targetDir.listFiles()!!.forEach { if (it.extension == "map") it.delete() }
            }
        }

        project.tasks.register("wranglerDev", Exec::class.java) { task ->
            task.group = "wrangler"
            task.description = "Run wrangler dev on port ${ext.wranglerPort.get()}"
            task.dependsOn(prepareWranglerRun)

            val workingDir = wranglerRunDir.get().asFile.apply { mkdirs() }
            task.workingDir(workingDir)

            task.doFirst {
                copyConfigFiles(project, ext, workingDir)
            }

            task.commandLine(
                "sh", "-c",
                buildShellCommand("wrangler dev --port ${ext.wranglerPort.get()}")
            )
            task.standardInput = System.`in`
            task.isIgnoreExitValue = false
        }
    }

    private fun registerDeployTasks(project: Project, ext: LambdaFunctionExtension) {
        val wranglerDeployDir = project.layout.buildDirectory.dir("wrangler-deploy")
        val compileProdTaskName = "compileProductionExecutableKotlinJs"

        val prepareProductionDeploy = project.tasks.register("prepareProductionDeploy") { task ->
            task.group = "wrangler"
            task.description = "Prepare production output for wrangler deploy"
            task.dependsOn(project.tasks.matching { it.name == compileProdTaskName })
            task.doLast {
                val targetDir = wranglerDeployDir.get().asFile
                targetDir.deleteRecursively()
                targetDir.mkdirs()

                val buildOutputDir = project.layout.buildDirectory
                    .dir("compileSync/js/main/productionExecutable/kotlin")
                    .get().asFile
                if (buildOutputDir.exists()) {
                    buildOutputDir.copyRecursively(targetDir, overwrite = true)
                } else {
                    project.logger.warn("Kotlin/JS production output not found at $buildOutputDir")
                }
                copyConfigFiles(project, ext, targetDir)
                targetDir.listFiles()!!.forEach { if (it.extension == "map") it.delete() }
            }
        }

        project.tasks.register("wranglerDeploy", Exec::class.java) { task ->
            task.group = "wrangler"
            task.description = "Deploy Cloudflare Worker with wrangler"
            task.dependsOn(prepareProductionDeploy)

            val workingDir = wranglerDeployDir.get().asFile.apply { mkdirs() }
            task.workingDir(workingDir)
            task.commandLine("sh", "-c", buildShellCommand("wrangler deploy"))
            task.standardInput = System.`in`
            task.isIgnoreExitValue = false
        }
    }

    private fun buildShellCommand(rawCommand: String): String =
        $$"""
        export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
        [ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
        exec $$rawCommand
        """.trimIndent()

    private fun copyConfigFiles(project: Project, ext: LambdaFunctionExtension, targetDir: File) {
        fun copyOrWarn(source: File?, destName: String) {
            val dest = targetDir.resolve(destName)
            if (source != null && source.exists()) {
                source.copyTo(dest, overwrite = true)
            } else {
                project.logger.warn("Config file not found: ${source?.absolutePath ?: destName}")
            }
        }

        copyOrWarn(ext.wranglerFile.orNull?.asFile, "wrangler.toml")
        copyOrWarn(ext.wranglerDevVars.orNull?.asFile, ".dev.vars")
    }
}