package me.owdding.customscoreboard

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeSpec.Companion.anonymousClassBuilder
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.writeTo

internal class Processor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private var ran = false

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (ran) return emptyList()
        ran = true
        logger.warn("--- Scoreboard Processor ---")

        resolver.getAnnotatedSymbols<AutoElement>().filterIsInstance<KSClassDeclaration>()
            .groupBy { it.getSuperClass() }
            .mapKeys { (key, _) -> Kind.getByName(key?.simpleName?.asString()) }
            .mapValues { (_, value) ->
                value.map { Declaration(it, ElementGroup.byName(it.getField<AutoElement, KSDeclaration>("element")?.simpleName?.asString())) }
                    .sortedBy { (_, group) -> group.ordinal }
            }
            .mapNotNull { (key, value) -> key?.let { it to value } }
            .map { (kind, declarations) -> this.createEnum(kind, declarations) }

        return emptyList()
    }

    private fun createEnum(kind: Kind, declarations: List<Declaration>) {
        logger.warn("Found ${declarations.size} entries for ${kind.typeName}")
        val file = FileSpec.builder(
            ModuleProvider::class.java.packageName + ".generated",
            kind.enumName,
        ).indent("    ").apply {
            this.addImport("me.owdding.customscoreboard.utils.TextUtils", "toComponent")
            this.addType(
                TypeSpec.enumBuilder(kind.enumName)
                    .apply {
                        val field = kind.name.lowercase()

                        this.primaryConstructor(
                            FunSpec.constructorBuilder()
                                .addParameter(
                                    field,
                                    ClassName(kind.packageName, kind.typeName),
                                ).build(),
                        )
                        this.addSuperinterface(ClassName("com.teamresourceful.resourcefulconfig.api.types.info", "TooltipProvider"))
                        this.addProperty(PropertySpec.builder(field).initializer(field).build())
                        this.addFunction(FunSpec.builder("toString")
                            .returns(String::class.asClassName())
                            .addModifiers(KModifier.OVERRIDE)
                            .addCode("return $field.configLine")
                            .build())
                        this.addFunction(FunSpec.builder("getTooltip")
                            .addModifiers(KModifier.OVERRIDE)
                            .returns(ClassName("net.minecraft.network.chat", "Component"))
                            .addCode("return $field.configLineHover.joinToString(\"\\n\").toComponent()")
                            .build())

                        declarations.forEach {
                            this.addEnumConstant(
                                kind.fix(it.type.simpleName.asString()),
                                anonymousClassBuilder()
                                    .addSuperclassConstructorParameter(it.type.qualifiedName!!.asString())
                                    .build(),
                            )
                        }
                    }
                    .build(),
            )
        }

        file.build().writeTo(codeGenerator, Dependencies(true))
    }
}

internal data class Declaration(val type: KSClassDeclaration, val group: ElementGroup)

internal enum class Kind(val enumName: String, val packageName: String, val typeName: String) {
    ELEMENT(
        "ScoreboardEntry",
        "me.owdding.customscoreboard.feature.customscoreboard.elements",
        "Element",
    ),
    EVENT(
        "ScoreboardEventEntry",
        "me.owdding.customscoreboard.feature.customscoreboard.events",
        "Event",
    ),
    ;

    fun fix(name: String) = name.replace(Regex("^(Element|Event)"), "").replace(Regex("([a-z])([A-Z])"), "$1_$2").uppercase()

    companion object {
        fun getByName(name: String?) = entries.firstOrNull { it.name.equals(name, true) }
    }
}

internal class ModuleProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment,
    ): SymbolProcessor {
        return Processor(
            environment.codeGenerator,
            environment.logger,
        )
    }
}
