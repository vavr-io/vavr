/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match.model;

import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;

import static javax.lang.model.type.TypeKind.DECLARED;
import static javax.lang.model.type.TypeKind.TYPEVAR;

/**
 * Representation of a generic type parameter.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public class TypeParameterModel {

    private final Elements elementUtils;
    private final TypeParameterElement typeParameterElement;

    public TypeParameterModel(Elements elementUtils, TypeParameterElement typeParameterElement) {
        this.elementUtils = elementUtils;
        this.typeParameterElement = typeParameterElement;
    }

    public ClassModel asType() {
        return ClassModel.of(elementUtils, typeParameterElement.asType());
    }

    public boolean isType() {
        return isTypeKind(DECLARED);
    }

    public boolean isTypeVar() {
        return isTypeKind(TYPEVAR);
    }

    private boolean isTypeKind(TypeKind typeKind) {
        return typeParameterElement.asType().getKind() == typeKind;
    }

    @Override
    public String toString() {
        return typeParameterElement.asType().toString();
    }
}
