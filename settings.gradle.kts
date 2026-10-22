rootProject.name = "lambda-function"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

includeSubModule("azure-runtime")
includeSubModule("gradle-plugin")

includeSubModule("azure-test")

fun includeSubModule(name: String, path: String? = null) {
    include(":$name")
    project(":$name").projectDir = file(path ?: "lambda-$name")
}