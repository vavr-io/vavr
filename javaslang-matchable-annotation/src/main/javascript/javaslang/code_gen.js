/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */

/*
 * Expands a given Array using a function `f` and optional
 * `delimiter`, `prefix` and `suffix`.
 *
 * Examples:
 *
 * // 1, 2, 3
 * range(1, 3).gen(function(j) j)(", ")
 *
 * // (1)(0)(-1)
 * range(1, -1).gen(function(j) "(${j})")()
 *
 * // {1, 0, -1}
 * // {2, 1, 0, -1, -2}
 * // {3, 2, 1, 0, -1, -2, -3}
 * range(1, 3).gen(function(i)
 *     range(i, -i).gen(function(j) j)(", ", "{", "}")
 * )("\n")
 *
 */
Array.prototype.gen = function(f) {
    var array = this
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

/*
 * Results in `code`, if `condition` is true, otherwise returns the empty string.
 *
 * Examples:
 *
 * var s = "Hi"
 *
 * // = even length
 * (s.length % 2 == 0).gen("even length")
 *
 * // = Hi!
 * s.isDefined().gen(s + "!)
 *
 * // (empty string)
 * s.isEmpty().gen(s + "!")
 *
 */
Boolean.prototype.gen = function(code) { return this.valueOf() ? code : "" }
String.prototype.isDefined = function() { return this.length > 0 }
String.prototype.isEmpty = function() { return this.length == 0 }

/*
 * Range (inclusive), returns an array containing [from, ..., to]
 *
 * Examples:
 *
 * // = [1, 2, 3]
 * range(1, 3)
 *
 * // = [1, 0, -1]
 * range(1, -1)
 *
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
