/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

/**
 * <p>
 * A type <em>HigherKinded</em> declares a generic type constructor, which consists of an inner type (component type)
 * and an outer type (container type).
 * </p>
 * <p>
 * HigherKinded is needed to (partially) simulate Higher-Kinded/Higher-Order Types, which  are not part of the Java
 * language but needed for generic type constructors.
 * </p>
 * <p>
 * Example: {@link javaslang.algebra.Monad1#flatMap(Function1)}
 * </p>
 *
 * @param <T1> Component type of the type to be constructed.
 * @param <TYPE> Container type of the type to be constructed.
 */
@SuppressWarnings("unused")
public interface HigherKinded1<T1, TYPE extends HigherKinded1<?, TYPE>> {

    // used for type declaration only
}