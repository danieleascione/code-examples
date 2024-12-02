plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.power-assert") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.aws.sdk.sqs)

    testImplementation(kotlin("test"))
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.localstack)
    testImplementation(libs.testcontainers.junit)
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-ea")
}
kotlin {
    jvmToolchain(21)

    // Enable preview features
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xenable-power-assert", // Enables Power Assert
            "-opt-in=kotlin.ExperimentalStdlibApi", // Opt-in for experimental features, if needed
        )
    }
}

powerAssert {
    functions = listOf("kotlin.assert", "kotlin.test.assertTrue", "kotlin.test.assertEquals", "kotlin.test.assertNull")
    includedSourceSets = listOf("test")
}
