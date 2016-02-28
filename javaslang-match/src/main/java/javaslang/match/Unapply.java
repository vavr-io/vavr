/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static javax.lang.model.element.Modifier.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Unapply {

    /**
     * Checks if an {@link javax.lang.model.element.ExecutableElement} is a valid {@code @Unapply} method.
     */
    class Checker extends BaseChecker<Checker, ExecutableElement> {

        static boolean isValid(ExecutableElement executableElement, Messager messager) {
            final Checker methodChecker = new Checker(executableElement, messager);
            return methodChecker.ensure(Checker::isMethod, () -> "Annotation " + Unapply.class.getName() + " only allowed for methods.") &&
                    methodChecker.ensure(Checker::doesNotThrow, () -> "@" + "Unapply method should not throw (checked) exceptions.") &&
                    methodChecker.ensure(e -> !e.elem.isDefault(), () -> "@" + "Unapply method needs to be declared in a class, not an interface.") &&
                    methodChecker.ensure(e -> !e.elem.isVarArgs(), () -> "@" + "Unapply method has varargs.") &&
                    methodChecker.ensure(e -> e.elem.getParameters().size() == 1, () -> "Unapply method must have exactly one parameter of the object to be deconstructed.") &&
                    methodChecker.ensure(e -> e.elem.getReturnType().toString().startsWith("javaslang.Tuple"), () -> "Return type of unapply method must be a Tuple.") &&
                    methodChecker.ensure(e -> e.hasAll(STATIC), () -> "Unapply method needs to be static.") &&
                    methodChecker.ensure(e -> e.hasNone(PRIVATE, PROTECTED, ABSTRACT), () -> "Unapply method may not be private or protected.");
        }

        private Checker(ExecutableElement elem, Messager messager) {
            super(elem, messager);
        }

        boolean isMethod() {
            final String name = elem.getSimpleName().toString();
            return !name.isEmpty() && !"<init>".equals(name) && !"<clinit>".equals(name);
        }

        boolean doesNotThrow() {
            return elem.getThrownTypes().isEmpty();
        }
    }
}
