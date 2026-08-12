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

def genMapOfEntriesTests(targetTest: String): Unit = {

  def genAllArity(im: ImportManager,
              mapName: String, mapBuilder: String,
              builderComparator: Boolean, keyComparator: Boolean): String = {
    val test = im.getType("org.junit.jupiter.api.Test")
    val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
    val assertThrows = im.getStatic("org.junit.jupiter.api.Assertions.assertThrows")
    val naturalComparator = if (builderComparator || keyComparator) im.getStatic(s"io.vavr.collection.Comparators.naturalComparator") else ""
    val map = im.getType(s"io.vavr.collection.$mapName")
    (1 to VARARGS).gen(arity => xs"""
      @$test
      public void shouldConstructFrom${arity}Entries${if(builderComparator) "WithBuilderComparator" else ""}${if(keyComparator) "WithKeyComparator" else ""}${mapBuilder.capitalize}() {
        final $map<Integer, String> map =
          $map${if (mapBuilder.isEmpty) "" else s".$mapBuilder"}${if (builderComparator) s"($naturalComparator())" else if (mapBuilder.isEmpty) "" else "()"}
          .of(${if(keyComparator) s"$naturalComparator(), " else ""}${(1 to arity).gen(j => s"""$j, "$j"""")(using ", ")});
        $assertThat(map.size()).isEqualTo($arity);
        ${(1 to arity).gen(j => {
          s"""${if (mapBuilder.isEmpty) "" else s"$assertThat(map.get($j).get() instanceof ${im.getType(s"io.vavr.collection.${mapBuilder.substring(4)}")}).isTrue();\n"}$assertThat(map.get($j).get()${if (mapName.contains("Multimap")) ".head()" else ""}).isEqualTo("$j");"""
        })(using "\n")}
      }
    """)(using "\n\n")
  }

  def genMapOfEntriesTest(mapName: String): Unit = {
    val mapBuilders:List[String] = if (mapName.contains("Multimap")) List("withSeq", "withSet", "withSortedSet") else List("")
    val keyComparators:List[Boolean] = if (mapName.startsWith("Tree")) List(true, false) else List(false)
    genVavrFile("io.vavr.collection", s"${mapName}OfEntriesTest", targetTest) ((im: ImportManager, packageName, className) => {
      xs"""
      public class ${mapName}OfEntriesTest {
        ${mapBuilders.flatMap(mapBuilder => {
        val builderComparators:List[Boolean] = if (mapBuilder.contains("Sorted")) List(true, false) else List(false)
        builderComparators.flatMap(builderComparator => keyComparators.map(keyComparator =>
          xs"""
            ${genAllArity(im, mapName, mapBuilder, builderComparator, keyComparator)}
            """
        ))
      }).mkString("\n\n")}
      }
      """
    })
  }

  genMapOfEntriesTest("HashMap")
  genMapOfEntriesTest("LinkedHashMap")
  genMapOfEntriesTest("TreeMap")
  genMapOfEntriesTest("HashMultimap")
  genMapOfEntriesTest("LinkedHashMultimap")
  genMapOfEntriesTest("TreeMultimap")

}
