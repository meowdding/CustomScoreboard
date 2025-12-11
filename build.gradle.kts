import net.fabricmc.loom.task.ValidateAccessWidenerTask
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    id("fabric-loom")
    `versioned-catalogues`
    kotlin("jvm") version "2.2.0"
    alias(libs.plugins.meowdding.auto.mixins)
    alias(libs.plugins.kotlin.symbol.processor)
}

repositories {
    fun scopedMaven(url: String, vararg paths: String) = maven(url) { content { paths.forEach(::includeGroupAndSubgroups) } }

    scopedMaven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1", "me.djtheredstoner")
    scopedMaven("https://repo.hypixel.net/repository/Hypixel", "net.hypixel")
    scopedMaven("https://maven.parchmentmc.org/", "org.parchmentmc")
    scopedMaven("https://api.modrinth.com/maven", "maven.modrinth")
    scopedMaven(
        "https://maven.teamresourceful.com/repository/maven-public/",
        "earth.terrarium",
        "com.teamresourceful",
        "tech.thatgravyboat",
        "me.owdding",
        "com.terraformersmc"
    )
    scopedMaven("https://maven.nucleoid.xyz/", "eu.pb4")
    scopedMaven(url = "https://maven.shedaniel.me/", "me.shedaniel", "dev.architectury")
    mavenCentral()
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    compilerOptions.optIn.add("kotlin.time.ExperimentalTime")
    compilerOptions.freeCompilerArgs.addAll(
        "-Xcontext-parameters",
        "-Xcontext-sensitive-resolution",
        "-Xnullability-annotations=@org.jspecify.annotations:warn"
    )
}

evaluationDependsOn(":annotations")

dependencies {
    minecraft(versionedCatalog["minecraft"])
    mappings(loom.layered {
        officialMojangMappings()
        parchment(variantOf(versionedCatalog["parchment"]) {
            artifactType("zip")
        })
    })
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.language.kotlin)
    modImplementation(versionedCatalog["fabric.api"])

    modRuntimeOnly(versionedCatalog["placeholders"])
    modCompileOnly(versionedCatalog["scoreboard.overhaul"])

    api(libs.skyblockapi) {
        capabilities { requireCapability("tech.thatgravyboat:skyblock-api-${stonecutter.current.version}") }
    }
    include(libs.skyblockapi) {
        capabilities { requireCapability("tech.thatgravyboat:skyblock-api-${stonecutter.current.version}-remapped") }
    }
    api(libs.meowdding.lib) {
        capabilities { requireCapability("me.owdding.meowdding-lib:meowdding-lib-${stonecutter.current.version}") }
    }
    include(libs.meowdding.lib) {
        capabilities { requireCapability("me.owdding.meowdding-lib:meowdding-lib-${stonecutter.current.version}-remapped") }
    }
    compileOnly(project(":annotations"))
    ksp(project(":annotations"))

    compileOnly(libs.meowdding.ktmodules)
    ksp(libs.meowdding.ktmodules)

    modImplementation(libs.hypixelapi)
    modImplementation(libs.mixinconstraints)

    includeImplementation(versionedCatalog["resourceful.lib"])
    includeImplementation(versionedCatalog["resourceful.config"])
    includeImplementation(versionedCatalog["olympus"])
    includeImplementation(libs.resourcefulkt.config)
}

fun DependencyHandler.includeImplementation(dep: Any) {
    include(dep)
    modImplementation(dep)
}


val mcVersion = stonecutter.current.version.replace(".", "")
val accessWidenerFile = rootProject.file("src/customscoreboard.accesswidener")
loom {
    runConfigs["client"].apply {
        ideConfigGenerated(true)
        runDir = "../../run"
        vmArg("-Dfabric.modsFolder=" + '"' + rootProject.projectDir.resolve("run/${mcVersion}Mods").absolutePath + '"')
    }

    if (accessWidenerFile.exists()) {
        accessWidenerPath.set(accessWidenerFile)
    }

    mixin {
        defaultRefmapName = "customscoreboard-refmap.json"
    }
}

ksp {
    arg("meowdding.project_name", "CustomScoreboard")
    arg("meowdding.package", "me.owdding.customscoreboard.generated")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    compilerOptions.optIn.add("kotlin.time.ExperimentalTime")
    compilerOptions.freeCompilerArgs.add(
        "-Xnullability-annotations=@org.jspecify.annotations:warn"
    )
}

tasks.processResources {
    val replacements = mapOf(
        "version" to version,
        "minecraft_start" to versionedCatalog.versions.getOrFallback("minecraft.start", "minecraft"),
        "minecraft_end" to versionedCatalog.versions.getOrFallback("minecraft.end", "minecraft"),
        "fabric_lang_kotlin" to libs.versions.fabric.language.kotlin.get(),
        "rlib" to versionedCatalog.versions["resourceful-lib"],
        "olympus" to versionedCatalog.versions["olympus"],
        "sbapi" to libs.versions.skyblockapi.get(),
        "mlib" to libs.versions.meowdding.lib.get(),
        "rconfigkt" to libs.versions.rconfigkt.get(),
        "rconfig" to versionedCatalog.versions["resourceful-config"],
    )
    inputs.properties(replacements)

    filesMatching("fabric.mod.json") {
        expand(replacements)
    }
}

autoMixins {
    mixinPackage = "me.owdding.customscoreboard.mixins"
    projectName = "customscoreboard"
    plugin = "com.moulberry.mixinconstraints.ConstraintsMixinPlugin"
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true

        excludeDirs.add(file("run"))
    }
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    filesMatching(listOf("**/*.fsh", "**/*.vsh")) {
        filter { if (it.startsWith("//!moj_import")) "#${it.substring(3)}" else it }
    }
    with(copySpec {
        from(rootProject.file("src/lang")).include("*.json").into("assets/customscoreboard/lang")
    })
    with(copySpec {
        from(accessWidenerFile)
    })
}

val archiveName = "CustomScoreboard"

base {
    archivesName.set("$archiveName-${archivesName.get()}")
}

tasks.named("build") {
    doLast {
        val sourceFile = rootProject.projectDir.resolve("versions/${project.name}/build/libs/${archiveName}-${stonecutter.current.version}-$version.jar")
        val targetFile = rootProject.projectDir.resolve("build/libs/${archiveName}-$version-${stonecutter.current.version}.jar")
        targetFile.parentFile.mkdirs()
        targetFile.writeBytes(sourceFile.readBytes())
    }
}

tasks.withType<ValidateAccessWidenerTask> { enabled = false }
