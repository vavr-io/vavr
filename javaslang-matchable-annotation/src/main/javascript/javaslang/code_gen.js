/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */

/*
 * Expands the given `array` using a function `f` and optional
 * `delimiter`, `prefix` and `suffix`.
 *
 * Examples:
 *
 * // 1, 2, 3
 * forAll(range(1, 3))(function(j) j)(", ")
 *
 * // >1<>0<>-1<
 * forAll(range(1, -1))(function(j) ">${j}<")()
 *
 * // {1, 0, -1}
 * // {2, 1, 0, -1, -2}
 * // {3, 2, 1, 0, -1, -2, -3}
 * forAll(range(1, 3))(function(i)
 *     forAll(range(i, -i))(function(j) j)(", ", "{", "}")
 * )("\n")
 *
 */
var forAll = function(array) {
    return function(f) {
        return function(delimiter, prefix, suffix) {
            var result = (prefix === undefined ? "" : prefix)
            var last = array.length - 1;
            for (var index = 0; index <= last; index++) {
                var elem = array[index]
                result = result + f(elem) + (index == last || delimiter === undefined ? "" : delimiter)
            }
            return result + (suffix === undefined ? "" : suffix)
        }
    }
}

/*
 * Results in `code`, if `condition` is true,
 * otherwise returns the empty string.
 */
var gen = function(condition) {
    return function(code) {
        return condition ? code : ""
    }
}

/*
 * Range (inclusive),
 * returns an array containing [from, ..., to]
 */
var range = function(from, to) {
    array = []
    if (from <= to) {
        index = to-from+1
        while (index--) array[index] = to--
    } else {
        index = from-to+1
        while (index--) array[index] = to++
    }
    return array
}
