/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.junit.Test;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SetsEqualityTest {

    private final String[] oneSetOfStrings = { "a", "b", "c", "D" };
    private final String[] anotherSetOfStrings = { "e", "F", "g", "D" };
    private final Supplier<String[]> oneSetOStringsSupplier = () -> oneSetOfStrings;
    private final Map<Class<?>, SetStringGenerator> generators = HashMap.of(
            HashSet.class, SetStringGenerator.hashSet,
            LinkedHashSet.class, SetStringGenerator.linkedHashSet,
            TreeSet.class, SetStringGenerator.treeSet);

    @Test
    public void shouldAllImplementationsBeEqualForTheSameData() {
        pairwiseDistinctSetClasses().forEach(
                pairOfSetsGenerators -> this.equalityTest(
                        pairOfSetsGenerators._1, pairOfSetsGenerators._2,
                        oneSetOStringsSupplier,
                        oneSetOStringsSupplier));
    }

    @Test
    public void shouldAllImplementationsBeEqualForTheSameDataWithDifferentOrder() {
        pairwiseDistinctSetClasses().forEach(
                pairOfSetsGenerators -> this.equalityTest(
                        pairOfSetsGenerators._1, pairOfSetsGenerators._2,
                        toss(oneSetOStringsSupplier),
                        toss(oneSetOStringsSupplier)));
    }

    @Test
    public void shouldAllImplementationsBeUnEqualForTheDifferentData() {
        pairwiseDistinctSetClasses().forEach(
                pairOfSetsGenerators -> this.unequalityTest(
                        pairOfSetsGenerators._1, pairOfSetsGenerators._2,
                        oneSetOStringsSupplier,
                        () -> anotherSetOfStrings));
    }

    private Supplier<String[]> toss(Supplier<String[]> original) {
        return () -> {
            final String[] fromOriginal = original.get();
            final Random random = new Random();
            for (int i = 0; i < fromOriginal.length; ++i) {
                if (random.nextInt() % 2 == 0) {
                    final int newIndex = Math.abs(random.nextInt() % fromOriginal.length);
                    final String swap = fromOriginal[i];
                    fromOriginal[i] = fromOriginal[newIndex];
                    fromOriginal[newIndex] = swap;
                }
            }
            return fromOriginal;
        };
    }
    private java.util.Set<Tuple2<? extends Class<?>, ? extends Class<?>>> pairwiseDistinctSetClasses() {
        return generators.keySet().toList()
                .combinations(2)
                .map(seq -> Tuple.of(seq.get(0), seq.get(1)))
                .collect(Collectors.toSet());
    }

    private void equalityTest(Class<?> firstClass, Class<?> secondClass, Supplier<String[]> stringsForFirstClass, Supplier<String[]> stringsForSecondClass) {
        // given
        final Option<Set<String>> first = generators.get(firstClass).map(sg -> sg.apply(stringsForFirstClass.get()));
        final Option<Set<String>> second = generators.get(secondClass).map(sg -> sg.apply(stringsForSecondClass.get()));

        // when
        // then
        assertThat(first).isNotEmpty();
        assertThat(first).describedAs("%s vs %s", firstClass, secondClass).isEqualTo(second);
    }

    private void unequalityTest(Class<?> firstClass, Class<?> secondClass, Supplier<String[]> stringsForFirstClass, Supplier<String[]> stringsForSecondClass) {
        // given
        final Option<Set<String>> first = generators.get(firstClass).map(sg -> sg.apply(stringsForFirstClass.get()));
        final Option<Set<String>> second = generators.get(secondClass).map(sg -> sg.apply(stringsForSecondClass.get()));

        // when
        // then
        assertThat(first).isNotEmpty();
        assertThat(first).describedAs("%s vs %s", firstClass, secondClass).isNotEqualTo(second);
    }

    @FunctionalInterface
    private interface SetStringGenerator extends Function<String[], Set<String>> {
        SetStringGenerator hashSet = HashSet::of;
        SetStringGenerator linkedHashSet = LinkedHashSet::of;
        SetStringGenerator treeSet = TreeSet::of;
    }
}

