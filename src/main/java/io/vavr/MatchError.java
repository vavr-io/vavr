/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
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
package io.vavr;

import java.util.NoSuchElementException;

/**
 * A {@link API.Match} throws a MatchError if no case matches the applied object.
 * @deprecated Will be removed in the next major version, along with VAVR's pattern matching, in favor of Java's native pattern matching.
 */
@Deprecated
public class MatchError extends NoSuchElementException {

    private static final long serialVersionUID = 1L;

    private final Object obj;

    /**
     * Internally called by {@link API.Match}.
     *
     * @param obj The object which could not be matched.
     */
    MatchError(Object obj) {
        super((obj == null) ? "null" : "type: " + obj.getClass().getName() + ", value: " + obj);
        this.obj = obj;
    }

    /**
     * Returns the object which could not be matched.
     *
     * @return An Object.
     */
    public Object getObject() {
        return obj;
    }

}
