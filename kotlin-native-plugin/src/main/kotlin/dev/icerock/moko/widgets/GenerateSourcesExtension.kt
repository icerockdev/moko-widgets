/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.StandardFileSystems
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.extensions.CollectAdditionalSourcesExtension
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.KtValueArgument
import org.jetbrains.kotlin.psi.stubs.KotlinAnnotationEntryStub
import org.jetbrains.kotlin.psi.stubs.KotlinClassStub
import org.jetbrains.kotlin.psi.stubs.KotlinFileStub
import org.jetbrains.kotlin.psi.stubs.KotlinImportDirectiveStub
import org.jetbrains.kotlin.psi.stubs.KotlinModifierListStub
import org.jetbrains.kotlin.psi.stubs.KotlinParameterStub
import org.jetbrains.kotlin.psi.stubs.KotlinPlaceHolderStub
import org.jetbrains.kotlin.psi.stubs.KotlinTypeParameterStub
import org.jetbrains.kotlin.psi.stubs.KotlinValueArgumentStub
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstance
import java.io.File

class GenerateSourcesExtension(private val messageCollector: MessageCollector) : CollectAdditionalSourcesExtension {

    inner class Generator(project: Project) : CommonGenerator<KtFile, KotlinFileStub>() {
        private val psiManager = PsiManager.getInstance(project)
        private val localFileSystem = VirtualFileManager.getInstance().getFileSystem(StandardFileSystems.FILE_PROTOCOL)

        override fun getStub(file: KtFile): KotlinFileStub? {
            val stubTree = file.calcStubTree()
            return stubTree.root as? KotlinFileStub
        }

        override fun getGeneratorInput(ktFileStub: KotlinFileStub): List<GeneratorInput> {
            val importList = ktFileStub.childrenStubs.firstOrNull { it.stubType == KtStubElementTypes.IMPORT_LIST }
            val imports = importList?.childrenStubs
                .orEmpty()
                .filterIsInstance<KotlinImportDirectiveStub>()
                .mapNotNull { import ->
                    val str = import.getImportedFqName()?.toString() ?: return@mapNotNull null
                    if(import.isAllUnder()) "$str.*"
                    else str
                }

            return ktFileStub.childrenStubs.filterIsInstance<KotlinClassStub>().mapNotNull { classStub ->
                val modifiersList =
                    classStub.childrenStubs.filterIsInstance<KotlinModifierListStub>().firstOrNull()
                        ?: return@mapNotNull null
                val annotations = modifiersList.childrenStubs.filterIsInstance<KotlinAnnotationEntryStub>()
                val widgetDefAnnotation = annotations
                    .firstOrNull { it.getShortName() == "WidgetDef" } ?: return@mapNotNull null

                val factoryClass = widgetDefAnnotation.childrenStubs
                    .firstOrNull { it.stubType == KtStubElementTypes.VALUE_ARGUMENT_LIST }
                    ?.childrenStubs.orEmpty()
                    .firstIsInstance<KotlinValueArgumentStub<KtValueArgument>>()
                    .psi.text.replace("::class", "")

                val typeParams =
                    classStub.childrenStubs.firstOrNull { it.stubType == KtStubElementTypes.TYPE_PARAMETER_LIST }
                val genericTypes = typeParams?.childrenStubs
                    ?.filterIsInstance<KotlinTypeParameterStub>()
                    ?.mapNotNull { it.name }
                    .orEmpty()

                val constructor =
                    classStub.childrenStubs.firstOrNull { it.stubType == KtStubElementTypes.PRIMARY_CONSTRUCTOR }
                        ?: return@mapNotNull null
                val constructorParameters =
                    constructor.childrenStubs.firstOrNull { it.stubType == KtStubElementTypes.VALUE_PARAMETER_LIST }
                        ?: return@mapNotNull null

                val params = constructorParameters
                    .childrenStubs
                    .filterIsInstance<KotlinParameterStub>()
                    .associate { parameterStub ->
                        val parameterName = parameterStub.name
                        val value = parameterStub.psi.defaultValue?.text

                        val type = parameterStub.childrenStubs
                            .mapNotNull { it as? KotlinPlaceHolderStub<KtTypeReference> }
                            .mapNotNull { typeReferenceStub ->
                                val parameterType = typeReferenceStub.psi.text
                                parameterType
                            }.first()

                        parameterName!! to GeneratorInput.ArgInfo(
                            type = type,
                            defaultValue = value
                        )
                    }

                GeneratorInput(
                    packageName = ktFileStub.getPackageFqName().asString(),
                    className = classStub.name!!,
                    imports = imports,
                    arguments = params,
                    factoryClass = factoryClass,
                    genericTypes = genericTypes
                )
            }
        }

        override fun getKtFileFromFile(file: File): KtFile {
            val virtualFile = localFileSystem.findFileByPath(file.path)!!
            return psiManager.findFile(virtualFile) as KtFile
        }

        override fun log(string: String) {
            messageCollector.report(CompilerMessageSeverity.INFO, string)
        }
    }

    override fun collectAdditionalSourcesAndUpdateConfiguration(
        knownSources: Collection<KtFile>,
        configuration: CompilerConfiguration,
        project: Project
    ): Collection<KtFile> {
        val generator = Generator(project)
        return generator.collectAdditionalSourcesAndUpdateConfiguration(knownSources, configuration[GenerationDir]!!)
    }
}
