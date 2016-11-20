/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.CheckedFunction1;
import javaslang.collection.Stream;
import javaslang.control.Option;
import javaslang.control.Try;
import javaslang.control.Try.NonFatalException;
import javaslang.test.Property.Condition;

import java.util.Objects;
import java.util.Random;

abstract class PropertyCheck<T> implements Checkable<T> {

    final String name;
    final Arbitrary<T> arbitrary;
    final CheckedFunction1<T, Condition> predicate;
    final Shrink<T> shrink;

    PropertyCheck(String name, Arbitrary<T> arbitrary, CheckedFunction1<T, Condition> predicate, Shrink<T> shrink) {
        this.name = name;
        this.arbitrary = arbitrary;
        this.predicate = predicate;
        this.shrink = shrink;
    }

    private static void logSatisfied(String name, int tries, long millis, boolean exhausted) {
        if (exhausted) {
            log(String.format("%s: Exhausted after %s tests in %s ms.", name, tries, millis));
        } else {
            log(String.format("%s: OK, passed %s tests in %s ms.", name, tries, millis));
        }
    }

    private static void logFalsified(String name, int currentTry, long millis) {
        log(String.format("%s: Falsified after %s passed tests in %s ms.", name, currentTry - 1, millis));
    }

    private static void logErroneous(String name, int currentTry, long millis, String errorMessage) {
        log(String.format("%s: Errored after %s passed tests in %s ms with message: %s", name, Math.max(0, currentTry - 1), millis, errorMessage));
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    /**
     * Creates an Error caused by an exception when obtaining a generator.
     *
     * @param size  The size hint passed to the {@linkplain Arbitrary} which caused the error.
     * @param cause The error which occurred when the {@linkplain Arbitrary} tried to obtain the generator {@linkplain Gen}.
     * @return a new Error instance.
     */
    private static Error arbitraryError(int size, Throwable cause) {
        return new Error(String.format("Arbitrary of size %s: %s", size, cause.getMessage()), cause);
    }

    /**
     * Creates an Error caused by an exception when generating a value.
     *
     * @param size  The size hint of the arbitrary which called the generator {@linkplain Gen} which caused the error.
     * @param cause The error which occurred when the {@linkplain Gen} tried to generate a random value.
     * @return a new Error instance.
     */
    private static Error genError(int size, Throwable cause) {
        return new Error(String.format("Gen of size %s: %s", size, cause.getMessage()), cause);
    }

    /**
     * Creates an Error caused by an exception when testing a Predicate.
     *
     * @param cause The error which occurred when applying the {@linkplain java.util.function.Predicate}.
     * @return a new Error instance.
     */
    private static Error predicateError(Throwable cause) {
        return new Error("Applying predicate: " + cause.getMessage(), cause);
    }

    @Override
    public CheckResult<T> check(Random random, int size, int tries) {
        final CheckResult<T> result = tryCheck(random, size, tries);
        return result.isSatisfied() ? result : shrink(result);
    }

    /**
     * Performs initial checks of this property.
     *
     * @param random An implementation of {@link java.util.Random}.
     * @param size   A (not necessarily positive) size hint.
     * @param tries  A non-negative number of tries to falsify the given property.
     * @return A {@linkplain CheckResult}
     */
    private CheckResult<T> tryCheck(Random random, int size, int tries) {
        Objects.requireNonNull(random, "random is null");
        if (tries < 0) {
            throw new IllegalArgumentException("tries < 0");
        }
        final long startTime = System.currentTimeMillis();
        try {
            final Gen<T> gen = Try.of(() -> arbitrary.apply(size)).recover(x -> { throw arbitraryError(size, x); }).get();
            boolean exhausted = true;
            for (int i = 1; i <= tries; i++) {
                try {
                    final T val = Try.of(() -> gen.apply(random)).recover(x -> { throw genError(size, x); }).get();
                    try {
                        final Condition condition = Try.of(() -> predicate.apply(val)).recover(x -> { throw predicateError(x); }).get();
                        if (condition.precondition) {
                            exhausted = false;
                            if (!condition.postcondition) {
                                logFalsified(name, i, System.currentTimeMillis() - startTime);
                                return new CheckResult.Falsified<>(name, i, val, 0);
                            }
                        }
                    } catch (NonFatalException nonFatal) {
                        logErroneous(name, i, System.currentTimeMillis() - startTime, nonFatal.getCause().getMessage());
                        return new CheckResult.Erroneous<>(name, i, (Error) nonFatal.getCause(), Option.some(val), 0);
                    }
                } catch (NonFatalException nonFatal) {
                    logErroneous(name, i, System.currentTimeMillis() - startTime, nonFatal.getCause().getMessage());
                    return new CheckResult.Erroneous<>(name, i, (Error) nonFatal.getCause(), Option.none(), 0);
                }
            }
            logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
            return new CheckResult.Satisfied<>(name, tries, exhausted);
        } catch (NonFatalException nonFatal) {
            logErroneous(name, 0, System.currentTimeMillis() - startTime, nonFatal.getCause().getMessage());
            return new CheckResult.Erroneous<>(name, 0, (Error) nonFatal.getCause(), Option.none(), 0);
        }
    }

    /**
     * Performs a shrinkage of not successful result
     *
     * @param lastResult result to shrink
     * @return a shrunk result or lastResult if no shrink is available
     */
    private CheckResult<T> shrink(CheckResult<T> lastResult) {
        final Stream<T> shrinks = lastResult.sample().map(shrink::apply).getOrElse(Stream.empty());
        return firstFailure(shrinks, lastResult)
                .map(this::shrink)      //recursively shrink failure
                .getOrElse(lastResult); //until no failures found
    }

    /**
     * Searches a stream of shrunk values first one that does not satisfy the property
     *
     * @param shrinks    a stream of shrunk values
     * @param lastResult last known failed result
     * @return option failure of shrunk values
     */
    private Option<CheckResult<T>> firstFailure(Stream<T> shrinks, CheckResult<T> lastResult) {
        return shrinks.map(t -> shrinkCheck(t, lastResult)).dropWhile(CheckResult::isSatisfied).headOption();
    }

    /**
     * Performs a check of this property in context of shrinkage
     *
     * @param value      value to check
     * @param lastResult last known failure result
     * @return check result for this value
     */
    private CheckResult<T> shrinkCheck(T value, CheckResult<T> lastResult) {
        try {
            final Condition condition = Try.of(() -> predicate.apply(value)).recover(x -> {
                throw predicateError(x);
            }).get();
            if (condition.isFalse()) {
                return new CheckResult.Falsified<>(name, lastResult.count(), value, lastResult.shrinks() + 1);
            }
        } catch (NonFatalException nonFatal) {
            return new CheckResult.Erroneous<>(name, lastResult.count(), (Error) nonFatal.getCause(), Option.some(value), lastResult.shrinks() + 1);
        }
        return new CheckResult.Satisfied<>(name, lastResult.count(), false);
    }

}
