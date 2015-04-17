/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.function.Consumer;
import javaslang.control.Try.CheckedConsumer;
import javaslang.control.Try.CheckedFunction;
import javaslang.control.Try.CheckedPredicate;

/**
 * Defines a CheckedMonad by generalizing the flatMap function.
 * <p>
 * All instances of the CheckedMonad interface should obey the three control laws:
 * <ul>
 *     <li><strong>Left identity:</strong> {@code unit(a).flatMap(f) ≡ f a}</li>
 *     <li><strong>Right identity:</strong> {@code m.flatMap(unit) ≡ m}</li>
 *     <li><strong>Associativity:</strong> {@code m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g)}</li>
 * </ul>
 * given
 * <ul>
 * <li>an object {@code m} of type {@code HigherKinded<T, M>}</li>
 * <li>an object {@code a} of type T</li>
 * <li>a constructor {@code unit} taking an {@code a} and producing an object of type {@code M}</li>
 * <li>a function {@code f: T → M}
 * </ul>
 *
 * To read further about monads in Java please refer to
 * <a href="http://java.dzone.com/articles/whats-wrong-java-8-part-iv">What's Wrong in Java 8, Part IV: Monads</a>.
 *
 * @param <T> component type of this checked monad
 * @param <M> placeholder for the type that implements this
 * @since 1.1.0
 */
public interface CheckedMonad<T, M extends HigherKinded<?, M>> extends CheckedFunctor<T>, HigherKinded<T, M> {

    /**
     * Returns the result of applying f to M's value of type T and returns a new M with value of type U.
     *
     * @param <U> component type of this monad
     * @param <MONAD> placeholder for the monad type of component type T and container type M
     * @param f a checked function that maps the monad value to a new monad instance
     * @return a new CheckedMonad instance of component type U and container type M
     */
    <U, MONAD extends HigherKinded<U, M>> CheckedMonad<U, M> flatMap(CheckedFunction<? super T, ? extends MONAD> f);

    /**
     * Returns a filterned instance of this {@code CheckedMonad}.
     * <p>
     * Note: monadic filtering may be interpreted in another way than just filtering contained elements.
     *
     * @param predicate A {@code Predicate}
     * @return An instance of this monad type
     */
    CheckedMonad<T, M> filter(CheckedPredicate<? super T> predicate);

    /**
     * Flattens a nested, monadic structure. Assumes that the elements are of type HigherKinded&lt;U, M&gt;
     *
     * <p>
     * A vivid example showing a simple container type:
     * <pre>
     * <code>
     * [[1],[2,3]].flatten() = [1,2,3]
     * </code>
     * </pre>
     *
     * @param <U> component type of the resulting {@code Monad}
     * @return A monadic structure containing flattened elements.
     */
    <U> CheckedMonad<U, M> flatten();

    /**
     * Flattens a nested, monadic structure using a function.
     * <p>
     * A vivid example showing a simple container type:
     * <pre>
     * <code>
     * // given a monad M&lt;T&gt;
     * [a,[b,c],d].flatten( Match
     *    .caze((M m) -&gt; m)
     *    .caze((T t) -&gt; new M(t))
     * ) = [a,b,c,d]
     * </code>
     * </pre>
     *
     * @param <U> component type of the resulting {@code Monad}
     * @param <MONAD> {@code Monad} type
     * @param f a function which maps elements of this monad to monads of the same kind
     * @return A monadic structure containing flattened elements.
     */
    <U, MONAD extends HigherKinded<U, M>> CheckedMonad<U, M> flatten(CheckedFunction<? super T, ? extends MONAD> f);

    /**
     * Performs an action on each element of this monad.
     *
     * @param action A {@code Consumer}
     */
    void forEach(Consumer<? super T> action);

    /**
     * Performs an action on each element of this monad.
     * @param action A {@code Consumer}
     * @return An instance of this monad type
     */
    CheckedMonad<T, M> peek(CheckedConsumer<? super T> action);

    @Override
    <U> CheckedMonad<U, M> map(CheckedFunction<? super T, ? extends U> f);
}