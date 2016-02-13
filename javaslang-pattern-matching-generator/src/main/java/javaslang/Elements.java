/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;

class Elements {

    // gets fqn without generics
    static String getRawParameterType(ExecutableElement elem, int index) {
        final String type = elem.getParameters().get(index).asType().toString();
        final int i = type.indexOf('<');
        return (i == -1) ? type : type.substring(0, i);
    }

}
