/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
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
package io.vavr;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static io.vavr.API.Match.*;

import io.vavr.collection.*;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The most basic Vavr functionality is accessed through this API class.
 *
 * <pre><code>
 * import static io.vavr.API.*;
 * </code></pre>
 *
 * <h2>For-comprehension</h2>
 * <p>
 * The {@code For}-comprehension is syntactic sugar for nested for-loops. We write
 *
 * <pre><code>
 * // lazily evaluated
 * Iterator&lt;R&gt; result = For(iterable1, iterable2, ..., iterableN).yield(f);
 * </code></pre>
 *
 * or
 *
 * <pre><code>
 * Iterator&lt;R&gt; result =
 *     For(iterable1, v1 -&gt;
 *         For(iterable2, v2 -&gt;
 *             ...
 *             For(iterableN).yield(vN -&gt; f.apply(v1, v2, ..., vN))
 *         )
 *     );
 * </code></pre>
 *
 * instead of
 *
 * <pre><code>
 * for(T1 v1 : iterable1) {
 *     for (T2 v2 : iterable2) {
 *         ...
 *         for (TN vN : iterableN) {
 *             R result = f.apply(v1, v2, ..., VN);
 *             //
 *             // We are forced to perform side effects to do s.th. meaningful with the result.
 *             //
 *         }
 *     }
 * }
 * </code></pre>
 *
 * Please note that values like Option, Try, Future, etc. are also iterable.
 * <p>
 * Given a suitable function
 * f: {@code (v1, v2, ..., vN) -> ...} and 1 &lt;= N &lt;= 8 iterables, the result is a Stream of the
 * mapped cross product elements.
 *
 * <pre><code>
 * { f(v1, v2, ..., vN) | v1 &isin; iterable1, ... vN &isin; iterableN }
 * </code></pre>
 *
 * As with all Vavr Values, the result of a For-comprehension can be converted
 * to standard Java library and Vavr types.
 */
public final class API {

    private API() {
    }

    //
    // Shortcuts
    //

    /**
     * A temporary replacement for an implementations used during prototyping.
     * <p>
     * Example:
     *
     * <pre><code>
     * public HttpResponse getResponse(HttpRequest request) {
     *     return TODO();
     * }
     *
     * final HttpResponse response = getHttpResponse(TODO());
     * </code></pre>
     *
     * @param <T> The result type of the missing implementation.
     * @return Nothing - this methods always throws.
     * @throws NotImplementedError when this methods is called
     * @see NotImplementedError#NotImplementedError()
     * @deprecated marked for removal
     */
    @Deprecated
    public static <T> T TODO() {
        throw new NotImplementedError();
    }

    /**
     * A temporary replacement for an implementations used during prototyping.
     * <p>
     * Example:
     *
     * <pre><code>
     * public HttpResponse getResponse(HttpRequest request) {
     *     return TODO("fake response");
     * }
     *
     * final HttpResponse response = getHttpResponse(TODO("fake request"));
     * </code></pre>
     *
     * @param msg An error message
     * @param <T> The result type of the missing implementation.
     * @return Nothing - this methods always throws.
     * @throws NotImplementedError when this methods is called
     * @see NotImplementedError#NotImplementedError(String)
     * @deprecated marked for removal
     */
    @Deprecated
    public static <T> T TODO(String msg) {
        throw new NotImplementedError(msg);
    }

    /**
     * Shortcut for {@code System.out.print(obj)}. See {@link PrintStream#print(Object)}.
     *
     * @param obj The <code>Object</code> to be printed
     */
    public static void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Shortcut for {@code System.out.printf(format, args)}. See {@link PrintStream#printf(String, Object...)}.
     *
     * @param format A format string as described in {@link Formatter}.
     * @param args   Arguments referenced by the format specifiers
     */
    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    /**
     * Shortcut for {@code System.out.println(obj)}. See {@link PrintStream#println(Object)}.
     *
     * @param obj The <code>Object</code> to be printed
     */
    public static void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Shortcut for {@code System.out.println()}. See {@link PrintStream#println()}.
     */
    public static void println() {
        System.out.println();
    }

    /**
     * Prints the given objects as space ' ' separated string using {@code System.out.println()}.
     *
     * @param objs The objects to be printed
     */
    public static void println(Object ...objs) {
        println(Iterator.of(objs).map(String::valueOf).mkString(" "));
    }

    //
    // Aliases for static factories
    //

    // -- Function

    /**
     * Alias for {@link Function0#of(Function0)}
     *
     * @param <R>             return type
     * @param methodReference A method reference
     * @return A {@link Function0}
     */
    public static <R> Function0<R> Function(Function0<R> methodReference) {
        return Function0.of(methodReference);
    }

    /**
     * Alias for {@link Function1#of(Function1)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param methodReference A method reference
     * @return A {@link Function1}
     */
    public static <T1, R> Function1<T1, R> Function(Function1<T1, R> methodReference) {
        return Function1.of(methodReference);
    }

    /**
     * Alias for {@link Function2#of(Function2)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param methodReference A method reference
     * @return A {@link Function2}
     */
    public static <T1, T2, R> Function2<T1, T2, R> Function(Function2<T1, T2, R> methodReference) {
        return Function2.of(methodReference);
    }

    /**
     * Alias for {@link Function3#of(Function3)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param methodReference A method reference
     * @return A {@link Function3}
     */
    public static <T1, T2, T3, R> Function3<T1, T2, T3, R> Function(Function3<T1, T2, T3, R> methodReference) {
        return Function3.of(methodReference);
    }

    /**
     * Alias for {@link Function4#of(Function4)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param methodReference A method reference
     * @return A {@link Function4}
     */
    public static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> Function(Function4<T1, T2, T3, T4, R> methodReference) {
        return Function4.of(methodReference);
    }

    /**
     * Alias for {@link Function5#of(Function5)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param <T5>            type of the 5th argument
     * @param methodReference A method reference
     * @return A {@link Function5}
     */
    public static <T1, T2, T3, T4, T5, R> Function5<T1, T2, T3, T4, T5, R> Function(Function5<T1, T2, T3, T4, T5, R> methodReference) {
        return Function5.of(methodReference);
    }

    /**
     * Alias for {@link Function6#of(Function6)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param <T5>            type of the 5th argument
     * @param <T6>            type of the 6th argument
     * @param methodReference A method reference
     * @return A {@link Function6}
     */
    public static <T1, T2, T3, T4, T5, T6, R> Function6<T1, T2, T3, T4, T5, T6, R> Function(Function6<T1, T2, T3, T4, T5, T6, R> methodReference) {
        return Function6.of(methodReference);
    }

    /**
     * Alias for {@link Function7#of(Function7)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param <T5>            type of the 5th argument
     * @param <T6>            type of the 6th argument
     * @param <T7>            type of the 7th argument
     * @param methodReference A method reference
     * @return A {@link Function7}
     */
    public static <T1, T2, T3, T4, T5, T6, T7, R> Function7<T1, T2, T3, T4, T5, T6, T7, R> Function(Function7<T1, T2, T3, T4, T5, T6, T7, R> methodReference) {
        return Function7.of(methodReference);
    }

    /**
     * Alias for {@link Function8#of(Function8)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param <T5>            type of the 5th argument
     * @param <T6>            type of the 6th argument
     * @param <T7>            type of the 7th argument
     * @param <T8>            type of the 8th argument
     * @param methodReference A method reference
     * @return A {@link Function8}
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> Function(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> methodReference) {
        return Function8.of(methodReference);
    }

    // -- CheckedFunction

    /**
     * Alias for {@link CheckedFunction0#of(CheckedFunction0)}
     *
     * @param <R>             return type
     * @param methodReference A method reference
     * @return A {@link CheckedFunction0}
     */
    public static <R> CheckedFunction0<R> CheckedFunction(CheckedFunction0<R> methodReference) {
        return CheckedFunction0.of(methodReference);
    }

    /**
     * Alias for {@link CheckedFunction1#of(CheckedFunction1)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param methodReference A method reference
     * @return A {@link CheckedFunction1}
     */
    public static <T1, R> CheckedFunction1<T1, R> CheckedFunction(CheckedFunction1<T1, R> methodReference) {
        return CheckedFunction1.of(methodReference);
    }

    /**
     * Alias for {@link CheckedFunction2#of(CheckedFunction2)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param methodReference A method reference
     * @return A {@link CheckedFunction2}
     */
    public static <T1, T2, R> CheckedFunction2<T1, T2, R> CheckedFunction(CheckedFunction2<T1, T2, R> methodReference) {
        return CheckedFunction2.of(methodReference);
    }

    /**
     * Alias for {@link CheckedFunction3#of(CheckedFunction3)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param methodReference A method reference
     * @return A {@link CheckedFunction3}
     */
    public static <T1, T2, T3, R> CheckedFunction3<T1, T2, T3, R> CheckedFunction(CheckedFunction3<T1, T2, T3, R> methodReference) {
        return CheckedFunction3.of(methodReference);
    }

    /**
     * Alias for {@link CheckedFunction4#of(CheckedFunction4)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param methodReference A method reference
     * @return A {@link CheckedFunction4}
     */
    public static <T1, T2, T3, T4, R> CheckedFunction4<T1, T2, T3, T4, R> CheckedFunction(CheckedFunction4<T1, T2, T3, T4, R> methodReference) {
        return CheckedFunction4.of(methodReference);
    }

    /**
     * Alias for {@link CheckedFunction5#of(CheckedFunction5)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param <T5>            type of the 5th argument
     * @param methodReference A method reference
     * @return A {@link CheckedFunction5}
     */
    public static <T1, T2, T3, T4, T5, R> CheckedFunction5<T1, T2, T3, T4, T5, R> CheckedFunction(CheckedFunction5<T1, T2, T3, T4, T5, R> methodReference) {
        return CheckedFunction5.of(methodReference);
    }

    /**
     * Alias for {@link CheckedFunction6#of(CheckedFunction6)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param <T5>            type of the 5th argument
     * @param <T6>            type of the 6th argument
     * @param methodReference A method reference
     * @return A {@link CheckedFunction6}
     */
    public static <T1, T2, T3, T4, T5, T6, R> CheckedFunction6<T1, T2, T3, T4, T5, T6, R> CheckedFunction(CheckedFunction6<T1, T2, T3, T4, T5, T6, R> methodReference) {
        return CheckedFunction6.of(methodReference);
    }

    /**
     * Alias for {@link CheckedFunction7#of(CheckedFunction7)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param <T5>            type of the 5th argument
     * @param <T6>            type of the 6th argument
     * @param <T7>            type of the 7th argument
     * @param methodReference A method reference
     * @return A {@link CheckedFunction7}
     */
    public static <T1, T2, T3, T4, T5, T6, T7, R> CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> CheckedFunction(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> methodReference) {
        return CheckedFunction7.of(methodReference);
    }

    /**
     * Alias for {@link CheckedFunction8#of(CheckedFunction8)}
     *
     * @param <R>             return type
     * @param <T1>            type of the 1st argument
     * @param <T2>            type of the 2nd argument
     * @param <T3>            type of the 3rd argument
     * @param <T4>            type of the 4th argument
     * @param <T5>            type of the 5th argument
     * @param <T6>            type of the 6th argument
     * @param <T7>            type of the 7th argument
     * @param <T8>            type of the 8th argument
     * @param methodReference A method reference
     * @return A {@link CheckedFunction8}
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> CheckedFunction(CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> methodReference) {
        return CheckedFunction8.of(methodReference);
    }

    // -- unchecked

    /**
     * Alias for {@link CheckedFunction0#unchecked}
     *
     * @param <R>  return type
     * @param f    A method reference
     * @return An unchecked wrapper of supplied {@link CheckedFunction0}
     */
    public static <R> Function0<R> unchecked(CheckedFunction0<R> f) {
        return f.unchecked();
    }

    /**
     * Alias for {@link CheckedFunction1#unchecked}
     *
     * @param <R>  return type
     * @param <T1> type of the 1st argument
     * @param f    A method reference
     * @return An unchecked wrapper of supplied {@link CheckedFunction1}
     */
    public static <T1, R> Function1<T1, R> unchecked(CheckedFunction1<T1, R> f) {
        return f.unchecked();
    }

    /**
     * Alias for {@link CheckedFunction2#unchecked}
     *
     * @param <R>  return type
     * @param <T1> type of the 1st argument
     * @param <T2> type of the 2nd argument
     * @param f    A method reference
     * @return An unchecked wrapper of supplied {@link CheckedFunction2}
     */
    public static <T1, T2, R> Function2<T1, T2, R> unchecked(CheckedFunction2<T1, T2, R> f) {
        return f.unchecked();
    }

    /**
     * Alias for {@link CheckedFunction3#unchecked}
     *
     * @param <R>  return type
     * @param <T1> type of the 1st argument
     * @param <T2> type of the 2nd argument
     * @param <T3> type of the 3rd argument
     * @param f    A method reference
     * @return An unchecked wrapper of supplied {@link CheckedFunction3}
     */
    public static <T1, T2, T3, R> Function3<T1, T2, T3, R> unchecked(CheckedFunction3<T1, T2, T3, R> f) {
        return f.unchecked();
    }

    /**
     * Alias for {@link CheckedFunction4#unchecked}
     *
     * @param <R>  return type
     * @param <T1> type of the 1st argument
     * @param <T2> type of the 2nd argument
     * @param <T3> type of the 3rd argument
     * @param <T4> type of the 4th argument
     * @param f    A method reference
     * @return An unchecked wrapper of supplied {@link CheckedFunction4}
     */
    public static <T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R> unchecked(CheckedFunction4<T1, T2, T3, T4, R> f) {
        return f.unchecked();
    }

    /**
     * Alias for {@link CheckedFunction5#unchecked}
     *
     * @param <R>  return type
     * @param <T1> type of the 1st argument
     * @param <T2> type of the 2nd argument
     * @param <T3> type of the 3rd argument
     * @param <T4> type of the 4th argument
     * @param <T5> type of the 5th argument
     * @param f    A method reference
     * @return An unchecked wrapper of supplied {@link CheckedFunction5}
     */
    public static <T1, T2, T3, T4, T5, R> Function5<T1, T2, T3, T4, T5, R> unchecked(CheckedFunction5<T1, T2, T3, T4, T5, R> f) {
        return f.unchecked();
    }

    /**
     * Alias for {@link CheckedFunction6#unchecked}
     *
     * @param <R>  return type
     * @param <T1> type of the 1st argument
     * @param <T2> type of the 2nd argument
     * @param <T3> type of the 3rd argument
     * @param <T4> type of the 4th argument
     * @param <T5> type of the 5th argument
     * @param <T6> type of the 6th argument
     * @param f    A method reference
     * @return An unchecked wrapper of supplied {@link CheckedFunction6}
     */
    public static <T1, T2, T3, T4, T5, T6, R> Function6<T1, T2, T3, T4, T5, T6, R> unchecked(CheckedFunction6<T1, T2, T3, T4, T5, T6, R> f) {
        return f.unchecked();
    }

    /**
     * Alias for {@link CheckedFunction7#unchecked}
     *
     * @param <R>  return type
     * @param <T1> type of the 1st argument
     * @param <T2> type of the 2nd argument
     * @param <T3> type of the 3rd argument
     * @param <T4> type of the 4th argument
     * @param <T5> type of the 5th argument
     * @param <T6> type of the 6th argument
     * @param <T7> type of the 7th argument
     * @param f    A method reference
     * @return An unchecked wrapper of supplied {@link CheckedFunction7}
     */
    public static <T1, T2, T3, T4, T5, T6, T7, R> Function7<T1, T2, T3, T4, T5, T6, T7, R> unchecked(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, R> f) {
        return f.unchecked();
    }

    /**
     * Alias for {@link CheckedFunction8#unchecked}
     *
     * @param <R>  return type
     * @param <T1> type of the 1st argument
     * @param <T2> type of the 2nd argument
     * @param <T3> type of the 3rd argument
     * @param <T4> type of the 4th argument
     * @param <T5> type of the 5th argument
     * @param <T6> type of the 6th argument
     * @param <T7> type of the 7th argument
     * @param <T8> type of the 8th argument
     * @param f    A method reference
     * @return An unchecked wrapper of supplied {@link CheckedFunction8}
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> unchecked(CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
        return f.unchecked();
    }

    // -- Tuple

    /**
     * Alias for {@link Tuple#empty()}
     *
     * @return the empty tuple.
     */
    public static Tuple0 Tuple() {
        return Tuple.empty();
    }

    /**
     * Alias for {@link Tuple#of(Object)}
     *
     * Creates a tuple of one element.
     *
     * @param <T1> type of the 1st element
     * @param t1   the 1st element
     * @return a tuple of one element.
     */
    public static <T1> Tuple1<T1> Tuple(T1 t1) {
        return Tuple.of(t1);
    }

    /**
     * Alias for {@link Tuple#of(Object, Object)}
     *
     * Creates a tuple of two elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @return a tuple of two elements.
     */
    public static <T1, T2> Tuple2<T1, T2> Tuple(T1 t1, T2 t2) {
        return Tuple.of(t1, t2);
    }

    /**
     * Alias for {@link Tuple#of(Object, Object, Object)}
     *
     * Creates a tuple of three elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @return a tuple of three elements.
     */
    public static <T1, T2, T3> Tuple3<T1, T2, T3> Tuple(T1 t1, T2 t2, T3 t3) {
        return Tuple.of(t1, t2, t3);
    }

    /**
     * Alias for {@link Tuple#of(Object, Object, Object, Object)}
     *
     * Creates a tuple of 4 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @return a tuple of 4 elements.
     */
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> Tuple(T1 t1, T2 t2, T3 t3, T4 t4) {
        return Tuple.of(t1, t2, t3, t4);
    }

    /**
     * Alias for {@link Tuple#of(Object, Object, Object, Object, Object)}
     *
     * Creates a tuple of 5 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @param t5   the 5th element
     * @return a tuple of 5 elements.
     */
    public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> Tuple(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return Tuple.of(t1, t2, t3, t4, t5);
    }

    /**
     * Alias for {@link Tuple#of(Object, Object, Object, Object, Object, Object)}
     *
     * Creates a tuple of 6 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @param t5   the 5th element
     * @param t6   the 6th element
     * @return a tuple of 6 elements.
     */
    public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> Tuple(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return Tuple.of(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Alias for {@link Tuple#of(Object, Object, Object, Object, Object, Object, Object)}
     *
     * Creates a tuple of 7 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @param t5   the 5th element
     * @param t6   the 6th element
     * @param t7   the 7th element
     * @return a tuple of 7 elements.
     */
    public static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> Tuple(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return Tuple.of(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Alias for {@link Tuple#of(Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * Creates a tuple of 8 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param t1   the 1st element
     * @param t2   the 2nd element
     * @param t3   the 3rd element
     * @param t4   the 4th element
     * @param t5   the 5th element
     * @param t6   the 6th element
     * @param t7   the 7th element
     * @param t8   the 8th element
     * @return a tuple of 8 elements.
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> Tuple(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return Tuple.of(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    // -- Either

    /**
     * Alias for {@link Either#right(Object)}
     *
     * @param <L>   Type of left value.
     * @param <R>   Type of right value.
     * @param right The value.
     * @return A new {@link Either} instance.
     */
    public static <L, R> Either<L, R> Right(R right) {
        return Either.right(right);
    }

    /**
     * Alias for {@link Either#left(Object)}
     *
     * @param <L>  Type of left value.
     * @param <R>  Type of right value.
     * @param left The value.
     * @return A new {@link Either} instance.
     */
    public static <L, R> Either<L, R> Left(L left) {
        return Either.left(left);
    }

    // -- Future

    /**
     * Alias for {@link Future#of(CheckedFunction0)}
     *
     * @param <T>         Type of the computation result.
     * @param computation A computation.
     * @return A new {@link Future} instance.
     * @throws NullPointerException if computation is null.
     */
    public static <T> Future<T> Future(CheckedFunction0<? extends T> computation) {
        return Future.of(computation);
    }

    /**
     * Alias for {@link Future#of(Executor, CheckedFunction0)}
     *
     * @param <T>             Type of the computation result.
     * @param executorService An executor service.
     * @param computation     A computation.
     * @return A new {@link Future} instance.
     * @throws NullPointerException if one of executorService or computation is null.
     */
    public static <T> Future<T> Future(Executor executorService, CheckedFunction0<? extends T> computation) {
        return Future.of(executorService, computation);
    }

    /**
     * Alias for {@link Future#successful(Object)}
     *
     * @param <T>    The value type of a successful result.
     * @param result The result.
     * @return A succeeded {@link Future}.
     */
    public static <T> Future<T> Future(T result) {
        return Future.successful(result);
    }

    /**
     * Alias for {@link Future#successful(Executor, Object)}
     *
     * @param <T>             The value type of a successful result.
     * @param executorService An {@code ExecutorService}.
     * @param result          The result.
     * @return A succeeded {@link Future}.
     * @throws NullPointerException if executorService is null
     */
    public static <T> Future<T> Future(Executor executorService, T result) {
        return Future.successful(executorService, result);
    }

    // -- Lazy

    /**
     * Alias for {@link Lazy#of(Supplier)}
     *
     * @param <T>      type of the lazy value
     * @param supplier A supplier
     * @return A new instance of {@link Lazy}
     */
    public static <T> Lazy<T> Lazy(Supplier<? extends T> supplier) {
        return Lazy.of(supplier);
    }

    // -- Option

    /**
     * Alias for {@link Option#of(Object)}
     *
     * @param <T>   type of the value
     * @param value A value
     * @return {@link Option.Some} if value is not {@code null}, {@link Option.None} otherwise
     */
    public static <T> Option<T> Option(T value) {
        return Option.of(value);
    }

    /**
     * Alias for {@link Option#some(Object)}
     *
     * @param <T>   type of the value
     * @param value A value
     * @return {@link Option}
     */
    public static <T> Option<T> Some(T value) {
        return Option.some(value);
    }

    /**
     * Alias for {@link Option#none()}
     *
     * @param <T> component type
     * @return the singleton instance of {@link Option}
     */
    public static <T> Option<T> None() {
        return Option.none();
    }

    // -- Try

    /**
     * Alias for {@link Try#of(CheckedFunction0)}
     *
     * @param <T>      Component type
     * @param supplier A checked supplier
     * @return {@link Try.Success} if no exception occurs, otherwise {@link Try.Failure} if an
     * exception occurs calling {@code supplier.get()}.
     */
    public static <T> Try<T> Try(CheckedFunction0<? extends T> supplier) {
        return Try.of(supplier);
    }

    /**
     * Alias for {@link Try#success(Object)}
     *
     * @param <T>   Type of the given {@code value}.
     * @param value A value.
     * @return A new {@link Try}.
     */
    public static <T> Try<T> Success(T value) {
        return Try.success(value);
    }

    /**
     * Alias for {@link Try#failure(Throwable)}
     *
     * @param <T>       Component type of the {@code Try}.
     * @param exception An exception.
     * @return A new {@link Try}.
     */
    public static <T> Try<T> Failure(Throwable exception) {
        return Try.failure(exception);
    }

    // -- Validation

    /**
     * Alias for {@link Validation#valid(Object)}
     *
     * @param <E>   type of the error
     * @param <T>   type of the given {@code value}
     * @param value A value
     * @return {@link Validation}
     * @throws NullPointerException if value is null
     */
    public static <E, T> Validation<E, T> Valid(T value) {
        return Validation.valid(value);
    }

    /**
     * Alias for {@link Validation#invalid(Object)}
     *
     * @param <E>   type of the given {@code error}
     * @param <T>   type of the value
     * @param error An error
     * @return {@link Validation}
     * @throws NullPointerException if error is null
     */
    public static <E, T> Validation<E, T> Invalid(E error) {
        return Validation.invalid(error);
    }

    // -- CharSeq

    /**
     * Alias for {@link CharSeq#of(char)}
     *
     * @param character A character.
     * @return A new {@link CharSeq} instance containing the given element
     */
    public static CharSeq CharSeq(char character) {
        return CharSeq.of(character);
    }

    /**
     * Alias for {@link CharSeq#of(char...)}
     *
     * @param characters Zero or more characters.
     * @return A new {@link CharSeq} instance containing the given characters in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    public static CharSeq CharSeq(char... characters) {
        return CharSeq.of(characters);
    }

    /**
     * Alias for {@link CharSeq#of(CharSequence)}
     *
     * @param sequence {@code CharSequence} instance.
     * @return A new {@link CharSeq} instance
     */
    public static CharSeq CharSeq(CharSequence sequence) {
        return CharSeq.of(sequence);
    }

    // -- TRAVERSABLES

    // -- PriorityQueue

    /**
     * Alias for {@link PriorityQueue#empty()}
     *
     * @param <T> Component type of element.
     * @return A new {@link PriorityQueue} empty instance
     */
    public static <T extends Comparable<? super T>> PriorityQueue<T> PriorityQueue() {
        return PriorityQueue.empty();
    }

    /**
     * Alias for {@link PriorityQueue#empty(Comparator)}
     *
     * @param <T>        Component type of element.
     * @param comparator The comparator used to sort the elements
     * @return A new {@link PriorityQueue} empty instance
     */
    public static <T extends Comparable<? super T>> PriorityQueue<T> PriorityQueue(Comparator<? super T> comparator) {
        return PriorityQueue.empty(comparator);
    }

    /**
     * Alias for {@link PriorityQueue#of(Comparable)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link PriorityQueue} instance containing the given element
     */
    public static <T extends Comparable<? super T>> PriorityQueue<T> PriorityQueue(T element) {
        return PriorityQueue.of(element);
    }

    /**
     * Alias for {@link PriorityQueue#of(Comparator, Object)}
     *
     * @param <T>        Component type of element.
     * @param comparator The comparator used to sort the elements
     * @param element    An element.
     * @return A new {@link PriorityQueue} instance containing the given element
     */
    public static <T> PriorityQueue<T> PriorityQueue(Comparator<? super T> comparator, T element) {
        return PriorityQueue.of(comparator, element);
    }

    /**
     * Alias for {@link PriorityQueue#of(Comparable...)}
     *
     * @param <T>      Component type of element.
     * @param elements Zero or more elements.
     * @return A new {@link PriorityQueue} instance containing the given elements
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T extends Comparable<? super T>> PriorityQueue<T> PriorityQueue(T... elements) {
        return PriorityQueue.of(elements);
    }

    /**
     * Alias for {@link PriorityQueue#of(Comparator, Object...)}
     *
     * @param <T>        Component type of element.
     * @param comparator The comparator used to sort the elements
     * @param elements   Zero or more elements.
     * @return A new {@link PriorityQueue} instance containing the given elements
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> PriorityQueue<T> PriorityQueue(Comparator<? super T> comparator, T... elements) {
        return PriorityQueue.of(comparator, elements);
    }

    // -- SEQUENCES

    // -- Seq

    /**
     * Alias for {@link List#empty()}
     *
     * @param <T> Component type of element.
     * @return A singleton instance of empty {@link List}
     */
    public static <T> Seq<T> Seq() {
        return List.empty();
    }

    /**
     * Alias for {@link List#of(Object)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link List} instance containing the given element
     */
    public static <T> Seq<T> Seq(T element) {
        return List.of(element);
    }

    /**
     * Alias for {@link List#of(Object...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link List} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Seq<T> Seq(T... elements) {
        return List.of(elements);
    }
    // -- IndexedSeq

    /**
     * Alias for {@link Vector#empty()}
     *
     * @param <T> Component type of element.
     * @return A singleton instance of empty {@link Vector}
     */
    public static <T> IndexedSeq<T> IndexedSeq() {
        return Vector.empty();
    }

    /**
     * Alias for {@link Vector#of(Object)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link Vector} instance containing the given element
     */
    public static <T> IndexedSeq<T> IndexedSeq(T element) {
        return Vector.of(element);
    }

    /**
     * Alias for {@link Vector#of(Object...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link Vector} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> IndexedSeq<T> IndexedSeq(T... elements) {
        return Vector.of(elements);
    }
    // -- Array

    /**
     * Alias for {@link Array#empty()}
     *
     * @param <T> Component type of element.
     * @return A singleton instance of empty {@link Array}
     */
    public static <T> Array<T> Array() {
        return Array.empty();
    }

    /**
     * Alias for {@link Array#of(Object)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link Array} instance containing the given element
     */
    public static <T> Array<T> Array(T element) {
        return Array.of(element);
    }

    /**
     * Alias for {@link Array#of(Object...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link Array} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Array<T> Array(T... elements) {
        return Array.of(elements);
    }
    // -- List

    /**
     * Alias for {@link List#empty()}
     *
     * @param <T> Component type of element.
     * @return A singleton instance of empty {@link List}
     */
    public static <T> List<T> List() {
        return List.empty();
    }

    /**
     * Alias for {@link List#of(Object)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link List} instance containing the given element
     */
    public static <T> List<T> List(T element) {
        return List.of(element);
    }

    /**
     * Alias for {@link List#of(Object...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link List} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> List<T> List(T... elements) {
        return List.of(elements);
    }
    // -- Queue

    /**
     * Alias for {@link Queue#empty()}
     *
     * @param <T> Component type of element.
     * @return A singleton instance of empty {@link Queue}
     */
    public static <T> Queue<T> Queue() {
        return Queue.empty();
    }

    /**
     * Alias for {@link Queue#of(Object)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link Queue} instance containing the given element
     */
    public static <T> Queue<T> Queue(T element) {
        return Queue.of(element);
    }

    /**
     * Alias for {@link Queue#of(Object...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link Queue} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Queue<T> Queue(T... elements) {
        return Queue.of(elements);
    }
    // -- Stream

    /**
     * Alias for {@link Stream#empty()}
     *
     * @param <T> Component type of element.
     * @return A singleton instance of empty {@link Stream}
     */
    public static <T> Stream<T> Stream() {
        return Stream.empty();
    }

    /**
     * Alias for {@link Stream#of(Object)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link Stream} instance containing the given element
     */
    public static <T> Stream<T> Stream(T element) {
        return Stream.of(element);
    }

    /**
     * Alias for {@link Stream#of(Object...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link Stream} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Stream<T> Stream(T... elements) {
        return Stream.of(elements);
    }
    // -- Vector

    /**
     * Alias for {@link Vector#empty()}
     *
     * @param <T> Component type of element.
     * @return A singleton instance of empty {@link Vector}
     */
    public static <T> Vector<T> Vector() {
        return Vector.empty();
    }

    /**
     * Alias for {@link Vector#of(Object)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link Vector} instance containing the given element
     */
    public static <T> Vector<T> Vector(T element) {
        return Vector.of(element);
    }

    /**
     * Alias for {@link Vector#of(Object...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link Vector} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Vector<T> Vector(T... elements) {
        return Vector.of(elements);
    }

    // -- SETS

    // -- Set

    /**
     * Alias for {@link HashSet#empty()}
     *
     * @param <T> Component type of element.
     * @return A singleton instance of empty {@link HashSet}
     */
    public static <T> Set<T> Set() {
        return HashSet.empty();
    }

    /**
     * Alias for {@link HashSet#of(Object)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link HashSet} instance containing the given element
     */
    public static <T> Set<T> Set(T element) {
        return HashSet.of(element);
    }

    /**
     * Alias for {@link HashSet#of(Object...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link HashSet} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Set<T> Set(T... elements) {
        return HashSet.of(elements);
    }
    // -- LinkedSet

    /**
     * Alias for {@link LinkedHashSet#empty()}
     *
     * @param <T> Component type of element.
     * @return A singleton instance of empty {@link LinkedHashSet}
     */
    public static <T> Set<T> LinkedSet() {
        return LinkedHashSet.empty();
    }

    /**
     * Alias for {@link LinkedHashSet#of(Object)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link LinkedHashSet} instance containing the given element
     */
    public static <T> Set<T> LinkedSet(T element) {
        return LinkedHashSet.of(element);
    }

    /**
     * Alias for {@link LinkedHashSet#of(Object...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link LinkedHashSet} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Set<T> LinkedSet(T... elements) {
        return LinkedHashSet.of(elements);
    }
    // -- SortedSet

    /**
     * Alias for {@link TreeSet#empty()}
     *
     * @param <T> Component type of element.
     * @return A new {@link TreeSet} empty instance
     */
    public static <T extends Comparable<? super T>> SortedSet<T> SortedSet() {
        return TreeSet.empty();
    }

    /**
     * Alias for {@link TreeSet#empty(Comparator)}
     *
     * @param <T>        Component type of element.
     * @param comparator The comparator used to sort the elements
     * @return A new {@link TreeSet} empty instance
     */
    public static <T extends Comparable<? super T>> SortedSet<T> SortedSet(Comparator<? super T> comparator) {
        return TreeSet.empty(comparator);
    }

    /**
     * Alias for {@link TreeSet#of(Comparable)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link TreeSet} instance containing the given element
     */
    public static <T extends Comparable<? super T>> SortedSet<T> SortedSet(T element) {
        return TreeSet.of(element);
    }

    /**
     * Alias for {@link TreeSet#of(Comparator, Object)}
     *
     * @param <T>        Component type of element.
     * @param comparator The comparator used to sort the elements
     * @param element    An element.
     * @return A new {@link TreeSet} instance containing the given element
     */
    public static <T> SortedSet<T> SortedSet(Comparator<? super T> comparator, T element) {
        return TreeSet.of(comparator, element);
    }

    /**
     * Alias for {@link TreeSet#of(Comparable...)}
     *
     * @param <T>      Component type of element.
     * @param elements Zero or more elements.
     * @return A new {@link TreeSet} instance containing the given elements
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T extends Comparable<? super T>> SortedSet<T> SortedSet(T... elements) {
        return TreeSet.of(elements);
    }

    /**
     * Alias for {@link TreeSet#of(Comparator, Object...)}
     *
     * @param <T>        Component type of element.
     * @param comparator The comparator used to sort the elements
     * @param elements   Zero or more elements.
     * @return A new {@link TreeSet} instance containing the given elements
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> SortedSet<T> SortedSet(Comparator<? super T> comparator, T... elements) {
        return TreeSet.of(comparator, elements);
    }

    // -- MAPS

    // -- Map

    /**
     * Alias for {@link HashMap#empty()}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @return A singleton instance of empty {@link HashMap}
     */
    public static <K, V> Map<K, V> Map() {
        return HashMap.empty();
    }

    /**
     * Alias for {@link HashMap#ofEntries(Tuple2...)}
     *
     * @param <K>     The key type.
     * @param <V>     The value type.
     * @param entries Map entries.
     * @return A new {@link HashMap} instance containing the given entries
     * @deprecated Will be removed in a future version.
     */
    @Deprecated
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <K, V> Map<K, V> Map(Tuple2<? extends K, ? extends V>... entries) {
        return HashMap.ofEntries(entries);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key
     * @param v1  The value
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1) {
        return HashMap.of(k1, v1);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2) {
        return HashMap.of(k1, v1, k2, v2);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2, K k3, V v3) {
        return HashMap.of(k1, v1, k2, v2, k3, v3);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return HashMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return HashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return HashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return HashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @param k8  The key of the 8th pair
     * @param v8  The value of the 8th pair
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return HashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @param k8  The key of the 8th pair
     * @param v8  The value of the 8th pair
     * @param k9  The key of the 9th pair
     * @param v9  The value of the 9th pair
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return HashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    /**
     * Alias for {@link HashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @param k8  The key of the 8th pair
     * @param v8  The value of the 8th pair
     * @param k9  The key of the 9th pair
     * @param v9  The value of the 9th pair
     * @param k10  The key of the 10th pair
     * @param v10  The value of the 10th pair
     * @return A new {@link HashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return HashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }
    // -- LinkedMap

    /**
     * Alias for {@link LinkedHashMap#empty()}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @return A singleton instance of empty {@link LinkedHashMap}
     */
    public static <K, V> Map<K, V> LinkedMap() {
        return LinkedHashMap.empty();
    }

    /**
     * Alias for {@link LinkedHashMap#ofEntries(Tuple2...)}
     *
     * @param <K>     The key type.
     * @param <V>     The value type.
     * @param entries Map entries.
     * @return A new {@link LinkedHashMap} instance containing the given entries
     * @deprecated Will be removed in a future version.
     */
    @Deprecated
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <K, V> Map<K, V> LinkedMap(Tuple2<? extends K, ? extends V>... entries) {
        return LinkedHashMap.ofEntries(entries);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key
     * @param v1  The value
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1) {
        return LinkedHashMap.of(k1, v1);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1, K k2, V v2) {
        return LinkedHashMap.of(k1, v1, k2, v2);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        return LinkedHashMap.of(k1, v1, k2, v2, k3, v3);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return LinkedHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return LinkedHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return LinkedHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return LinkedHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @param k8  The key of the 8th pair
     * @param v8  The value of the 8th pair
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return LinkedHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @param k8  The key of the 8th pair
     * @param v8  The value of the 8th pair
     * @param k9  The key of the 9th pair
     * @param v9  The value of the 9th pair
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return LinkedHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    /**
     * Alias for {@link LinkedHashMap#of(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @param k8  The key of the 8th pair
     * @param v8  The value of the 8th pair
     * @param k9  The key of the 9th pair
     * @param v9  The value of the 9th pair
     * @param k10  The key of the 10th pair
     * @param v10  The value of the 10th pair
     * @return A new {@link LinkedHashMap} instance containing the given entries
     */
    public static <K, V> Map<K, V> LinkedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return LinkedHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }
    /**
     * Alias for {@link TreeMap#empty()}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @return A new empty {@link TreeMap} instance
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap() {
        return TreeMap.empty();
    }

    /**
     * Alias for {@link TreeMap#empty(Comparator)}
     *
     * @param <K>           The key type.
     * @param <V>           The value type.
     * @param keyComparator The comparator used to sort the entries by their key
     * @return A new empty {@link TreeMap} instance
     */
    public static <K, V> SortedMap<K, V> SortedMap(Comparator<? super K> keyComparator) {
        return TreeMap.empty(keyComparator);
    }

    /**
     * Alias for {@link TreeMap#of(Comparator, Object, Object)}
     *
     * @param <K>           The key type.
     * @param <V>           The value type.
     * @param keyComparator The comparator used to sort the entries by their key
     * @param key           A singleton map key.
     * @param value         A singleton map value.
     * @return A new {@link TreeMap} instance containing the given entry
     */
    public static <K, V> SortedMap<K, V> SortedMap(Comparator<? super K> keyComparator, K key, V value) {
        return TreeMap.of(keyComparator, key, value);
    }

    /**
     * Alias for {@link TreeMap#ofEntries(Tuple2...)}
     *
     * @param <K>     The key type.
     * @param <V>     The value type.
     * @param entries Map entries.
     * @return A new {@link TreeMap} instance containing the given entries
     * @deprecated Will be removed in a future version.
     */
    @Deprecated
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(Tuple2<? extends K, ? extends V>... entries) {
        return TreeMap.ofEntries(entries);
    }

    /**
     * Alias for {@link TreeMap#ofEntries(Comparator, Tuple2...)}
     *
     * @param <K>           The key type.
     * @param <V>           The value type.
     * @param keyComparator The comparator used to sort the entries by their key
     * @param entries       Map entries.
     * @return A new {@link TreeMap} instance containing the given entry
     * @deprecated Will be removed in a future version.
     */
    @Deprecated
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <K, V> SortedMap<K, V> SortedMap(Comparator<? super K> keyComparator, Tuple2<? extends K, ? extends V>... entries) {
        return TreeMap.ofEntries(keyComparator, entries);
    }

    /**
     * Alias for {@link TreeMap#ofAll(java.util.Map)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param map A map entry.
     * @return A new {@link TreeMap} instance containing the given map
     * @deprecated Will be removed in a future version.
     */
    @Deprecated
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(java.util.Map<? extends K, ? extends V> map) {
        return TreeMap.ofAll(map);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key
     * @param v1  The value
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1) {
        return TreeMap.of(k1, v1);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object, Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1, K k2, V v2) {
        return TreeMap.of(k1, v1, k2, v2);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object, Comparable, Object, Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        return TreeMap.of(k1, v1, k2, v2, k3, v3);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return TreeMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return TreeMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return TreeMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return TreeMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @param k8  The key of the 8th pair
     * @param v8  The value of the 8th pair
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return TreeMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @param k8  The key of the 8th pair
     * @param v8  The value of the 8th pair
     * @param k9  The key of the 9th pair
     * @param v9  The value of the 9th pair
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return TreeMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    /**
     * Alias for {@link TreeMap#of(Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object, Comparable, Object)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param k1  The key of the 1st pair
     * @param v1  The value of the 1st pair
     * @param k2  The key of the 2nd pair
     * @param v2  The value of the 2nd pair
     * @param k3  The key of the 3rd pair
     * @param v3  The value of the 3rd pair
     * @param k4  The key of the 4th pair
     * @param v4  The value of the 4th pair
     * @param k5  The key of the 5th pair
     * @param v5  The value of the 5th pair
     * @param k6  The key of the 6th pair
     * @param v6  The value of the 6th pair
     * @param k7  The key of the 7th pair
     * @param v7  The value of the 7th pair
     * @param k8  The key of the 8th pair
     * @param v8  The value of the 8th pair
     * @param k9  The key of the 9th pair
     * @param v9  The value of the 9th pair
     * @param k10  The key of the 10th pair
     * @param v10  The value of the 10th pair
     * @return A new {@link TreeMap} instance containing the given entries
     */
    public static <K extends Comparable<? super K>, V> SortedMap<K, V> SortedMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return TreeMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    //
    // Java type tweaks
    //

    /**
     * Runs a {@code unit} of work and returns {@code Void}. This is helpful when a return value is expected,
     * e.g. by {@code Match}:
     *
     * <pre><code>Match(i).of(
     *     Case($(is(0)), i -&gt; run(() -&gt; System.out.println("zero"))),
     *     Case($(is(1)), i -&gt; run(() -&gt; System.out.println("one"))),
     *     Case($(), o -&gt; run(() -&gt; System.out.println("many")))
     * )</code></pre>
     *
     * @param unit A block of code to be run.
     * @return the single instance of {@code Void}, namely {@code null}
     */
    public static Void run(Runnable unit) {
        unit.run();
        return null;
    }

    //
    // For-Comprehension
    //

    /**
     * A shortcut for {@code Iterator.ofAll(ts).flatMap(f)} which allows us to write real for-comprehensions using
     * {@code For(...).yield(...)}.
     * <p>
     * Example:
     * <pre><code>
     * For(getPersons(), person -&gt;
     *     For(person.getTweets(), tweet -&gt;
     *         For(tweet.getReplies())
     *             .yield(reply -&gt; person + ", " + tweet + ", " + reply)));
     * </code></pre>
     *
     * @param ts A {@link Iterable}
     * @param f A function {@code T -> Iterable<U>}
     * @param <T> element type of {@code ts}
     * @param <U> component type of the resulting {@code Iterator}
     * @return A new Iterator
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T, U> Iterator<U> For(Iterable<T> ts, Function<? super T, ? extends Iterable<U>> f) {
        return Iterator.ofAll(ts).flatMap(f);
    }

    /**
     * Creates a {@code For}-comprehension of one Iterable.
     *
     * @param ts1 the 1st Iterable

     * @param <T1> right component type of the 1st Iterable
     * @return a new {@code For}-comprehension of arity 1
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1> For1<T1> For(Iterable<T1> ts1) {
        Objects.requireNonNull(ts1, "ts1 is null");
        return new For1<>(ts1);
    }

    /**
     * Creates a {@code For}-comprehension of two Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable

     * @param <T1> right component type of the 1st Iterable
     * @param <T2> right component type of the 2nd Iterable
     * @return a new {@code For}-comprehension of arity 2
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2> For2<T1, T2> For(Iterable<T1> ts1, Iterable<T2> ts2) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        return new For2<>(ts1, ts2);
    }

    /**
     * Creates a {@code For}-comprehension of three Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable

     * @param <T1> right component type of the 1st Iterable
     * @param <T2> right component type of the 2nd Iterable
     * @param <T3> right component type of the 3rd Iterable
     * @return a new {@code For}-comprehension of arity 3
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3> For3<T1, T2, T3> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        return new For3<>(ts1, ts2, ts3);
    }

    /**
     * Creates a {@code For}-comprehension of 4 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable

     * @param <T1> right component type of the 1st Iterable
     * @param <T2> right component type of the 2nd Iterable
     * @param <T3> right component type of the 3rd Iterable
     * @param <T4> right component type of the 4th Iterable
     * @return a new {@code For}-comprehension of arity 4
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4> For4<T1, T2, T3, T4> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        return new For4<>(ts1, ts2, ts3, ts4);
    }

    /**
     * Creates a {@code For}-comprehension of 5 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable
     * @param ts5 the 5th Iterable

     * @param <T1> right component type of the 1st Iterable
     * @param <T2> right component type of the 2nd Iterable
     * @param <T3> right component type of the 3rd Iterable
     * @param <T4> right component type of the 4th Iterable
     * @param <T5> right component type of the 5th Iterable
     * @return a new {@code For}-comprehension of arity 5
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5> For5<T1, T2, T3, T4, T5> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        return new For5<>(ts1, ts2, ts3, ts4, ts5);
    }

    /**
     * Creates a {@code For}-comprehension of 6 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable
     * @param ts5 the 5th Iterable
     * @param ts6 the 6th Iterable

     * @param <T1> right component type of the 1st Iterable
     * @param <T2> right component type of the 2nd Iterable
     * @param <T3> right component type of the 3rd Iterable
     * @param <T4> right component type of the 4th Iterable
     * @param <T5> right component type of the 5th Iterable
     * @param <T6> right component type of the 6th Iterable
     * @return a new {@code For}-comprehension of arity 6
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6> For6<T1, T2, T3, T4, T5, T6> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        return new For6<>(ts1, ts2, ts3, ts4, ts5, ts6);
    }

    /**
     * Creates a {@code For}-comprehension of 7 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable
     * @param ts5 the 5th Iterable
     * @param ts6 the 6th Iterable
     * @param ts7 the 7th Iterable

     * @param <T1> right component type of the 1st Iterable
     * @param <T2> right component type of the 2nd Iterable
     * @param <T3> right component type of the 3rd Iterable
     * @param <T4> right component type of the 4th Iterable
     * @param <T5> right component type of the 5th Iterable
     * @param <T6> right component type of the 6th Iterable
     * @param <T7> right component type of the 7th Iterable
     * @return a new {@code For}-comprehension of arity 7
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7> For7<T1, T2, T3, T4, T5, T6, T7> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        return new For7<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7);
    }

    /**
     * Creates a {@code For}-comprehension of 8 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable
     * @param ts5 the 5th Iterable
     * @param ts6 the 6th Iterable
     * @param ts7 the 7th Iterable
     * @param ts8 the 8th Iterable

     * @param <T1> right component type of the 1st Iterable
     * @param <T2> right component type of the 2nd Iterable
     * @param <T3> right component type of the 3rd Iterable
     * @param <T4> right component type of the 4th Iterable
     * @param <T5> right component type of the 5th Iterable
     * @param <T6> right component type of the 6th Iterable
     * @param <T7> right component type of the 7th Iterable
     * @param <T8> right component type of the 8th Iterable
     * @return a new {@code For}-comprehension of arity 8
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7, T8> For8<T1, T2, T3, T4, T5, T6, T7, T8> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7, Iterable<T8> ts8) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        Objects.requireNonNull(ts8, "ts8 is null");
        return new For8<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7, ts8);
    }

    /**
     * Creates a {@code For}-comprehension of one Option.
     *
     * @param ts1 the 1st Option

     * @param <T1> right component type of the 1st Option
     * @return a new {@code For}-comprehension of arity 1
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1> For1Option<T1> For(Option<T1> ts1) {
        Objects.requireNonNull(ts1, "ts1 is null");
        return new For1Option<>(ts1);
    }

    /**
     * Creates a {@code For}-comprehension of two Options.
     *
     * @param ts1 the 1st Option
     * @param ts2 the 2nd Option

     * @param <T1> right component type of the 1st Option
     * @param <T2> right component type of the 2nd Option
     * @return a new {@code For}-comprehension of arity 2
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2> For2Option<T1, T2> For(Option<T1> ts1, Option<T2> ts2) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        return new For2Option<>(ts1, ts2);
    }

    /**
     * Creates a {@code For}-comprehension of three Options.
     *
     * @param ts1 the 1st Option
     * @param ts2 the 2nd Option
     * @param ts3 the 3rd Option

     * @param <T1> right component type of the 1st Option
     * @param <T2> right component type of the 2nd Option
     * @param <T3> right component type of the 3rd Option
     * @return a new {@code For}-comprehension of arity 3
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3> For3Option<T1, T2, T3> For(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        return new For3Option<>(ts1, ts2, ts3);
    }

    /**
     * Creates a {@code For}-comprehension of 4 Options.
     *
     * @param ts1 the 1st Option
     * @param ts2 the 2nd Option
     * @param ts3 the 3rd Option
     * @param ts4 the 4th Option

     * @param <T1> right component type of the 1st Option
     * @param <T2> right component type of the 2nd Option
     * @param <T3> right component type of the 3rd Option
     * @param <T4> right component type of the 4th Option
     * @return a new {@code For}-comprehension of arity 4
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4> For4Option<T1, T2, T3, T4> For(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        return new For4Option<>(ts1, ts2, ts3, ts4);
    }

    /**
     * Creates a {@code For}-comprehension of 5 Options.
     *
     * @param ts1 the 1st Option
     * @param ts2 the 2nd Option
     * @param ts3 the 3rd Option
     * @param ts4 the 4th Option
     * @param ts5 the 5th Option

     * @param <T1> right component type of the 1st Option
     * @param <T2> right component type of the 2nd Option
     * @param <T3> right component type of the 3rd Option
     * @param <T4> right component type of the 4th Option
     * @param <T5> right component type of the 5th Option
     * @return a new {@code For}-comprehension of arity 5
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5> For5Option<T1, T2, T3, T4, T5> For(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4, Option<T5> ts5) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        return new For5Option<>(ts1, ts2, ts3, ts4, ts5);
    }

    /**
     * Creates a {@code For}-comprehension of 6 Options.
     *
     * @param ts1 the 1st Option
     * @param ts2 the 2nd Option
     * @param ts3 the 3rd Option
     * @param ts4 the 4th Option
     * @param ts5 the 5th Option
     * @param ts6 the 6th Option

     * @param <T1> right component type of the 1st Option
     * @param <T2> right component type of the 2nd Option
     * @param <T3> right component type of the 3rd Option
     * @param <T4> right component type of the 4th Option
     * @param <T5> right component type of the 5th Option
     * @param <T6> right component type of the 6th Option
     * @return a new {@code For}-comprehension of arity 6
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6> For6Option<T1, T2, T3, T4, T5, T6> For(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4, Option<T5> ts5, Option<T6> ts6) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        return new For6Option<>(ts1, ts2, ts3, ts4, ts5, ts6);
    }

    /**
     * Creates a {@code For}-comprehension of 7 Options.
     *
     * @param ts1 the 1st Option
     * @param ts2 the 2nd Option
     * @param ts3 the 3rd Option
     * @param ts4 the 4th Option
     * @param ts5 the 5th Option
     * @param ts6 the 6th Option
     * @param ts7 the 7th Option

     * @param <T1> right component type of the 1st Option
     * @param <T2> right component type of the 2nd Option
     * @param <T3> right component type of the 3rd Option
     * @param <T4> right component type of the 4th Option
     * @param <T5> right component type of the 5th Option
     * @param <T6> right component type of the 6th Option
     * @param <T7> right component type of the 7th Option
     * @return a new {@code For}-comprehension of arity 7
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7> For7Option<T1, T2, T3, T4, T5, T6, T7> For(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4, Option<T5> ts5, Option<T6> ts6, Option<T7> ts7) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        return new For7Option<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7);
    }

    /**
     * Creates a {@code For}-comprehension of 8 Options.
     *
     * @param ts1 the 1st Option
     * @param ts2 the 2nd Option
     * @param ts3 the 3rd Option
     * @param ts4 the 4th Option
     * @param ts5 the 5th Option
     * @param ts6 the 6th Option
     * @param ts7 the 7th Option
     * @param ts8 the 8th Option

     * @param <T1> right component type of the 1st Option
     * @param <T2> right component type of the 2nd Option
     * @param <T3> right component type of the 3rd Option
     * @param <T4> right component type of the 4th Option
     * @param <T5> right component type of the 5th Option
     * @param <T6> right component type of the 6th Option
     * @param <T7> right component type of the 7th Option
     * @param <T8> right component type of the 8th Option
     * @return a new {@code For}-comprehension of arity 8
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7, T8> For8Option<T1, T2, T3, T4, T5, T6, T7, T8> For(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4, Option<T5> ts5, Option<T6> ts6, Option<T7> ts7, Option<T8> ts8) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        Objects.requireNonNull(ts8, "ts8 is null");
        return new For8Option<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7, ts8);
    }

    /**
     * Creates a {@code For}-comprehension of one Future.
     *
     * @param ts1 the 1st Future

     * @param <T1> right component type of the 1st Future
     * @return a new {@code For}-comprehension of arity 1
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1> For1Future<T1> For(Future<T1> ts1) {
        Objects.requireNonNull(ts1, "ts1 is null");
        return new For1Future<>(ts1);
    }

    /**
     * Creates a {@code For}-comprehension of two Futures.
     *
     * @param ts1 the 1st Future
     * @param ts2 the 2nd Future

     * @param <T1> right component type of the 1st Future
     * @param <T2> right component type of the 2nd Future
     * @return a new {@code For}-comprehension of arity 2
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2> For2Future<T1, T2> For(Future<T1> ts1, Future<T2> ts2) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        return new For2Future<>(ts1, ts2);
    }

    /**
     * Creates a {@code For}-comprehension of three Futures.
     *
     * @param ts1 the 1st Future
     * @param ts2 the 2nd Future
     * @param ts3 the 3rd Future

     * @param <T1> right component type of the 1st Future
     * @param <T2> right component type of the 2nd Future
     * @param <T3> right component type of the 3rd Future
     * @return a new {@code For}-comprehension of arity 3
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3> For3Future<T1, T2, T3> For(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        return new For3Future<>(ts1, ts2, ts3);
    }

    /**
     * Creates a {@code For}-comprehension of 4 Futures.
     *
     * @param ts1 the 1st Future
     * @param ts2 the 2nd Future
     * @param ts3 the 3rd Future
     * @param ts4 the 4th Future

     * @param <T1> right component type of the 1st Future
     * @param <T2> right component type of the 2nd Future
     * @param <T3> right component type of the 3rd Future
     * @param <T4> right component type of the 4th Future
     * @return a new {@code For}-comprehension of arity 4
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4> For4Future<T1, T2, T3, T4> For(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        return new For4Future<>(ts1, ts2, ts3, ts4);
    }

    /**
     * Creates a {@code For}-comprehension of 5 Futures.
     *
     * @param ts1 the 1st Future
     * @param ts2 the 2nd Future
     * @param ts3 the 3rd Future
     * @param ts4 the 4th Future
     * @param ts5 the 5th Future

     * @param <T1> right component type of the 1st Future
     * @param <T2> right component type of the 2nd Future
     * @param <T3> right component type of the 3rd Future
     * @param <T4> right component type of the 4th Future
     * @param <T5> right component type of the 5th Future
     * @return a new {@code For}-comprehension of arity 5
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5> For5Future<T1, T2, T3, T4, T5> For(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4, Future<T5> ts5) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        return new For5Future<>(ts1, ts2, ts3, ts4, ts5);
    }

    /**
     * Creates a {@code For}-comprehension of 6 Futures.
     *
     * @param ts1 the 1st Future
     * @param ts2 the 2nd Future
     * @param ts3 the 3rd Future
     * @param ts4 the 4th Future
     * @param ts5 the 5th Future
     * @param ts6 the 6th Future

     * @param <T1> right component type of the 1st Future
     * @param <T2> right component type of the 2nd Future
     * @param <T3> right component type of the 3rd Future
     * @param <T4> right component type of the 4th Future
     * @param <T5> right component type of the 5th Future
     * @param <T6> right component type of the 6th Future
     * @return a new {@code For}-comprehension of arity 6
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6> For6Future<T1, T2, T3, T4, T5, T6> For(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4, Future<T5> ts5, Future<T6> ts6) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        return new For6Future<>(ts1, ts2, ts3, ts4, ts5, ts6);
    }

    /**
     * Creates a {@code For}-comprehension of 7 Futures.
     *
     * @param ts1 the 1st Future
     * @param ts2 the 2nd Future
     * @param ts3 the 3rd Future
     * @param ts4 the 4th Future
     * @param ts5 the 5th Future
     * @param ts6 the 6th Future
     * @param ts7 the 7th Future

     * @param <T1> right component type of the 1st Future
     * @param <T2> right component type of the 2nd Future
     * @param <T3> right component type of the 3rd Future
     * @param <T4> right component type of the 4th Future
     * @param <T5> right component type of the 5th Future
     * @param <T6> right component type of the 6th Future
     * @param <T7> right component type of the 7th Future
     * @return a new {@code For}-comprehension of arity 7
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7> For7Future<T1, T2, T3, T4, T5, T6, T7> For(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4, Future<T5> ts5, Future<T6> ts6, Future<T7> ts7) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        return new For7Future<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7);
    }

    /**
     * Creates a {@code For}-comprehension of 8 Futures.
     *
     * @param ts1 the 1st Future
     * @param ts2 the 2nd Future
     * @param ts3 the 3rd Future
     * @param ts4 the 4th Future
     * @param ts5 the 5th Future
     * @param ts6 the 6th Future
     * @param ts7 the 7th Future
     * @param ts8 the 8th Future

     * @param <T1> right component type of the 1st Future
     * @param <T2> right component type of the 2nd Future
     * @param <T3> right component type of the 3rd Future
     * @param <T4> right component type of the 4th Future
     * @param <T5> right component type of the 5th Future
     * @param <T6> right component type of the 6th Future
     * @param <T7> right component type of the 7th Future
     * @param <T8> right component type of the 8th Future
     * @return a new {@code For}-comprehension of arity 8
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7, T8> For8Future<T1, T2, T3, T4, T5, T6, T7, T8> For(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4, Future<T5> ts5, Future<T6> ts6, Future<T7> ts7, Future<T8> ts8) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        Objects.requireNonNull(ts8, "ts8 is null");
        return new For8Future<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7, ts8);
    }

    /**
     * Creates a {@code For}-comprehension of one Try.
     *
     * @param ts1 the 1st Try

     * @param <T1> right component type of the 1st Try
     * @return a new {@code For}-comprehension of arity 1
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1> For1Try<T1> For(Try<T1> ts1) {
        Objects.requireNonNull(ts1, "ts1 is null");
        return new For1Try<>(ts1);
    }

    /**
     * Creates a {@code For}-comprehension of two Trys.
     *
     * @param ts1 the 1st Try
     * @param ts2 the 2nd Try

     * @param <T1> right component type of the 1st Try
     * @param <T2> right component type of the 2nd Try
     * @return a new {@code For}-comprehension of arity 2
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2> For2Try<T1, T2> For(Try<T1> ts1, Try<T2> ts2) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        return new For2Try<>(ts1, ts2);
    }

    /**
     * Creates a {@code For}-comprehension of three Trys.
     *
     * @param ts1 the 1st Try
     * @param ts2 the 2nd Try
     * @param ts3 the 3rd Try

     * @param <T1> right component type of the 1st Try
     * @param <T2> right component type of the 2nd Try
     * @param <T3> right component type of the 3rd Try
     * @return a new {@code For}-comprehension of arity 3
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3> For3Try<T1, T2, T3> For(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        return new For3Try<>(ts1, ts2, ts3);
    }

    /**
     * Creates a {@code For}-comprehension of 4 Trys.
     *
     * @param ts1 the 1st Try
     * @param ts2 the 2nd Try
     * @param ts3 the 3rd Try
     * @param ts4 the 4th Try

     * @param <T1> right component type of the 1st Try
     * @param <T2> right component type of the 2nd Try
     * @param <T3> right component type of the 3rd Try
     * @param <T4> right component type of the 4th Try
     * @return a new {@code For}-comprehension of arity 4
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4> For4Try<T1, T2, T3, T4> For(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        return new For4Try<>(ts1, ts2, ts3, ts4);
    }

    /**
     * Creates a {@code For}-comprehension of 5 Trys.
     *
     * @param ts1 the 1st Try
     * @param ts2 the 2nd Try
     * @param ts3 the 3rd Try
     * @param ts4 the 4th Try
     * @param ts5 the 5th Try

     * @param <T1> right component type of the 1st Try
     * @param <T2> right component type of the 2nd Try
     * @param <T3> right component type of the 3rd Try
     * @param <T4> right component type of the 4th Try
     * @param <T5> right component type of the 5th Try
     * @return a new {@code For}-comprehension of arity 5
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5> For5Try<T1, T2, T3, T4, T5> For(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4, Try<T5> ts5) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        return new For5Try<>(ts1, ts2, ts3, ts4, ts5);
    }

    /**
     * Creates a {@code For}-comprehension of 6 Trys.
     *
     * @param ts1 the 1st Try
     * @param ts2 the 2nd Try
     * @param ts3 the 3rd Try
     * @param ts4 the 4th Try
     * @param ts5 the 5th Try
     * @param ts6 the 6th Try

     * @param <T1> right component type of the 1st Try
     * @param <T2> right component type of the 2nd Try
     * @param <T3> right component type of the 3rd Try
     * @param <T4> right component type of the 4th Try
     * @param <T5> right component type of the 5th Try
     * @param <T6> right component type of the 6th Try
     * @return a new {@code For}-comprehension of arity 6
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6> For6Try<T1, T2, T3, T4, T5, T6> For(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4, Try<T5> ts5, Try<T6> ts6) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        return new For6Try<>(ts1, ts2, ts3, ts4, ts5, ts6);
    }

    /**
     * Creates a {@code For}-comprehension of 7 Trys.
     *
     * @param ts1 the 1st Try
     * @param ts2 the 2nd Try
     * @param ts3 the 3rd Try
     * @param ts4 the 4th Try
     * @param ts5 the 5th Try
     * @param ts6 the 6th Try
     * @param ts7 the 7th Try

     * @param <T1> right component type of the 1st Try
     * @param <T2> right component type of the 2nd Try
     * @param <T3> right component type of the 3rd Try
     * @param <T4> right component type of the 4th Try
     * @param <T5> right component type of the 5th Try
     * @param <T6> right component type of the 6th Try
     * @param <T7> right component type of the 7th Try
     * @return a new {@code For}-comprehension of arity 7
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7> For7Try<T1, T2, T3, T4, T5, T6, T7> For(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4, Try<T5> ts5, Try<T6> ts6, Try<T7> ts7) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        return new For7Try<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7);
    }

    /**
     * Creates a {@code For}-comprehension of 8 Trys.
     *
     * @param ts1 the 1st Try
     * @param ts2 the 2nd Try
     * @param ts3 the 3rd Try
     * @param ts4 the 4th Try
     * @param ts5 the 5th Try
     * @param ts6 the 6th Try
     * @param ts7 the 7th Try
     * @param ts8 the 8th Try

     * @param <T1> right component type of the 1st Try
     * @param <T2> right component type of the 2nd Try
     * @param <T3> right component type of the 3rd Try
     * @param <T4> right component type of the 4th Try
     * @param <T5> right component type of the 5th Try
     * @param <T6> right component type of the 6th Try
     * @param <T7> right component type of the 7th Try
     * @param <T8> right component type of the 8th Try
     * @return a new {@code For}-comprehension of arity 8
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7, T8> For8Try<T1, T2, T3, T4, T5, T6, T7, T8> For(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4, Try<T5> ts5, Try<T6> ts6, Try<T7> ts7, Try<T8> ts8) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        Objects.requireNonNull(ts8, "ts8 is null");
        return new For8Try<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7, ts8);
    }

    /**
     * Creates a {@code For}-comprehension of one Either.
     *
     * @param ts1 the 1st Either
     @param <L> left component type of the given Either types
     * @param <T1> right component type of the 1st Either
     * @return a new {@code For}-comprehension of arity 1
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <L, T1> For1Either<L, T1> For(Either<L, T1> ts1) {
        Objects.requireNonNull(ts1, "ts1 is null");
        return new For1Either<>(ts1);
    }

    /**
     * Creates a {@code For}-comprehension of two Eithers.
     *
     * @param ts1 the 1st Either
     * @param ts2 the 2nd Either
     @param <L> left component type of the given Either types
     * @param <T1> right component type of the 1st Either
     * @param <T2> right component type of the 2nd Either
     * @return a new {@code For}-comprehension of arity 2
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <L, T1, T2> For2Either<L, T1, T2> For(Either<L, T1> ts1, Either<L, T2> ts2) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        return new For2Either<>(ts1, ts2);
    }

    /**
     * Creates a {@code For}-comprehension of three Eithers.
     *
     * @param ts1 the 1st Either
     * @param ts2 the 2nd Either
     * @param ts3 the 3rd Either
     @param <L> left component type of the given Either types
     * @param <T1> right component type of the 1st Either
     * @param <T2> right component type of the 2nd Either
     * @param <T3> right component type of the 3rd Either
     * @return a new {@code For}-comprehension of arity 3
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <L, T1, T2, T3> For3Either<L, T1, T2, T3> For(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        return new For3Either<>(ts1, ts2, ts3);
    }

    /**
     * Creates a {@code For}-comprehension of 4 Eithers.
     *
     * @param ts1 the 1st Either
     * @param ts2 the 2nd Either
     * @param ts3 the 3rd Either
     * @param ts4 the 4th Either
     @param <L> left component type of the given Either types
     * @param <T1> right component type of the 1st Either
     * @param <T2> right component type of the 2nd Either
     * @param <T3> right component type of the 3rd Either
     * @param <T4> right component type of the 4th Either
     * @return a new {@code For}-comprehension of arity 4
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <L, T1, T2, T3, T4> For4Either<L, T1, T2, T3, T4> For(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        return new For4Either<>(ts1, ts2, ts3, ts4);
    }

    /**
     * Creates a {@code For}-comprehension of 5 Eithers.
     *
     * @param ts1 the 1st Either
     * @param ts2 the 2nd Either
     * @param ts3 the 3rd Either
     * @param ts4 the 4th Either
     * @param ts5 the 5th Either
     @param <L> left component type of the given Either types
     * @param <T1> right component type of the 1st Either
     * @param <T2> right component type of the 2nd Either
     * @param <T3> right component type of the 3rd Either
     * @param <T4> right component type of the 4th Either
     * @param <T5> right component type of the 5th Either
     * @return a new {@code For}-comprehension of arity 5
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <L, T1, T2, T3, T4, T5> For5Either<L, T1, T2, T3, T4, T5> For(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4, Either<L, T5> ts5) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        return new For5Either<>(ts1, ts2, ts3, ts4, ts5);
    }

    /**
     * Creates a {@code For}-comprehension of 6 Eithers.
     *
     * @param ts1 the 1st Either
     * @param ts2 the 2nd Either
     * @param ts3 the 3rd Either
     * @param ts4 the 4th Either
     * @param ts5 the 5th Either
     * @param ts6 the 6th Either
     @param <L> left component type of the given Either types
     * @param <T1> right component type of the 1st Either
     * @param <T2> right component type of the 2nd Either
     * @param <T3> right component type of the 3rd Either
     * @param <T4> right component type of the 4th Either
     * @param <T5> right component type of the 5th Either
     * @param <T6> right component type of the 6th Either
     * @return a new {@code For}-comprehension of arity 6
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <L, T1, T2, T3, T4, T5, T6> For6Either<L, T1, T2, T3, T4, T5, T6> For(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4, Either<L, T5> ts5, Either<L, T6> ts6) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        return new For6Either<>(ts1, ts2, ts3, ts4, ts5, ts6);
    }

    /**
     * Creates a {@code For}-comprehension of 7 Eithers.
     *
     * @param ts1 the 1st Either
     * @param ts2 the 2nd Either
     * @param ts3 the 3rd Either
     * @param ts4 the 4th Either
     * @param ts5 the 5th Either
     * @param ts6 the 6th Either
     * @param ts7 the 7th Either
     @param <L> left component type of the given Either types
     * @param <T1> right component type of the 1st Either
     * @param <T2> right component type of the 2nd Either
     * @param <T3> right component type of the 3rd Either
     * @param <T4> right component type of the 4th Either
     * @param <T5> right component type of the 5th Either
     * @param <T6> right component type of the 6th Either
     * @param <T7> right component type of the 7th Either
     * @return a new {@code For}-comprehension of arity 7
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <L, T1, T2, T3, T4, T5, T6, T7> For7Either<L, T1, T2, T3, T4, T5, T6, T7> For(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4, Either<L, T5> ts5, Either<L, T6> ts6, Either<L, T7> ts7) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        return new For7Either<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7);
    }

    /**
     * Creates a {@code For}-comprehension of 8 Eithers.
     *
     * @param ts1 the 1st Either
     * @param ts2 the 2nd Either
     * @param ts3 the 3rd Either
     * @param ts4 the 4th Either
     * @param ts5 the 5th Either
     * @param ts6 the 6th Either
     * @param ts7 the 7th Either
     * @param ts8 the 8th Either
     @param <L> left component type of the given Either types
     * @param <T1> right component type of the 1st Either
     * @param <T2> right component type of the 2nd Either
     * @param <T3> right component type of the 3rd Either
     * @param <T4> right component type of the 4th Either
     * @param <T5> right component type of the 5th Either
     * @param <T6> right component type of the 6th Either
     * @param <T7> right component type of the 7th Either
     * @param <T8> right component type of the 8th Either
     * @return a new {@code For}-comprehension of arity 8
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <L, T1, T2, T3, T4, T5, T6, T7, T8> For8Either<L, T1, T2, T3, T4, T5, T6, T7, T8> For(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4, Either<L, T5> ts5, Either<L, T6> ts6, Either<L, T7> ts7, Either<L, T8> ts8) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        Objects.requireNonNull(ts8, "ts8 is null");
        return new For8Either<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7, ts8);
    }

    /**
     * Creates a {@code For}-comprehension of one List.
     *
     * @param ts1 the 1st List

     * @param <T1> right component type of the 1st List
     * @return a new {@code For}-comprehension of arity 1
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1> For1List<T1> For(List<T1> ts1) {
        Objects.requireNonNull(ts1, "ts1 is null");
        return new For1List<>(ts1);
    }

    /**
     * Creates a {@code For}-comprehension of two Lists.
     *
     * @param ts1 the 1st List
     * @param ts2 the 2nd List

     * @param <T1> right component type of the 1st List
     * @param <T2> right component type of the 2nd List
     * @return a new {@code For}-comprehension of arity 2
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2> For2List<T1, T2> For(List<T1> ts1, List<T2> ts2) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        return new For2List<>(ts1, ts2);
    }

    /**
     * Creates a {@code For}-comprehension of three Lists.
     *
     * @param ts1 the 1st List
     * @param ts2 the 2nd List
     * @param ts3 the 3rd List

     * @param <T1> right component type of the 1st List
     * @param <T2> right component type of the 2nd List
     * @param <T3> right component type of the 3rd List
     * @return a new {@code For}-comprehension of arity 3
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3> For3List<T1, T2, T3> For(List<T1> ts1, List<T2> ts2, List<T3> ts3) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        return new For3List<>(ts1, ts2, ts3);
    }

    /**
     * Creates a {@code For}-comprehension of 4 Lists.
     *
     * @param ts1 the 1st List
     * @param ts2 the 2nd List
     * @param ts3 the 3rd List
     * @param ts4 the 4th List

     * @param <T1> right component type of the 1st List
     * @param <T2> right component type of the 2nd List
     * @param <T3> right component type of the 3rd List
     * @param <T4> right component type of the 4th List
     * @return a new {@code For}-comprehension of arity 4
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4> For4List<T1, T2, T3, T4> For(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        return new For4List<>(ts1, ts2, ts3, ts4);
    }

    /**
     * Creates a {@code For}-comprehension of 5 Lists.
     *
     * @param ts1 the 1st List
     * @param ts2 the 2nd List
     * @param ts3 the 3rd List
     * @param ts4 the 4th List
     * @param ts5 the 5th List

     * @param <T1> right component type of the 1st List
     * @param <T2> right component type of the 2nd List
     * @param <T3> right component type of the 3rd List
     * @param <T4> right component type of the 4th List
     * @param <T5> right component type of the 5th List
     * @return a new {@code For}-comprehension of arity 5
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5> For5List<T1, T2, T3, T4, T5> For(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4, List<T5> ts5) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        return new For5List<>(ts1, ts2, ts3, ts4, ts5);
    }

    /**
     * Creates a {@code For}-comprehension of 6 Lists.
     *
     * @param ts1 the 1st List
     * @param ts2 the 2nd List
     * @param ts3 the 3rd List
     * @param ts4 the 4th List
     * @param ts5 the 5th List
     * @param ts6 the 6th List

     * @param <T1> right component type of the 1st List
     * @param <T2> right component type of the 2nd List
     * @param <T3> right component type of the 3rd List
     * @param <T4> right component type of the 4th List
     * @param <T5> right component type of the 5th List
     * @param <T6> right component type of the 6th List
     * @return a new {@code For}-comprehension of arity 6
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6> For6List<T1, T2, T3, T4, T5, T6> For(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4, List<T5> ts5, List<T6> ts6) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        return new For6List<>(ts1, ts2, ts3, ts4, ts5, ts6);
    }

    /**
     * Creates a {@code For}-comprehension of 7 Lists.
     *
     * @param ts1 the 1st List
     * @param ts2 the 2nd List
     * @param ts3 the 3rd List
     * @param ts4 the 4th List
     * @param ts5 the 5th List
     * @param ts6 the 6th List
     * @param ts7 the 7th List

     * @param <T1> right component type of the 1st List
     * @param <T2> right component type of the 2nd List
     * @param <T3> right component type of the 3rd List
     * @param <T4> right component type of the 4th List
     * @param <T5> right component type of the 5th List
     * @param <T6> right component type of the 6th List
     * @param <T7> right component type of the 7th List
     * @return a new {@code For}-comprehension of arity 7
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7> For7List<T1, T2, T3, T4, T5, T6, T7> For(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4, List<T5> ts5, List<T6> ts6, List<T7> ts7) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        return new For7List<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7);
    }

    /**
     * Creates a {@code For}-comprehension of 8 Lists.
     *
     * @param ts1 the 1st List
     * @param ts2 the 2nd List
     * @param ts3 the 3rd List
     * @param ts4 the 4th List
     * @param ts5 the 5th List
     * @param ts6 the 6th List
     * @param ts7 the 7th List
     * @param ts8 the 8th List

     * @param <T1> right component type of the 1st List
     * @param <T2> right component type of the 2nd List
     * @param <T3> right component type of the 3rd List
     * @param <T4> right component type of the 4th List
     * @param <T5> right component type of the 5th List
     * @param <T6> right component type of the 6th List
     * @param <T7> right component type of the 7th List
     * @param <T8> right component type of the 8th List
     * @return a new {@code For}-comprehension of arity 8
     *
     * @deprecated to be replaced with revised JDK13-compliant API
     */
    @Deprecated
    public static <T1, T2, T3, T4, T5, T6, T7, T8> For8List<T1, T2, T3, T4, T5, T6, T7, T8> For(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4, List<T5> ts5, List<T6> ts6, List<T7> ts7, List<T8> ts8) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        Objects.requireNonNull(ts8, "ts8 is null");
        return new For8List<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7, ts8);
    }

     /**
      * For-comprehension with one Iterable.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For1<T1> {

         private final Iterable<T1> ts1;

         private For1(Iterable<T1> ts1) {
             this.ts1 = ts1;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Iterable.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Iterator} elements
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Iterator<R> yield(Function<? super T1, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return Iterator.ofAll(ts1).map(f);
         }

         /**
          * A shortcut for {@code yield(Function.identity())}.
          *
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public Iterator<T1> yield() {
             return this.yield(Function.identity());
         }
     }

     /**
      * For-comprehension with two Iterables.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For2<T1, T2> {

         private final Iterable<T1> ts1;
         private final Iterable<T2> ts2;

         private For2(Iterable<T1> ts1, Iterable<T2> ts2) {
             this.ts1 = ts1;
             this.ts2 = ts2;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Iterables.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Iterator} elements
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Iterator<R> yield(BiFunction<? super T1, ? super T2, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 Iterator.ofAll(ts1).flatMap(t1 ->
                 Iterator.ofAll(ts2).map(t2 -> f.apply(t1, t2)));
         }

     }

     /**
      * For-comprehension with three Iterables.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For3<T1, T2, T3> {

         private final Iterable<T1> ts1;
         private final Iterable<T2> ts2;
         private final Iterable<T3> ts3;

         private For3(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Iterables.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Iterator} elements
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Iterator<R> yield(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 Iterator.ofAll(ts1).flatMap(t1 ->
                 Iterator.ofAll(ts2).flatMap(t2 ->
                 Iterator.ofAll(ts3).map(t3 -> f.apply(t1, t2, t3))));
         }

     }

     /**
      * For-comprehension with 4 Iterables.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For4<T1, T2, T3, T4> {

         private final Iterable<T1> ts1;
         private final Iterable<T2> ts2;
         private final Iterable<T3> ts3;
         private final Iterable<T4> ts4;

         private For4(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Iterables.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Iterator} elements
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Iterator<R> yield(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 Iterator.ofAll(ts1).flatMap(t1 ->
                 Iterator.ofAll(ts2).flatMap(t2 ->
                 Iterator.ofAll(ts3).flatMap(t3 ->
                 Iterator.ofAll(ts4).map(t4 -> f.apply(t1, t2, t3, t4)))));
         }

     }

     /**
      * For-comprehension with 5 Iterables.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For5<T1, T2, T3, T4, T5> {

         private final Iterable<T1> ts1;
         private final Iterable<T2> ts2;
         private final Iterable<T3> ts3;
         private final Iterable<T4> ts4;
         private final Iterable<T5> ts5;

         private For5(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Iterables.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Iterator} elements
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Iterator<R> yield(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 Iterator.ofAll(ts1).flatMap(t1 ->
                 Iterator.ofAll(ts2).flatMap(t2 ->
                 Iterator.ofAll(ts3).flatMap(t3 ->
                 Iterator.ofAll(ts4).flatMap(t4 ->
                 Iterator.ofAll(ts5).map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
         }

     }

     /**
      * For-comprehension with 6 Iterables.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For6<T1, T2, T3, T4, T5, T6> {

         private final Iterable<T1> ts1;
         private final Iterable<T2> ts2;
         private final Iterable<T3> ts3;
         private final Iterable<T4> ts4;
         private final Iterable<T5> ts5;
         private final Iterable<T6> ts6;

         private For6(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Iterables.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Iterator} elements
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Iterator<R> yield(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 Iterator.ofAll(ts1).flatMap(t1 ->
                 Iterator.ofAll(ts2).flatMap(t2 ->
                 Iterator.ofAll(ts3).flatMap(t3 ->
                 Iterator.ofAll(ts4).flatMap(t4 ->
                 Iterator.ofAll(ts5).flatMap(t5 ->
                 Iterator.ofAll(ts6).map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
         }

     }

     /**
      * For-comprehension with 7 Iterables.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For7<T1, T2, T3, T4, T5, T6, T7> {

         private final Iterable<T1> ts1;
         private final Iterable<T2> ts2;
         private final Iterable<T3> ts3;
         private final Iterable<T4> ts4;
         private final Iterable<T5> ts5;
         private final Iterable<T6> ts6;
         private final Iterable<T7> ts7;

         private For7(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Iterables.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Iterator} elements
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Iterator<R> yield(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 Iterator.ofAll(ts1).flatMap(t1 ->
                 Iterator.ofAll(ts2).flatMap(t2 ->
                 Iterator.ofAll(ts3).flatMap(t3 ->
                 Iterator.ofAll(ts4).flatMap(t4 ->
                 Iterator.ofAll(ts5).flatMap(t5 ->
                 Iterator.ofAll(ts6).flatMap(t6 ->
                 Iterator.ofAll(ts7).map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
         }

     }

     /**
      * For-comprehension with 8 Iterables.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For8<T1, T2, T3, T4, T5, T6, T7, T8> {

         private final Iterable<T1> ts1;
         private final Iterable<T2> ts2;
         private final Iterable<T3> ts3;
         private final Iterable<T4> ts4;
         private final Iterable<T5> ts5;
         private final Iterable<T6> ts6;
         private final Iterable<T7> ts7;
         private final Iterable<T8> ts8;

         private For8(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7, Iterable<T8> ts8) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
             this.ts8 = ts8;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Iterables.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Iterator} elements
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Iterator<R> yield(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 Iterator.ofAll(ts1).flatMap(t1 ->
                 Iterator.ofAll(ts2).flatMap(t2 ->
                 Iterator.ofAll(ts3).flatMap(t3 ->
                 Iterator.ofAll(ts4).flatMap(t4 ->
                 Iterator.ofAll(ts5).flatMap(t5 ->
                 Iterator.ofAll(ts6).flatMap(t6 ->
                 Iterator.ofAll(ts7).flatMap(t7 ->
                 Iterator.ofAll(ts8).map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
         }

     }

     /**
      * For-comprehension with one Option.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For1Option<T1> {

         private final Option<T1> ts1;

         private For1Option(Option<T1> ts1) {
             this.ts1 = ts1;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Option.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Option} elements
          * @return an {@code Option} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Option<R> yield(Function<? super T1, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return ts1.map(f);
         }

         /**
          * A shortcut for {@code yield(Function.identity())}.
          *
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public Option<T1> yield() {
             return this.yield(Function.identity());
         }
     }

     /**
      * For-comprehension with two Options.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For2Option<T1, T2> {

         private final Option<T1> ts1;
         private final Option<T2> ts2;

         private For2Option(Option<T1> ts1, Option<T2> ts2) {
             this.ts1 = ts1;
             this.ts2 = ts2;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Options.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Option} elements
          * @return an {@code Option} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Option<R> yield(BiFunction<? super T1, ? super T2, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.map(t2 -> f.apply(t1, t2)));
         }

     }

     /**
      * For-comprehension with three Options.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For3Option<T1, T2, T3> {

         private final Option<T1> ts1;
         private final Option<T2> ts2;
         private final Option<T3> ts3;

         private For3Option(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Options.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Option} elements
          * @return an {@code Option} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Option<R> yield(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.map(t3 -> f.apply(t1, t2, t3))));
         }

     }

     /**
      * For-comprehension with 4 Options.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For4Option<T1, T2, T3, T4> {

         private final Option<T1> ts1;
         private final Option<T2> ts2;
         private final Option<T3> ts3;
         private final Option<T4> ts4;

         private For4Option(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Options.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Option} elements
          * @return an {@code Option} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Option<R> yield(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.map(t4 -> f.apply(t1, t2, t3, t4)))));
         }

     }

     /**
      * For-comprehension with 5 Options.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For5Option<T1, T2, T3, T4, T5> {

         private final Option<T1> ts1;
         private final Option<T2> ts2;
         private final Option<T3> ts3;
         private final Option<T4> ts4;
         private final Option<T5> ts5;

         private For5Option(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4, Option<T5> ts5) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Options.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Option} elements
          * @return an {@code Option} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Option<R> yield(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
         }

     }

     /**
      * For-comprehension with 6 Options.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For6Option<T1, T2, T3, T4, T5, T6> {

         private final Option<T1> ts1;
         private final Option<T2> ts2;
         private final Option<T3> ts3;
         private final Option<T4> ts4;
         private final Option<T5> ts5;
         private final Option<T6> ts6;

         private For6Option(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4, Option<T5> ts5, Option<T6> ts6) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Options.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Option} elements
          * @return an {@code Option} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Option<R> yield(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
         }

     }

     /**
      * For-comprehension with 7 Options.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For7Option<T1, T2, T3, T4, T5, T6, T7> {

         private final Option<T1> ts1;
         private final Option<T2> ts2;
         private final Option<T3> ts3;
         private final Option<T4> ts4;
         private final Option<T5> ts5;
         private final Option<T6> ts6;
         private final Option<T7> ts7;

         private For7Option(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4, Option<T5> ts5, Option<T6> ts6, Option<T7> ts7) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Options.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Option} elements
          * @return an {@code Option} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Option<R> yield(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
         }

     }

     /**
      * For-comprehension with 8 Options.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For8Option<T1, T2, T3, T4, T5, T6, T7, T8> {

         private final Option<T1> ts1;
         private final Option<T2> ts2;
         private final Option<T3> ts3;
         private final Option<T4> ts4;
         private final Option<T5> ts5;
         private final Option<T6> ts6;
         private final Option<T7> ts7;
         private final Option<T8> ts8;

         private For8Option(Option<T1> ts1, Option<T2> ts2, Option<T3> ts3, Option<T4> ts4, Option<T5> ts5, Option<T6> ts6, Option<T7> ts7, Option<T8> ts8) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
             this.ts8 = ts8;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Options.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Option} elements
          * @return an {@code Option} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Option<R> yield(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.flatMap(t7 ->
                 ts8.map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
         }

     }

     /**
      * For-comprehension with one Future.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For1Future<T1> {

         private final Future<T1> ts1;

         private For1Future(Future<T1> ts1) {
             this.ts1 = ts1;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Future.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Future} elements
          * @return an {@code Future} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Future<R> yield(Function<? super T1, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return ts1.map(f);
         }

         /**
          * A shortcut for {@code yield(Function.identity())}.
          *
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public Future<T1> yield() {
             return this.yield(Function.identity());
         }
     }

     /**
      * For-comprehension with two Futures.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For2Future<T1, T2> {

         private final Future<T1> ts1;
         private final Future<T2> ts2;

         private For2Future(Future<T1> ts1, Future<T2> ts2) {
             this.ts1 = ts1;
             this.ts2 = ts2;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Futures.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Future} elements
          * @return an {@code Future} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Future<R> yield(BiFunction<? super T1, ? super T2, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.map(t2 -> f.apply(t1, t2)));
         }

     }

     /**
      * For-comprehension with three Futures.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For3Future<T1, T2, T3> {

         private final Future<T1> ts1;
         private final Future<T2> ts2;
         private final Future<T3> ts3;

         private For3Future(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Futures.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Future} elements
          * @return an {@code Future} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Future<R> yield(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.map(t3 -> f.apply(t1, t2, t3))));
         }

     }

     /**
      * For-comprehension with 4 Futures.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For4Future<T1, T2, T3, T4> {

         private final Future<T1> ts1;
         private final Future<T2> ts2;
         private final Future<T3> ts3;
         private final Future<T4> ts4;

         private For4Future(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Futures.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Future} elements
          * @return an {@code Future} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Future<R> yield(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.map(t4 -> f.apply(t1, t2, t3, t4)))));
         }

     }

     /**
      * For-comprehension with 5 Futures.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For5Future<T1, T2, T3, T4, T5> {

         private final Future<T1> ts1;
         private final Future<T2> ts2;
         private final Future<T3> ts3;
         private final Future<T4> ts4;
         private final Future<T5> ts5;

         private For5Future(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4, Future<T5> ts5) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Futures.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Future} elements
          * @return an {@code Future} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Future<R> yield(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
         }

     }

     /**
      * For-comprehension with 6 Futures.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For6Future<T1, T2, T3, T4, T5, T6> {

         private final Future<T1> ts1;
         private final Future<T2> ts2;
         private final Future<T3> ts3;
         private final Future<T4> ts4;
         private final Future<T5> ts5;
         private final Future<T6> ts6;

         private For6Future(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4, Future<T5> ts5, Future<T6> ts6) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Futures.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Future} elements
          * @return an {@code Future} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Future<R> yield(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
         }

     }

     /**
      * For-comprehension with 7 Futures.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For7Future<T1, T2, T3, T4, T5, T6, T7> {

         private final Future<T1> ts1;
         private final Future<T2> ts2;
         private final Future<T3> ts3;
         private final Future<T4> ts4;
         private final Future<T5> ts5;
         private final Future<T6> ts6;
         private final Future<T7> ts7;

         private For7Future(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4, Future<T5> ts5, Future<T6> ts6, Future<T7> ts7) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Futures.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Future} elements
          * @return an {@code Future} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Future<R> yield(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
         }

     }

     /**
      * For-comprehension with 8 Futures.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For8Future<T1, T2, T3, T4, T5, T6, T7, T8> {

         private final Future<T1> ts1;
         private final Future<T2> ts2;
         private final Future<T3> ts3;
         private final Future<T4> ts4;
         private final Future<T5> ts5;
         private final Future<T6> ts6;
         private final Future<T7> ts7;
         private final Future<T8> ts8;

         private For8Future(Future<T1> ts1, Future<T2> ts2, Future<T3> ts3, Future<T4> ts4, Future<T5> ts5, Future<T6> ts6, Future<T7> ts7, Future<T8> ts8) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
             this.ts8 = ts8;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Futures.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Future} elements
          * @return an {@code Future} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Future<R> yield(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.flatMap(t7 ->
                 ts8.map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
         }

     }

     /**
      * For-comprehension with one Try.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For1Try<T1> {

         private final Try<T1> ts1;

         private For1Try(Try<T1> ts1) {
             this.ts1 = ts1;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Try.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Try} elements
          * @return an {@code Try} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Try<R> yield(Function<? super T1, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return ts1.map(f);
         }

         /**
          * A shortcut for {@code yield(Function.identity())}.
          *
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public Try<T1> yield() {
             return this.yield(Function.identity());
         }
     }

     /**
      * For-comprehension with two Trys.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For2Try<T1, T2> {

         private final Try<T1> ts1;
         private final Try<T2> ts2;

         private For2Try(Try<T1> ts1, Try<T2> ts2) {
             this.ts1 = ts1;
             this.ts2 = ts2;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Trys.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Try} elements
          * @return an {@code Try} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Try<R> yield(BiFunction<? super T1, ? super T2, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.map(t2 -> f.apply(t1, t2)));
         }

     }

     /**
      * For-comprehension with three Trys.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For3Try<T1, T2, T3> {

         private final Try<T1> ts1;
         private final Try<T2> ts2;
         private final Try<T3> ts3;

         private For3Try(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Trys.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Try} elements
          * @return an {@code Try} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Try<R> yield(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.map(t3 -> f.apply(t1, t2, t3))));
         }

     }

     /**
      * For-comprehension with 4 Trys.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For4Try<T1, T2, T3, T4> {

         private final Try<T1> ts1;
         private final Try<T2> ts2;
         private final Try<T3> ts3;
         private final Try<T4> ts4;

         private For4Try(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Trys.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Try} elements
          * @return an {@code Try} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Try<R> yield(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.map(t4 -> f.apply(t1, t2, t3, t4)))));
         }

     }

     /**
      * For-comprehension with 5 Trys.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For5Try<T1, T2, T3, T4, T5> {

         private final Try<T1> ts1;
         private final Try<T2> ts2;
         private final Try<T3> ts3;
         private final Try<T4> ts4;
         private final Try<T5> ts5;

         private For5Try(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4, Try<T5> ts5) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Trys.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Try} elements
          * @return an {@code Try} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Try<R> yield(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
         }

     }

     /**
      * For-comprehension with 6 Trys.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For6Try<T1, T2, T3, T4, T5, T6> {

         private final Try<T1> ts1;
         private final Try<T2> ts2;
         private final Try<T3> ts3;
         private final Try<T4> ts4;
         private final Try<T5> ts5;
         private final Try<T6> ts6;

         private For6Try(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4, Try<T5> ts5, Try<T6> ts6) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Trys.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Try} elements
          * @return an {@code Try} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Try<R> yield(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
         }

     }

     /**
      * For-comprehension with 7 Trys.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For7Try<T1, T2, T3, T4, T5, T6, T7> {

         private final Try<T1> ts1;
         private final Try<T2> ts2;
         private final Try<T3> ts3;
         private final Try<T4> ts4;
         private final Try<T5> ts5;
         private final Try<T6> ts6;
         private final Try<T7> ts7;

         private For7Try(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4, Try<T5> ts5, Try<T6> ts6, Try<T7> ts7) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Trys.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Try} elements
          * @return an {@code Try} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Try<R> yield(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
         }

     }

     /**
      * For-comprehension with 8 Trys.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For8Try<T1, T2, T3, T4, T5, T6, T7, T8> {

         private final Try<T1> ts1;
         private final Try<T2> ts2;
         private final Try<T3> ts3;
         private final Try<T4> ts4;
         private final Try<T5> ts5;
         private final Try<T6> ts6;
         private final Try<T7> ts7;
         private final Try<T8> ts8;

         private For8Try(Try<T1> ts1, Try<T2> ts2, Try<T3> ts3, Try<T4> ts4, Try<T5> ts5, Try<T6> ts6, Try<T7> ts7, Try<T8> ts8) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
             this.ts8 = ts8;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Trys.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Try} elements
          * @return an {@code Try} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Try<R> yield(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.flatMap(t7 ->
                 ts8.map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
         }

     }

     /**
      * For-comprehension with one Either.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For1Either<L, T1> {

         private final Either<L, T1> ts1;

         private For1Either(Either<L, T1> ts1) {
             this.ts1 = ts1;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Either.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Either} elements
          * @return an {@code Either} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Either<L, R> yield(Function<? super T1, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return ts1.map(f);
         }

         /**
          * A shortcut for {@code yield(Function.identity())}.
          *
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public Either<L, T1> yield() {
             return this.yield(Function.identity());
         }
     }

     /**
      * For-comprehension with two Eithers.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For2Either<L, T1, T2> {

         private final Either<L, T1> ts1;
         private final Either<L, T2> ts2;

         private For2Either(Either<L, T1> ts1, Either<L, T2> ts2) {
             this.ts1 = ts1;
             this.ts2 = ts2;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Eithers.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Either} elements
          * @return an {@code Either} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Either<L, R> yield(BiFunction<? super T1, ? super T2, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.map(t2 -> f.apply(t1, t2)));
         }

     }

     /**
      * For-comprehension with three Eithers.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For3Either<L, T1, T2, T3> {

         private final Either<L, T1> ts1;
         private final Either<L, T2> ts2;
         private final Either<L, T3> ts3;

         private For3Either(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Eithers.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Either} elements
          * @return an {@code Either} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Either<L, R> yield(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.map(t3 -> f.apply(t1, t2, t3))));
         }

     }

     /**
      * For-comprehension with 4 Eithers.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For4Either<L, T1, T2, T3, T4> {

         private final Either<L, T1> ts1;
         private final Either<L, T2> ts2;
         private final Either<L, T3> ts3;
         private final Either<L, T4> ts4;

         private For4Either(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Eithers.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Either} elements
          * @return an {@code Either} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Either<L, R> yield(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.map(t4 -> f.apply(t1, t2, t3, t4)))));
         }

     }

     /**
      * For-comprehension with 5 Eithers.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For5Either<L, T1, T2, T3, T4, T5> {

         private final Either<L, T1> ts1;
         private final Either<L, T2> ts2;
         private final Either<L, T3> ts3;
         private final Either<L, T4> ts4;
         private final Either<L, T5> ts5;

         private For5Either(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4, Either<L, T5> ts5) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Eithers.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Either} elements
          * @return an {@code Either} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Either<L, R> yield(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
         }

     }

     /**
      * For-comprehension with 6 Eithers.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For6Either<L, T1, T2, T3, T4, T5, T6> {

         private final Either<L, T1> ts1;
         private final Either<L, T2> ts2;
         private final Either<L, T3> ts3;
         private final Either<L, T4> ts4;
         private final Either<L, T5> ts5;
         private final Either<L, T6> ts6;

         private For6Either(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4, Either<L, T5> ts5, Either<L, T6> ts6) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Eithers.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Either} elements
          * @return an {@code Either} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Either<L, R> yield(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
         }

     }

     /**
      * For-comprehension with 7 Eithers.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For7Either<L, T1, T2, T3, T4, T5, T6, T7> {

         private final Either<L, T1> ts1;
         private final Either<L, T2> ts2;
         private final Either<L, T3> ts3;
         private final Either<L, T4> ts4;
         private final Either<L, T5> ts5;
         private final Either<L, T6> ts6;
         private final Either<L, T7> ts7;

         private For7Either(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4, Either<L, T5> ts5, Either<L, T6> ts6, Either<L, T7> ts7) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Eithers.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Either} elements
          * @return an {@code Either} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Either<L, R> yield(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
         }

     }

     /**
      * For-comprehension with 8 Eithers.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For8Either<L, T1, T2, T3, T4, T5, T6, T7, T8> {

         private final Either<L, T1> ts1;
         private final Either<L, T2> ts2;
         private final Either<L, T3> ts3;
         private final Either<L, T4> ts4;
         private final Either<L, T5> ts5;
         private final Either<L, T6> ts6;
         private final Either<L, T7> ts7;
         private final Either<L, T8> ts8;

         private For8Either(Either<L, T1> ts1, Either<L, T2> ts2, Either<L, T3> ts3, Either<L, T4> ts4, Either<L, T5> ts5, Either<L, T6> ts6, Either<L, T7> ts7, Either<L, T8> ts8) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
             this.ts8 = ts8;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Eithers.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code Either} elements
          * @return an {@code Either} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> Either<L, R> yield(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.flatMap(t7 ->
                 ts8.map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
         }

     }

     /**
      * For-comprehension with one List.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For1List<T1> {

         private final List<T1> ts1;

         private For1List(List<T1> ts1) {
             this.ts1 = ts1;
         }

         /**
          * Yields a result for elements of the cross product of the underlying List.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code List} elements
          * @return an {@code List} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> List<R> yield(Function<? super T1, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return ts1.map(f);
         }

         /**
          * A shortcut for {@code yield(Function.identity())}.
          *
          * @return an {@code Iterator} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public List<T1> yield() {
             return this.yield(Function.identity());
         }
     }

     /**
      * For-comprehension with two Lists.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For2List<T1, T2> {

         private final List<T1> ts1;
         private final List<T2> ts2;

         private For2List(List<T1> ts1, List<T2> ts2) {
             this.ts1 = ts1;
             this.ts2 = ts2;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Lists.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code List} elements
          * @return an {@code List} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> List<R> yield(BiFunction<? super T1, ? super T2, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.map(t2 -> f.apply(t1, t2)));
         }

     }

     /**
      * For-comprehension with three Lists.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For3List<T1, T2, T3> {

         private final List<T1> ts1;
         private final List<T2> ts2;
         private final List<T3> ts3;

         private For3List(List<T1> ts1, List<T2> ts2, List<T3> ts3) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Lists.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code List} elements
          * @return an {@code List} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> List<R> yield(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.map(t3 -> f.apply(t1, t2, t3))));
         }

     }

     /**
      * For-comprehension with 4 Lists.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For4List<T1, T2, T3, T4> {

         private final List<T1> ts1;
         private final List<T2> ts2;
         private final List<T3> ts3;
         private final List<T4> ts4;

         private For4List(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Lists.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code List} elements
          * @return an {@code List} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> List<R> yield(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.map(t4 -> f.apply(t1, t2, t3, t4)))));
         }

     }

     /**
      * For-comprehension with 5 Lists.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For5List<T1, T2, T3, T4, T5> {

         private final List<T1> ts1;
         private final List<T2> ts2;
         private final List<T3> ts3;
         private final List<T4> ts4;
         private final List<T5> ts5;

         private For5List(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4, List<T5> ts5) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Lists.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code List} elements
          * @return an {@code List} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> List<R> yield(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
         }

     }

     /**
      * For-comprehension with 6 Lists.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For6List<T1, T2, T3, T4, T5, T6> {

         private final List<T1> ts1;
         private final List<T2> ts2;
         private final List<T3> ts3;
         private final List<T4> ts4;
         private final List<T5> ts5;
         private final List<T6> ts6;

         private For6List(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4, List<T5> ts5, List<T6> ts6) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Lists.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code List} elements
          * @return an {@code List} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> List<R> yield(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
         }

     }

     /**
      * For-comprehension with 7 Lists.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For7List<T1, T2, T3, T4, T5, T6, T7> {

         private final List<T1> ts1;
         private final List<T2> ts2;
         private final List<T3> ts3;
         private final List<T4> ts4;
         private final List<T5> ts5;
         private final List<T6> ts6;
         private final List<T7> ts7;

         private For7List(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4, List<T5> ts5, List<T6> ts6, List<T7> ts7) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Lists.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code List} elements
          * @return an {@code List} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> List<R> yield(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
         }

     }

     /**
      * For-comprehension with 8 Lists.
      *
      * @deprecated to be replaced with revised JDK13-compliant API
      */
     @Deprecated
     public static class For8List<T1, T2, T3, T4, T5, T6, T7, T8> {

         private final List<T1> ts1;
         private final List<T2> ts2;
         private final List<T3> ts3;
         private final List<T4> ts4;
         private final List<T5> ts5;
         private final List<T6> ts6;
         private final List<T7> ts7;
         private final List<T8> ts8;

         private For8List(List<T1> ts1, List<T2> ts2, List<T3> ts3, List<T4> ts4, List<T5> ts5, List<T6> ts6, List<T7> ts7, List<T8> ts8) {
             this.ts1 = ts1;
             this.ts2 = ts2;
             this.ts3 = ts3;
             this.ts4 = ts4;
             this.ts5 = ts5;
             this.ts6 = ts6;
             this.ts7 = ts7;
             this.ts8 = ts8;
         }

         /**
          * Yields a result for elements of the cross product of the underlying Lists.
          *
          * @param f a function that maps an element of the cross product to a result
          * @param <R> type of the resulting {@code List} elements
          * @return an {@code List} of mapped results
          *
          * @deprecated to be replaced with revised JDK13-compliant API
          */
         @Deprecated
         public <R> List<R> yield(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
             Objects.requireNonNull(f, "f is null");
             return
                 ts1.flatMap(t1 ->
                 ts2.flatMap(t2 ->
                 ts3.flatMap(t3 ->
                 ts4.flatMap(t4 ->
                 ts5.flatMap(t5 ->
                 ts6.flatMap(t6 ->
                 ts7.flatMap(t7 ->
                 ts8.map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
         }

     }

    //
    // Structural Pattern Matching
    //

    // -- static Match API

    /**
     * Entry point of the match API.
     *
     * @param value a value to be matched
     * @param <T> type of the value
     * @return a new {@code Match} instance
     */
    public static <T> Match<T> Match(T value) {
        return new Match<>(value);
    }

    // -- static Case API

    // - Pattern0

    public static <T, R> Case<T, R> Case(Pattern0<T> pattern, Function<? super T, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case0<>(pattern, f);
    }

    public static <T, R> Case<T, R> Case(Pattern0<T> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case0<>(pattern, ignored -> supplier.get());
    }

    public static <T, R> Case<T, R> Case(Pattern0<T> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case0<>(pattern, ignored -> retVal);
    }

    // - Pattern1

    public static <T, T1, R> Case<T, R> Case(Pattern1<T, T1> pattern, Function<? super T1, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case1<>(pattern, f);
    }

    public static <T, T1, R> Case<T, R> Case(Pattern1<T, T1> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case1<>(pattern, _1 -> supplier.get());
    }

    public static <T, T1, R> Case<T, R> Case(Pattern1<T, T1> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case1<>(pattern, _1 -> retVal);
    }

    // - Pattern2

    public static <T, T1, T2, R> Case<T, R> Case(Pattern2<T, T1, T2> pattern, BiFunction<? super T1, ? super T2, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case2<>(pattern, f);
    }

    public static <T, T1, T2, R> Case<T, R> Case(Pattern2<T, T1, T2> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case2<>(pattern, (_1, _2) -> supplier.get());
    }

    public static <T, T1, T2, R> Case<T, R> Case(Pattern2<T, T1, T2> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case2<>(pattern, (_1, _2) -> retVal);
    }

    // - Pattern3

    public static <T, T1, T2, T3, R> Case<T, R> Case(Pattern3<T, T1, T2, T3> pattern, Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case3<>(pattern, f);
    }

    public static <T, T1, T2, T3, R> Case<T, R> Case(Pattern3<T, T1, T2, T3> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case3<>(pattern, (_1, _2, _3) -> supplier.get());
    }

    public static <T, T1, T2, T3, R> Case<T, R> Case(Pattern3<T, T1, T2, T3> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case3<>(pattern, (_1, _2, _3) -> retVal);
    }

    // - Pattern4

    public static <T, T1, T2, T3, T4, R> Case<T, R> Case(Pattern4<T, T1, T2, T3, T4> pattern, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case4<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, R> Case<T, R> Case(Pattern4<T, T1, T2, T3, T4> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case4<>(pattern, (_1, _2, _3, _4) -> supplier.get());
    }

    public static <T, T1, T2, T3, T4, R> Case<T, R> Case(Pattern4<T, T1, T2, T3, T4> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case4<>(pattern, (_1, _2, _3, _4) -> retVal);
    }

    // - Pattern5

    public static <T, T1, T2, T3, T4, T5, R> Case<T, R> Case(Pattern5<T, T1, T2, T3, T4, T5> pattern, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case5<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, T5, R> Case<T, R> Case(Pattern5<T, T1, T2, T3, T4, T5> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case5<>(pattern, (_1, _2, _3, _4, _5) -> supplier.get());
    }

    public static <T, T1, T2, T3, T4, T5, R> Case<T, R> Case(Pattern5<T, T1, T2, T3, T4, T5> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case5<>(pattern, (_1, _2, _3, _4, _5) -> retVal);
    }

    // - Pattern6

    public static <T, T1, T2, T3, T4, T5, T6, R> Case<T, R> Case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case6<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, T5, T6, R> Case<T, R> Case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case6<>(pattern, (_1, _2, _3, _4, _5, _6) -> supplier.get());
    }

    public static <T, T1, T2, T3, T4, T5, T6, R> Case<T, R> Case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case6<>(pattern, (_1, _2, _3, _4, _5, _6) -> retVal);
    }

    // - Pattern7

    public static <T, T1, T2, T3, T4, T5, T6, T7, R> Case<T, R> Case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case7<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, T5, T6, T7, R> Case<T, R> Case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case7<>(pattern, (_1, _2, _3, _4, _5, _6, _7) -> supplier.get());
    }

    public static <T, T1, T2, T3, T4, T5, T6, T7, R> Case<T, R> Case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case7<>(pattern, (_1, _2, _3, _4, _5, _6, _7) -> retVal);
    }

    // - Pattern8

    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, R> Case<T, R> Case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case8<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, R> Case<T, R> Case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case8<>(pattern, (_1, _2, _3, _4, _5, _6, _7, _8) -> supplier.get());
    }

    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, R> Case<T, R> Case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case8<>(pattern, (_1, _2, _3, _4, _5, _6, _7, _8) -> retVal);
    }

    // PRE-DEFINED PATTERNS

    // 1) Atomic patterns $(), $(value), $(predicate)

    /**
     * Wildcard pattern, matches any value.
     *
     * @param <T> injected type of the underlying value
     * @return a new {@code Pattern0} instance
     */
    public static <T> Pattern0<T> $() {
        return Pattern0.any();
    }

    /**
     * Value pattern, checks for equality.
     *
     * @param <T>       type of the prototype
     * @param prototype the value that should be equal to the underlying object
     * @return a new {@code Pattern0} instance
     */
    public static <T> Pattern0<T> $(T prototype) {
        return new Pattern0<T>() {

            private static final long serialVersionUID = 1L;

            @Override
            public T apply(T obj) {
                return obj;
            }

            @Override
            public boolean isDefinedAt(T obj) {
                if (obj == prototype) {
                    return true;
                } else if (prototype != null && prototype.getClass().isInstance(obj)) {
                    return Objects.equals(obj, prototype);
                } else {
                    return false;
                }
            }
        };
    }

    /**
     * Guard pattern, checks if a predicate is satisfied.
     * <p>
     * This method is intended to be used with lambdas and method references, for example:
     *
     * <pre><code>
     * String evenOrOdd(int num) {
     *     return Match(num).of(
     *             Case($(i -&gt; i % 2 == 0), "even"),
     *             Case($(this::isOdd), "odd")
     *     );
     * }
     *
     * boolean isOdd(int i) {
     *     return i % 2 == 1;
     * }
     * </code></pre>
     *
     * It is also valid to pass {@code Predicate} instances:
     *
     * <pre><code>
     * Predicate&lt;Integer&gt; isOdd = i -&gt; i % 2 == 1;
     *
     * Match(num).of(
     *         Case($(i -&gt; i % 2 == 0), "even"),
     *         Case($(isOdd), "odd")
     * );
     * </code></pre>
     *
     * <strong>Note:</strong> Please take care when matching {@code Predicate} instances. In general,
     * <a href="http://cstheory.stackexchange.com/a/14152" target="_blank">function equality</a>
     * is an undecidable problem in computer science. In Vavr we are only able to check,
     * if two functions are the same instance.
     * <p>
     * However, this code will fail:
     *
     * <pre><code>
     * Predicate&lt;Integer&gt; p = i -&gt; true;
     * Match(p).of(
     *     Case($(p), 1) // WRONG! It calls $(Predicate)
     * );
     * </code></pre>
     *
     * Instead we have to use {@link Predicates#is(Object)}:
     *
     * <pre><code>
     * Predicate&lt;Integer&gt; p = i -&gt; true;
     * Match(p).of(
     *     Case($(is(p)), 1) // CORRECT! It calls $(T)
     * );
     * </code></pre>
     *
     * @param <T>       type of the prototype
     * @param predicate the predicate that tests a given value
     * @return a new {@code Pattern0} instance
     */
    public static <T> Pattern0<T> $(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return new Pattern0<T>() {

            private static final long serialVersionUID = 1L;

            @Override
            public T apply(T obj) {
                return obj;
            }

            @Override
            public boolean isDefinedAt(T obj) {
                try {
                    return predicate.test(obj);
                } catch (ClassCastException x) {
                    return false;
                }
            }
        };
    }

    /**
     * Scala-like structural pattern matching for Java. Instances are obtained via {@link API#Match(Object)}.
     * @param <T> type of the object that is matched
     */
    public static final class Match<T> {

        private final T value;

        private Match(T value) {
            this.value = value;
        }

        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <R> R of(Case<? extends T, ? extends R>... cases) {
            Objects.requireNonNull(cases, "cases is null");
            for (Case<? extends T, ? extends R> _case : cases) {
                final Case<T, R> __case = (Case<T, R>) _case;
                if (__case.isDefinedAt(value)) {
                    return __case.apply(value);
                }
            }
            throw new MatchError(value);
        }

        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <R> Option<R> option(Case<? extends T, ? extends R>... cases) {
            Objects.requireNonNull(cases, "cases is null");
            for (Case<? extends T, ? extends R> _case : cases) {
                final Case<T, R> __case = (Case<T, R>) _case;
                if (__case.isDefinedAt(value)) {
                    return Option.some(__case.apply(value));
                }
            }
            return Option.none();
        }

        // -- CASES

        public interface Case<T, R> extends PartialFunction<T, R> {

            /**
             * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
             */
            long serialVersionUID = 1L;
        }

        public static final class Case0<T, R> implements Case<T, R> {

            private static final long serialVersionUID = 1L;

            private final Pattern0<T> pattern;
            private final Function<? super T, ? extends R> f;

            private Case0(Pattern0<T> pattern, Function<? super T, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public R apply(T obj) {
                return f.apply(pattern.apply(obj));
            }

            @Override
            public boolean isDefinedAt(T obj) {
                return pattern.isDefinedAt(obj);
            }
        }

        public static final class Case1<T, T1, R> implements Case<T, R> {

            private static final long serialVersionUID = 1L;

            private final Pattern1<T, T1> pattern;
            private final Function<? super T1, ? extends R> f;

            private Case1(Pattern1<T, T1> pattern, Function<? super T1, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public R apply(T obj) {
                return f.apply(pattern.apply(obj));
            }

            @Override
            public boolean isDefinedAt(T obj) {
                return pattern.isDefinedAt(obj);
            }
        }

        public static final class Case2<T, T1, T2, R> implements Case<T, R> {

            private static final long serialVersionUID = 1L;

            private final Pattern2<T, T1, T2> pattern;
            private final BiFunction<? super T1, ? super T2, ? extends R> f;

            private Case2(Pattern2<T, T1, T2> pattern, BiFunction<? super T1, ? super T2, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public R apply(T obj) {
                return pattern.apply(obj).apply(f);
            }

            @Override
            public boolean isDefinedAt(T obj) {
                return pattern.isDefinedAt(obj);
            }
        }

        public static final class Case3<T, T1, T2, T3, R> implements Case<T, R> {

            private static final long serialVersionUID = 1L;

            private final Pattern3<T, T1, T2, T3> pattern;
            private final Function3<? super T1, ? super T2, ? super T3, ? extends R> f;

            private Case3(Pattern3<T, T1, T2, T3> pattern, Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public R apply(T obj) {
                return pattern.apply(obj).apply(f);
            }

            @Override
            public boolean isDefinedAt(T obj) {
                return pattern.isDefinedAt(obj);
            }
        }

        public static final class Case4<T, T1, T2, T3, T4, R> implements Case<T, R> {

            private static final long serialVersionUID = 1L;

            private final Pattern4<T, T1, T2, T3, T4> pattern;
            private final Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f;

            private Case4(Pattern4<T, T1, T2, T3, T4> pattern, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public R apply(T obj) {
                return pattern.apply(obj).apply(f);
            }

            @Override
            public boolean isDefinedAt(T obj) {
                return pattern.isDefinedAt(obj);
            }
        }

        public static final class Case5<T, T1, T2, T3, T4, T5, R> implements Case<T, R> {

            private static final long serialVersionUID = 1L;

            private final Pattern5<T, T1, T2, T3, T4, T5> pattern;
            private final Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f;

            private Case5(Pattern5<T, T1, T2, T3, T4, T5> pattern, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public R apply(T obj) {
                return pattern.apply(obj).apply(f);
            }

            @Override
            public boolean isDefinedAt(T obj) {
                return pattern.isDefinedAt(obj);
            }
        }

        public static final class Case6<T, T1, T2, T3, T4, T5, T6, R> implements Case<T, R> {

            private static final long serialVersionUID = 1L;

            private final Pattern6<T, T1, T2, T3, T4, T5, T6> pattern;
            private final Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f;

            private Case6(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public R apply(T obj) {
                return pattern.apply(obj).apply(f);
            }

            @Override
            public boolean isDefinedAt(T obj) {
                return pattern.isDefinedAt(obj);
            }
        }

        public static final class Case7<T, T1, T2, T3, T4, T5, T6, T7, R> implements Case<T, R> {

            private static final long serialVersionUID = 1L;

            private final Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern;
            private final Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f;

            private Case7(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public R apply(T obj) {
                return pattern.apply(obj).apply(f);
            }

            @Override
            public boolean isDefinedAt(T obj) {
                return pattern.isDefinedAt(obj);
            }
        }

        public static final class Case8<T, T1, T2, T3, T4, T5, T6, T7, T8, R> implements Case<T, R> {

            private static final long serialVersionUID = 1L;

            private final Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern;
            private final Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f;

            private Case8(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public R apply(T obj) {
                return pattern.apply(obj).apply(f);
            }

            @Override
            public boolean isDefinedAt(T obj) {
                return pattern.isDefinedAt(obj);
            }
        }

        // -- PATTERNS

        /**
         * A Pattern is a partial {@link Function} in the sense that a function applications returns an
         * optional result of type {@code Option<R>}.
         *
         * @param <T> Class type that is matched by this pattern
         * @param <R> Type of the single or composite part this pattern decomposes
         */
        // javac needs fqn's here
        public interface Pattern<T, R> extends PartialFunction<T, R> {
        }

        // These can't be @FunctionalInterfaces because of ambiguities.
        // For benchmarks lambda vs. abstract class see http://www.oracle.com/technetwork/java/jvmls2013kuksen-2014088.pdf

        public static abstract class Pattern0<T> implements Pattern<T, T> {

            private static final long serialVersionUID = 1L;

            private static final Pattern0<Object> ANY = new Pattern0<Object>() {

                private static final long serialVersionUID = 1L;

                @Override
                public Object apply(Object obj) {
                    return obj;
                }

                @Override
                public boolean isDefinedAt(Object obj) {
                    return true;
                }
            };

            @SuppressWarnings("unchecked")
            public static <T> Pattern0<T> any() {
                return (Pattern0<T>) ANY;
            }

            // DEV-NOTE: We need the lower bound `Class<? super T>` instead of the more appropriate `Class<T>`
            //           because it allows us to create patterns for generic types, which would otherwise not be
            //           possible: `Pattern0<Some<String>> p = Pattern0.of(Some.class);`
            public static <T> Pattern0<T> of(Class<? super T> type) {
                return new Pattern0<T>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public T apply(T obj) {
                        return obj;
                    }

                    @Override
                    public boolean isDefinedAt(T obj) {
                        return type.isInstance(obj);
                    }
                };
            }
        }

        public static abstract class Pattern1<T, T1> implements Pattern<T, T1> {

            private static final long serialVersionUID = 1L;

            public static <T, T1 extends U1, U1> Pattern1<T, T1> of(Class<? super T> type, Pattern<T1, ?> p1, Function<T, Tuple1<U1>> unapply) {
                return new Pattern1<T, T1>() {

                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public T1 apply(T obj) {
                        return (T1) unapply.apply(obj)._1;
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isDefinedAt(T obj) {
                        if (type.isInstance(obj)) {
                            final Tuple1<U1> u = unapply.apply(obj);
                            return
                                    ((Pattern<U1, ?>) p1).isDefinedAt(u._1);
                        } else {
                            return false;
                        }
                    }
                };
            }
        }

        public static abstract class Pattern2<T, T1, T2> implements Pattern<T, Tuple2<T1, T2>> {

            private static final long serialVersionUID = 1L;

            public static <T, T1 extends U1, U1, T2 extends U2, U2> Pattern2<T, T1, T2> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Function<T, Tuple2<U1, U2>> unapply) {
                return new Pattern2<T, T1, T2>() {

                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public Tuple2<T1, T2> apply(T obj) {
                        return (Tuple2<T1, T2>) unapply.apply(obj);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isDefinedAt(T obj) {
                        if (type.isInstance(obj)) {
                            final Tuple2<U1, U2> u = unapply.apply(obj);
                            return
                                    ((Pattern<U1, ?>) p1).isDefinedAt(u._1) &&
                                    ((Pattern<U2, ?>) p2).isDefinedAt(u._2);
                        } else {
                            return false;
                        }
                    }
                };
            }
        }

        public static abstract class Pattern3<T, T1, T2, T3> implements Pattern<T, Tuple3<T1, T2, T3>> {

            private static final long serialVersionUID = 1L;

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3> Pattern3<T, T1, T2, T3> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Function<T, Tuple3<U1, U2, U3>> unapply) {
                return new Pattern3<T, T1, T2, T3>() {

                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public Tuple3<T1, T2, T3> apply(T obj) {
                        return (Tuple3<T1, T2, T3>) unapply.apply(obj);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isDefinedAt(T obj) {
                        if (type.isInstance(obj)) {
                            final Tuple3<U1, U2, U3> u = unapply.apply(obj);
                            return
                                    ((Pattern<U1, ?>) p1).isDefinedAt(u._1) &&
                                    ((Pattern<U2, ?>) p2).isDefinedAt(u._2) &&
                                    ((Pattern<U3, ?>) p3).isDefinedAt(u._3);
                        } else {
                            return false;
                        }
                    }
                };
            }
        }

        public static abstract class Pattern4<T, T1, T2, T3, T4> implements Pattern<T, Tuple4<T1, T2, T3, T4>> {

            private static final long serialVersionUID = 1L;

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4> Pattern4<T, T1, T2, T3, T4> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Function<T, Tuple4<U1, U2, U3, U4>> unapply) {
                return new Pattern4<T, T1, T2, T3, T4>() {

                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public Tuple4<T1, T2, T3, T4> apply(T obj) {
                        return (Tuple4<T1, T2, T3, T4>) unapply.apply(obj);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isDefinedAt(T obj) {
                        if (type.isInstance(obj)) {
                            final Tuple4<U1, U2, U3, U4> u = unapply.apply(obj);
                            return
                                    ((Pattern<U1, ?>) p1).isDefinedAt(u._1) &&
                                    ((Pattern<U2, ?>) p2).isDefinedAt(u._2) &&
                                    ((Pattern<U3, ?>) p3).isDefinedAt(u._3) &&
                                    ((Pattern<U4, ?>) p4).isDefinedAt(u._4);
                        } else {
                            return false;
                        }
                    }
                };
            }
        }

        public static abstract class Pattern5<T, T1, T2, T3, T4, T5> implements Pattern<T, Tuple5<T1, T2, T3, T4, T5>> {

            private static final long serialVersionUID = 1L;

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4, T5 extends U5, U5> Pattern5<T, T1, T2, T3, T4, T5> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Function<T, Tuple5<U1, U2, U3, U4, U5>> unapply) {
                return new Pattern5<T, T1, T2, T3, T4, T5>() {

                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public Tuple5<T1, T2, T3, T4, T5> apply(T obj) {
                        return (Tuple5<T1, T2, T3, T4, T5>) unapply.apply(obj);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isDefinedAt(T obj) {
                        if (type.isInstance(obj)) {
                            final Tuple5<U1, U2, U3, U4, U5> u = unapply.apply(obj);
                            return
                                    ((Pattern<U1, ?>) p1).isDefinedAt(u._1) &&
                                    ((Pattern<U2, ?>) p2).isDefinedAt(u._2) &&
                                    ((Pattern<U3, ?>) p3).isDefinedAt(u._3) &&
                                    ((Pattern<U4, ?>) p4).isDefinedAt(u._4) &&
                                    ((Pattern<U5, ?>) p5).isDefinedAt(u._5);
                        } else {
                            return false;
                        }
                    }
                };
            }
        }

        public static abstract class Pattern6<T, T1, T2, T3, T4, T5, T6> implements Pattern<T, Tuple6<T1, T2, T3, T4, T5, T6>> {

            private static final long serialVersionUID = 1L;

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4, T5 extends U5, U5, T6 extends U6, U6> Pattern6<T, T1, T2, T3, T4, T5, T6> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Pattern<T6, ?> p6, Function<T, Tuple6<U1, U2, U3, U4, U5, U6>> unapply) {
                return new Pattern6<T, T1, T2, T3, T4, T5, T6>() {

                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public Tuple6<T1, T2, T3, T4, T5, T6> apply(T obj) {
                        return (Tuple6<T1, T2, T3, T4, T5, T6>) unapply.apply(obj);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isDefinedAt(T obj) {
                        if (type.isInstance(obj)) {
                            final Tuple6<U1, U2, U3, U4, U5, U6> u = unapply.apply(obj);
                            return
                                    ((Pattern<U1, ?>) p1).isDefinedAt(u._1) &&
                                    ((Pattern<U2, ?>) p2).isDefinedAt(u._2) &&
                                    ((Pattern<U3, ?>) p3).isDefinedAt(u._3) &&
                                    ((Pattern<U4, ?>) p4).isDefinedAt(u._4) &&
                                    ((Pattern<U5, ?>) p5).isDefinedAt(u._5) &&
                                    ((Pattern<U6, ?>) p6).isDefinedAt(u._6);
                        } else {
                            return false;
                        }
                    }
                };
            }
        }

        public static abstract class Pattern7<T, T1, T2, T3, T4, T5, T6, T7> implements Pattern<T, Tuple7<T1, T2, T3, T4, T5, T6, T7>> {

            private static final long serialVersionUID = 1L;

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4, T5 extends U5, U5, T6 extends U6, U6, T7 extends U7, U7> Pattern7<T, T1, T2, T3, T4, T5, T6, T7> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Pattern<T6, ?> p6, Pattern<T7, ?> p7, Function<T, Tuple7<U1, U2, U3, U4, U5, U6, U7>> unapply) {
                return new Pattern7<T, T1, T2, T3, T4, T5, T6, T7>() {

                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public Tuple7<T1, T2, T3, T4, T5, T6, T7> apply(T obj) {
                        return (Tuple7<T1, T2, T3, T4, T5, T6, T7>) unapply.apply(obj);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isDefinedAt(T obj) {
                        if (type.isInstance(obj)) {
                            final Tuple7<U1, U2, U3, U4, U5, U6, U7> u = unapply.apply(obj);
                            return
                                    ((Pattern<U1, ?>) p1).isDefinedAt(u._1) &&
                                    ((Pattern<U2, ?>) p2).isDefinedAt(u._2) &&
                                    ((Pattern<U3, ?>) p3).isDefinedAt(u._3) &&
                                    ((Pattern<U4, ?>) p4).isDefinedAt(u._4) &&
                                    ((Pattern<U5, ?>) p5).isDefinedAt(u._5) &&
                                    ((Pattern<U6, ?>) p6).isDefinedAt(u._6) &&
                                    ((Pattern<U7, ?>) p7).isDefinedAt(u._7);
                        } else {
                            return false;
                        }
                    }
                };
            }
        }

        public static abstract class Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> implements Pattern<T, Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {

            private static final long serialVersionUID = 1L;

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4, T5 extends U5, U5, T6 extends U6, U6, T7 extends U7, U7, T8 extends U8, U8> Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Pattern<T6, ?> p6, Pattern<T7, ?> p7, Pattern<T8, ?> p8, Function<T, Tuple8<U1, U2, U3, U4, U5, U6, U7, U8>> unapply) {
                return new Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8>() {

                    private static final long serialVersionUID = 1L;

                    @SuppressWarnings("unchecked")
                    @Override
                    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> apply(T obj) {
                        return (Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>) unapply.apply(obj);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isDefinedAt(T obj) {
                        if (type.isInstance(obj)) {
                            final Tuple8<U1, U2, U3, U4, U5, U6, U7, U8> u = unapply.apply(obj);
                            return
                                    ((Pattern<U1, ?>) p1).isDefinedAt(u._1) &&
                                    ((Pattern<U2, ?>) p2).isDefinedAt(u._2) &&
                                    ((Pattern<U3, ?>) p3).isDefinedAt(u._3) &&
                                    ((Pattern<U4, ?>) p4).isDefinedAt(u._4) &&
                                    ((Pattern<U5, ?>) p5).isDefinedAt(u._5) &&
                                    ((Pattern<U6, ?>) p6).isDefinedAt(u._6) &&
                                    ((Pattern<U7, ?>) p7).isDefinedAt(u._7) &&
                                    ((Pattern<U8, ?>) p8).isDefinedAt(u._8);
                        } else {
                            return false;
                        }
                    }
                };
            }
        }
    }
}