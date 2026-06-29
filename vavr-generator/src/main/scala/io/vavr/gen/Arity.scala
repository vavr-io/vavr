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
import JavaGenerator.ImportManager

/**
 * Pre-computed type variable strings for a given arity.
 *
 * Centralizes the repeated patterns like "T1, T2, T3", "<T1, T2, T3, R>", "t1, t2, t3", etc.
 * that are rebuilt identically throughout the generator.
 *
 * @param i the arity (number of type parameters), 0 to N
 */
case class Arity(i: Int) {
  val generics: String = (1 to i).gen(j => s"T$j")(using ", ") // "T1, T2, T3"
  val genericsTuple: String = if (i > 0) s"<$generics>" else "" // "<T1, T2, T3>" or "" for arity 0
  val fullGenerics: String = s"<${(i > 0).gen(s"$generics, ")}R>" // "<T1, T2, T3, R>"
  val wideGenerics: String = (1 to i).gen(j => s"? super T$j")(using ", ") // "? super T1, ? super T2"
  val covariantGenerics: String = (1 to i).gen(j => s"? extends T$j")(using ", ") // "? extends T1, ? extends T2"
  val fullWideGenerics: String = s"<${(i > 0).gen(s"$wideGenerics, ")}? extends R>" // "<? super T1, ? super T2, ? extends R>"
  val genericsReversed: String = (1 to i).reverse.gen(j => s"T$j")(using ", ") // "T3, T2, T1"
  val genericsFunction: String = if (i > 0) s"$generics, " else "" // "T1, T2, T3, " or "" for arity 0 (trailing comma for prepending to R)
  val genericsReversedFunction: String = if (i > 0) s"$genericsReversed, " else "" // "T3, T2, T1, " or "" for arity 0 (trailing comma)
  val paramsDecl: String = (1 to i).gen(j => s"T$j t$j")(using ", ") // "T1 t1, T2 t2, T3 t3"
  val params: String = (1 to i).gen(j => s"t$j")(using ", ") // "t1, t2, t3"
  val paramsReversed: String = (1 to i).reverse.gen(j => s"t$j")(using ", ") // "t3, t2, t1"
  val tupled: String = (1 to i).gen(j => s"t._$j")(using ", ") // "t._1, t._2, t._3"
  val underscoreParams: String = (1 to i).gen(j => s"_$j")(using ", ") // "_1, _2, _3"

  /** Generates @param javadoc tags for type parameters T1..Ti */
  def typeParamDocs(description: Int => String = j => s"type of the ${j.ordinal} element"): String =
    (0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> ${description(j)}")(using "\n")
}

/**
 * Returns the standard java.util.function type name for the given arity.
 * Arities 0, 1, and 2 map to Supplier, Function, and BiFunction respectively;
 * higher arities map to Vavr's FunctionN.
 */
def javaFunctionType(i: Int, im: ImportManager): String = i match {
  case 0 => im.getType("java.util.function.Supplier")
  case 1 => im.getType("java.util.function.Function")
  case 2 => im.getType("java.util.function.BiFunction")
  case _ => s"Function$i"
}
