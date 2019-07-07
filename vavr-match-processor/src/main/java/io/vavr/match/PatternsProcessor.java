/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.match;

import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;
import io.vavr.match.generator.Generator;
import io.vavr.match.model.ClassModel;
import io.vavr.match.model.MethodModel;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * A code generator for Vavr <em>structural pattern matching</em> patterns.
 * <p>
 * <strong>Note:</strong>
 * <p>
 * If javac complains {@code [WARNING] No processor claimed any of these annotations: ...}
 * we need to provide the compiler arg {@code -Xlint:-processing}.
 * <p>
 * See <a href="https://bugs.openjdk.java.net/browse/JDK-6999068">JDK-6999068 bug</a>.
 */
// See Difference between Element, Type and Mirror: http://stackoverflow.com/a/2127320/1110815
public class PatternsProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // we do not use @SupportedAnnotationTypes in order to be type-safe
        return Collections.singleton(Patterns.class.getName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // intended to be used with Java 8+
        return SourceVersion.latestSupported();
    }

    /**
     * Gathers annotated elements, transforms elements to a generator model and generates the model to code.
     *
     * @param annotations the annotation types requested to be processed
     * @param roundEnv    environment for information about the current and prior round
     * @return whether or not the set of annotation types are claimed by this processor
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!annotations.isEmpty()) {
            final Set<TypeElement> typeElements = roundEnv.getElementsAnnotatedWith(Patterns.class).stream()
                    .filter(element -> element instanceof TypeElement)
                    .map(element -> (TypeElement) element)
                    .collect(Collectors.toSet());
            if (!typeElements.isEmpty()) {
                final Set<ClassModel> classModels = transform(typeElements);
                if (!classModels.isEmpty()) {
                    generate(classModels);
                }
            }
        }
        return true;
    }

    // Verify correct usage of annotations @Patterns and @Unapply
    private Set<ClassModel> transform(Set<TypeElement> typeElements) {
        final Set<ClassModel> classModels = new HashSet<>();
        final javax.lang.model.util.Elements elementUtils = processingEnv.getElementUtils();
        final Messager messager = processingEnv.getMessager();
        for (TypeElement typeElement : typeElements) {
            final ClassModel classModel = ClassModel.of(elementUtils, typeElement);
            final List<MethodModel> methodModels = classModel.getMethods().stream()
                    .filter(method -> method.isAnnotatedWith(Unapply.class))
                    .collect(toList());
            if (methodModels.isEmpty()) {
                messager.printMessage(Diagnostic.Kind.WARNING, "No @Unapply methods found.", classModel.typeElement());
            } else {
                final boolean methodsValid = methodModels.stream().reduce(true, (bool, method) -> bool && UnapplyChecker.isValid(method.getExecutableElement(), messager), (b1, b2) -> b1 && b2);
                if (methodsValid) {
                    classModels.add(classModel);
                }
            }
        }
        return classModels;
    }

    // Expands all @Patterns classes
    private void generate(Set<ClassModel> classModels) {
        final Filer filer = processingEnv.getFiler();
        for (ClassModel classModel : classModels) {
            final String derivedClassName = deriveClassName(classModel);
            final String code = Generator.generate(derivedClassName, classModel);
            final String fqn = (classModel.hasDefaultPackage() ? "" : classModel.getPackageName() + ".") + derivedClassName;
            try (final Writer writer = filer.createSourceFile(fqn, classModel.typeElement()).openWriter()) {
                writer.write(code);
            } catch (IOException x) {
                throw new Error("Error writing " + fqn, x);
            }
        }
    }

    private String deriveClassName(ClassModel classModel) {
        final String name = classModel.getClassName();
        return ("$".equals(name) ? "" : name.endsWith(".$") ? name.substring(0, name.length() - 2) : name)
                .replaceAll("\\.", "_")
                + Patterns.class.getSimpleName();
    }
}
