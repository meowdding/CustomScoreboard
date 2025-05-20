plugins {
    alias(libs.plugins.kotlin)
}

repositories {
    mavenCentral()
    maven("https://maven.teamresourceful.com/repository/maven-public/")
}

dependencies {
    implementation(libs.kotlin.poet)
    implementation(libs.kotlin.poet.ksp)
    implementation(libs.ksp)
}

kotlin {
    jvmToolchain(21)
}
