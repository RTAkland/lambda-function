import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.build.config)
    alias(libs.plugins.java.gradle.plugin)
}

dependencies {
    implementation(libs.kotlin.gradle.plugin.api)
    implementation(libs.kotlin.gradle.plugin)
}

buildConfig {
    packageName(project.group.toString())
    buildConfigField("String", "KOTLIN_PLUGIN_ID", "\"${rootProject.group}\"")
    buildConfigField("String", "KOTLIN_PLUGIN_VERSION", "\"${version}\"")
}

gradlePlugin {
    plugins {
        create("LambdaFunction") {
            id = "cn.rtast.lambdafunction"
            displayName = "Lambda Function"
            description = "Kotlin Native Serverless Function Runtime"
            implementationClass = "cn.rtast.lambda.function.plugin.LambdaFunctionPlugin"
        }
    }
}

tasks.compileKotlin {
    compilerOptions.jvmTarget = JvmTarget.JVM_11
}

tasks.compileJava {
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

kotlin {
    explicitApi()
}