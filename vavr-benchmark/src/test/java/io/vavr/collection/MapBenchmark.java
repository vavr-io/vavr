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
package io.vavr.collection;

import io.vavr.Function1;
import io.vavr.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.Random;

import static io.vavr.JmhRunner.create;
import static io.vavr.JmhRunner.getRandomValues;

public class MapBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            IterateKeys.class,
            VavrKeys.class,
            VavrValues.class,
            IterateValues.class,
            Get.class,
            Miss.class,
            PutOrdered.class,
            PutShuffled.class,
            ReplaceSingle.class,
            ReplaceAll.class,
            ReplaceAllOneByOne.class,
            Remove.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({"10", "100", "1000", "2500"})
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;
        Integer[] KEYS;
        Integer[] REMOVAL;
        Map<Integer, Integer> sampleTreeMap;

        //        scala.collection.immutable.Map<Integer, Integer> scalaPersistent;
        org.pcollections.PMap<Integer, Integer> pcollectionsPersistent;
        io.usethesource.capsule.Map.Immutable<Integer, Integer> capsulePersistent;
        Map<Integer, Integer> vavrHash;
        Map<Integer, Integer> vavrTreeMap;
        Map<Integer, Integer> vavrLinkedHash;

        @Setup
        public void setup() {
            ELEMENTS = getRandomValues(CONTAINER_SIZE, 0);
            sampleTreeMap = index(ELEMENTS);
            KEYS = predicableShuffle(sampleTreeMap.keySet().toJavaArray(Integer[]::new));
            REMOVAL = predicableShuffle(KEYS.clone());
            EXPECTED_AGGREGATE = sampleTreeMap.values().reduce(JmhRunner::aggregate);

            pcollectionsPersistent = create(
                    org.pcollections.HashTreePMap::from,
                    sampleTreeMap.toJavaMap(),
                    sampleTreeMap.size(),
                    v -> sampleTreeMap.forAll((e) -> v.get(e._1).equals(e._2)));
            capsulePersistent = create(
                    io.usethesource.capsule.util.collection.AbstractSpecialisedImmutableMap::mapOf,
                    sampleTreeMap.toJavaMap(),
                    sampleTreeMap.size(),
                    v -> sampleTreeMap.forAll((e) -> v.get(e._1).equals(e._2)));
            vavrTreeMap = doCreateMap(TreeMap::ofAll, sampleTreeMap);
            vavrHash = doCreateMap(HashMap::ofAll, sampleTreeMap);
            vavrLinkedHash = doCreateMap(LinkedHashMap::ofAll, sampleTreeMap);
        }

        private TreeMap<Integer, Integer> index(Integer[] array) {
            java.util.Map<Integer, Integer> javaMap = new java.util.HashMap<>();
            for (int i = 0; i < array.length; i++) {
                javaMap.put(i, array[i]);
            }
            return TreeMap.ofAll(javaMap);
        }

        /**
         * Shuffle the array. Use a random number generator with a fixed seed.
         */
        private Integer[] predicableShuffle(Integer[] array) {
            java.util.Collections.shuffle(Arrays.asList(array), new Random(42));
            return array;
        }

        private <M extends Map<Integer, Integer>> M doCreateMap(Function1<java.util.Map<Integer, Integer>, M> factory, M prototype) {
            return create(factory, prototype.toJavaMap(), prototype.size(), v -> prototype.forAll(v::contains));
        }
    }

    @SuppressWarnings("Duplicates")
    public static class PutShuffled extends Base {
        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PMap<Integer, Integer> values = org.pcollections.HashTreePMap.empty();
            Integer[] elements = ELEMENTS;
            for (Integer key : KEYS) {
                values = values.plus(key, elements[key]);
            }
            org.pcollections.PMap<Integer, Integer> result = values;
            assert vavrTreeMap.forAll((e) -> result.get(e._1).equals(e._2));
            return result;
        }

        @Benchmark
        public Object capsule_persistent() {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = io.usethesource.capsule.core.PersistentTrieMap.of();
            Integer[] elements = ELEMENTS;
            for (Integer key : KEYS) {
                values = values.__put(key, elements[key]);
            }
            io.usethesource.capsule.Map.Immutable<Integer, Integer> result = values;
            assert vavrTreeMap.forAll((e) -> result.get(e._1).equals(e._2));
            return result;
        }

        @Benchmark
        public Object vavr_tree() {
            Map<Integer, Integer> values = TreeMap.empty();
            Integer[] elements = ELEMENTS;
            for (Integer key : KEYS) {
                values = values.put(key, elements[key]);
            }
            assert vavrTreeMap.forAll(values::contains);
            return values;
        }

        @Benchmark
        public Object vavr_hash() {
            Map<Integer, Integer> values = HashMap.empty();
            Integer[] elements = ELEMENTS;
            for (Integer key : KEYS) {
                values = values.put(key, elements[key]);
            }
            assert vavrTreeMap.forAll(values::contains);
            return values;
        }

        @Benchmark
        public Object vavr_linked_hash() {
            Map<Integer, Integer> values = LinkedHashMap.empty();
            Integer[] elements = ELEMENTS;
            for (Integer key : KEYS) {
                values = values.put(key, elements[key]);
            }
            assert vavrTreeMap.forAll(values::contains);
            return values;
        }
    }

    public static class PutOrdered extends Base {
        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PMap<Integer, Integer> values = org.pcollections.HashTreePMap.empty();
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.plus(i, elements[i]);
            }
            org.pcollections.PMap<Integer, Integer> result = values;
            assert vavrTreeMap.forAll((e) -> result.get(e._1).equals(e._2));
            return result;
        }

        @Benchmark
        public Object capsule_persistent() {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = io.usethesource.capsule.core.PersistentTrieMap.of();
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.__put(i, elements[i]);
            }
            io.usethesource.capsule.Map.Immutable<Integer, Integer> result = values;
            assert vavrTreeMap.forAll((e) -> result.get(e._1).equals(e._2));
            return result;
        }

        @Benchmark
        public Object vavr_tree() {
            Map<Integer, Integer> values = TreeMap.empty();
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.put(i, elements[i]);
            }
            assert vavrTreeMap.forAll(values::contains);
            return values;
        }

        @Benchmark
        public Object vavr_hash() {
            Map<Integer, Integer> values = HashMap.empty();
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.put(i, elements[i]);
            }
            assert vavrTreeMap.forAll(values::contains);
            return values;
        }

        @Benchmark
        public Object vavr_linked_hash() {
            Map<Integer, Integer> values = LinkedHashMap.empty();
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.put(i, elements[i]);
            }
            assert vavrTreeMap.forAll(values::contains);
            return values;
        }
    }

    public static class Get extends Base {
        @Benchmark
        public void pcollections_persistent(Blackhole bh) {
            org.pcollections.PMap<Integer, Integer> values = pcollectionsPersistent;
            for (Integer key : KEYS) {
                bh.consume(values.get(key));
            }
        }

        @Benchmark
        public void capsule_persistent(Blackhole bh) {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = capsulePersistent;
            for (Integer key : KEYS) {
                bh.consume(values.get(key));
            }
        }

        @Benchmark
        public void vavr_tree(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            Integer dflt = 1;
            for (Integer key : KEYS) {
                bh.consume(values.getOrElse(key, dflt));
            }
        }

        @Benchmark
        public void vavr_hash(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            Integer dflt = 1;
            for (Integer key : KEYS) {
                bh.consume(values.getOrElse(key, dflt));
            }
        }

        @Benchmark
        public void vavr_linked_hash(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            Integer dflt = 1;
            for (Integer key : KEYS) {
                bh.consume(values.getOrElse(key, dflt));
            }
        }
    }

    public static class Miss extends Base {
        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PMap<Integer, Integer> values = pcollectionsPersistent;
            return values.get(-1);
        }

        @Benchmark
        public Object capsule_persistent() {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = capsulePersistent;
            return values.get(-1);
        }

        @Benchmark
        public Object vavr_tree() {
            Map<Integer, Integer> values = vavrTreeMap;
            return values.get(-1);
        }

        @Benchmark
        public Object vavr_hash() {
            Map<Integer, Integer> values = vavrHash;
            return values.get(-1);
        }

        @Benchmark
        public Object vavr_linked_hash() {
            Map<Integer, Integer> values = vavrLinkedHash;
            return values.get(-1);
        }
    }

    public static class IterateKeys extends Base {
        @Benchmark
        public void pcollections_persistent(Blackhole bh) {
            org.pcollections.PMap<Integer, Integer> values = pcollectionsPersistent;
            for (Integer integer : values.keySet()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void capsule_persistent(Blackhole bh) {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = capsulePersistent;
            for (java.util.Iterator<Integer> it = values.keyIterator(); it.hasNext(); ) {
                bh.consume(it.next());
            }
        }

        @Benchmark
        public void vavr_tree(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer integer : values.iterator((k, v) -> k)) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_tree_keys(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer integer : values.keysIterator()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_hash(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            for (Integer integer : values.iterator((k, v) -> k)) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_hash_keys(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            for (Integer integer : values.keysIterator()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_linked_hash(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer integer : values.iterator((k, v) -> k)) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_linked_hash_keys(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer integer : values.keysIterator()) {
                bh.consume(integer);
            }
        }
    }

    public static class VavrKeys extends Base {
        @Benchmark
        public void vavr_hash_keySet(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            for (Integer integer : values.keySet()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_hash_iterator(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            for (Integer integer : values.iterator((k, v) -> k)) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_hash_keys(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            for (Integer integer : values.keysIterator()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_tree_keySet(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer integer : values.keySet()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_tree_iterator(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer integer : values.iterator((k, v) -> k)) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_tree_keys(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer integer : values.keysIterator()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_linked_hash_keySet(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer integer : values.keySet()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_linked_hash_iterator(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer integer : values.iterator((k, v) -> k)) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_linked_hash_keys(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer integer : values.keysIterator()) {
                bh.consume(integer);
            }
        }

    }

    public static class VavrValues extends Base {
        @Benchmark
        public void vavr_hash_keySet(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            for (Integer integer : values.values()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_hash_iterator(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            for (Integer integer : values.iterator((k, v) -> v)) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_hash_keys(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            for (Integer integer : values.valuesIterator()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_tree_keySet(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer integer : values.values()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_tree_iterator(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer integer : values.iterator((k, v) -> v)) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_tree_keys(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer integer : values.valuesIterator()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_linked_hash_keySet(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer integer : values.values()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_linked_hash_iterator(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer integer : values.iterator((k, v) -> v)) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_linked_hash_keys(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer integer : values.valuesIterator()) {
                bh.consume(integer);
            }
        }

    }

    public static class IterateValues extends Base {
        @Benchmark
        public void pcollections_persistent(Blackhole bh) {
            org.pcollections.PMap<Integer, Integer> values = pcollectionsPersistent;
            for (Integer integer : values.values()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void capsule_persistent(Blackhole bh) {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = capsulePersistent;
            for (java.util.Iterator<Integer> it = values.valueIterator(); it.hasNext(); ) {
                bh.consume(it.next());
            }
        }

        @Benchmark
        public void vavr_tree(Blackhole bh) {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer integer : values.valuesIterator()) {
                bh.consume(integer);
            }
        }

        @Benchmark
        public void vavr_hash(Blackhole bh) {
            Map<Integer, Integer> values = vavrHash;
            for (Integer integer : values.valuesIterator()) {
                bh.consume(integer);
            }

        }

        @Benchmark
        public void vavr_linked_hash(Blackhole bh) {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer integer : values.valuesIterator()) {
                bh.consume(integer);
            }
        }
    }

    @SuppressWarnings("Duplicates")
    public static class Remove extends Base {
        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PMap<Integer, Integer> values = pcollectionsPersistent;
            for (Integer removeMe : REMOVAL) {
                values = values.minus(removeMe);
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object capsule_persistent() {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = capsulePersistent;
            for (Integer removeMe : REMOVAL) {
                values = values.__remove(removeMe);
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object vavr_tree() {
            Map<Integer, Integer> values = vavrTreeMap;
            for (Integer removeMe : REMOVAL) {
                values = values.remove(removeMe);
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object vavr_hash() {
            Map<Integer, Integer> values = vavrHash;
            for (Integer removeMe : REMOVAL) {
                values = values.remove(removeMe);
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object vavr_linked_hash() {
            Map<Integer, Integer> values = vavrLinkedHash;
            for (Integer removeMe : REMOVAL) {
                values = values.remove(removeMe);
            }
            assert values.isEmpty();
            return values;
        }
    }

    @SuppressWarnings("Duplicates")
    public static class ReplaceSingle extends Base {
        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PMap<Integer, Integer> values = pcollectionsPersistent;
            Integer key = REMOVAL[0];
            Integer newValue = ELEMENTS[key] + 1;
            values = values.plus(key, newValue);
            return values;
        }

        @Benchmark
        public Object capsule_persistent() {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = capsulePersistent;
            Integer key = REMOVAL[0];
            Integer newValue = ELEMENTS[key] + 1;
            values = values.__put(key, newValue);
            return values;
        }

        @Benchmark
        public Object vavr_tree() {
            Map<Integer, Integer> values = vavrTreeMap;
            Integer key = REMOVAL[0];
            Integer newValue = ELEMENTS[key] + 1;
            values = values.put(key, newValue);
            return values;
        }

        @Benchmark
        public Object vavr_hash() {
            Map<Integer, Integer> values = vavrHash;
            Integer key = REMOVAL[0];
            Integer newValue = ELEMENTS[key] + 1;
            values = values.put(key, newValue);
            return values;
        }

        @Benchmark
        public Object vavr_linked_hash() {
            Map<Integer, Integer> values = vavrLinkedHash;
            Integer key = REMOVAL[0];
            Integer newValue = ELEMENTS[key] + 1;
            values = values.put(key, newValue);
            return values;
        }
    }

    @SuppressWarnings("CollectionAddedToSelf")
    public static class ReplaceAll extends Base {
        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PMap<Integer, Integer> values = pcollectionsPersistent;
            values = values.plusAll(values);
            org.pcollections.PMap<Integer, Integer> result = values;
            assert vavrTreeMap.forAll((e) -> result.get(e._1).equals(e._2));
            return values;
        }

        @Benchmark
        public Object capsule_persistent() {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = capsulePersistent;
            values = values.__putAll(values);
            io.usethesource.capsule.Map.Immutable<Integer, Integer> result = values;
            assert vavrTreeMap.forAll((e) -> result.get(e._1).equals(e._2));
            return values;
        }

        @Benchmark
        public Object vavr_tree() {
            Map<Integer, Integer> values = vavrTreeMap;
            values = values.merge(values, (l, r) -> r);
            return values;
        }

        @Benchmark
        public Object vavr_hash() {
            Map<Integer, Integer> values = vavrHash;
            values = values.merge(values, (l, r) -> r);
            return values;
        }

        @Benchmark
        public Object vavr_linked_hash() {
            Map<Integer, Integer> values = vavrLinkedHash;
            values = values.merge(values, (l, r) -> r);
            return values;
        }
    }

    @SuppressWarnings("CollectionAddedToSelf")
    public static class ReplaceAllOneByOne extends Base {
        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PMap<Integer, Integer> values = pcollectionsPersistent;
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.plus(i, elements[i] + 1);
            }
            return values;
        }

        @Benchmark
        public Object capsule_persistent() {
            io.usethesource.capsule.Map.Immutable<Integer, Integer> values = capsulePersistent;
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.__put(i, elements[i] + 1);
            }
            return values;
        }

        @Benchmark
        public Object vavr_tree() {
            Map<Integer, Integer> values = vavrTreeMap;
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.put(i, elements[i] + 1);
            }
            return values;
        }

        @Benchmark
        public Object vavr_hash() {
            Map<Integer, Integer> values = vavrHash;
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.put(i, elements[i] + 1);
            }
            return values;
        }

        @Benchmark
        public Object vavr_linked_hash() {
            Map<Integer, Integer> values = vavrLinkedHash;
            Integer[] elements = ELEMENTS;
            for (int i = 0; i < elements.length; i++) {
                values = values.put(i, elements[i] + 1);
            }
            return values;
        }
    }

}
