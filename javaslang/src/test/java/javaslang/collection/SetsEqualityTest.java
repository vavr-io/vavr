package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SetsEqualityTest {

    private final String[] testStrings = { "a", "b", "c", "D" };

    private final Map<Class<?>, SetStringGenerator> generators = HashMap.of(
            HashSet.class, SetStringGenerator.hashSet,
            LinkedHashSet.class, SetStringGenerator.linkedHashSet,
            TreeSet.class, SetStringGenerator.treeSet);

    @Test
    public void shouldAllImplementationsBeEqualForTheSameData() {
        pairwiseDistinctSetClasses().forEach(pair -> this.equalityTest(pair._1, pair._2));
    }

    @Test
    public void shouldAllImplementationsBeCongruentForTheSameData() {
        pairwiseDistinctSetClasses().forEach(pair -> this.congruenceTest(pair._1, pair._2));
    }

    private Set<Tuple2<? extends Class<?>, ? extends Class<?>>> pairwiseDistinctSetClasses() {
        return generators.keySet()
                .flatMap(firstClass -> generators.keySet()
                        .filter(filteredKey -> filteredKey != firstClass)
                        .map(inner -> Tuple.of(firstClass, inner))
                        .collect(Collectors.toSet()));
    }
    private void equalityTest(Class<?> firstClass, Class<?> secondClass) {
        // given
        final Option<Set<String>> first = generators.get(firstClass).map(sg -> sg.apply(testStrings));
        final Option<Set<String>> second = generators.get(secondClass).map(sg -> sg.apply(testStrings));

        // when
        // then
        assertThat(first).isNotEmpty();
        assertThat(first).describedAs("%s vs %s", firstClass, secondClass).isEqualTo(second);
    }

    private void congruenceTest(Class<?> firstClass, Class<?> secondClass) {
        // given
        final Option<Set<String>> first = generators.get(firstClass).map(sg -> sg.apply(testStrings));
        final Option<Set<String>> second = generators.get(secondClass).map(sg -> sg.apply(testStrings));

        // when
        // then
        assertThat(first).isNotEmpty();
        assertThat(first.get().eq(second.get())).describedAs("%s vs %s", firstClass, secondClass).isTrue();
    }

    @FunctionalInterface
    private interface SetStringGenerator extends Function<String[], Set<String>> {
        SetStringGenerator hashSet = HashSet::of;
        SetStringGenerator linkedHashSet = LinkedHashSet::of;
        SetStringGenerator treeSet = TreeSet::of;
    }
}

