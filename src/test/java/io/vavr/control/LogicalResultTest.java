package io.vavr.control;

import io.vavr.Tuple;
import io.vavr.Tuple1;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * The Logical result test.
 */
@SuppressWarnings("deprecation")
public class LogicalResultTest extends TestCase {

    /**
     * Test of 1.
     */
    @Test
    public void testOf1() {
        // unknown
        Tuple1<Boolean> tuple1 = Tuple.of(null);

        LogicalResult logicalResult = LogicalResult.of(tuple1);

        assertFalse(logicalResult.isSuccess());
        assertFalse(logicalResult.isFailure());
        assertTrue(logicalResult.isUnknown());
    }

    /**
     * Test of 2.
     */
    @Test
    public void testOf2() {
        // success
        Either<Boolean, Boolean> either = Either.right(false);

        LogicalResult logicalResult = LogicalResult.of(either);

        assertTrue(logicalResult.isSuccess());
        assertFalse(logicalResult.isFailure());
        assertFalse(logicalResult.isUnknown());
    }

    /**
     * Test of 3.
     */
    @Test
    public void testOf3() {
        // failure
        Option<Boolean> option = Option.none();

        LogicalResult logicalResult = LogicalResult.of(option);

        assertFalse(logicalResult.isSuccess());
        assertTrue(logicalResult.isFailure());
        assertFalse(logicalResult.isUnknown());
    }

    /**
     * Test failure.
     */
    @Test
    public void testFailure() {
        LogicalResult failure = LogicalResult.failure();

        assertEquals(failure, LogicalResult.FAILURE_INSTANCE);
    }

    /**
     * Test success.
     */
    @Test
    public void testSuccess() {
        LogicalResult success = LogicalResult.success();

        assertEquals(success, LogicalResult.SUCCESS_INSTANCE);
    }

    /**
     * Test unknown.
     */
    @Test
    public void testUnknown() {
        LogicalResult unknown = LogicalResult.unknown();

        assertEquals(unknown, LogicalResult.UNKNOWN_INSTANCE);
    }
}