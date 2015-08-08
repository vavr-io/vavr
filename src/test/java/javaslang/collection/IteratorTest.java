package javaslang.collection;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IteratorTest /* TODO: extends AbstractSeqTest */ {

    // TODO remove
    @Test
    public void shouldFilterNonEmptyTraversable() {
        Iterator<Integer> it = List.of(1, 2, 3, 4).iterator();
        assertThat(List.ofAll(() -> it.filter(i -> i % 2 == 0))).isEqualTo(List.of(2, 4));
    }

}
