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
 * Generator of Tuple tests
 */
def genTupleTests(targetTest: String): Unit = {

  def genArgsForComparing(digits: Int, p: Int): String = {
    (1 to digits).gen(i => if(i == p) "1" else "0")(using ", ")
  }

  (0 to N).foreach(i => {

    genVavrFile("io.vavr", s"Tuple${i}Test", targetTest)((im: ImportManager, packageName, className) => {

      val test = im.getType("org.junit.jupiter.api.Test")
      val assertThrows = im.getStatic("org.junit.jupiter.api.Assertions.assertThrows")
      val seq = im.getType("io.vavr.collection.Seq")
      val list = im.getType("io.vavr.collection.List")
      val stream = if (i == 0) "" else im.getType("io.vavr.collection.Stream")
      val comparator = im.getType("java.util.Comparator")
      val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
      val generics = if (i == 0) "" else s"<${(1 to i).gen(j => s"Object")(using ", ")}>"
      val intGenerics = if (i == 0) "" else s"<${(1 to i).gen(j => s"Integer")(using ", ")}>"
      val functionArgs = if (i == 0) "()" else s"${(i > 1).gen("(") + (1 to i).gen(j => s"o$j")(using ", ") + (i > 1).gen(")")}"
      val nullArgs = (1 to i).gen(j => "null")(using ", ")
      if(i==2){
        im.getType("java.util.AbstractMap")
        im.getType("java.util.Map")
      }


      xs"""
        public class $className {

            @$test
            public void shouldCreateTuple() {
                final Tuple$i$generics tuple = createTuple();
                $assertThat(tuple).isNotNull();
            }

            @$test
            public void shouldGetArity() {
                final Tuple$i$generics tuple = createTuple();
                $assertThat(tuple.arity()).isEqualTo($i);
            }

            ${(i > 0).gen(xs"""
              @$test
              public void shouldReturnElements() {
                  final Tuple$i$intGenerics tuple = createIntTuple(${(1 to i).gen(j => s"$j")(using ", ")});
                  ${(1 to i).gen(j => s"$assertThat(tuple._$j).isEqualTo($j);\n")}
              }
            """)}

            ${(1 to i).gen(j =>
              xs"""
                @$test
                public void shouldUpdate$j() {
                  final Tuple$i$intGenerics tuple = createIntTuple(${(1 to i).gen(j => s"$j")(using ", ")}).update$j(42);
                  ${(1 to i).gen(k => s"$assertThat(tuple._$k).isEqualTo(${if (j == k) 42 else k});\n")}
                }
              """)(using "\n\n")}

            @$test
            public void shouldConvertToSeq() {
                final $seq<?> actual = createIntTuple(${genArgsForComparing(i, 1)}).toSeq();
                $assertThat(actual).isEqualTo($list.of(${genArgsForComparing(i, 1)}));
            }

            @$test
            public void shouldCompareEqual() {
                final Tuple$i$intGenerics t0 = createIntTuple(${genArgsForComparing(i, 0)});
                $assertThat(t0.compareTo(t0)).isZero();
                $assertThat(intTupleComparator.compare(t0, t0)).isZero();
            }

            ${(1 to i).gen(j => xs"""
              @$test
              public void shouldCompare${j.ordinal}Arg() {
                  final Tuple$i$intGenerics t0 = createIntTuple(${genArgsForComparing(i, 0)});
                  final Tuple$i$intGenerics t$j = createIntTuple(${genArgsForComparing(i, j)});
                  $assertThat(t0.compareTo(t$j)).isNegative();
                  $assertThat(t$j.compareTo(t0)).isPositive();
                  $assertThat(intTupleComparator.compare(t0, t$j)).isNegative();
                  $assertThat(intTupleComparator.compare(t$j, t0)).isPositive();
              }
            """)(using "\n\n")}

            ${(i == 2).gen(xs"""
              @$test
              public void shouldSwap() {
                  $assertThat(createIntTuple(1, 2).swap()).isEqualTo(createIntTuple(2, 1));
              }

              @$test
              public void shouldConvertToEntry() {
                  Tuple$i$intGenerics tuple = createIntTuple(1,2);
                  Map.Entry$intGenerics entry = new AbstractMap.SimpleEntry<>(1, 2);
                  assertThat(tuple.toEntry().equals(entry));
              }

            """)}

            ${(i > 0).gen(xs"""
              @$test
              public void shouldMap() {
                  final Tuple$i$generics tuple = createTuple();
                  ${if (i == 1) xs"""
                    final Tuple$i$generics actual = tuple.map(o -> o);
                    $assertThat(actual).isEqualTo(tuple);
                  """ else xs"""
                    final Tuple$i$generics actual = tuple.map($functionArgs -> tuple);
                    $assertThat(actual).isEqualTo(tuple);
                  """}
              }

              @$test
              public void shouldMapComponents() {
                final Tuple$i$generics tuple = createTuple();
                ${(1 to i).gen(j => xs"""final Function1<Object, Object> f$j = Function1.identity();""")(using "\n")}
                final Tuple$i$generics actual = tuple.map(${(1 to i).gen(j => s"f$j")(using ", ")});
                $assertThat(actual).isEqualTo(tuple);
              }

              @$test
              public void shouldReturnTuple${i}OfSequence$i() {
                final $seq<Tuple$i<${(1 to i).gen(j => xs"Integer")(using ", ")}>> iterable = $list.of(${(1 to i).gen(j => xs"Tuple.of(${(1 to i).gen(k => xs"${k+2*j-1}")(using ", ")})")(using ", ")});
                final Tuple$i<${(1 to i).gen(j => xs"$seq<Integer>")(using ", ")}> expected = Tuple.of(${(1 to i).gen(j => xs"$stream.of(${(1 to i).gen(k => xs"${2*k+j-1}")(using ", ")})")(using ", ")});
                $assertThat(Tuple.sequence$i(iterable)).isEqualTo(expected);
              }
            """)}

            ${(i > 1).gen(xs"""
              @$test
              public void shouldReturnTuple${i}OfSequence1() {
                final $seq<Tuple$i<${(1 to i).gen(j => xs"Integer")(using ", ")}>> iterable = $list.of(Tuple.of(${(1 to i).gen(k => xs"$k")(using ", ")}));
                final Tuple$i<${(1 to i).gen(j => xs"$seq<Integer>")(using ", ")}> expected = Tuple.of(${(1 to i).gen(j => xs"$stream.of($j)")(using ", ")});
                $assertThat(Tuple.sequence$i(iterable)).isEqualTo(expected);
              }
            """)}

            ${(i > 1) `gen` (1 to i).gen(j => {
              val substitutedResultTypes = if (i == 0) "" else s"<${(1 to i).gen(k => if (k == j) "String" else "Integer")(using ", ")}>"
              val ones = (1 to i).gen(_ => "1")(using ", ")
              val result = (1 to i).gen(k => if (k == j) "\"X\"" else "1")(using ", ")
              xs"""
                @$test
                public void shouldMap${j.ordinal}Component() {
                  final Tuple$i$substitutedResultTypes actual = Tuple.of($ones).map$j(i -> "X");
                  final Tuple$i$substitutedResultTypes expected = Tuple.of($result);
                  assertThat(actual).isEqualTo(expected);
                }
              """
            })(using "\n\n")}

            @$test
            public void shouldApplyTuple() {
                final Tuple$i$generics tuple = createTuple();
                final Tuple0 actual = tuple.apply($functionArgs -> Tuple0.instance());
                assertThat(actual).isEqualTo(Tuple0.instance());
            }

            ${(i < N).gen(xs"""
              @$test
              public void shouldAppendValue() {
                  final Tuple${i+1}<${(1 to i+1).gen(j => s"Integer")(using ", ")}> actual = ${ if (i == 0) "Tuple0.instance()" else s"Tuple.of(${(1 to i).gen(j => xs"$j")(using ", ")})"}.append(${i+1});
                  final Tuple${i+1}<${(1 to i+1).gen(j => s"Integer")(using ", ")}> expected = Tuple.of(${(1 to i+1).gen(j => xs"$j")(using ", ")});
                  assertThat(actual).isEqualTo(expected);
              }
            """)}

            ${(i < N) `gen` (1 to N-i).gen(j => xs"""
              @$test
              public void shouldConcatTuple$j() {
                  final Tuple${i+j}<${(1 to i+j).gen(j => s"Integer")(using ", ")}> actual = ${ if (i == 0) "Tuple0.instance()" else s"Tuple.of(${(1 to i).gen(j => xs"$j")(using ", ")})"}.concat(Tuple.of(${(i+1 to i+j).gen(k => s"$k")(using ", ")}));
                  final Tuple${i+j}<${(1 to i+j).gen(j => s"Integer")(using ", ")}> expected = Tuple.of(${(1 to i+j).gen(j => xs"$j")(using ", ")});
                  assertThat(actual).isEqualTo(expected);
              }
            """)(using "\n\n")}

            @$test
            public void shouldRecognizeEquality() {
                final Tuple$i$generics tuple1 = createTuple();
                final Tuple$i$generics tuple2 = createTuple();
                $assertThat((Object) tuple1).isEqualTo(tuple2);
            }

            @$test
            public void shouldRecognizeNonEquality() {
                final Tuple$i$generics tuple = createTuple();
                final Object other = new Object();
                $assertThat(tuple).isNotEqualTo(other);
            }

            ${(i > 0).gen(xs"""
              @$test
              public void shouldRecognizeNonEqualityPerComponent() {
                  final Tuple$i<${(1 to i).gen(_ => "String")(using ", ")}> tuple = Tuple.of(${(1 to i).gen(j => "\"" + j + "\"")(using ", ")});
                  ${(1 to i).gen(j => {
                    val that = "Tuple.of(" + (1 to i).gen(k => if (j == k) "\"X\"" else "\"" + k + "\"")(using ", ") + ")"
                    s"$assertThat(tuple.equals($that)).isFalse();"
                  })(using "\n")}
              }
            """)}

            @$test
            public void shouldComputeCorrectHashCode() {
                final int actual = createTuple().hashCode();
                final int expected = ${im.getType("java.util.Objects")}.${if (i == 1) "hashCode" else "hash"}($nullArgs);
                $assertThat(actual).isEqualTo(expected);
            }

            @$test
            public void shouldImplementToString() {
                final String actual = createTuple().toString();
                final String expected = "($nullArgs)";
                $assertThat(actual).isEqualTo(expected);
            }

            private $comparator<Tuple$i$intGenerics> intTupleComparator = Tuple$i.comparator(${(1 to i).gen($j => s"Integer::compare")(using ", ")});

            private Tuple$i$generics createTuple() {
                return ${if (i == 0) "Tuple0.instance()" else s"new Tuple$i<>($nullArgs)"};
            }

            private Tuple$i$intGenerics createIntTuple(${(1 to i).gen(j => s"Integer i$j")(using ", ")}) {
                return ${if (i == 0) "Tuple0.instance()" else s"new Tuple$i<>(${(1 to i).gen(j => s"i$j")(using ", ")})"};
            }
        }
      """
    })
  })
}
