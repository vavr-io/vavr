/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;

import static javax.lang.model.element.Modifier.*;

/**
 * Checks if an {@link javax.lang.model.element.ExecutableElement} is a valid {@code @Unapply} method.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
class UnapplyChecker extends BaseChecker<UnapplyChecker, ExecutableElement> {

    static boolean isValid(ExecutableElement executableElement, Messager messager) {
        final UnapplyChecker methodChecker = new UnapplyChecker(executableElement, messager);
        return methodChecker.ensure(UnapplyChecker::doesNotThrow, () -> "@" + "Unapply method should not throw (checked) exceptions.") &&
                methodChecker.ensure(e -> !e.elem.isDefault(), () -> "@" + "Unapply method needs to be declared in a class, not an interface.") &&
                methodChecker.ensure(e -> !e.elem.isVarArgs(), () -> "@" + "Unapply method has varargs.") &&
                methodChecker.ensure(e -> e.elem.getParameters().size() == 1, () -> "Unapply method must have exactly one parameter of the object to be deconstructed.") &&
                methodChecker.ensure(e -> e.elem.getParameters().get(0).asType().getKind() == TypeKind.DECLARED, () -> "Unapply method parameter must be a declared type.") &&
                methodChecker.ensure(e -> e.elem.getReturnType().toString().startsWith("javaslang.Tuple"), () -> "Return type of unapply method must be a Tuple.") &&
                methodChecker.ensure(e -> !e.elem.getReturnType().toString().endsWith("Tuple"), () -> "Return type is no Tuple implementation.") &&
                methodChecker.ensure(e -> e.hasAll(STATIC), () -> "Unapply method needs to be static.") &&
                methodChecker.ensure(e -> e.hasNone(PRIVATE, PROTECTED, ABSTRACT), () -> "Unapply method may not be private or protected.");
    }

    private UnapplyChecker(ExecutableElement elem, Messager messager) {
        super(elem, messager);
    }

    boolean doesNotThrow() {
        return elem.getThrownTypes().isEmpty();
    }
}
