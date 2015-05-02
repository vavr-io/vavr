/*
 * Computes the sum of iterable elements as double.
 */
var avg = function(iterable) {
    // division by zero is NaN
    return compute(iterable, 0.0, function(acc, value) { return acc + value }, function(acc, count) { return acc / count })
}

/*
 * Computes the product of iterable elements as double.
 */
var product = function(iterable) {
    return compute(iterable, 1.0, function(acc, value) { return acc * value }, function(acc, count) { return acc })
}

/*
 * Computes the sum of iterable elements as double.
 */
var sum = function(iterable) {
    return compute(iterable, 0.0, function(acc, value) { return acc + value }, function(acc, count) { return acc })
}

/*
 * Represents a computation given iterable elements.
 *
 * @param iterable A java.lang.Iterable of arbitrary objects.
 * @param zero The neutral element regarding the accumulate operation.
 * @param accumulate(acc, value) Accumulates the current accumulator with the given value.
 * @param result(acc, count) Computes the result given the accumulator and the element count.
 * @return a double value or NaN if an element has no numerical representation using parseFloat()
 */
var compute = function(iterable, zero, accumulate, result) {
    var acc = zero
    var count = 0
    for each (elem in iterable) {
        var float = parseFloat(elem)
        if (isNaN(float)) {
            return float
        }
        acc = accumulate(acc, float)
        count += 1
    }
    return result(acc, count)
}
