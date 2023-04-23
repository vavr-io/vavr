package io.vavr.collection.champ;

import java.io.Serial;
import java.io.Serializable;

/**
 * An object with a unique identity within this VM.
 */
public class IdentityObject implements Serializable {
    @Serial
    private final static long serialVersionUID = 0L;

    public IdentityObject() {
    }
}
