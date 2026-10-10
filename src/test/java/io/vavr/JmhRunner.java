/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Programmatic JMH entry point. A benchmark name regex may be passed as the first argument
 * (defaults to all benchmarks).
 */
public final class JmhRunner {

    private JmhRunner() {
    }

    public static void main(String[] args) throws Exception {
        final String include = args.length > 0 ? args[0] : ".*Benchmark.*";
        final Options options = new OptionsBuilder()
                .include(include)
                .shouldDoGC(true)
                .build();
        new Runner(options).run();
    }
}
