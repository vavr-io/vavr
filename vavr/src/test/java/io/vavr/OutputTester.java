/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import java.io.*;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Small utility that allows to test code which write to standard error or standard out.
 *
 * @author Sebastian Zarnekow
 */
public class OutputTester {

    @Test
    public void shouldNormalizeToUnixLineSeparators() {
        assertThat(captureStdOut(() -> System.out.print("\r\n"))).isEqualTo("\n");
    }

    /**
     * Encapsulate the standard output and error stream accessors.
     */
    private enum Output {
        OUT {
            @Override
            void set(PrintStream stream) {
                System.setOut(stream);
            }

            @Override
            PrintStream get() {
                return System.out;
            }
        },
        ERR {
            @Override
            void set(PrintStream stream) {
                System.setErr(stream);
            }

            @Override
            PrintStream get() {
                return System.err;
            }
        };

        /**
         * Modifier for the output slot.
         */
        abstract void set(PrintStream stream);

        /**
         * Accessor for the output slot.
         */
        abstract PrintStream get();

        /**
         * Capture the output written to this standard stream and normalize to unix line endings.
         */
        String capture(Runnable action) {
            synchronized (this) {
                try {
                    PrintStream orig = get();
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream(); PrintStream inmemory = new PrintStream(out) {
                    }) {
                        set(inmemory);
                        action.run();
                        return new String(out.toByteArray(), Charset.defaultCharset()).replace("\r\n", "\n");
                    } finally {
                        set(orig);
                    }
                } catch (IOException e) {
                    fail("Unexpected IOException", e);
                    return "UNREACHABLE";
                }
            }
        }

        /**
         * Each attempt to write the this standard output will fail with an IOException
         */
        void failOnWrite(Runnable action) {
            synchronized (this) {
                final PrintStream original = get();
                try (PrintStream failingPrintStream = failingPrintStream()) {
                    set(failingPrintStream);
                    action.run();
                } finally {
                    set(original);
                }
            }
        }

    }

    private static OutputStream failingOutputStream() {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        };
    }

    /**
     * Obtain a stream that fails on every attempt to write a byte.
     *
     * @return a new stram that will fail immediately.
     */
    public static PrintStream failingPrintStream() {
        return new PrintStream(failingOutputStream());
    }

    /**
     * Obtain a writer that fails on every attempt to write a byte.
     *
     * @return a new stram that will fail immediately.
     */
    public static PrintWriter failingPrintWriter() {
        return new PrintWriter(failingOutputStream());
    }

    /**
     * Execute the given runnable in a context, where each attempt to
     * write to stderr will fail with an {@link IOException}
     *
     * @param runnable the runnable to be executed.
     */
    public static void withFailingErrOut(Runnable runnable) {
        Output.ERR.failOnWrite(runnable);
    }

    /**
     * Execute the given runnable in a context, where each attempt to
     * write to stdout will fail with an {@link IOException}
     *
     * @param runnable the runnable to be executed.
     */

    public static void withFailingStdOut(Runnable runnable) {
        Output.OUT.failOnWrite(runnable);
    }

    /**
     * Execute the given runnable and capture everything that written
     * to stdout. The written text is normalized to unix line feeds before its returned.
     *
     * @param runnable the runnable to be executed.
     * @return the content written to stdout, normalized to unix line endings.
     */
    public static String captureStdOut(Runnable runnable) {
        return Output.OUT.capture(runnable);
    }
}
