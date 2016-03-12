/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;

/**
 * Checks if an {@link javax.lang.model.element.TypeElement} is a valid {@code @Patterns} type.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
class PatternsChecker extends BaseChecker<PatternsChecker, TypeElement> {

    static boolean isValid(TypeElement typeElement, Messager messager) {
        final PatternsChecker typeChecker = new PatternsChecker(typeElement, messager);
        return typeChecker.ensure(e -> !e.isNested(), () -> "Patterns need to be defined in top-level classes.");
    }

    private PatternsChecker(TypeElement elem, Messager messager) {
        super(elem, messager);
    }

    boolean isNested() {
        return elem.getNestingKind().isNested();
    }

}
