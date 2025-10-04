@file:Suppress("UnstableApiUsage")

import earth.terrarium.cloche.api.metadata.ModMetadata
import net.msrandom.minecraftcodev.core.utils.lowerCamelCaseGradleName
import net.msrandom.minecraftcodev.fabric.task.JarInJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("me.owdding.gradle") version "1.1.1"
    kotlin("jvm") version "2.2.0"
    alias(libs.plugins.terrarium.cloche)
    id("maven-publish")
    alias(libs.plugins.kotlin.symbol.processor)
}

repositories {
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
    maven(url = "https://repo.hypixel.net/repository/Hypixel/")
    maven(url = "https://api.modrinth.com/maven")
    maven(url = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven(url = "https://maven.nucleoid.xyz")
    maven(url = "https://maven.msrandom.net/repository/cloche")
    maven(url = "https://maven.msrandom.net/repository/root")
    mavenCentral()
    mavenLocal()
}

evaluationDependsOn(":annotations")

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_2
        freeCompilerArgs.addAll(
            "-Xmulti-platform",
            "-Xno-check-actual",
            "-Xexpect-actual-classes",
            "-Xopt-in=kotlin.time.ExperimentalTime",
        )
    }
}

dependencies {
    ksp(libs.meowdding.ktmodules)
    compileOnly(project(":annotations"))
    ksp(project(":annotations"))
}

cloche {
    metadata {
        modId = "customscoreboard"
        name = "Custom Scoreboard"
        license = ""
        clientOnly = true
        icon = "assets/customscoreboard/icon.png"
    }

    common {
        mixins.from("src/mixins/customscoreboard.mixins.json")

        dependencies {
            compileOnly(project(":annotations"))
            compileOnly(libs.meowdding.ktmodules)

            modImplementation(libs.hypixelapi)
            modImplementation(libs.skyblockapi)

            modImplementation(libs.mixinconstraints)

            modImplementation(libs.fabric.language.kotlin)

            modImplementation(libs.skyblockapi)
            modImplementation(libs.meowdding.lib)

            //modRuntimeOnly(libs.modmenu)
        }
    }

    fun createVersion(
        name: String,
        version: String = name,
        loaderVersion: Provider<String> = libs.versions.fabric.loader,
        fabricApiVersion: Provider<String> = libs.versions.fabric.api,
        endAtSameVersion: Boolean = true,
        minecraftVersionRange: ModMetadata.VersionRange.() -> Unit = {
            start = version
            if (endAtSameVersion) {
                end = version
                endExclusive = false
            }
        },
        dependencies: MutableMap<String, Provider<MinimalExternalModuleDependency>>.() -> Unit = { },
    ) {
        val dependencies = mutableMapOf<String, Provider<MinimalExternalModuleDependency>>().apply(dependencies)
        val rlib = dependencies["resourcefullib"]!!
        val rconfig = dependencies["resourcefulconfig"]!!
        val olympus = dependencies["olympus"]!!
        val scoreboardOverhaul = dependencies["scoreboard-overhaul"]!!

        fabric(name) {
            includedClient()
            minecraftVersion = version
            this.loaderVersion = loaderVersion.get()

            metadata {
                entrypoint("client") {
                    adapter = "kotlin"
                    value = "me.owdding.customscoreboard.Main"
                }

                fun dependency(modId: String, version: Provider<String>? = null) {
                    dependency {
                        this.modId = modId
                        this.required = true
                        if (version != null) version {
                            this.start = version
                        }
                    }
                }

                dependency {
                    modId = "minecraft"
                    required = true
                    version(minecraftVersionRange)
                }
                dependency("fabric")
                dependency("fabricloader", libs.versions.fabric.loader)
                dependency("resourcefulconfigkt", libs.versions.rconfigkt)
                //dependency("resourcefulconfig", rconfig.map { it.version!! })
                dependency("fabric-language-kotlin", libs.versions.fabric.language.kotlin)
                dependency("resourcefullib", rlib.map { it.version!! })
                dependency("skyblock-api", libs.versions.skyblockapi)
                dependency("olympus", olympus.map { it.version!! })
                dependency("meowdding-lib", libs.versions.meowdding.lib)
            }

            dependencies {
                fabricApi(fabricApiVersion, minecraftVersion)
                modImplementation(olympus) { exclude("net.fabricmc.fabric-api") }
                modImplementation(rconfig)  { exclude("net.fabricmc.fabric-api") }
                modImplementation(rlib) { exclude("net.fabricmc.fabric-api") }
                modImplementation(libs.resourcefulkt.config) { exclude("net.fabricmc.fabric-api") }

                modCompileOnly(scoreboardOverhaul)

                include(libs.skyblockapi)
                include(libs.meowdding.lib)
                include(rlib)
                include(olympus)
                include(rconfig)
                include(libs.resourcefulkt.config)
            }

            runs {
                client()
            }
        }
    }

    createVersion("1.21.5", fabricApiVersion = provider { "0.127.1" }) {
        this["resourcefullib"] = libs.resourceful.lib1215
        this["resourcefulconfig"] = libs.resourceful.config1215
        this["olympus"] = libs.olympus.lib1215
        this["scoreboard-overhaul"] = libs.scoreboard.overhaul1215
    }
    createVersion("1.21.8", minecraftVersionRange = {
        start = "1.21.6"
        end = "1.21.8"
        endExclusive = false
    }) {
        this["resourcefullib"] = libs.resourceful.lib1218
        this["resourcefulconfig"] = libs.resourceful.config1218
        this["olympus"] = libs.olympus.lib1218
        this["scoreboard-overhaul"] = libs.scoreboard.overhaul1218
    }
    createVersion("1.21.9", endAtSameVersion = false, fabricApiVersion = provider { "0.133.7" }) {
        this["resourcefullib"] = libs.resourceful.lib1219
        this["resourcefulconfig"] = libs.resourceful.config1219
        this["olympus"] = libs.olympus.lib1219
        this["scoreboard-overhaul"] = libs.scoreboard.overhaul1219
    }

    mappings { official() }
}

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

java {
    withSourcesJar()
}

meowdding {
    setupClocheClasspathFix()
    configureModules = true
    projectName = "CustomScoreboard"
}

tasks.withType<JarInJar>().configureEach {
    include { !it.name.endsWith("-dev.jar") }
    archiveBaseName = "CustomScoreboard"
}

cloche.targets.forEach {
    tasks.named(lowerCamelCaseGradleName("accessWiden", it.name, "CommonMinecraft")) {
        dependsOn(tasks.getByPath(":annotations:build"))
    }
}
