rootProject.name = "kmp-library-template"

includeSubModule("example")

fun includeSubModule(name: String, path: String? = null) {
    include(":$name")
    project(":$name").projectDir = file(path ?: "lib-$name")
}