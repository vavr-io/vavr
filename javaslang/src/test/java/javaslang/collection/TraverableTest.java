package javaslang.collection;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TraverableTest {

    // -- max()

    @Test
    public void maxShouldBeEmpty() {
         assertTrue(Iterator.empty().max().isEmpty());
    }

    @Test
    public void maxShouldBeTheOnlyValue() {
        assertEquals(Integer.valueOf(5), Iterator.of(5).max().get());
    }

    @Test
    public void maxShouldBeTheMaxValue() {
        assertEquals(Integer.valueOf(3), Iterator.of(2, 3).max().get());
    }

    // -- min()

    @Test
    public void minShouldBeEmpty() {
        assertTrue(Iterator.empty().min().isEmpty());
    }

    @Test
    public void minShouldBeTheOnlyValue() {
        assertEquals(Integer.valueOf(5), Iterator.of(5).min().get());
    }

    @Test
    public void minShouldBeTheMinValue() {
        assertEquals(Integer.valueOf(2), Iterator.of(2, 3).min().get());
    }

}
