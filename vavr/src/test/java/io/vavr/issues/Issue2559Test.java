/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.issues;

import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * partition method seems to not work correctly, comparing to scala
 * https://github.com/vavr-io/vavr/issues/2559
 */
public class Issue2559Test {

    private java.util.Map<String, Eat> fruitsBeingEaten = new java.util.HashMap<>();

    @BeforeEach
    public void setUp() {
        fruitsBeingEaten = new java.util.HashMap<>();
    }

    @Test
    public void partitionShouldBeUnique() {
        final Set<String> fruitsToEat = HashSet.of("apple", "banana");
        final Tuple2<? extends Set<String>, ? extends Set<String>> partition = fruitsToEat.partition(this::biteAndCheck);
        assertThat(partition._1()).isEmpty();
        assertThat(partition._2()).isEqualTo(HashSet.of("apple", "banana"));
        assertThat(fruitsBeingEaten)
          .hasSize(2)
          .containsEntry("apple", new Eat(1, "apple"))
          .containsEntry("banana", new Eat(1, "banana"));
    }

    private boolean biteAndCheck(String name) {
        final Eat eat = fruitsBeingEaten.getOrDefault(name, Eat.prepare(name)).bite();
        fruitsBeingEaten.put(name, eat);
        return eat.isEaten();
    }

    private static class Eat {
        final int bites;
        final String name;

        public static Eat prepare(String name) {
            return new Eat(0, name);
        }

        private Eat(int bites, String name) {
            this.bites = bites;
            this.name = name;
        }

        public Eat bite() {
            return new Eat(bites + 1, name);
        }

        public boolean isEaten() {
            return bites >= 2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Eat)) return false;
            Eat eat = (Eat) o;
            return bites == eat.bites && Objects.equals(name, eat.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bites, name);
        }

        @Override
        public String toString() {
            return "Eat{" +
              "bites=" + bites +
              ", name='" + name + '\'' +
              '}';
        }
    }
}
