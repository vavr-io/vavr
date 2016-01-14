/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/**
 * Recursive self type representing {@code TYPE<T>}, which allows similar behavior to higher-kinded types.
 * <p>
 * Example usage:
 * <pre><code>
 * interface MyInterface&lt;TYPE extends Kind&lt;TYPE, ?&gt;, T&gt; extends Kind&lt;TYPE, T&gt; {
 *
 *     &lt;U&gt; MyInterface&lt;TYPE, U&gt; m1(Function&lt;? super T, ? extends U&gt; f);
 *
 *     &lt;U&gt; MyInterface&lt;TYPE, U&gt; m2(Function&lt;? super T, ? extends Kind&lt;? extends TYPE, ? extends U&gt;&gt; f);
 * }
 *
 * class MyClass&lt;T&gt; implements MyInterface&lt;MyClass&lt;?&gt;, T&gt; {
 *
 *     @Override
 *     &lt;U&gt; MyClass&lt;U&gt; m1(Function&lt;? super T, ? extends U&gt; f);
 *
 *     @Override
 *     &lt;U&gt; MyClass&lt;U&gt; m2(Function&lt;? super T, ? extends Kind&lt;? extends TYPE, ? extends U&gt;> f);
 * }
 * </code></pre>
 *
 * @param <TYPE> The recursive type described by {@code Kind}
 * @param <T>    The first type, described by {@code Kind}
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Kind<TYPE extends Kind<TYPE, ?>, T> {
}
