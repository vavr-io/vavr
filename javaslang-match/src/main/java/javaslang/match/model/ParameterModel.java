/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match.model;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;

/**
 * Representation of a method parameter.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
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
