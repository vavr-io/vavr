/**
 * This package contains basic building blocks creating fast, asynchronous, non-blocking parallel code.
 * <p>
 * A {@linkplain javaslang.concurrent.Future} represents an asynchronous read-only task. It is a placeholder for a
 * value that becomes available at some point. With the help of {@code Future} we efficiently perform many non-blocking
 * operations in parallel. The value of a Future is supplied concurrently and can subsequently be used. Multiple
 * concurrent tasks represented by Futures can be composed to a single Future.
 * <p>
 * While Futures are concurrent read-only tasks, a {@linkplain javaslang.concurrent.Promise} creates a writable-once
 * Future. The {@code Promise} is used to complete its contained {@code Future}.
 *
 * @since 2.0.0
 */
package javaslang.concurrent;
