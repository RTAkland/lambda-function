# What is lambda-function

Kotlin native serverless lambda function runtime for `Azure Functions` custom handler,
`lambda-function` currently supports develop on Linux and Windows only.

`lambda-function` is based on `ktor-server-core` and `ktor-server-cio`, it means all
ktor plugins supports native platform can be used on this runtime.

# How to use lambda-function

`lambda-function` provided a Gradle plugin to running functions, packaging and deploy to Azure Portal,
it's optional, but recommend to apply.

## Install Azure Function dev tools

> Before starting, you need to install `Azure Function Core Tools` and `Azure Command-Line Interface (CLI)`,
> following Microsoft Azure documents to learn how to install

- [Azure Function Core Tools](https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=linux%2Cisolated-process%2Cnode-v4%2Cpython-v2%2Chttp-trigger%2Ccontainer-apps&pivots=programming-language-java#install-the-azure-functions-core-tools)
- [Azure Command-Line Interface (CLI)](https://learn.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest)

## Apply Gradle Plugin

```kotlin
// settings.gradle.kts
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.rtast.cn/packages")
    }
}

// build.gradle.kts
plugins {
    // Replace the version to latest version
    id("cn.rtast.lambdafunction") version "0.1.1"
}

repositories {
    mavenCentral()
    maven("https://repo.rtast.cn/packages")
}

/**
 * Configure the required json file to your own,
 * All options except `appName` are optional, if not set, plugin fallbak to builtin config
 */
lambdaFunction {
    appName = "myazurefunction"
    hostJsonFile = file("./host.json")
    functionJsonFile = file("./function.json")  // do not change this if you know what are you doing
    localSettingsFile = file("./local.settings.json")
    // Including resources files
    customResources.addAll(*file("src/commonMain/resources").listFiles())
}
```

### Builtin config JSON files

- [host.json](lambda-gradle-plugin/src/main/resources/host.json)
- [function.json](lambda-gradle-plugin/src/main/resources/delegate_/function.json)
- [local.settings.json](lambda-gradle-plugin/src/main/resources/local.settings.json)

## Install dependencies

```kotlin
// settings.gradle.kts

kotlin {
    linuxX64()
    mingwX64()

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries { executable { entryPoint = "com.example.main" } }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("cn.rtast.lambdafunction:azure-runtime-cio:0.1.1")
        }
    }
}

// com/example/main.kt

fun main(): Unit = runAzureLambda {
    routing {
        get("/") {
            call.respond("Hello index")
        }

        get("/{name}") {
            call.respond("Hello ${call.pathParameters["name"]}")
        }
    }
}
```

# Running Function

```shell
./gradlew azureFunctionRun
```

# Packaging Function

```shell
./gradlew packageLinuxX64
./gradlew packageMingwX64
```

# Deploying Function to Azure Portal

> Login into Azure Portal by executing `az login` in terminal before execute Gradle task

```shell
./gradlew deployLinuxX64
./gradlew deployMingwX64
```

# Running Function Manually

<details>
<summary>Click to expand contents</summary>

## Building executable binary file

The function run directory file tree should look like:

```text
.
├── azure-test.kexe
├── delegate_
│   └── function.json
├── host.json
└── local.settings.json
```0.0.

```shell
./gradlew linkReleaseExecutableLinuxX64

mkdir build/lambda-function-run -p
mkdir build/lambda-function-run/delegate_ -p
chmod +x build/bin/linuxX64/releaseExecutable/azure-test.kexe
cp build/bin/linuxX64/releaseExecutable/azure-test.kexe build/lambda-function-run

# Copying host.json local.settings.json to `build/lambda-function-run`
# Replace {{AUTO_DETECTED}} in host.json with `azure-test.kexe` 
# Copying function.json to `build/lambda-function-run/delegate_`

cd build/lambda-function-run
func start
```

> Functions server listening on port `http://127.0.0.1:7071`

```shell
curl http://127.0.0.1:7071/  # Expect response: Hello index
curl http://127.0.0.1:7071/RTAkland  # Expect response: Hello RTAkland
```

## Packaging function to zip

```shell
cd build/lambda-function-run
func pack
```

## Deploying to Azure Portal

```shell
cd build/lambda-function-run
func azure functionapp publish myfunction
```

</details>

# Open Source

`lambda-function` Licensed under [Apache-2.0](LICENSE)