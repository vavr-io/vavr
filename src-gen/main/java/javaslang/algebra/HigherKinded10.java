/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

/**
 * <p>The <em>HigherKinded10</em> type declares a generic type constructor, which consists of
 * 10 inner/component types and one outer/container type.</p>
 * <p>HigherKinded10 is used to approximately simulate higher-kinded/higher-order types, which cannot be
 * expressed with Java.</p>
 * <p>Example: {@linkplain javaslang.Tuple10} implements HigherKind10 in order to override
 * {@link javaslang.algebra.Monad10#flatMap(javaslang.Function10)}.</p>
 *
 * See also
 * <ul>
 * <li><a href="http://adriaanm.github.io/files/higher.pdf">Generics of a Higher Kind</a> (Moors, Piessens, Odersky)</li>
 * <li><a href="http://en.wikipedia.org/wiki/Kind_(type_theory)">Kind (type theory)</a> (wikipedia)</li>
 * <li><a href="http://en.wikipedia.org/wiki/Type_constructor">Type constructor</a> (wikipedia)</li>
 * </ul>
 *
 * @param <T1> 1st component type of the type to be constructed
 * @param <T2> 2nd component type of the type to be constructed
 * @param <T3> 3rd component type of the type to be constructed
 * @param <T4> 4th component type of the type to be constructed
 * @param <T5> 5th component type of the type to be constructed
 * @param <T6> 6th component type of the type to be constructed
 * @param <T7> 7th component type of the type to be constructed
 * @param <T8> 8th component type of the type to be constructed
 * @param <T9> 9th component type of the type to be constructed
 * @param <T10> 10th component type of the type to be constructed
 * @param <TYPE> the container type, i.e. the type to be constructed.
 */
@SuppressWarnings("unused")
public interface HigherKinded10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, TYPE extends HigherKinded10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TYPE>> {

    // used for type declaration only
}