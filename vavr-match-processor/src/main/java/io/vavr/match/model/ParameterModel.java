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

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;

/**
 * Representation of a method parameter.
 *
 * @author Daniel Dietrich
 */
public class ParameterModel {

    private final Elements elementUtils;
    private final VariableElement variableElement;

    public ParameterModel(Elements elementUtils, VariableElement variableElement) {
        this.elementUtils = elementUtils;
        this.variableElement = variableElement;
    }

    public ClassModel getType() {
        return new ClassModel(elementUtils, (DeclaredType) variableElement.asType());
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof ParameterModel && toString().equals(o.toString()));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return variableElement.toString();
    }
}
