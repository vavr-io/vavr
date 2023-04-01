/*
 * @(#)UniqueId.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;

import java.io.Serializable;

/**
 * An object with a unique identity within this VM.
 */
class IdentityObject implements Serializable {
    private final static long serialVersionUID = 0L;

    public IdentityObject() {
    }
}
