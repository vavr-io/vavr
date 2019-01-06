/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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

import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import java.util.Comparator;
import java.util.Objects;
import org.junit.Test;

public class Tuple0Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple0 tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple0 tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(0);
    }

    @Test
    public void shouldConvertToSeq() {
        final Seq<?> actual = createIntTuple().toSeq();
        assertThat(actual).isEqualTo(List.of());
    }

    @Test
    public void shouldCompareEqual() {
        final Tuple0 t0 = createIntTuple();
        assertThat(t0.compareTo(t0)).isZero();
        assertThat(intTupleComparator.compare(t0, t0)).isZero();
    }

    @Test
    public void shouldApplyTuple() {
        final Tuple0 tuple = createTuple();
        final Tuple0 actual = tuple.apply(() -> Tuple0.instance());
        assertThat(actual).isEqualTo(Tuple0.instance());
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple0 tuple1 = createTuple();
        final Tuple0 tuple2 = createTuple();
        assertThat((Object) tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple0 tuple = createTuple();
        final Object other = new Object();
        assertThat(tuple).isNotEqualTo(other);
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "()";
        assertThat(actual).isEqualTo(expected);
    }

    private Comparator<Tuple0> intTupleComparator = Tuple0.comparator();

    private Tuple0 createTuple() {
        return Tuple0.instance();
    }

    private Tuple0 createIntTuple() {
        return Tuple0.instance();
    }
}