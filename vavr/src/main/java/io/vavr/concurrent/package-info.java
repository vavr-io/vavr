/**
 * This package contains basic building blocks for creating fast, asynchronous, non-blocking parallel code.
 * <p>
 * A {@linkplain io.vavr.concurrent.Future} represents an asynchronous task. It is a placeholder for a
 * value that becomes available at some point. With the help of {@code Future} we efficiently perform many non-blocking
 * operations in parallel. The value of a Future is supplied concurrently and can subsequently be used. Multiple
 * concurrent tasks represented by Futures can be composed to a single Future.
 */
package io.vavr.concurrent;
