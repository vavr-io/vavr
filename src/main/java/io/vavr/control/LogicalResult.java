package io.vavr.control;

import io.vavr.Tuple1;

import java.util.Objects;

/**
 * The Results of Logical function returns.
 */
@SuppressWarnings("deprecation")
public interface LogicalResult extends Results {

    long serialVersionUID = 1L;

    // -- Create from value

    /**
     * Of logical result.
     *
     * @param tuple1 the tuple 1
     * @return the logical result
     */
    static LogicalResult of(Tuple1<Boolean> tuple1) {
        Objects.requireNonNull(tuple1, "tuple1 is null");
        if (tuple1._1() == null) {
            return unknown();
        }
        if (tuple1._1()) {
            return success();
        } else {
            return failure();
        }
    }

    /**
     * Of logical result.
     *
     * @param either the either
     * @return the logical result
     */
    static <L, R> LogicalResult of(Either<L, R> either) {
        Objects.requireNonNull(either, "either is null");
        if (either.isRight()) {
            return success();
        } else {
            return failure();
        }
    }

    /**
     * Of logical result.
     *
     * @param option the option
     * @return the logical result
     */
    static <T> LogicalResult of(Option<T> option) {
        Objects.requireNonNull(option, "option is null");
        if (!option.isEmpty()) {
            return success();
        } else {
            return failure();
        }
    }

    // -- Failure

    /**
     * Failure instance.
     */
    LogicalResult FAILURE_INSTANCE = new FailureLogicalResult();

    /**
     * Failure logical result.
     *
     * @return the logical result
     */
    static LogicalResult failure() {
        return FAILURE_INSTANCE;
    }

    // -- Success

    /**
     * Success instance.
     */
    LogicalResult SUCCESS_INSTANCE = new SuccessLogicalResult();

    /**
     * Success logical result.
     *
     * @return the logical result
     */
    static LogicalResult success() {
        return SUCCESS_INSTANCE;
    }

    // -- Unknown

    /**
     * Unknown instance.
     */
    LogicalResult UNKNOWN_INSTANCE = new UnknownLogicalResult();

    /**
     * Unknown logical result.
     *
     * @return the logical result
     */
    static LogicalResult unknown() {
        return UNKNOWN_INSTANCE;
    }

    // -- Result class

    /**
     * The Abstract logical result.
     */
    abstract class AbstractLogicalResult implements LogicalResult {

        private static final long serialVersionUID = 1L;

        /**
         * The constant TRUE.
         */
        protected static final Boolean TRUE = Boolean.TRUE;
        /**
         * The constant FALSE.
         */
        protected static final Boolean FALSE = Boolean.FALSE;

        protected AbstractLogicalResult() {
        }
    }

    /**
     * The Success logical result.
     */
    final class SuccessLogicalResult extends AbstractLogicalResult {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean isSuccess() {
            return TRUE;
        }

        @Override
        public boolean isFailure() {
            return FALSE;
        }

        @Override
        public boolean isUnknown() {
            return FALSE;
        }

        protected SuccessLogicalResult() {
        }
    }

    /**
     * The Failure logical result.
     */
    final class FailureLogicalResult extends AbstractLogicalResult {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean isSuccess() {
            return FALSE;
        }

        @Override
        public boolean isFailure() {
            return TRUE;
        }

        @Override
        public boolean isUnknown() {
            return FALSE;
        }

        protected FailureLogicalResult() {
        }
    }

    /**
     * The Unknown logical result.
     */
    final class UnknownLogicalResult extends AbstractLogicalResult {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean isSuccess() {
            return FALSE;
        }

        @Override
        public boolean isFailure() {
            return FALSE;
        }

        @Override
        public boolean isUnknown() {
            return TRUE;
        }

        protected UnknownLogicalResult() {
        }
    }
}
