/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collector;

public abstract class AbstractTraversableTest extends AbstractTraversableOnceTest {

    abstract protected <T> Collector<T, ArrayList<T>, ? extends Traversable<T>> collector();

    @Override
    abstract protected <T> Traversable<T> empty();

    @Override
    abstract protected <T> Traversable<T> of(T element);

    @SuppressWarnings("unchecked")
    @Override
    abstract protected <T> Traversable<T> of(T... elements);

    @Override
    abstract protected <T> Traversable<T> ofAll(java.lang.Iterable<? extends T> elements);

    @Override
    abstract protected Traversable<Boolean> ofAll(boolean[] array);

    @Override
    abstract protected Traversable<Byte> ofAll(byte[] array);

    @Override
    abstract protected Traversable<Character> ofAll(char[] array);

    @Override
    abstract protected Traversable<Double> ofAll(double[] array);

    @Override
    abstract protected Traversable<Float> ofAll(float[] array);

    @Override
    abstract protected Traversable<Integer> ofAll(int[] array);

    @Override
    abstract protected Traversable<Long> ofAll(long[] array);

    @Override
    abstract protected Traversable<Short> ofAll(short[] array);

    // -- static collector()

    @Test
    public void shouldStreamAndCollectNil() {
        final Traversable<?> actual = java.util.stream.Stream.empty().collect(this.<Object> collector());
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldStreamAndCollectNonNil() {
        final Traversable<?> actual = java.util.stream.Stream.of(1, 2, 3).collect(this.<Object> collector());
        assertThat(actual).isEqualTo(of(1, 2, 3));
    }

    @Test
    public void shouldParallelStreamAndCollectNil() {
        final Traversable<?> actual = java.util.stream.Stream.empty().parallel().collect(this.<Object> collector());
        assertThat(actual).isEmpty();
    }

    @Test
    public void shouldParallelStreamAndCollectNonNil() {
        final Traversable<?> actual = java.util.stream.Stream.of(1, 2, 3).parallel().collect(this.<Object> collector());
        assertThat(actual).isEqualTo(of(1, 2, 3));
    }

}
