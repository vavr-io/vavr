/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import java.util.NoSuchElementException;
import org.jspecify.annotations.Nullable;

/**
 * A {@link API.Match} throws a MatchError if no case matches the applied object.
 *
 * @author Daniel Dietrich
 */
public class MatchError extends NoSuchElementException {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("serial") // Conditionally serializable
    private final @Nullable Object obj;

    /**
     * Internally called by {@link API.Match}.
     *
     * @param obj The object which could not be matched, may be {@code null} if the matched value itself was {@code null}.
     */
    MatchError(@Nullable Object obj) {
        super((obj == null) ? "null" : "type: " + obj.getClass().getName() + ", value: " + obj);
        this.obj = obj;
    }

    /**
     * Returns the object which could not be matched.
     *
     * @return the object which could not be matched, or {@code null} if the matched value itself was {@code null}.
     */
    public @Nullable Object getObject() {
        return obj;
    }
}
