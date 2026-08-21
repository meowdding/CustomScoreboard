import net.fabricmc.loom.task.ValidateAccessWidenerTask
import org.gradle.kotlin.dsl.compileOnly
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("net.fabricmc.fabric-loom")
    id("versioned-catalogues")
    kotlin("jvm")
    id("me.owdding.auto-mixins")
    id("com.google.devtools.ksp")
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
    compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
    compilerOptions.optIn.add("kotlin.time.ExperimentalTime")
    compilerOptions.freeCompilerArgs.addAll(
        "-Xcontext-parameters",
        "-Xcontext-sensitive-resolution",
        "-Xnullability-annotations=@org.jspecify.annotations:warn"
    )
}

evaluationDependsOn(":annotations")


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
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(25)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
    compilerOptions.optIn.add("kotlin.time.ExperimentalTime")
    compilerOptions.freeCompilerArgs.add(
        "-Xnullability-annotations=@org.jspecify.annotations:warn"
    )
}

tasks.processResources {
    val range = if (versionedCatalog.versions.has("minecraft.range")) {
        versionedCatalog.versions.get("minecraft.range").toString()
    } else {
        val start = versionedCatalog.versions.getOrFallback("minecraft.start", "minecraft")
        val end = versionedCatalog.versions.getOrFallback("minecraft.end", "minecraft")
        ">=$start <=$end"
    }

    val replacements = mapOf(
        "version" to version,
        "minecraft_range" to range,
        "fabric_lang_kotlin" to versionedCatalog.versions["fabric.language.kotlin"],
        "rlib" to versionedCatalog.versions["resourceful-lib"],
        "olympus" to versionedCatalog.versions["olympus"],
        "sbapi" to versionedCatalog.versions["skyblockapi"],
        "mlib" to versionedCatalog.versions["meowdding.lib"],
        "rconfigkt" to versionedCatalog.versions["rconfigkt"],
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
        rename { it.replace(".obf", "") }
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

dependencies {
    minecraft(versionedCatalog["minecraft"])

    implementation(versionedCatalog["fabric.loader"])
    implementation(versionedCatalog["fabric.language.kotlin"])
    implementation(versionedCatalog["fabric.api"])

    runtimeOnly(versionedCatalog["placeholders"])
    if (versionedCatalog.has("scoreboard.overhaul")) {
        compileOnly(versionedCatalog["scoreboard.overhaul"])
    }

    api(versionedCatalog["skyblockapi"]) {
        capabilities { requireCapability("tech.thatgravyboat:skyblock-api-${stonecutter.current.version}") }
    }
    include(versionedCatalog["skyblockapi"]) {
        capabilities { requireCapability("tech.thatgravyboat:skyblock-api-${stonecutter.current.version}") }
    }
    api(versionedCatalog["meowdding.lib"]) {
        capabilities { requireCapability("me.owdding.meowdding-lib:meowdding-lib-${stonecutter.current.version}") }
    }
    include(versionedCatalog["meowdding.lib"]) {
        capabilities { requireCapability("me.owdding.meowdding-lib:meowdding-lib-${stonecutter.current.version}") }
    }

    compileOnlyKsp(project(":annotations"))
    compileOnlyKsp(versionedCatalog["meowdding.ktmodules"])
    compileOnlyKsp(versionedCatalog["meowdding.ktcodecs"])

    implementation(versionedCatalog["mixinconstraints"])

    includeImplementation(versionedCatalog["resourceful.lib"])
    includeImplementation(versionedCatalog["resourceful.config"])
    includeImplementation(versionedCatalog["olympus"])
    includeImplementation(versionedCatalog["resourcefulkt.config"])
}

fun DependencyHandlerScope.includeImplementation(dep: Any) {
    include(dep)
    implementation(dep)
}

fun DependencyHandlerScope.compileOnlyKsp(dep: Any) {
    compileOnly(dep)
    ksp(dep)
}
