/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2025 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr;

import org.junit.jupiter.api.Test;

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
     * @return a new stream that will fail immediately.
     */
    public static PrintStream failingPrintStream() {
        return new PrintStream(failingOutputStream());
    }

    /**
     * Obtain a writer that fails on every attempt to write a byte.
     *
     * @return a new stream that will fail immediately.
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

    /**
     * Execute the given runnable and capture everything that written
     * to stderr. The written text is normalized to unix line feeds before its returned.
     *
     * @param runnable the runnable to be executed.
     * @return the content written to stderr, normalized to unix line endings.
     */
    public static String captureErrOut(Runnable runnable) {
        return Output.ERR.capture(runnable);
    }
}
