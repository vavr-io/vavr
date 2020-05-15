/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2020 Vavr, http://vavr.io
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
 */
class UnapplyChecker {

    static boolean isValid(ExecutableElement elem, Messager messager) {
        return ensure(elem, doesNotThrow(elem), messager, () -> "@" + "Unapply method should not throw (checked) exceptions.") &&
                ensure(elem, !elem.isDefault(), messager, () -> "@" + "Unapply method needs to be declared in a class, not an interface.") &&
                ensure(elem, !elem.isVarArgs(), messager, () -> "@" + "Unapply method has varargs.") &&
                ensure(elem, elem.getParameters().size() == 1, messager, () -> "Unapply method must have exactly one parameter of the object to be deconstructed.") &&
                ensure(elem, elem.getParameters().get(0).asType().getKind() == TypeKind.DECLARED, messager, () -> "Unapply method parameter must be a declared type.") &&
                ensure(elem, elem.getReturnType().toString().startsWith("io.vavr.Tuple"), messager, () -> "Return type of unapply method must be a Tuple.") &&
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
