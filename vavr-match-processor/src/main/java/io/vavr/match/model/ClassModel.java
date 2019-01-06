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
package io.vavr.match.model;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Representation of a class.
 *
 * @author Daniel Dietrich
 */
public class ClassModel {

    private final Elements elementUtils;
    private final DeclaredType declaredType;

    public static ClassModel of(Elements elementUtils, TypeElement typeElement) {
        return new ClassModel(elementUtils, (DeclaredType) typeElement.asType());
    }

    public ClassModel(Elements elementUtils, DeclaredType declaredType) {
        this.elementUtils = elementUtils;
        this.declaredType = declaredType;
    }

    public TypeElement typeElement() {
        return (TypeElement) declaredType.asElement();
    }

    // returns the simple name for top level class and the combined class name for inner classes
    public String getClassName() {
        final String fqn = getFullQualifiedName();
        return hasDefaultPackage() ? fqn : fqn.substring(getPackageName().length() + 1);
    }

    public String getFullQualifiedName() {
        return typeElement().getQualifiedName().toString();
    }

    public List<MethodModel> getMethods() {
        return typeElement().getEnclosedElements().stream()
                .filter(element -> {
                    final String name = element.getSimpleName().toString();
                    return element instanceof ExecutableElement && !name.isEmpty() && !"<init>".equals(name) && !"<clinit>".equals(name);
                })
                .map(element -> new MethodModel(elementUtils, (ExecutableElement) element))
                .collect(toList());
    }

    public String getPackageName() {
        return elementUtils.getPackageOf(typeElement()).getQualifiedName().toString();
    }

    public List<TypeParameterModel> getTypeParameters() {
        return declaredType.getTypeArguments().stream()
                .map(typeMirror -> new TypeParameterModel(elementUtils, typeMirror))
                .collect(toList());
    }

    public boolean hasDefaultPackage() {
        return elementUtils.getPackageOf(typeElement()).isUnnamed();
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof ClassModel && toString().equals(o.toString()));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return declaredType.toString();
    }
}
