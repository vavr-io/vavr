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
package io.vavr.gen

import Generator._
import JavaGenerator._
import Config._

/**
 * Generator of Match
 */
def genAPI(targetMain: String): Unit = {

  genVavrFile("io.vavr", "API", targetMain)(genAPI)

  def genAPI(im: ImportManager, packageName: String, className: String): String = {

    xs"""
      /**
       * The most basic Vavr functionality is accessed through this API class.
       *
       * <pre>{@code 
       * import static io.vavr.API.*;
       * }</pre>
       *
       * <h2>For-comprehension</h2>
       * <p>
       * The {@code For}-comprehension is syntactic sugar for nested for-loops. We write
       *
       * <pre>{@code 
       * // lazily evaluated
       * Iterator<R> result = For(iterable1, iterable2, ..., iterableN).yield(f);
       * }</pre>
       *
       * or
       *
       * <pre>{@code 
       * Iterator<R> result =
       *     For(iterable1, v1 ->
       *         For(iterable2, v2 ->
       *             ...
       *             For(iterableN).yield(vN -> f.apply(v1, v2, ..., vN))
       *         )
       *     );
       * }</pre>
       *
       * instead of
       *
       * <pre>{@code 
       * for(T1 v1 : iterable1) {
       *     for (T2 v2 : iterable2) {
       *         ...
       *         for (TN vN : iterableN) {
       *             R result = f.apply(v1, v2, ..., VN);
       *             //
       *             // We are forced to perform side effects to do s.th. meaningful with the result.
       *             //
       *         }
       *     }
       * }
       * }</pre>
       *
       * Please note that values like Option, Try, Future, etc. are also iterable.
       * <p>
       * Given a suitable function
       * f: {@code (v1, v2, ..., vN) -> ...} and {@code 1 <= N <= 8} iterables, the result is a Stream of the
       * mapped cross product elements.
       *
       * <pre>{@code 
       * { f(v1, v2, ..., vN) | v1 ∈ iterable1, ... vN ∈ iterableN }
       * }</pre>
       *
       * As with all Vavr Values, the result of a For-comprehension can be converted
       * to standard Java library and Vavr types.
       * @author Daniel Dietrich, Grzegorz Piwowarek
       */
      public final class API {

          private API() {
          }

          ${genAPIShortcuts(im)}

          ${genAPIAliases(im)}

          ${genAPIJavaTypeTweaks(im)}

          ${genAPIForComprehensions(im, isLazy = false)}

          ${genAPIForComprehensions(im, isLazy = true)}

          ${genAPIMatch(im)}
      }
    """
  }
}
