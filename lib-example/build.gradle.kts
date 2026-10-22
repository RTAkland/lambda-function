import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

kotlin {
    linuxX64()
    linuxArm64()
    mingwX64()
    jvm { compilerOptions.jvmTarget = JvmTarget.JVM_1_8 }

    explicitApi()
    withSourcesJar()

    targets.withType<KotlinNativeTarget>().configureEach target@{
        compilations["main"].cinterops {
            create("example") {
                includeDirs(file("libexample/"))
                extraOpts("-libraryPath", file("nativeLibraries/${this@target.name}"))
            }
        }
    }
}