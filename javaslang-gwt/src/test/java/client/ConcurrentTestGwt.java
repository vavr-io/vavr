/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.concurrent.Promise;

public class ConcurrentTestGwt extends GWTTestCase {

    @Override public String getModuleName()  {
        return "TestModule";
    }

    public void testFutureSuccess() {
        boolean[] onCompleteCalled = new boolean[] { false };
        Promise<String> promise = Promise.make();
        promise.future().onComplete(value -> {
            onCompleteCalled[0] = true;
            assertEquals("value", value.get());
        });
        promise.success("value");
        assertTrue("onComplete handler should have been called", onCompleteCalled[0]);
    }

    public void testFutureFailure() {
        boolean[] onFailureCalled = new boolean[] { false };
        Promise<String> promise = Promise.make();
        promise.future().onFailure(e -> {
            onFailureCalled[0] = true;
            assertEquals("message", e.getMessage());
        });
        promise.failure(new Exception("message"));
        assertTrue("onFailure handler should have been called", onFailureCalled[0]);
    }

    public void testFutureSequence() {
        boolean[] onCompleteCalled = new boolean[] { false };
        Promise<String> promise1 = Promise.make();
        Promise<String> promise2 = Promise.make();
        Future.sequence(List.of(promise1.future(), promise2.future()))
              .onComplete(results -> {
                  onCompleteCalled[0] = true;
                  assertEquals(2, results.get().size());
              });
        promise1.success("success1");
        promise2.success("success2");
        assertTrue("onComplete handler should have been called", onCompleteCalled[0]);
    }
}
