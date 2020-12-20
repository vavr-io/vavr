package io.vavr.control;

import io.vavr.Tuple1;
import io.vavr.Tuple2;
import io.vavr.Tuple3;

import java.util.Objects;

/**
 * The Results of Process function returns.
 *
 * @param <T> the process result type
 */
@SuppressWarnings("deprecation")
public interface ProcessResult<T> extends Results {

    long serialVersionUID = 1L;

    // -- Create from value

    /**
     * Of process result.
     *
     * @param <T>    the process result type
     * @param tuple1 the tuple 1
     * @return the process result
     */
    static <T> ProcessResult<T> of(Tuple1<Boolean> tuple1) {
        Objects.requireNonNull(tuple1, "tuple1 is null");
        if (tuple1._1() == null) {
            return new UnknownProcessResult<>(null, null);
        }
        if (tuple1._1()) {
            return new SuccessProcessResult<>(null, null);
        } else {
            return new FailureProcessResult<>(null, null);
        }
    }

    /**
     * Of process result.
     *
     * @param <T>    the process result type
     * @param tuple2 the tuple 2
     * @return the process result
     */
    static <T> ProcessResult<T> of(Tuple2<Boolean, String> tuple2) {
        Objects.requireNonNull(tuple2, "tuple2 is null");
        if (tuple2._1() == null) {
            return new UnknownProcessResult<>(tuple2._2(), null);
        }
        if (tuple2._1()) {
            return new SuccessProcessResult<>(tuple2._2(), null);
        } else {
            return new FailureProcessResult<>(tuple2._2(), null);
        }
    }

    /**
     * Of process result.
     *
     * @param <Obj>  the process result type
     * @param tuple3 the tuple 3
     * @return the process result
     */
    static <Obj> ProcessResult<Obj> of(Tuple3<Boolean, String, Obj> tuple3) {
        Objects.requireNonNull(tuple3, "tuple3 is null");
        if (tuple3._1() == null) {
            return new UnknownProcessResult<>(tuple3._2(), tuple3._3());
        }
        if (tuple3._1()) {
            return new SuccessProcessResult<>(tuple3._2(), tuple3._3());
        } else {
            return new FailureProcessResult<>(tuple3._2(), tuple3._3());
        }
    }

    /**
     * Of process result.
     *
     * @param <L>    the type parameter
     * @param <T>    the process result type
     * @param either the either
     * @return the process result
     */
    static <L, T> ProcessResult<T> of(Either<L, T> either) {
        Objects.requireNonNull(either, "either is null");
        if (either.isRight()) {
            return new SuccessProcessResult<>(null, either.get());
        } else {
            return new FailureProcessResult<>(either.getLeft().toString(), null);
        }
    }

    /**
     * Of process result.
     *
     * @param <T>    the process result type
     * @param option the option
     * @return the process result
     */
    static <T> ProcessResult<T> of(Option<T> option) {
        Objects.requireNonNull(option, "option is null");
        if (!option.isEmpty()) {
            return new SuccessProcessResult<>(null, option.get());
        } else {
            return new FailureProcessResult<>(null, null);
        }
    }

    // -- Failure

    /**
     * Failure process result.
     *
     * @param errorMessage the error message
     * @return the process result
     */
    static ProcessResult<Void> failure(String errorMessage) {
        return new FailureProcessResult<>(errorMessage, null);
    }

    /**
     * Failure process result.
     *
     * @param <Obj>         the process result type
     * @param errorMessage  the error message
     * @param processResult the process result
     * @return the process result
     */
    static <Obj> ProcessResult<Obj> failure(String errorMessage, Obj processResult) {
        return new FailureProcessResult<>(errorMessage, processResult);
    }

    // -- Success

    /**
     * Success instance.
     */
    ProcessResult<Void> SUCCESS_INSTANCE = new SuccessProcessResult<>(null, null);

    /**
     * Success process result.
     *
     * @return the process result
     */
    static ProcessResult<Void> success() {
        return SUCCESS_INSTANCE;
    }

    /**
     * Success process result.
     *
     * @param <Obj>         the process result type
     * @param processResult the process result
     * @return the process result
     */
    static <Obj> ProcessResult<Obj> success(Obj processResult) {
        return new SuccessProcessResult<>(null, processResult);
    }

    // -- Unknown

    /**
     * Unknown process result.
     *
     * @param errorMessage the error message
     * @return the process result
     */
    static ProcessResult<Void> unknown(String errorMessage) {
        return new UnknownProcessResult<>(errorMessage, null);
    }

    /**
     * Unknown process result.
     *
     * @param <Obj>         the process result type
     * @param errorMessage  the error message
     * @param processResult the process result
     * @return the process result
     */
    static <Obj> ProcessResult<Obj> unknown(String errorMessage, Obj processResult) {
        return new UnknownProcessResult<>(errorMessage, processResult);
    }

    // -- Abstract func

    /**
     * Gets error message.
     *
     * @return the error message
     */
    String getErrorMessage();

    /**
     * Get process result.
     *
     * @return the process result
     */
    T get();

    // -- Result class

    /**
     * The Abstract process result.
     *
     * @param <T> the process result type
     */
    abstract class AbstractProcessResult<T> implements ProcessResult<T> {

        private static final long serialVersionUID = 1L;

        /**
         * The Process error message.
         */
        protected final String processErrorMessage;
        /**
         * The Process result.
         */
        protected final T processResult;

        /**
         * Instantiates a new Abstract process result.
         *
         * @param processErrorMessage the process error message
         * @param processResult       the process result
         */
        protected AbstractProcessResult(String processErrorMessage, T processResult) {
            this.processErrorMessage = processErrorMessage;
            this.processResult = processResult;
        }

        @Override
        public String getErrorMessage() {
            return processErrorMessage;
        }

        @Override
        public T get() {
            return processResult;
        }
    }

    /**
     * The Success process result.
     *
     * @param <T> the process result type
     */
    final class SuccessProcessResult<T> extends AbstractProcessResult<T> {

        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new Success process result.
         *
         * @param processErrorMessage the process error message
         * @param processResult       the process result
         */
        protected SuccessProcessResult(String processErrorMessage, T processResult) {
            super(processErrorMessage, processResult);
        }

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
    }

    /**
     * The Failure process result.
     *
     * @param <T> the process result type
     */
    final class FailureProcessResult<T> extends AbstractProcessResult<T> {

        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new Failure process result.
         *
         * @param processErrorMessage the process error message
         * @param processResult       the process result
         */
        protected FailureProcessResult(String processErrorMessage, T processResult) {
            super(processErrorMessage, processResult);
        }

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
    }

    /**
     * The Unknown process result.
     *
     * @param <T> the process result type
     */
    final class UnknownProcessResult<T> extends AbstractProcessResult<T> {

        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new Unknown process result.
         *
         * @param processErrorMessage the process error message
         * @param processResult       the process result
         */
        protected UnknownProcessResult(String processErrorMessage, T processResult) {
            super(processErrorMessage, processResult);
        }

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
    }
}
