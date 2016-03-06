/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

final class Elements {

    private Elements() {
    }

    // `Type` -> <empty>, `some.pkg.Type` -> some.pkg
    static String getPackage(TypeElement elem) {
        final String simpleName = elem.getSimpleName().toString();
        final String qualifiedName = elem.getQualifiedName().toString();
        return qualifiedName.isEmpty() ? "" : qualifiedName.substring(0, qualifiedName.length() - simpleName.length() - 1);
    }

    // `$` -> <empty>, `Type` -> `Type`, `OuterType.InnerType` -> `OuterType.InnerType`
    static String getSimpleName(TypeElement elem) {
        final String simpleName = elem.getSimpleName().toString();
        return ("$".equals(simpleName) ? "" : simpleName) + Patterns.class.getSimpleName();
    }

    static String getFullQualifiedName(TypeElement elem) {
        final String pkg = getPackage(elem);
        final String name = getSimpleName(elem);
        return pkg.isEmpty() ? name : pkg + "." + name;
    }

    // `R m(fqn.Type1<T1, ..., T2> arg1, ...)` -> fqn.Type1<T1, ..., T2> (for index = 0)
    static String getParameterType(ExecutableElement elem, int index) {
        return elem.getParameters().get(index).asType().toString();
    }

    // `R m(fqn.Type1<T1, ..., T2> arg1, ...)` -> fqn.Type1 (for index = 0)
    static String getRawParameterType(ExecutableElement elem, int index) {
        final String type = getParameterType(elem, index);
        final int i = type.indexOf('<');
        return (i == -1) ? type : type.substring(0, i);
    }

    // `Tuple2<String, T1> m(params)` -> [String, T1]
    static String[] getReturnTypeArgs(ExecutableElement elem) {
        TypeMirror returnType = elem.getReturnType();
        switch (returnType.getKind()) {
            case DECLARED: {
                return ((DeclaredType) returnType).getTypeArguments().stream().map(TypeMirror::toString).toArray(String[]::new);
            }
            default: {
                throw new Error("Unhandled return type: " + returnType.getKind());
            }
        }
    }

    // `<T1, ..., Tn> m(params)` -> [T1, ..., Tn]
    static String[] getTypeParameters(ExecutableElement elem) {
        return elem.getTypeParameters().stream().map(typeArg -> typeArg.asType().toString()).toArray(String[]::new);
    }

    static int typeParameterCount(ExecutableElement elem) {
        return elem.getTypeParameters().size();
    }

    static boolean hasTypeParameters(ExecutableElement elem) {
        return !elem.getTypeParameters().isEmpty();
    }
}
