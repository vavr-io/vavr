/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match.generator;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A base class for checking annotated {@link javax.lang.model.element.Element}s during annotation processing.
 *
 * @param <SELF> The class of this {@code BaseChecker} implementation.
 * @param <E> The element type to be checked.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
abstract class BaseChecker<SELF extends BaseChecker<SELF, E>, E extends Element> {

    final E elem;
    final Messager messager;

    BaseChecker(E elem, Messager messager) {
        this.elem = elem;
        this.messager = messager;
    }

    boolean ensure(Predicate<? super SELF> condition, Supplier<String> msg) {
        @SuppressWarnings("unchecked")
        final boolean result = condition.test((SELF) this);
        if (!result) {
            messager.printMessage(Diagnostic.Kind.ERROR, msg.get(), elem);
        }
        return result;
    }

    boolean hasAll(Modifier... modifiers) {
        return elem.getModifiers().containsAll(Arrays.asList(modifiers));
    }

    boolean hasNone(Modifier... modifiers) {
        final Set<Modifier> set = elem.getModifiers();
        for (Modifier modifier : modifiers) {
            if (set.contains(modifier)) {
                return false;
            }
        }
        return true;
    }
}
