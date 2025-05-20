package me.owdding.customscoreboard

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration


internal inline fun <reified T : Annotation> Resolver.getAnnotatedSymbols() = this.getSymbolsWithAnnotation(T::class.qualifiedName!!).toList()
internal fun KSClassDeclaration.getSuperClass(): KSClassDeclaration? = superTypes.map { it.resolve().declaration }.filterIsInstance<KSClassDeclaration>().firstOrNull { it.classKind != ClassKind.INTERFACE }
inline fun <reified T> KSAnnotated.getAnnotation(): KSAnnotation? = this.annotations.firstOrNull {
    it.annotationType.resolve().declaration.qualifiedName!!.asString() == T::class.qualifiedName
}

@Suppress("UNCHECKED_CAST")
internal fun <T> KSAnnotation.getAs(id: String) = this.arguments.firstOrNull { it.name?.asString() == id }?.value as? T
internal inline fun <reified A : Annotation, T> KSAnnotated.getField(name: String): T? = this.getAnnotation<A>()?.getAs(name)
