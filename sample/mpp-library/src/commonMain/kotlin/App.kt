/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import com.icerockdev.library.universal.CartNavigationScreen
import com.icerockdev.library.universal.CartScreen
import com.icerockdev.library.universal.ProductScreen
import com.icerockdev.library.universal.ProductsNavigationScreen
import com.icerockdev.library.universal.ProductsScreen
import com.icerockdev.library.universal.RootBottomNavigationScreen
import com.icerockdev.library.universal.WidgetsScreen
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.BaseApplication
import dev.icerock.moko.widgets.screen.Screen
import kotlin.reflect.KClass

object App : BaseApplication() {
    override fun setup() {
        registerScreenFactory(RootBottomNavigationScreen::class) { RootBottomNavigationScreen(this) }
        registerScreenFactory(ProductsNavigationScreen::class) { ProductsNavigationScreen(this) }
        registerScreenFactory(CartNavigationScreen::class) { CartNavigationScreen(this) }
        registerScreenFactory(WidgetsScreen::class) { WidgetsScreen() }
        registerScreenFactory(ProductsScreen::class) { ProductsScreen() }
        registerScreenFactory(CartScreen::class) { CartScreen() }
        registerScreenFactory(ProductScreen::class) { ProductScreen() }
    }

    override fun getRootScreen(): KClass<out Screen<Args.Empty>> {
        return RootBottomNavigationScreen::class
    }
}

/*
e: Compilation failed: null

 * Source files: App.kt, SharedFactory.kt, Theme.kt, CryptoProfileSample.kt, McommerceProfileSample.kt, PostsSample.kt, SharedScreenSample.kt, SocialProfileSample.kt, StateSample.kt, UsersSample.kt, PostUnitItem.kt, UserUnitItem.kt
 * Compiler version info: Konan: 1.3.50 / Kotlin: 1.3.50
 * Output kind: FRAMEWORK

e: java.lang.StackOverflowError
	at kotlin.collections.ArraysKt___ArraysKt.zip(_Arrays.kt:15297)
	at org.jetbrains.kotlin.resolve.calls.inference.CapturedTypeConstructorKt.wrapWithCapturingSubstitution(CapturedTypeConstructor.kt:111)
	at org.jetbrains.kotlin.resolve.calls.inference.CapturedTypeConstructorKt.wrapWithCapturingSubstitution$default(CapturedTypeConstructor.kt:107)
	at org.jetbrains.kotlin.resolve.scopes.SubstitutingScope.<init>(SubstitutingScope.kt:32)
	at org.jetbrains.kotlin.descriptors.impl.AbstractClassDescriptor.getMemberScope(AbstractClassDescriptor.java:104)
	at org.jetbrains.kotlin.types.KotlinTypeFactory.computeMemberScope(KotlinTypeFactory.kt:36)
	at org.jetbrains.kotlin.types.KotlinTypeFactory.simpleType(KotlinTypeFactory.kt:59)
	at org.jetbrains.kotlin.types.TypeSubstitutionKt.replace(TypeSubstitution.kt:141)
	at org.jetbrains.kotlin.types.TypeSubstitutionKt.replace(TypeSubstitution.kt:126)
	at org.jetbrains.kotlin.types.TypeSubstitutor.substituteCompoundType(TypeSubstitutor.java:260)
	at org.jetbrains.kotlin.types.TypeSubstitutor.unsafeSubstitute(TypeSubstitutor.java:225)
	at org.jetbrains.kotlin.types.TypeSubstitutor.substituteWithoutApproximation(TypeSubstitutor.java:124)
	at org.jetbrains.kotlin.types.TypeSubstitutor.substitute(TypeSubstitutor.java:109)
	at org.jetbrains.kotlin.types.TypeSubstitutor.substitute(TypeSubstitutor.java:102)
	at org.jetbrains.kotlin.types.StarProjectionImplKt.starProjectionType(StarProjectionImpl.kt:50)
	at org.jetbrains.kotlin.types.StarProjectionImpl$_type$2.invoke(StarProjectionImpl.kt:33)
	at org.jetbrains.kotlin.types.StarProjectionImpl$_type$2.invoke(StarProjectionImpl.kt:24)
	at kotlin.SafePublicationLazyImpl.getValue(LazyJVM.kt:107)
	at org.jetbrains.kotlin.types.StarProjectionImpl.get_type(StarProjectionImpl.kt)
	at org.jetbrains.kotlin.types.StarProjectionImpl.getType(StarProjectionImpl.kt:36)
	at org.jetbrains.kotlin.backend.konan.objcexport.ObjCExportTranslatorImpl.mapReferenceTypeIgnoringNullability$backend_native_compiler(ObjCExportHeaderGenerator.kt:817)
	at org.jetbrains.kotlin.backend.konan.objcexport.ObjCExportTranslatorImpl.mapReferenceTypeIgnoringNullability$backend_native_compiler(ObjCExportHeaderGenerator.kt:817)
	at org.jetbrains.kotlin.backend.konan.objcexport.ObjCExportTranslatorImpl.mapReferenceTypeIgnoringNullability$backend_native_compiler(ObjCExportHeaderGenerator.kt:817)
	at org.jetbrains.kotlin.backend.konan.objcexport.ObjCExportTranslatorImpl.mapReferenceTypeIgnoringNullability$backend_native_compiler(ObjCExportHeaderGenerator.kt:817)
	at org.jetbrains.kotlin.backend.konan.objcexport.ObjCExportTranslatorImpl.mapReferenceTypeIgnoringNullability$backend_native_compiler(ObjCExportHeaderGenerator.kt:817)
 */
//open class Bla<T: Bla<T>> {
//
//}
//
//class B2: Bla<B2>() {
//
//}
//
//val anyBla: Bla<*> = B2()