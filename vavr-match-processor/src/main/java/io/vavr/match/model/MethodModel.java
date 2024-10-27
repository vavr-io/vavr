/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, https://vavr.io
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import java.lang.annotation.Annotation;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Representation of a method.
 *
 * @author Daniel Dietrich
 */
public class MethodModel {

    private final Elements elementUtils;
    private final ExecutableElement executableElement;

    public MethodModel(Elements elementUtils, ExecutableElement executableElement) {
        this.elementUtils = elementUtils;
        this.executableElement = executableElement;
    }

    public ExecutableElement getExecutableElement() {
        return executableElement;
    }

    public String getName() {
        return executableElement.getSimpleName().toString();
    }

    public ParameterModel getParameter(int index) {
        final List<? extends VariableElement> parameters = executableElement.getParameters();
        if (index < 0 || index > parameters.size()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return new ParameterModel(elementUtils, parameters.get(index));
    }

    public ClassModel getReturnType() {
        return new ClassModel(elementUtils, (DeclaredType) executableElement.getReturnType());
    }

    public List<TypeParameterModel> getTypeParameters() {
        return executableElement.getTypeParameters().stream()
                .map(typeParam -> new TypeParameterModel(elementUtils, typeParam.asType()))
                .collect(toList());
    }

    public <A extends Annotation> boolean isAnnotatedWith(Class<A> annotationType) {
        return executableElement.getAnnotationsByType(annotationType).length > 0;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof MethodModel && toString().equals(o.toString()));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return executableElement.toString();
    }
}
