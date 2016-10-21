package client;

import com.google.gwt.junit.client.GWTTestCase;
import javaslang.Function1;
import javaslang.Tuple;
import javaslang.Tuple1;
import javaslang.collection.*;

public class CollectionsTestGwt extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "TestModule";
    }

    public void testTuple() {
        Tuple1<Integer> t = Tuple.of(1);
        assertEquals((int) t._1, 1);
        assertEquals((int) t._1(), 1);
    }

    private void applyCollection(Function1<char[], Traversable<Character>> factory) {
        Traversable<Character> traversable = factory.apply(new char[] {'a', 'b', 'c'});
        assertEquals(traversable.count(i -> i != 'b'), 2);
    }

    public void testCompileArray() {
        applyCollection(Array::ofAll);
    }

    public void testCompileBitSet() {
        applyCollection(BitSet::ofAll);
    }

    public void testCompileCharSeq() {
        applyCollection(chars -> CharSeq.ofAll(Iterator.ofAll(chars)));
    }

    public void testCompileHashSet() {
        applyCollection(HashSet::ofAll);
    }

    public void testCompileLinkedHashSet() {
        applyCollection(LinkedHashSet::ofAll);
    }

    public void testCompileList() {
        applyCollection(List::ofAll);
    }

    public void testCompilePriorityQueue() {
        applyCollection(chars -> PriorityQueue.ofAll(Iterator.ofAll(chars)));
    }

    public void testCompileQueue() {
        applyCollection(Queue::ofAll);
    }

    public void testCompileTreeSet() {
        applyCollection(chars -> TreeSet.ofAll(Iterator.ofAll(chars)));
    }

    @SuppressWarnings("Convert2MethodRef")
    public void testCompileVector() {
        applyCollection(chars -> Vector.ofAll(chars));
    }
}