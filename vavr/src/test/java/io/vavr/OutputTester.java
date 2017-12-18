/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2017 Vavr, http://vavr.io
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

import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Small utility that allows to test code which write to standard error or standard out.
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

        abstract void set(PrintStream stream);

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

    public static PrintStream failingPrintStream() {
        return new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
    }

    public static PrintWriter failingPrintWriter() {
        return new PrintWriter(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }
        });
    }

    public static void withFailingErrOut(Runnable runnable) {
        Output.ERR.failOnWrite(runnable);
    }

    public static void withFailingStdOut(Runnable runnable) {
        Output.OUT.failOnWrite(runnable);
    }

    public static String captureStdOut(Runnable action) {
        return Output.OUT.capture(action);
    }

    public static String captureErrOut(Runnable action) {
        return Output.ERR.capture(action);
    }
}
