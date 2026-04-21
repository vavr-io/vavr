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
 * Generator of APITest
 */
def genAPITests(targetTest: String): Unit = {

  genVavrFile("io.vavr", s"APITest", targetTest)((im: ImportManager, packageName, className) => {

    val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
    val nested = im.getType("org.junit.jupiter.api.Nested")
    val test = im.getType("org.junit.jupiter.api.Test")

    val API = im.getType("io.vavr.API")
    val AssertionsExtensions = im.getType("io.vavr.AssertionsExtensions")
    val ListType = im.getType("io.vavr.collection.List")
    val StreamType = im.getType("io.vavr.collection.Stream")
    val SeqType = im.getType("io.vavr.collection.Seq")
    val MapType = im.getType("io.vavr.collection.Map")
    val OptionType = im.getType("io.vavr.control.Option")
    val EitherType = im.getType("io.vavr.control.Either")
    val ValidationType = im.getType("io.vavr.control.Validation")
    val FutureType = im.getType("io.vavr.concurrent.Future")
    val ExecutorsType = im.getType("java.util.concurrent.Executors")
    val ExecutorService = s"$ExecutorsType.newSingleThreadExecutor()"
    val TryType = im.getType("io.vavr.control.Try")
    val JavaComparatorType = im.getType("java.util.Comparator")

    val monadicTypesFor = List(OptionType, EitherType, ValidationType)
    val monadicTypeMetadataFor = Map(
      OptionType -> ("", "of"),
      EitherType -> ("Object, ", "right"),
      ValidationType -> ("Object, ", "valid")
    )
    val monadicFunctionTypesFor = List(FutureType, TryType)

    val d = "$"

    im.getStatic("io.vavr.API.*")

    def genFutureTests(name: String, value: String, success: Boolean): String = {
      val check = if (success) "isSuccess" else "isFailure"
      xs"""
        @$test
        public void shouldFutureWith${name}ReturnNotNull() {
            final $FutureType<?> future = Future($value).await();
            assertThat(future).isNotNull();
            assertThat(future.$check()).isTrue();
        }

        @$test
        public void shouldFutureWithinExecutorWith${name}ReturnNotNull() {
            final $FutureType<?> future = Future($ExecutorService, $value).await();
            assertThat(future).isNotNull();
            assertThat(future.$check()).isTrue();
        }
      """
    }

    def genExtAliasTest(name: String, func: String, value: String, check: String): String = {
      xs"""
        @$test
        public void should$name() {
            assertThat($func($value)).$check;
        }
      """
    }

    def genMediumAliasTest(name: String, func: String, value: String): String = genExtAliasTest(s"${name}ReturnNotNull", func, value, "isNotNull()")

    def genSimpleAliasTest(name: String, value: String): String = genMediumAliasTest(name, name, value)

    def genTraversableTests(func: String): String = {
      xs"""
        ${genMediumAliasTest(s"Empty$func", func, "")}

        ${genMediumAliasTest(s"${func}WithSingle", func, "'1'")}

        ${genMediumAliasTest(s"${func}WithVarArg", func, "'1', '2', '3'")}

      """
    }

    def genSortedTraversableTests(func: String): String = {
      xs"""
        ${genMediumAliasTest(s"Empty$func", func, "")}

        ${genMediumAliasTest(s"Empty${func}WithComparator", func, s"($JavaComparatorType<Character>) Character::compareTo")}

        ${genMediumAliasTest(s"${func}WithSingle", func, "'1'")}

        ${genMediumAliasTest(s"${func}WithSingleAndComparator", func, "Character::compareTo, '1'")}

        ${genMediumAliasTest(s"${func}WithVarArg", func, "'1', '2', '3'")}

        ${genMediumAliasTest(s"${func}WithVarArgAndComparator", func, s"($JavaComparatorType<Character>) Character::compareTo, '1', '2', '3'")}

      """
    }

    def genMapTests(func: String): String = {
      xs"""
        ${genMediumAliasTest(s"Empty$func", func, "")}

        ${genMediumAliasTest(s"${func}FromSingle", func, "1, '1'")}

        ${genMediumAliasTest(s"${func}FromTuples", func, "Tuple(1, '1'), Tuple(2, '2'), Tuple(3, '3')")}

        ${genMediumAliasTest(s"${func}FromPairs", func, "1, '1', 2, '2', 3, '3'")}

        ${(1 to VARARGS).gen(i => {
          xs"""
            @$test
            public void shouldCreate${func}From${i}Pairs() {
              $MapType<Integer, Integer> map = $func(${(1 to i).gen(j => s"$j, ${j*2}")(using ", ")});
              ${(1 to i).gen(j => s"assertThat(map.apply($j)).isEqualTo(${j*2});")(using "\n")}
            }
          """
        })(using "\n\n")}

      """
    }

    def genTryTests(func: String, value: String, success: Boolean): String = {
      val check = if (success) "isSuccess" else "isFailure"
      xs"""
        @$test
        public void should${func.firstUpper}ReturnNotNull() {
            final $TryType<?> t = $func($value);
            assertThat(t).isNotNull();
            assertThat(t.$check()).isTrue();
        }
      """
    }

    def genAliasesTests(): String = {
      xs"""
        ${(0 to N).gen(i => {
          val params = (1 to i).gen(j => s"v$j")(using ", ")
          xs"""
            @$test
            public void shouldFunction${i}ReturnNotNull() {
                assertThat(Function(($params) -> null)).isNotNull();
            }

            @$test
            public void shouldCheckedFunction${i}ReturnNotNull() {
                assertThat(CheckedFunction(($params) -> null)).isNotNull();
            }

          """
        })(using "\n\n")}

        ${(0 to N).gen(i => {
          val params = (1 to i).gen(j => s"v$j")(using ", ")
          genExtAliasTest(s"Unchecked${i}ReturnNonCheckedFunction", "unchecked", s"($params) -> null", s"isInstanceOf(Function$i.class)")
        })(using "\n\n")}

        ${(0 to N).gen(i => {
          val params = (1 to i).gen(j => s"$j")(using ", ")
          xs"""
            @$test
            public void shouldTuple${i}ReturnNotNull() {
                assertThat(Tuple($params)).isNotNull();
            }

          """
        })(using "\n\n")}

        ${Seq("Right", "Left").gen(name => {
          xs"""
            @$test
            public void should${name}ReturnNotNull() {
                assertThat($name(null)).isNotNull();
            }

          """
        })(using "\n\n")}

        ${genFutureTests("Supplier", "() -> 1", success = true)}

        ${genFutureTests("Value", "1", success = true)}

        ${genSimpleAliasTest("Lazy", "() -> 1")}

        ${genSimpleAliasTest("Option", "1")}

        ${genSimpleAliasTest("Some", "1")}

        ${genSimpleAliasTest("None", "")}

        ${genTryTests("Try", "() -> 1", success = true)}

        ${genTryTests("Success", "1", success = true)}

        ${genTryTests("Failure", "new Error()", success = false)}

        ${genSimpleAliasTest("Valid", "1")}

        ${genSimpleAliasTest("Invalid", "new Error()")}

        ${genMediumAliasTest("Char", "(Iterable<Character>) CharSeq", "'1'")}

        ${genMediumAliasTest("CharArray", "(Iterable<Character>) CharSeq", "'1', '2', '3'")}

        ${genMediumAliasTest("CharSeq", "(Iterable<Character>) CharSeq", "\"123\"")}

        ${genTraversableTests("Array")}

        ${genTraversableTests("Vector")}

        ${genTraversableTests("List")}

        ${genTraversableTests("Stream")}

        ${genTraversableTests("Queue")}

        ${genTraversableTests("LinkedSet")}

        ${genTraversableTests("Set")}

        ${genTraversableTests("Seq")}

        ${genTraversableTests("IndexedSeq")}

        ${genSortedTraversableTests("SortedSet")}

        ${genSortedTraversableTests("PriorityQueue")}

        ${genMapTests("LinkedMap")}

        ${genMapTests("Map")}

        ${genMapTests("SortedMap")}

        ${genMediumAliasTest("EmptySortedMapFromComparator", "SortedMap", "Integer::compareTo")}

        ${genMediumAliasTest("SortedMapFromSingleAndComparator", "SortedMap", "Integer::compareTo, 1, '1'")}

        ${genMediumAliasTest("SortedMapFromTuplesAndComparator", "SortedMap", s"($JavaComparatorType<Integer>)Integer::compareTo, Tuple(1, '1'), Tuple(2, '2'), Tuple(3, '3')")}
      """
    }

    def genShortcutsTests(): String = {

      val fail = im.getStatic("org.junit.jupiter.api.Assertions.fail")

      xs"""
        @$test
        public void shouldCompileTODOAndThrowDefaultMessageAtRuntime() {
            try {
                final String s = TODO();
                $fail("TODO() should throw. s: " + s);
            } catch(NotImplementedError err) {
                assertThat(err.getMessage()).isEqualTo("An implementation is missing.");
            }
        }

        @$test
        public void shouldCompileTODOAndThrowGivenMessageAtRuntime() {
            final String msg = "Don't try this in production!";
            try {
                final String s = TODO(msg);
                $fail("TODO(String) should throw. s: " + s);
            } catch(NotImplementedError err) {
                assertThat(err.getMessage()).isEqualTo(msg);
            }
        }
      """
    }

    xs"""
      @SuppressWarnings("deprecation")
      public class $className {

          @$test
          public void shouldNotBeInstantiable() {
              $AssertionsExtensions.assertThat($API.class).isNotInstantiable();
          }

          @Nested
          class ShortcutTests {

              ${genShortcutsTests()}

          }

          //
          // Alias should return not null.
          // More specific test for each aliased class implemented in separate test class
          //

          @Nested
          class AliasTests {

              ${genAliasesTests()}

          }

          @Nested
          class RunTests {

              @$test
              public void shouldRunUnitAndReturnVoid() {
                  int[] i = { 0 };
                  Void nothing = run(() -> i[0]++);
                  $assertThat(nothing).isNull();
                  $assertThat(i[0]).isEqualTo(1);
              }

          }

          @Nested
          class ForTests {

              @$test
              public void shouldIterateFor1UsingSimpleYield() {
                  final $ListType<Integer> list = List.of(1, 2, 3);
                  final $ListType<Integer> actual = For(list).yield().toList();
                  $assertThat(actual).isEqualTo(list);
              }

              ${(1 to N).gen(i => xs"""
                @$test
                public void shouldIterateFor$ListType$i() {
                    final $ListType<Integer> result = For(
                        ${(1 to i).gen(j => s"$ListType.of(1, 2, 3)")(using ",\n")}
                    ).yield(${(i > 1).gen("(")}${(1 to i).gen(j => s"i$j")(using ", ")}${(i > 1).gen(")")} -> ${(1 to i).gen(j => s"i$j")(using " + ")}).toList();
                    $assertThat(result.length()).isEqualTo((int) Math.pow(3, $i));
                    $assertThat(result.head()).isEqualTo($i);
                    $assertThat(result.last()).isEqualTo(3 * $i);
                }
              """)(using "\n\n")}

              ${monadicTypesFor.gen(mtype => (1 to N).gen(i =>
                val (parameterInset, builderName) = monadicTypeMetadataFor(mtype)
                { xs"""

                @$test
                public void shouldIterateFor$mtype$i() {
                    final $mtype<${parameterInset}Integer> result = For(
                        ${(1 to i).gen(j => s"$mtype.$builderName($j)")(using ",\n")}
                    ).yield(${(i > 1).gen("(")}${(1 to i).gen(j => s"i$j")(using ", ")}${(i > 1).gen(")")} -> ${(1 to i).gen(j => s"i$j")(using " + ")});
                    $assertThat(result.get()).isEqualTo(${(1 to i).sum});
                }

                @$test
                public void shouldIterateLazyFor$mtype$i() {
                    final $mtype<${parameterInset}Integer> result = For(
                        ${(1 to i).gen(j => if (j == 1) {
                            s"$mtype.$builderName($j)"
                        } else {
                            val args = (1 until j).map(k => s"r$k").mkString(", ")
                            val argsUsed = (1 until j).map(k => s"r$k").mkString(" + ")
                            s"($args) -> $mtype.$builderName($argsUsed + $j)"
                        } )(using ",\n")}
                    ).yield(${(i > 1).gen("(")}${(1 to i).gen(j => s"i$j")(using ", ")}${(i > 1).gen(")")} -> ${(1 to i).gen(j => s"i$j")(using " + ")});

                    // Each step builds on the sum of all previous results plus its index
                    // This forms a sequence rₙ = 2ⁿ - 1, and the yield sums all rᵢ.
                    // Hence total = Σ(2ⁱ - 1) for i = 1..n = (2ⁿ⁺¹ - 2) - n
                    assertThat(result.get()).isEqualTo((1 << ($i + 1)) - 2 - $i);
                }
              """})(using "\n\n"))(using "\n\n")}


              ${monadicFunctionTypesFor.gen(mtype => (1 to N).gen(i => { xs"""
                @$test
                public void shouldIterateFor$mtype$i() {
                    final $mtype<Integer> result = For(
                        ${(1 to i).gen(j => s"$mtype.of(() -> $j)")(using ",\n")}
                    ).yield(${(i > 1).gen("(")}${(1 to i).gen(j => s"i$j")(using ", ")}${(i > 1).gen(")")} -> ${(1 to i).gen(j => s"i$j")(using " + ")});
                    $assertThat(result.get()).isEqualTo(${(1 to i).sum});
                }
              """})(using "\n\n"))(using "\n\n")}

              @$test
              public void shouldIterateNestedFor() {
                  final $ListType<String> result =
                          For(${im.getType("java.util.Arrays")}.asList(1, 2), i ->
                                  For(${im.getType("io.vavr.collection.List")}.of('a', 'b')).yield(c -> i + ":" + c)).toList();
                  assertThat(result).isEqualTo($ListType.of("1:a", "1:b", "2:a", "2:b"));
              }

          }

          @Nested
          class MatchTests {

              @$test
              public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndSupplier() {
                  final Match.Case<Object, Integer> _case = Case($$(ignored -> true), ignored -> 1);
                  assertThat(_case.isDefinedAt(null)).isTrue();
                  assertThat(_case.apply(null)).isEqualTo(1);
              }

              @$test
              public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndSupplier() {
                  assertThat(Case($$(ignored -> false), ignored -> 1).isDefinedAt(null)).isFalse();
              }

              @$test
              public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndValue() {
                  final Match.Case<Object, Integer> _case = Case($$(ignored -> true), 1);
                  assertThat(_case.isDefinedAt(null)).isTrue();
                  assertThat(_case.apply(null)).isEqualTo(1);
              }

              @$test
              public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndValue() {
                  assertThat(Case($$(ignored -> false), 1).isDefinedAt(null)).isFalse();
              }

              @$test
              public void shouldPassIssue2401() {
                  final $SeqType<String> empty = $StreamType.empty();
                  try {
                      Match(empty).of(
                              Case($$($ListType.empty()), ignored -> "list")
                      );
                      fail("expected MatchError");
                  } catch (MatchError err) {
                      // ok!
                  }
              }

              @$test
              public void shouldCatchClassCastExceptionWhenPredicateHasDifferentType() {
                  try {
                      final Object o = "";
                      Match(o).of(
                              Case($$((Integer i) -> true), "never")
                      );
                      fail("expected MatchError");
                  } catch (MatchError err) {
                      // ok!
                  }
              }

          }

          @Nested
          class MatchPatternTests {

              class ClzMatch {}
              class ClzMatch1 extends ClzMatch {}
              class ClzMatch2 extends ClzMatch {}

              ${(1 to N).gen(i => {

                im.getStatic("io.vavr.API.*")
                im.getStatic("io.vavr.Patterns.*")

                xs"""
                  @$test
                  public void shouldMatchPattern$i() {
                      final Tuple$i<${(1 to i).gen(j => s"Integer")(using ", ")}> tuple = Tuple.of(${(1 to i).gen(j => s"1")(using ", ")});
                      final String func = Match(tuple).of(
                              Case($$Tuple$i($d(0)${(2 to i).gen(j => s", $d()")}), (${(1 to i).gen(j => s"m$j")(using ", ")}) -> "fail"),
                              Case($$Tuple$i(${(1 to i).gen(j => s"$d()")(using ", ")}), (${(1 to i).gen(j => s"m$j")(using ", ")}) -> "okFunc")
                      );
                      assertThat(func).isEqualTo("okFunc");
                      final String supp = Match(tuple).of(
                              Case($$Tuple$i($d(0)${(2 to i).gen(j => s", $d()")}), () -> "fail"),
                              Case($$Tuple$i(${(1 to i).gen(j => s"$d()")(using ", ")}), () -> "okSupp")
                      );
                      assertThat(supp).isEqualTo("okSupp");
                      final String val = Match(tuple).of(
                              Case($$Tuple$i($d(0)${(2 to i).gen(j => s", $d()")}), "fail"),
                              Case($$Tuple$i(${(1 to i).gen(j => s"$d()")(using ", ")}), "okVal")
                      );
                      assertThat(val).isEqualTo("okVal");

                      final ClzMatch c = new ClzMatch2();
                      final String match = Match(c).of(
                              Case(Match.Pattern$i.of(ClzMatch1.class, ${(1 to i).gen(j => s"$d()")(using ", ")}, t -> Tuple.of(${(1 to i).gen(j => s"null")(using ", ")})), "fail"),
                              Case(Match.Pattern$i.of(ClzMatch2.class, ${(1 to i).gen(j => s"$d()")(using ", ")}, t -> Tuple.of(${(1 to i).gen(j => s"null")(using ", ")})), "okMatch")
                      );
                      assertThat(match).isEqualTo("okMatch");
                  }
                """
              })(using "\n\n")}

          }
      }
    """
  })
}
