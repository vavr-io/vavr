/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class AssertionsExtensions {

    private AssertionsExtensions() {
    }

    public static ClassAssert assertThat(Class<?> clazz) {
        return new ClassAssert(clazz);
    }

    public static class ClassAssert {

        final Class<?> clazz;

        ClassAssert(Class<?> clazz) {
            this.clazz = clazz;
        }

        public void isNotInstantiable() {
            try {
                final Constructor<?> cons = clazz.getDeclaredConstructor();
                cons.setAccessible(true);
                cons.newInstance();
            } catch (InvocationTargetException x) {
                final String exception = ((x.getCause() == null) ? x : x.getCause()).getClass().getSimpleName();
                final String actualMessage = (x.getCause() == null) ? x.getMessage() : x.getCause().getMessage();
                final String expectedMessage = clazz.getName() + " is not intended to be instantiated.";
                if (!expectedMessage.equals(actualMessage)) {
                    throw new AssertionError(String.format("Expected AssertionError(\"%s\") but was %s(\"%s\")",
                            expectedMessage, exception, actualMessage));
                }
            } catch (Exception x) {
                throw new RuntimeException("Error instantiating " + clazz.getName(), x);
            }
        }
    }
}
