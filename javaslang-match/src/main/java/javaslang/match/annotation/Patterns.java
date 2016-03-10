/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match.annotation;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Structural pattern matching annotation for pattern declarations.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Patterns {

    /**
     * Checks if an {@link javax.lang.model.element.TypeElement} is a valid {@code @Patterns} type.
     */
    class Checker extends BaseChecker<Checker, TypeElement> {

        public static boolean isValid(TypeElement typeElement, Messager messager) {
            final Checker typeChecker = new Checker(typeElement, messager);
            return typeChecker.ensure(e -> !e.isNested(), () -> "Patterns need to be defined in top-level classes.");
        }

        private Checker(TypeElement elem, Messager messager) {
            super(elem, messager);
        }

        boolean isNested() {
            return elem.getNestingKind().isNested();
        }
    }
}
