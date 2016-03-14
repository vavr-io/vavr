/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;

import static javax.lang.model.element.Modifier.*;

/**
 * Checks if an {@link javax.lang.model.element.ExecutableElement} is a valid {@code @Unapply} method.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
class UnapplyChecker {

    static boolean isValid(ExecutableElement elem, Messager messager) {
        return ensure(elem, doesNotThrow(elem), messager, () -> "@" + "Unapply method should not throw (checked) exceptions.") &&
                ensure(elem, !elem.isDefault(), messager, () -> "@" + "Unapply method needs to be declared in a class, not an interface.") &&
                ensure(elem, !elem.isVarArgs(), messager, () -> "@" + "Unapply method has varargs.") &&
                ensure(elem, elem.getParameters().size() == 1, messager, () -> "Unapply method must have exactly one parameter of the object to be deconstructed.") &&
                ensure(elem, elem.getParameters().get(0).asType().getKind() == TypeKind.DECLARED, messager, () -> "Unapply method parameter must be a declared type.") &&
                ensure(elem, elem.getReturnType().toString().startsWith("javaslang.Tuple"), messager, () -> "Return type of unapply method must be a Tuple.") &&
                ensure(elem, !elem.getReturnType().toString().endsWith("Tuple"), messager, () -> "Return type is no Tuple implementation.") &&
                ensure(elem, hasAll(elem, STATIC), messager, () -> "Unapply method needs to be static.") &&
                ensure(elem, hasNone(elem, PRIVATE, PROTECTED, ABSTRACT), messager, () -> "Unapply method may not be private or protected.");
    }

    private static boolean ensure(ExecutableElement elem, boolean condition, Messager messager, Supplier<String> msg) {
        if (!condition) {
            messager.printMessage(Diagnostic.Kind.ERROR, msg.get(), elem);
        }
        return condition;
    }

    private static boolean hasAll(ExecutableElement elem, Modifier... modifiers) {
        return elem.getModifiers().containsAll(Arrays.asList(modifiers));
    }

    private static boolean hasNone(ExecutableElement elem, Modifier... modifiers) {
        final Set<Modifier> set = elem.getModifiers();
        for (Modifier modifier : modifiers) {
            if (set.contains(modifier)) {
                return false;
            }
        }
        return true;
    }

    private static boolean doesNotThrow(ExecutableElement elem) {
        return elem.getThrownTypes().isEmpty();
    }
}
