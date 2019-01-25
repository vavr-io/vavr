package io.vavr.control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckedSupplierTest {

    // -- .get()

    @Test
    void shouldGetExceptionalCase() {
        final CheckedSupplier f = () -> { throw new Exception("error"); };
        assertEquals(
                "error",
                assertThrows(Exception.class, f::get).getMessage()
        );
    }
}
