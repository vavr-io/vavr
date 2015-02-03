/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.lang;

import java.io.Serializable;

/**
 * Checked version of java.lang.Runnable.
 * Essentially the same as {@code CheckedFunction0<Void>}, or short {@code X0<Void>}.
 */
@FunctionalInterface
public interface CheckedRunnable extends Serializable {

    void run() throws Throwable;
}
