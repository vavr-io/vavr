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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Small utility that allows to test code which write to standard error or standard out.
 *
 * @author Sebastian Zarnekow
 */
public class OutputTester {

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
}
