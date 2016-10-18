/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * GWT emulated implementation of {@link ExecutorService} which executes all tasks in the current thread.
 */
public class CurrentThreadExecutorService implements ExecutorService {

    @Override public Future<?> submit(Runnable task) {
        task.run();

        return new CurrentThreadFuture<>(null, null);
    }

    @Override public void execute(Runnable command) {
        command.run();
    }
}
