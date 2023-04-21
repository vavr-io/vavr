/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
// @formatter:off
// CHECKSTYLE:OFF
package io.vavr;

import io.vavr.API.Match.Pattern;
import io.vavr.API.Match.Pattern3;
import io.vavr.control.Option;

/**
 * @deprecated Will be removed in the next major version, along with VAVR's pattern matching, in favor of Java's native pattern matching.
 */
@Deprecated
public final class MatchTest_DeveloperPatterns {

    private MatchTest_DeveloperPatterns() {
    }

    public static <_1 extends String, _2 extends Boolean, _3 extends Option<Number>> Pattern3<MatchTest.Developer, _1, _2, _3> $Developer(Pattern<_1, ?> p1, Pattern<_2, ?> p2, Pattern<_3, ?> p3) {
        return Pattern3.of(MatchTest.Developer.class, p1, p2, p3, io.vavr.MatchTest.Developer.$::Developer);
    }

}
// CHECKSTYLE:ON
// @formatter:on
