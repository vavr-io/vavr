package javaslang.collection;

import javaslang.JmhRunner;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.compat.java8.JFunction;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static javaslang.JmhRunner.create;
import static javaslang.JmhRunner.getRandomValues;
import static javaslang.collection.Collections.areEqual;
import static scala.collection.JavaConversions.asJavaCollection;
import static scala.collection.JavaConversions.asScalaBuffer;

@SuppressWarnings({"ALL", "unchecked"})
public class VectorBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            Access.class,
            Create.class,
            Head.class,
            Tail.class,
            Get.class,
            Update.class,
            Prepend.class,
            Append.class,
            GroupBy.class,
            Slice.class,
            Iterate.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES);
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        @Param({"32", "1024", "32768" /*, "1048576", "33554432", "1073741824" */}) /* i.e. depth 1,2,3(,4,5,6) for a branching factor of 32 */
        public int CONTAINER_SIZE;

        int EXPECTED_AGGREGATE;
        Integer[] ELEMENTS;
        int[] INT_ELEMENTS;
        int[] RANDOMIZED_INDICES;

        /* Only use this for non-mutating operations */
        java.util.ArrayList<Integer> javaMutable;

        scala.collection.immutable.Vector<Integer> scalaPersistent;
        clojure.lang.PersistentVector clojurePersistent;
        fj.data.Seq<Integer> fjavaPersistent;
        org.pcollections.PVector<Integer> pcollectionsPersistent;
        javaslang.collection.Vector<Integer> slangPersistent;
        javaslang.collection.Vector<Integer> slangPersistentInt;

        @Setup
        public void setup() {
            final Random random = new Random(0);
            ELEMENTS = getRandomValues(CONTAINER_SIZE, false, random);
            INT_ELEMENTS = Arrays2.toPrimitiveArray(int.class, ELEMENTS);
            RANDOMIZED_INDICES = Arrays2.shuffle(Array.range(0, CONTAINER_SIZE).toJavaStream().mapToInt(Integer::intValue).toArray(), random);

            EXPECTED_AGGREGATE = Array.of(ELEMENTS).reduce(JmhRunner::aggregate);

            javaMutable = create(java.util.ArrayList::new, asList(ELEMENTS), v -> areEqual(v, asList(ELEMENTS)));
            scalaPersistent = create(v -> (scala.collection.immutable.Vector<Integer>) scala.collection.immutable.Vector$.MODULE$.apply(asScalaBuffer(v)), javaMutable, v -> areEqual(asJavaCollection(v), javaMutable));
            clojurePersistent = create(clojure.lang.PersistentVector::create, javaMutable, v -> areEqual(v, javaMutable));
            pcollectionsPersistent = create(org.pcollections.TreePVector::from, javaMutable, v -> areEqual(v, javaMutable));
            fjavaPersistent = create(fj.data.Seq::fromJavaList, javaMutable, v -> areEqual(v, javaMutable));
            slangPersistent = create(javaslang.collection.Vector::ofAll, javaMutable, v -> areEqual(v, javaMutable));
            slangPersistentInt = create(v -> javaslang.collection.Vector.ofAll(INT_ELEMENTS), javaMutable, v -> areEqual(v, javaMutable) && (v.type == int.class));
        }
    }

    /** Bulk creation from array based, boxed source */
    public static class Access extends Base {
        @Benchmark
        public Object create() {
            return Arrays2.arrayCopy(Integer.class, CONTAINER_SIZE, ELEMENTS, 0, 0, CONTAINER_SIZE);
        }

        @Benchmark
        public Object set() {
            final Object[] values = new Object[CONTAINER_SIZE];
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                Arrays2.setAt(values, i, ELEMENTS[i]);
            }
            return values;
        }

        @Benchmark
        public void get(Blackhole bh) {
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                bh.consume(Arrays2.<Object> getAt(ELEMENTS, i));
            }
        }

        @Benchmark
        public int length() {
            return Arrays2.getLength(ELEMENTS);
        }
    }

    /** Bulk creation from array based, boxed source */
    public static class Create extends Base {
        @Benchmark
        public Object java_mutable() {
            final ArrayList<Integer> values = new ArrayList<>(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object java_mutable_boxed() {
            final ArrayList<Integer> values = new ArrayList<>();
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values.add(INT_ELEMENTS[i]);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            final scala.collection.immutable.Vector<?> values = scala.collection.immutable.Vector$.MODULE$.apply(scalaPersistent);
            assert Objects.equals(values, scalaPersistent);
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            final clojure.lang.PersistentVector values = clojure.lang.PersistentVector.create(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            final fj.data.Seq<Integer> values = fj.data.Seq.fromJavaList(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            final org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.from(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            final javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.ofAll(javaMutable);
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object slang_persistent_int() {
            final javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.ofAll(INT_ELEMENTS);
            assert (values.type == int.class) && areEqual(values, javaMutable);
            return values;
        }
    }

    public static class Head extends Base {
        @Benchmark
        public Object java_mutable() {
            final Object head = javaMutable.get(0);
            assert Objects.equals(head, ELEMENTS[0]);
            return head;
        }

        @Benchmark
        public Object scala_persistent() {
            final Object head = scalaPersistent.head();
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public Object clojure_persistent() {
            final Object head = clojurePersistent.nth(0);
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public Object fjava_persistent() {
            final Object head = fjavaPersistent.head();
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public Object pcollections_persistent() {
            final Object head = pcollectionsPersistent.get(0);
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public Object slang_persistent() {
            final Object head = slangPersistent.head();
            assert Objects.equals(head, javaMutable.get(0));
            return head;
        }

        @Benchmark
        public int slang_persistent_int() {
            final int head = ((int[]) slangPersistentInt.leafUnsafe(0))[0];
            assert Objects.equals(head, javaMutable.get(slangPersistentInt.leafIndex(0)));
            return head;
        }
    }

    public static class Tail extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                assert areEqual(javaMutable, asList(state.ELEMENTS));
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
            }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            java.util.List<Integer> values = state.javaMutable;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.subList(1, values.size()); // remove(0) would copy everything, but this will slow access down because of nesting
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scalaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fjavaPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = pcollectionsPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.minus(0);
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = slangPersistent;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }

        @Benchmark
        public Object slang_persistent_int() {
            javaslang.collection.Vector<Integer> values = slangPersistentInt;
            for (int i = 0; i < CONTAINER_SIZE; i++) {
                values = values.tail();
            }
            assert values.isEmpty();
            return values;
        }
    }

    /** Aggregated, randomized access to every element */
    public static class Get extends Base {
        @Benchmark
        public int java_mutable() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= javaMutable.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int scala_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= scalaPersistent.apply(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int clojure_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= (int) clojurePersistent.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= fjavaPersistent.index(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int pcollections_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= pcollectionsPersistent.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int slang_persistent() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                aggregate ^= slangPersistent.get(i);
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int slang_persistent_int() {
            int aggregate = 0;
            for (int i : RANDOMIZED_INDICES) {
                final int[] leaf = (int[]) slangPersistentInt.leafUnsafe(i);
                aggregate ^= leaf[slangPersistentInt.leafIndex(i)];
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }

    /** Randomized update of every element */
    public static class Update extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                assert areEqual(javaMutable, asList(state.ELEMENTS));
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
            }
        }

        @Benchmark
        public Object java_mutable(Initialized state) {
            final java.util.ArrayList<Integer> values = state.javaMutable;
            for (int i : RANDOMIZED_INDICES) {
                values.set(i, 0);
            }
            assert Array.ofAll(values).forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scalaPersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.updateAt(i, 0);
            }
            assert Array.ofAll(asJavaCollection(values)).forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojurePersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.assocN(i, 0);
            }
            assert Array.of(values.toArray()).forAll(e -> Objects.equals(e, 0));
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fjavaPersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.update(i, 0);
            }
            assert Array.ofAll(values).forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = pcollectionsPersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.with(i, 0);
            }
            assert Array.ofAll(values).forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = slangPersistent;
            for (int i : RANDOMIZED_INDICES) {
                values = values.update(i, 0);
            }
            assert values.forAll(e -> e == 0);
            return values;
        }

        @Benchmark
        public Object slang_persistent_int() {
            javaslang.collection.Vector<Integer> values = slangPersistentInt;
            for (int i : RANDOMIZED_INDICES) {
                values = values.update(i, 0);
            }
            assert (values.type == int.class) && values.forAll(e -> e == 0);
            return values;
        }
    }

    public static class Prepend extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(); // no initial value, as we're simulating dynamic usage
            for (Integer element : ELEMENTS) {
                values.add(0, element);
            }
            assert areEqual(Array.ofAll(values).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scala.collection.immutable.Vector$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                values = values.appendFront(element);
            }
            assert areEqual(Array.ofAll(asJavaCollection(values)).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojure.lang.PersistentVector.EMPTY;
            for (int i = 0; i < ELEMENTS.length; i++) {
                clojure.lang.PersistentVector prepended = clojure.lang.PersistentVector.create(ELEMENTS[i]);
                for (Object value : values) {
                    prepended = prepended.cons(value); // rebuild everything via append
                }
                values = prepended;
            }
            assert areEqual(Array.ofAll(values).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fj.data.Seq.empty();
            for (Integer element : ELEMENTS) {
                values = values.cons(element);
            }
            assert areEqual(Array.ofAll(values).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                values = values.plus(0, element);
            }
            assert areEqual(Array.ofAll(values).reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.empty();
            for (Integer element : ELEMENTS) {
                values = values.prepend(element);
            }
            assert areEqual(values.reverse(), javaMutable);
            return values;
        }

        @Benchmark
        public Object slang_persistent_int() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.ofAll(new int[] {ELEMENTS[0]});
            for (int i = 1; i < ELEMENTS.length; i++) {
                values = values.prepend(ELEMENTS[i]);
            }
            assert (values.type == int.class) && areEqual(values.reverse(), javaMutable);
            return values;
        }
    }

    public static class Append extends Base {
        @Benchmark
        public Object java_mutable() {
            final java.util.ArrayList<Integer> values = new java.util.ArrayList<>(); // no initial value as we're simulating dynamic usage
            for (Integer element : ELEMENTS) {
                values.add(element);
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object scala_persistent() {
            scala.collection.immutable.Vector<Integer> values = scala.collection.immutable.Vector$.MODULE$.empty();
            for (Integer element : ELEMENTS) {
                final scala.collection.immutable.Vector<Integer> newValues = values.appendBack(element);
                assert values != newValues;
                values = newValues;
            }
            assert areEqual(asJavaCollection(values), javaMutable);
            return values;
        }

        @Benchmark
        public Object clojure_persistent() {
            clojure.lang.PersistentVector values = clojure.lang.PersistentVector.EMPTY;
            for (Integer element : ELEMENTS) {
                final clojure.lang.PersistentVector newValues = values.cons(element);
                assert values != newValues;
                values = newValues;
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object fjava_persistent() {
            fj.data.Seq<Integer> values = fj.data.Seq.empty();
            for (Integer element : ELEMENTS) {
                final fj.data.Seq<Integer> newValues = values.snoc(element);
                assert values != newValues;
                values = newValues;
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object pcollections_persistent() {
            org.pcollections.PVector<Integer> values = org.pcollections.TreePVector.empty();
            for (Integer element : ELEMENTS) {
                final org.pcollections.PVector<Integer> newValues = values.plus(element);
                assert values != newValues;
                values = newValues;
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object slang_persistent() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.empty();
            for (Integer element : ELEMENTS) {
                final Vector<Integer> newValues = values.append(element);
                assert values != newValues;
                values = newValues;
            }
            assert areEqual(values, javaMutable);
            return values;
        }

        @Benchmark
        public Object slang_persistent_int() {
            javaslang.collection.Vector<Integer> values = javaslang.collection.Vector.ofAll(new int[] {ELEMENTS[0]});
            for (int i = 1; i < ELEMENTS.length; i++) {
                final Vector<Integer> newValues = values.append(ELEMENTS[i]);
                assert values != newValues;
                values = newValues;
            }
            assert (values.type == int.class) && areEqual(values, javaMutable);
            return values;
        }
    }

    public static class GroupBy extends Base {
        @Benchmark
        public Object java_mutable() {
            return javaMutable.stream().collect(Collectors.groupingBy(Integer::bitCount));
        }

        @Benchmark
        public Object scala_persistent() {
            return scalaPersistent.groupBy(JFunction.func(Integer::bitCount));
        }

        @Benchmark
        public Object slang_persistent() {
            return slangPersistent.groupBy(Integer::bitCount);
        }
    }

    /** Consume the vector one-by-one, from the front and back */
    public static class Slice extends Base {
        @Benchmark
        public void java_mutable(Blackhole bh) { // stores the whole list underneath
            java.util.List<Integer> values = javaMutable;
            while (!values.isEmpty()) {
                values = values.subList(1, values.size());
                values = values.subList(0, values.size() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void clojure_persistent(Blackhole bh) { // stores the whole list underneath
            java.util.List<?> values = clojurePersistent;
            while (!values.isEmpty()) {
                values = values.subList(1, values.size());
                values = values.subList(0, values.size() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void scala_persistent(Blackhole bh) {
            scala.collection.immutable.Vector<Integer> values = this.scalaPersistent;
            while (!values.isEmpty()) {
                values = values.slice(1, values.size());
                values = values.slice(0, values.size() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void slang_persistent(Blackhole bh) {
            javaslang.collection.Vector<Integer> values = this.slangPersistent;
            while (!values.isEmpty()) {
                values = values.slice(1, values.size());
                values = values.slice(0, values.size() - 1);
                bh.consume(values);
            }
        }

        @Benchmark
        public void slang_persistent_int(Blackhole bh) {
            javaslang.collection.Vector<Integer> values = this.slangPersistentInt;
            while (!values.isEmpty()) {
                values = values.slice(1, values.size());
                values = values.slice(0, values.size() - 1);
                bh.consume(values);
            }
        }
    }

    /* Sequential access for all elements */
    public static class Iterate extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            final java.util.ArrayList<Integer> javaMutable = new java.util.ArrayList<>();

            @Setup(Level.Invocation)
            public void initializeMutable(Base state) {
                java.util.Collections.addAll(javaMutable, state.ELEMENTS);
                assert areEqual(javaMutable, asList(state.ELEMENTS));
            }

            @TearDown(Level.Invocation)
            public void tearDown() {
                javaMutable.clear();
            }
        }

        @Benchmark
        public int java_mutable(Initialized state) {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = state.javaMutable.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int scala_persistent() {
            int aggregate = 0;
            for (final scala.collection.Iterator<Integer> iterator = scalaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int clojure_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = clojurePersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int fjava_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = fjavaPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int pcollections_persistent() {
            int aggregate = 0;
            for (final java.util.Iterator<Integer> iterator = pcollectionsPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int slang_persistent() {
            int aggregate = 0;
            for (final Iterator<Integer> iterator = slangPersistent.iterator(); iterator.hasNext(); ) {
                aggregate ^= iterator.next();
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }

        @Benchmark
        public int slang_persistent_int() {
            int aggregate = 0;
            for (int i = 0; i < slangPersistentInt.length(); ) {
                final int[] leaf = (int[]) slangPersistentInt.leafUnsafe(i);
                final int offset = slangPersistentInt.leafIndex(i);
                for (int j = offset; j < leaf.length; j++) {
                    aggregate ^= leaf[j];
                }
                i += leaf.length - offset;
            }
            assert aggregate == EXPECTED_AGGREGATE;
            return aggregate;
        }
    }
}