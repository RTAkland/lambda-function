rootProject.name = "lambda-function"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

includeSubModule("gradle-plugin")
includeSubModule("azure-runtime")
//includeSubModule("aws-runtime")

includeSubModule("azure-test")
//includeSubModule("aws-test")

fun includeSubModule(name: String, path: String? = null) {
    include(":$name")
    project(":$name").projectDir = file(path ?: "lambda-$name")
}