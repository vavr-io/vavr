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

/**
 * This exception is temporarily used during development in order to indicate that an implementation
 * is missing.
 *
 * <p>The idiomatic way is to use one of {@link API#TODO()} and {@link API#TODO(String)}.
 */
public class NotImplementedError extends Error {

  private static final long serialVersionUID = 1L;

  /**
   * Creates a {@code NotImplementedError} containing the message "an implementation is missing".
   */
  public NotImplementedError() {
    super("An implementation is missing.");
  }

  /**
   * Creates a {@code NotImplementedError} containing the given {@code message}.
   *
   * @param message A text that describes the error
   */
  public NotImplementedError(String message) {
    super(message);
  }
}
