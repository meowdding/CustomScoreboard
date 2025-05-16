import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    kotlin("jvm") version "2.0.0"
    id("fabric-loom") version "1.10-SNAPSHOT"
    alias(libs.plugins.ksp)
}

ksp {
    arg("meowdding.modules.project_name", "CustomScoreboard")
    arg("meowdding.modules.package", "gay.j10a1n15.customscoreboard.generated")
}

repositories {
    maven("https://maven.teamresourceful.com/repository/maven-public/")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://repo.hypixel.net/repository/Hypixel/")
    maven("https://api.modrinth.com/maven")
}

dependencies {
    compileOnly(libs.meowdding.ktmodules)
    ksp(libs.meowdding.ktmodules)

    minecraft(libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.21.5:2025.04.19@zip")
    })
    modImplementation(libs.loader)
    modImplementation(libs.fabrickotlin)
    modImplementation(libs.fabric)

    modImplementation(libs.hypixelapi)
    modImplementation(libs.skyblockapi)
    modImplementation(libs.rconfig)
    modImplementation(libs.rconfigkt)
    modImplementation(libs.rlib)
    modImplementation(libs.olympus)
    modImplementation(libs.meowdding.lib)

    include(libs.hypixelapi)
    include(libs.skyblockapi)
    include(libs.rconfig)
    include(libs.rconfigkt)
    include(libs.rlib)
    include(libs.olympus)
    include(libs.meowdding.lib)

    modRuntimeOnly(libs.devauth)
}

loom {
    runs {
        getByName("client") {
            property("devauth.configDir", rootProject.file(".devauth").absolutePath)
        }
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }

    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

}

java {
    withSourcesJar()
}
