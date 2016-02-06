/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
// ${package ? "package ${package};" : ""}

var generate = function(package, _class) { return <<STR
${gen(package)("package ${package};")}

public final class ${_class} {

    // hidden
    private ${_class}() {
    }

    public static <...> Pattern...<...> List(...) {
        ...
    }

}
STR
}

// code generator

var gen = function(condition) {
    return function(code) {
        return condition ? code : ""
    }
}
