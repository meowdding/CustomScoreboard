@file:Suppress("UnstableApiUsage")

import net.msrandom.minecraftcodev.core.utils.toPath
import net.msrandom.minecraftcodev.runs.task.WriteClasspathFile
import net.msrandom.stubs.GenerateStubApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.io.path.readText
import kotlin.io.path.writeText

plugins {
    java
    kotlin("jvm") version "2.0.0"
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

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_0
        freeCompilerArgs.addAll(
            "-Xmulti-platform",
            "-Xno-check-actual",
            "-Xexpect-actual-classes",
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

            include(libs.hypixelapi)
            include(libs.skyblockapi)
            include(libs.meowdding.lib)
            include(rlib)
            include(olympus)
            include(rconfig)
            include(libs.resourcefulkt.config)

            metadata {
                entrypoint("client") {
                    adapter = "kotlin"
                    value = "me.owdding.customscoreboard.Main"
                }

                fun dependency(modId: String, version: Provider<String>) {
                    dependency {
                        this.modId = modId
                        this.required = true
                        version {
                            this.start = version
                        }
                    }
                }

                dependency("fabric-language-kotlin", libs.versions.fabric.language.kotlin)
                dependency("resourcefullib", rlib.map { it.version!! })
                dependency("skyblock-api", libs.versions.skyblockapi)
                dependency("olympus", olympus.map { it.version!! })
                dependency("meowdding-lib", libs.versions.meowdding.lib)
            }

            dependencies {
                fabricApi(fabricApiVersion, minecraftVersion)
                modImplementation(olympus)
                modImplementation(rconfig)
                modImplementation(libs.resourcefulkt.config)

                modCompileOnly(scoreboardOverhaul)
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
    createVersion("1.21.8") {
        this["resourcefullib"] = libs.resourceful.lib1218
        this["resourcefulconfig"] = libs.resourceful.config1218
        this["olympus"] = libs.olympus.lib1218
        this["scoreboard-overhaul"] = libs.scoreboard.overhaul1218
    }

    mappings { official() }
}

tasks.named("createCommonApiStub", GenerateStubApi::class) {
    excludes.add(libs.skyblockapi.get().module.toString())
    excludes.add(libs.meowdding.lib.get().module.toString())
}

// TODO temporary workaround for a cloche issue on certain systems, remove once fixed
tasks.withType<WriteClasspathFile>().configureEach {
    actions.clear()
    actions.add {
        generate()
        val file = output.get().toPath()
        file.writeText(file.readText().lines().joinToString(File.pathSeparator))
    }
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


ksp {
    arg("meowdding.modules.project_name", "CustomScoreboard")
    arg("meowdding.modules.package", "me.owdding.customscoreboard.generated")
    this@ksp.excludedSources.from(sourceSets.getByName("1215").kotlin.srcDirs)
    this@ksp.excludedSources.from(sourceSets.getByName("1218").kotlin.srcDirs)
}
