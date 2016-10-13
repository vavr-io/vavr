/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package client;

import com.google.gwt.junit.client.GWTTestCase;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.concurrent.Promise;

import java.util.concurrent.atomic.AtomicBoolean;

public class GwtTestConcurrent extends GWTTestCase {

    @Override public String getModuleName()  {
        return "TestModule";
    }

    public void testFutureSuccess() {
        AtomicBoolean onCompleteCalled = new AtomicBoolean(false);
        Promise<String> promise = Promise.make();
        promise.future().onComplete(value -> onCompleteCalled.set(true));
        promise.success("value");

        if (!onCompleteCalled.get()) {
            fail("onComplete handler should have been called");
        }
    }

    public void testFutureFailure() {
        AtomicBoolean onFailureCalled = new AtomicBoolean(false);
        Promise<String> promise = Promise.make();
        promise.future().onFailure(e -> onFailureCalled.set(true));
        promise.failure(new Exception());

        if (!onFailureCalled.get()) {
            fail("onFailure handler should have been called");
        }
    }

    public void testFutureSequence() {
        AtomicBoolean onCompleteCalled = new AtomicBoolean(false);
        Promise<String> promise1 = Promise.make();
        Promise<String> promise2 = Promise.make();
        Future.sequence(List.of(promise1.future(), promise2.future()))
              .onComplete(results -> onCompleteCalled.set(true));
        promise1.success("success1");
        promise2.success("success2");

        if (!onCompleteCalled.get()) {
            fail("onComplete handler should have been called");
        }
    }
}
