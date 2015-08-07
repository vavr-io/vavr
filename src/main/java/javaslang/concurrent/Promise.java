/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.control.Failure;
import javaslang.control.Option;
import javaslang.control.Success;
import javaslang.control.Try;

import java.util.concurrent.CancellationException;

/**
 * Promise is a way of creating a {@link javaslang.concurrent.Future} that can be fulfilled with either a success or failure later.
 * The Future can be obtained with {@link #future()} and success with {@link #success(Object)} or {@link #failure(Throwable)}.
 *
 * @param <T> The type of this Promise and Future's return type.
 *
 * @since 2.0.0
 */
public class Promise<T> {

    private final Future<T> future;
	private volatile boolean completed = false;

    /**
     * Creates a new Promise with an inner Future.
     */
    public Promise(){
        this.future = new Future<>();
    }

    /**
     * @return The {@link javaslang.concurrent.Future} created by this Promise
     */
    public Future<T> future(){
        return future;
    }

    /**
     * Complete the attached Future with this value, triggering all call backs assigned to it.
     * @param t Value to complete the Future with.
     * @throws IllegalStateException if this Promise has already been completed.
     */
    public void success(T t){
	    complete(new Success<>(t));
    }

    /**
     * Complete the attached Future with this exception, triggering all call backs assigned to it.
     * @param e Exception to complete the Future with.
     * @throws IllegalStateException if this Promise has already been completed.
     */
    public void failure(Throwable e){
	    complete(new Failure<>(e));
    }

    /**
     * Complete the attached Future with this Try, which will call success or failure depending on the Try's contents and weil trigger all call backs assigned to it.
     * @param source Try containing either a successful value or a failure exception.
     * @throws IllegalStateException if this Promise has already been completed.
     */
    public void complete(Try<T> source){
	    if(!completed){
		    completed = true;
	        future.complete(source);
	    } else
		    throw new IllegalStateException("This Promise has already been completed!");
    }

    /**
     * Complete the attached Future with the result of this Future, making them functionally the same.
     * @param source Future who's value will be passed on to this Future when it completes.
     * @throws IllegalStateException if this Promise has already been completed.
     * @throws IllegalArgumentException if the given Future is the same one created by this Promise,
     * as the Future can't succeed by itself, this would result in a Future that can never complete.
     */
    public void completeWith(Future<T> source) {
	    if(source == future)
		    throw new IllegalArgumentException("Can't complete a Future with itself!");

	    if(!completed){
            source.onCompletedTry(this::complete);
	    } else
		    throw new IllegalStateException("This Promise has already been completed!");
    }

    /**
     *
     * @return True if this Promise has already been success, False if it has not.
     */
    public boolean isCompleted(){
        return completed;
    }

    /**
     * Retrieves the value of this Promise.
     * @return None if this Promise has not been success yet, or Some containing a Try holding either the successful result or exception.
     */
    public Option<Try<T>> value(){
        return future.value();
    }

    /**
     * Completes this Promise with a {@link java.util.concurrent.CancellationException}.
     */
    public void cancel(){
        failure(new CancellationException("Promise was broken!"));
    }
}
