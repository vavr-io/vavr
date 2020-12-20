package io.vavr.control;

import java.io.Serializable;

/**
 * The Results of function returns.
 */
@SuppressWarnings("deprecation")
public interface Results extends Serializable {

    long serialVersionUID = 1L;

    /**
     * The constant TRUE.
     */
    Boolean TRUE = Boolean.TRUE;
    /**
     * The constant FALSE.
     */
    Boolean FALSE = Boolean.FALSE;

    /**
     * Checks if this is a Success.
     *
     * @return true, if this is a Success, otherwise false, if this is a Failure/Unknown
     */
    boolean isSuccess();

    /**
     * Checks if this is a Failure.
     *
     * @return true, if this is a Failure, otherwise false, if this is a Success/Unknown
     */
    boolean isFailure();

    /**
     * Checks if this is a Unknown.
     *
     * @return true, if this is a Unknown, otherwise false, if this is a Success/Failure
     */
    boolean isUnknown();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
