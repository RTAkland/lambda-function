plugins {
    kotlin("multiplatform") version "2.4.21" apply false
    id("maven-publish")
}

allprojects {
    group = "cn.rtast.example"
    if (name != rootProject.name) version = property("version.$name")!!

    repositories {
        mavenCentral()
    }
}

subprojects {
    pluginManager.apply("org.jetbrains.kotlin.multiplatform")
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
                name = "example"
                description = "Example"
                url = "https://repo.rtast.cn/RTAkland/kmp-library-template"
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