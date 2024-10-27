/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * Interface for checkable properties, allowing composition via {@linkplain #and(Checkable)} and {@linkplain #or(Checkable)}.
 *
 * @author Daniel Dietrich
 */
@FunctionalInterface
public interface Checkable {

    /**
     * A thread-safe, equally distributed random number generator.
     */
    Supplier<Random> RNG = ThreadLocalRandom::current;

    /**
     * Default size hint for generators: 100
     */
    int DEFAULT_SIZE = 100;

    /**
     * Default tries to check a property: 1000
     */
    int DEFAULT_TRIES = 1000;

    /**
     * Checks this property.
     *
     * @param randomNumberGenerator An implementation of {@link java.util.Random}.
     * @param size                  A (not necessarily positive) size hint.
     * @param tries                 A non-negative number of tries to falsify the given property.
     * @return A {@linkplain CheckResult}
     */
    CheckResult check(Random randomNumberGenerator, int size, int tries);

    /**
     * Checks this property using the default random number generator {@link #RNG}.
     *
     * @param size  A (not necessarily positive) size hint.
     * @param tries A non-negative number of tries to falsify the given property.
     * @return A {@linkplain CheckResult}
     */
    default CheckResult check(int size, int tries) {
        if (tries < 0) {
            throw new IllegalArgumentException("tries < 0");
        }
        return check(RNG.get(), size, tries);
    }

    /**
     * Checks this property using the default random number generator {@link #RNG} by calling {@link #check(int, int)},
     * where size is {@link #DEFAULT_SIZE} and tries is {@link #DEFAULT_TRIES}.
     *
     * @return A {@linkplain CheckResult}
     */
    default CheckResult check() {
        return check(RNG.get(), DEFAULT_SIZE, DEFAULT_TRIES);
    }

    /**
     * Returns a new Checkable which is satisfied if this Checkable <em>and</em> the given checkable are satisfied.
     * <p>
     * First this Checkable is checked.
     *
     * @param checkable A Checkable
     * @return A new Checkable
     */
    default Checkable and(Checkable checkable) {
        return (rng, size, tries) -> {
            final CheckResult result = check(rng, size, tries);
            if (result.isSatisfied()) {
                return checkable.check(rng, size, tries);
            } else {
                return result;
            }
        };
    }

    /**
     * Returns a new Checkable which is satisfied if this Checkable <em>or</em> the given checkable are satisfied.
     * <p>
     * First this Checkable is checked.
     *
     * @param checkable A Checkable
     * @return A new Checkable
     */
    default Checkable or(Checkable checkable) {
        return (rng, size, tries) -> {
            final CheckResult result = check(rng, size, tries);
            if (result.isSatisfied()) {
                return result;
            } else {
                return checkable.check(rng, size, tries);
            }
        };
    }
}
