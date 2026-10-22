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
}

gradlePlugin {
    plugins {
        create("AzureFunctionsCustomHandler") {
            id = "cn.rtast.lambdafunction"
            displayName = "Lambda Function"
            description = "Kotlin Native Azure and AWS Lambda Serverless Framework"
            implementationClass = "cn.rtast.lambda.function.plugin.LambdaFunctionPlugin"
        }
    }
}