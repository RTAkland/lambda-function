rootProject.name = "lambda-function"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
        maven("https://repo.rtast.cn/packages")
    }
}

includeSubModule("gradle-plugin")
includeSubModule("azure-runtime")
includeSubModule("cloudflare-runtime")
includeSubModule("azure-test")

fun includeSubModule(name: String, path: String? = null) {
    include(":$name")
    project(":$name").projectDir = file(path ?: "lambda-$name")
}