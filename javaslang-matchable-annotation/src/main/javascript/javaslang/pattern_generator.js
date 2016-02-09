/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */

var letters = ["a", "bc", "def"]

function test(elem) {
    return ">" + elem + "<"
}

var generate = function(package, _class) { return <<CODE
${gen(package)("package ${package};")}

public final class ${_class} {

    // hidden
    private ${_class}() {
    }

    ----

    ${forAll(letters)(test)()}

    ----



    public static <...> Pattern...<...> List(...) {
        ...
    }

}
CODE
}

//var genLetter = function(letter) { return <<CODE
//test>${letter}<test
//CODE
//}
