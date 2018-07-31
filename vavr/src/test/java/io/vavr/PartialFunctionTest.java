/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
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

import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

// DEV-NOTE: we must not write pf.apply(t) tests for the case pf.isDefinedAt(t) == false
public class PartialFunctionTest {

    // -- andThen

    @Test
    public void shouldComposeWithAndThenAndCheckDefinedState() {
        assertThat(hyperbola.andThen(hyperbola).isDefinedAt(0.5d)).isTrue();
    }

    @Test
    public void shouldComposeWithAndThenAndApplyDefinedValue() {
        assertThat(hyperbola.andThen(hyperbola).apply(0.5d)).isEqualTo(0.5d);
    }

    @Test
    public void shouldComposeWithAndThenAndCheckUndefinedState() {
        assertThat(hyperbola.andThen(hyperbola).isDefinedAt(0d)).isFalse();
    }

    // -- apply

    @Test
    public void shouldApplyDefinedDomainValue() {
        assertThat(hyperbola.apply(1d)).isEqualTo(1d);
    }

    // -- applyOrElse

    @Test
    public void shouldApplyOrElseWhenDefined() {
        assertThat(hyperbola.applyOrElse(1d, d -> 0d)).isEqualTo(1d);
    }

    @Test
    public void shouldApplyOrElseWhenUndefined() {
        assertThat(hyperbola.applyOrElse(0d, d -> Double.NaN).isNaN()).isTrue();
    }

    // -- compose

    @Test
    public void shouldComposePartialFunctionWithFunctionDefinedCase() {
        assertThat(hyperbola.compose(Function.identity()).apply(1d)).isEqualTo(1d);
    }

    // -- isDefinedAt

    @Test
    public void  shouldRecognizeDefinedDomainValue() {
        assertThat(hyperbola.isDefinedAt(1d)).isTrue();
    }

    @Test
    public void  shouldRecognizeUndefinedDomainValue() {
        assertThat(hyperbola.isDefinedAt(0d)).isFalse();
    }

    // -- orElse

    @Test
    public void shouldComposeWithOrElseThisDefinedFallbackDefined() {
        final PartialFunction<Double, Double> testee = hyperbola.orElse(nan);
        assertThat(testee.isDefinedAt(1d)).isTrue();
        assertThat(testee.apply(1d)).isEqualTo(1d);
    }

    @Test
    public void shouldComposeWithOrElseThisDefinedFallbackUndefined() {
        final PartialFunction<Double, Double> testee = hyperbola.orElse(undefined);
        assertThat(testee.isDefinedAt(1d)).isTrue();
    }

    @Test
    public void shouldComposeWithOrElseThisUndefinedFallbackDefined() {
        final PartialFunction<Double, Double> testee = hyperbola.orElse(nan);
        assertThat(testee.isDefinedAt(0d)).isTrue();
        assertThat(testee.apply(0d)).isNaN();
    }

    @Test
    public void shouldComposeWithOrElseThisUndefinedFallbackUndefined() {
        final PartialFunction<Double, Double> testee = hyperbola.orElse(undefined);
        assertThat(testee.isDefinedAt(0d)).isFalse();
    }

    // -- runWith

    @Test
    public void shouldRunWithDefinedPartialFunction() {
        final boolean[] sideEffect = new boolean[] { false };
        final Function<Double, Boolean> testee = hyperbola.runWith(d -> sideEffect[0] = true);
        assertThat(testee.apply(1d)).isTrue();
        assertThat(sideEffect[0]).isTrue();
    }

    @Test
    public void shouldRunWithUndefinedPartialFunction() {
        final boolean[] sideEffect = new boolean[] { false };
        final Function<Double, Boolean> testee = hyperbola.runWith(d -> sideEffect[0] = true);
        assertThat(testee.apply(0d)).isFalse();
        assertThat(sideEffect[0]).isFalse();
    }

    // -- testees

    private static PartialFunction<Double, Double> hyperbola = new PartialFunction<Double, Double>() {
        private static final long serialVersionUID = 1L;
        @Override
        public Double apply(Double x) {
            return 1/x;
        }
        @Override
        public boolean isDefinedAt(Double x) {
            return x != 0;
        }
    };

    private static PartialFunction<Double, Double> nan = new PartialFunction<Double, Double>() {
        private static final long serialVersionUID = 1L;
        @Override
        public Double apply(Double aDouble) {
            return Double.NaN;
        }
        @Override
        public boolean isDefinedAt(Double value) {
            return true;
        }
    };

    private static PartialFunction<Double, Double> undefined = new PartialFunction<Double, Double>() {
        private static final long serialVersionUID = 1L;
        @Override
        public Double apply(Double aDouble) {
            throw new UnsupportedOperationException();
        }
        @Override
        public boolean isDefinedAt(Double value) {
            return false;
        }
    };

}
