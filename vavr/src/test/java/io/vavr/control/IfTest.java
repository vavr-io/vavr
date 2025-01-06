package io.vavr.control;

import io.vavr.AbstractValueTest;
import io.vavr.Value;
import org.junit.jupiter.api.Test;

import java.util.Objects;


@SuppressWarnings("unused")
public class IfTest extends AbstractValueTest{


    @Override
    protected <T> Value<T> empty() {
        return null;
    }

    @Override
    protected <T> Value<T> of(T element) {
        return null;
    }

    @Override
    @SafeVarargs
    protected final  <T> Value<T> of(T... elements) {
        return null;
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 0;
    }

    @Test
    public void ifNewTest() {
        assertThat(If.ifNew(value(null), Objects::isNull, "default")).isEqualTo("default");
        assertThat(If.ifNew(value("a"), Objects::isNull, "default")).isEqualTo("a");
    }



    public static boolean randomBoolean() {
        return Math.random() < 0.5;
    }


    public static <V> V value(V v) {
        System.out.println("invoke method 'value'");
        return v;
    }

}
