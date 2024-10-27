/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
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

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import static javax.lang.model.type.TypeKind.DECLARED;
import static javax.lang.model.type.TypeKind.TYPEVAR;

/**
 * Representation of a generic type parameter.
 *
 * @author Daniel Dietrich
 */
public class TypeParameterModel {

    private final Elements elementUtils;
    private final TypeMirror typeMirror;

    public TypeParameterModel(Elements elementUtils, TypeMirror typeMirror) {
        this.elementUtils = elementUtils;
        this.typeMirror = typeMirror;
    }

    public ClassModel asType() {
        return new ClassModel(elementUtils, (DeclaredType) typeMirror);
    }

    public String asTypeVar() {
        return typeMirror.toString();
    }

    public boolean isType() {
        return isTypeKind(DECLARED);
    }

    public boolean isTypeVar() {
        return isTypeKind(TYPEVAR);
    }

    private boolean isTypeKind(TypeKind typeKind) {
        return typeMirror.getKind() == typeKind;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof TypeParameterModel && toString().equals(o.toString()));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return typeMirror.toString();
    }
}
