plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.maven.publish)
}

allprojects {
    group = "cn.rtast.lambdafunction"
    property("version")!!

    repositories {
        mavenCentral()
        maven("https://repo.rtast.cn/packages")
    }
}

subprojects s@{
    if (this@s.name.contains("cloudflare")
        || this@s.name.contains("test")
        || this@s.name.contains("nghttp2")
    ) return@s
    pluginManager.apply("maven-publish")

    publishing {
        repositories {
            maven("https://repo.rtast.cn/packages") {
                credentials(HttpHeaderCredentials::class) {
                    name = "Authorization"
                    val token = providers.gradleProperty("PUBLISH_TOKEN")
                        .orElse(providers.environmentVariable("PUBLISH_TOKEN"))
                        .orNull
                    value = "Bearer $token"
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }

        publications.withType<MavenPublication> {
            pom {
                name = "lambda-function"
                description = "Kotlin Native Azure Function Custom Handler Runtime based on ktor(native)"
                url = "https://repo.rtast.cn/RTAkland/lambda-function"
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    }
                }
                developers {
                    developer {
                        id = "rtakland"
                        name = "RTAkland"
                    }
                }
            }
        }
    }
}