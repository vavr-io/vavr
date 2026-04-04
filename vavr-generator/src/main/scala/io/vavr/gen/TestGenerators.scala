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

def generateTestClasses(targetTest: String): Unit = {

  genAPITests()
  genFunctionTests()
  genMapOfEntriesTests()
  genTupleTests()

  /**
   * Generator of Function tests
   */
  def genAPITests(): Unit = {

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
        (OptionType -> ("", "of")),
        (EitherType -> ("Object, ", "right")),
        (ValidationType -> ("Object, ", "valid"))
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

      def genAliasesTests(im: ImportManager, packageName: String, className: String): String = {
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

      def genShortcutsTests(im: ImportManager, packageName: String, className: String): String = {

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

                ${genShortcutsTests(im, packageName, className)}

            }

            //
            // Alias should return not null.
            // More specific test for each aliased class implemented in separate test class
            //

            @Nested
            class AliasTests {

                ${genAliasesTests(im, packageName, className)}

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
                  val (parameterInset, builderName) = monadicTypeMetadataFor(mtype);
                  { xs"""

                  @$test
                  public void shouldIterateFor$mtype$i() {
                      final $mtype<${parameterInset}Integer> result = For(
                          ${(1 to i).gen(j => s"$mtype.${builderName}($j)")(using ",\n")}
                      ).yield(${(i > 1).gen("(")}${(1 to i).gen(j => s"i$j")(using ", ")}${(i > 1).gen(")")} -> ${(1 to i).gen(j => s"i$j")(using " + ")});
                      $assertThat(result.get()).isEqualTo(${(1 to i).sum});
                  }

                  @$test
                  public void shouldIterateLazyFor$mtype$i() {
                      final $mtype<${parameterInset}Integer> result = For(
                          ${(1 to i).gen(j => if (j == 1) {
                              s"$mtype.${builderName}($j)"
                          } else {
                              val args = (1 until j).map(k => s"r$k").mkString(", ")
                              val argsUsed = (1 until j).map(k => s"r$k").mkString(" + ")
                              s"($args) -> $mtype.${builderName}(${argsUsed} + $j)"
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

  /**
   * Generator of Function tests
   */
  def genFunctionTests(): Unit = {

    (0 to N).foreach(i => {

      genVavrFile("io.vavr", s"CheckedFunction${i}Test", targetTest)(genFunctionTest("CheckedFunction", checked = true))
      genVavrFile("io.vavr", s"Function${i}Test", targetTest)(genFunctionTest("Function", checked = false))

      def genFunctionTest(name: String, checked: Boolean)(im: ImportManager, packageName: String, className: String): String = {

        val AtomicInteger = im.getType("java.util.concurrent.atomic.AtomicInteger")
        val nested = im.getType("org.junit.jupiter.api.Nested")

        val functionArgsDecl = (1 to i).gen(j => s"Object o$j")(using ", ")
        val functionArgs = (1 to i).gen(j => s"o$j")(using ", ")
        val generics = (1 to i + 1).gen(j => "Object")(using ", ")

        val test = im.getType("org.junit.jupiter.api.Test")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
        val assertThrows = im.getStatic("org.junit.jupiter.api.Assertions.assertThrows")
        val recFuncF1 = if (i == 0) "11;" else s"i1 <= 0 ? i1 : $className.recurrent2.apply(${(1 to i).gen(j => s"i$j" + (j == 1).gen(s" - 1"))(using ", ")}) + 1;"

        def curriedType(max: Int, function: String): String = max match {
          case 0 => s"${function}0<Object>"
          case 1 => s"${function}1<Object, Object>"
          case _ => s"Function1<Object, ${curriedType(max - 1, function)}>"
        }

        val wideGenericArgs = (1 to i).gen(j => "Number")(using ", ")
        val wideGenericResult = "String"
        val wideFunctionPattern = (1 to i).gen(j => "%s")(using ", ")
        val narrowGenericArgs = (1 to i).gen(j => "Integer")(using ", ")
        val narrowGenericResult = im.getType("java.lang.CharSequence")
        val narrowArgs = (1 to i).gen(j => j.toString)(using ", ")

        xs"""
          public class $className {

              @$test
              public void shouldCreateFromMethodReference() {
                  class Type {
                      Object methodReference($functionArgsDecl) {
                          return null;
                      }
                  }
                  final Type type = new Type();
                  assertThat($name$i.of(type::methodReference)).isNotNull();
              }

              @$test
              public void shouldLiftPartialFunction() {
                  assertThat($name$i.lift(($functionArgs) -> { while(true); })).isNotNull();
              }

              ${(i == 1).gen(xs"""
                @$test
                public void shouldCreateIdentityFunction()${checked.gen(" throws Throwable")} {
                    final $name$i<String, String> identity = $name$i.identity();
                    final String s = "test";
                    assertThat(identity.apply(s)).isEqualTo(s);
                }
              """)}

              ${(i == 0 && !checked).gen(
                xs"""
                  @$test
                  public void shouldGetValue() {
                      final String s = "test";
                      final ${name}0<String> supplier = () -> s;
                      assertThat(supplier.get()).isEqualTo(s);
                  }
                """
              )}

              ${(i > 1).gen(xs"""
                @$test
                public void shouldPartiallyApply()${checked.gen(" throws Throwable")} {
                    final $name$i<$generics> f = ($functionArgs) -> null;
                    ${(1 until i).gen(j => {
                      val partialArgs = (1 to j).gen(k => "null")(using ", ")
                      s"$assertThat(f.apply($partialArgs)).isNotNull();"
                    })(using "\n")}
                }
              """)}

              @$test
              public void shouldGetArity() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  $assertThat(f.arity()).isEqualTo($i);
              }

              @$test
              public void shouldConstant()${checked.gen(" throws Throwable")} {
                  final $name$i<$generics> f = $name$i.constant(6);
                  $assertThat(f.apply(${(1 to i).gen(j => s"$j")(using ", ")})).isEqualTo(6);
              }

              @$test
              public void shouldCurry() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  final ${curriedType(i, name)} curried = f.curried();
                  $assertThat(curried).isNotNull();
              }

              @$test
              public void shouldTuple() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  final ${name}1<Tuple$i${(i > 0).gen(s"<${(1 to i).gen(j => "Object")(using ", ")}>")}, Object> tupled = f.tupled();
                  $assertThat(tupled).isNotNull();
              }

              @$test
              public void shouldReverse() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  $assertThat(f.reversed()).isNotNull();
              }

              @$test
              public void shouldMemoize()${checked.gen(" throws Throwable")} {
                  final $AtomicInteger integer = new $AtomicInteger();
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> f = (${(1 to i).gen(j => s"i$j")(using ", ")}) -> ${(1 to i).gen(j => s"i$j")(using " + ")}${(i > 0).gen(" + ")}integer.getAndIncrement();
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> memo = f.memoized();
                  // should apply f on first apply()
                  final int expected = memo.apply(${(1 to i).gen(j => s"$j")(using ", ")});
                  // should return memoized value of second apply()
                  $assertThat(memo.apply(${(1 to i).gen(j => s"$j")(using ", ")})).isEqualTo(expected);
                  ${(i > 0).gen(xs"""
                    $comment should calculate new values when called subsequently with different parameters
                    $assertThat(memo.apply(${(1 to i).gen(j => s"${j + 1} ")(using ", ")})).isEqualTo(${(1 to i).gen(j => s"${j + 1} ")(using " + ")} + 1);
                    $comment should return memoized value of second apply() (for new value)
                    $assertThat(memo.apply(${(1 to i).gen(j => s"${j + 1} ")(using ", ")})).isEqualTo(${(1 to i).gen(j => s"${j + 1} ")(using " + ")} + 1);
                  """)}
              }

              @$test
              public void shouldNotMemoizeAlreadyMemoizedFunction()${checked.gen(" throws Throwable")} {
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> f = (${(1 to i).gen(j => s"i$j")(using ", ")}) -> null;
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> memo = f.memoized();
                  $assertThat(memo.memoized() == memo).isTrue();
              }

              ${(i > 0).gen(xs"""
                @$test
                public void shouldMemoizeValueGivenNullArguments()${checked.gen(" throws Throwable")} {
                    final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> f = (${(1 to i).gen(j => s"i$j")(using ", ")}) -> null;
                    final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> memo = f.memoized();
                    $assertThat(memo.apply(${(1 to i).gen(j => "null")(using ", ")})).isNull();
                }
              """)}

              @$test
              public void shouldRecognizeMemoizedFunctions() {
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> f = (${(1 to i).gen(j => s"i$j")(using ", ")}) -> null;
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> memo = f.memoized();
                  $assertThat(f.isMemoized()).isFalse();
                  $assertThat(memo.isMemoized()).isTrue();
              }

              ${(i == 1 && !checked).gen({
                val assertThatThrownBy = im.getStatic("org.assertj.core.api.Assertions.assertThatThrownBy")
                xs"""
                  @$test
                  public void shouldThrowOnPartialWithNullPredicate() {
                      final Function1<Integer, String> f = String::valueOf;
                      $assertThatThrownBy(() -> f.partial(null))
                              .isInstanceOf(NullPointerException.class)
                              .hasMessage("isDefinedAt is null");
                  }

                  @$test
                  public void shouldCreatePartialFunction() {
                      final Function1<Integer, String> f = String::valueOf;
                      final PartialFunction<Integer, String> pf = f.partial(i -> i % 2 == 0);
                      assertThat(pf.isDefinedAt(0)).isTrue();
                      assertThat(pf.isDefinedAt(1)).isFalse();
                      assertThat(pf.apply(0)).isEqualTo("0");
                      assertThat(pf.apply(1)).isEqualTo("1"); $comment it is valid to return a value, even if isDefinedAt returns false
                  }
                """})}

              ${(!checked).gen(xs"""
                @$test
                public void shouldLiftTryPartialFunction() {
                    $AtomicInteger integer = new $AtomicInteger();
                    $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> divByZero = (${(1 to i).gen(j => s"i$j")(using ", ")}) -> 10 / integer.get();
                    $name$i<${(1 to i).gen(j => "Integer, ")(using "")}Try<Integer>> divByZeroTry = $name$i.liftTry(divByZero);

                    ${im.getType("io.vavr.control.Try")}<Integer> res = divByZeroTry.apply(${(1 to i).gen(j => s"0")(using ", ")});
                    assertThat(res.isFailure()).isTrue();
                    assertThat(res.getCause()).isNotNull();
                    assertThat(res.getCause().getMessage()).isEqualToIgnoringCase("/ by zero");

                    integer.incrementAndGet();
                    res = divByZeroTry.apply(${(1 to i).mkString(", ")});
                    assertThat(res.isSuccess()).isTrue();
                    assertThat(res.get()).isEqualTo(10);
                }
              """)}

              ${checked.gen(xs"""
                ${(i == 0).gen(xs"""
                  @$test
                  public void shouldRecover() {
                      final $AtomicInteger integer = new $AtomicInteger();
                      $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance(integer.get() == 0 ? "MD5" : "Unknown");
                      Function$i<MessageDigest> recover = digest.recover(throwable -> () -> null);
                      MessageDigest md5 = recover.apply();
                      assertThat(md5).isNotNull();
                      assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                      assertThat(md5.getDigestLength()).isEqualTo(16);
                      integer.incrementAndGet();
                      assertThat(recover.apply()).isNull();
                  }

                  @$test
                  public void shouldRecoverNonNull() {
                      final $AtomicInteger integer = new $AtomicInteger();
                      $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance(integer.get() == 0 ? "MD5" : "Unknown");
                      Function$i<MessageDigest> recover = digest.recover(throwable -> null);

                      MessageDigest md5 = recover.apply();
                      assertThat(md5).isNotNull();
                      assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                      assertThat(md5.getDigestLength()).isEqualTo(16);

                      integer.incrementAndGet();
                      ${im.getType("io.vavr.control.Try")}<MessageDigest> unknown = Function$i.liftTry(recover).apply();
                      assertThat(unknown).isNotNull();
                      assertThat(unknown.isFailure()).isTrue();
                      assertThat(unknown.getCause()).isNotNull().isInstanceOf(NullPointerException.class);
                      assertThat(unknown.getCause().getMessage()).isNotEmpty().isEqualToIgnoringCase("recover return null for class java.security.NoSuchAlgorithmException: Unknown MessageDigest not available");
                  }

                  @$test
                  public void shouldUncheckedWork() {
                      $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance("MD5");
                      Function$i<MessageDigest> unchecked = digest.unchecked();
                      MessageDigest md5 = unchecked.apply();
                      assertThat(md5).isNotNull();
                      assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                      assertThat(md5.getDigestLength()).isEqualTo(16);
                  }

                  @$test
                  public void shouldThrowCheckedExceptionWhenUnchecked() {
                      $assertThrows(${im.getType("java.security.NoSuchAlgorithmException")}.class, () -> {
                          $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance("Unknown");
                          Function$i<MessageDigest> unchecked = digest.unchecked();
                          unchecked.apply(); $comment Look ma, we throw an undeclared checked exception!
                      });
                  }

                  @$test
                  public void shouldLiftTryPartialFunction() {
                      final $AtomicInteger integer = new $AtomicInteger();
                      $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance(integer.get() == 0 ? "MD5" : "Unknown");
                      Function$i<Try<MessageDigest>> liftTry = $name$i.liftTry(digest);
                      ${im.getType("io.vavr.control.Try")}<MessageDigest> md5 = liftTry.apply();
                      assertThat(md5.isSuccess()).isTrue();
                      assertThat(md5.get()).isNotNull();
                      assertThat(md5.get().getAlgorithm()).isEqualToIgnoringCase("MD5");
                      assertThat(md5.get().getDigestLength()).isEqualTo(16);

                      integer.incrementAndGet();
                      ${im.getType("io.vavr.control.Try")}<MessageDigest> unknown = liftTry.apply();
                      assertThat(unknown.isFailure()).isTrue();
                      assertThat(unknown.getCause()).isNotNull();
                      assertThat(unknown.getCause().getMessage()).isEqualToIgnoringCase("Unknown MessageDigest not available");
                  }
                """)}
                ${(i > 0).gen(xs"""
                  ${
                    val types = s"<${(1 to i).gen(j => "String")(using ", ")}, MessageDigest>"
                    def toArgList (s: String) = s.split("", i).mkString("\"", "\", \"", "\"") + (s.length + 2 to i).gen(j => ", \"\"")
                    xs"""

                      private static final $name$i$types digest = (${(1 to i).gen(j => s"s$j")(using ", ")}) -> ${im.getType("java.security.MessageDigest")}.getInstance(${(1 to i).gen(j => s"s$j")(using " + ")});

                      @$test
                      public void shouldRecover() {
                          final Function$i<${(1 to i).gen(j => "String")(using ", ")}, MessageDigest> recover = digest.recover(throwable -> (${(1 to i).gen(j => s"s$j")(using ", ")}) -> null);
                          final MessageDigest md5 = recover.apply(${toArgList("MD5")});
                          assertThat(md5).isNotNull();
                          assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                          assertThat(md5.getDigestLength()).isEqualTo(16);
                          assertThat(recover.apply(${toArgList("Unknown")})).isNull();
                      }

                      @$test
                      public void shouldRecoverNonNull() {
                          final Function$i<${(1 to i).gen(j => "String")(using ", ")}, MessageDigest> recover = digest.recover(throwable -> null);
                          final MessageDigest md5 = recover.apply(${toArgList("MD5")});
                          assertThat(md5).isNotNull();
                          assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                          assertThat(md5.getDigestLength()).isEqualTo(16);
                          final ${im.getType("io.vavr.control.Try")}<MessageDigest> unknown = Function$i.liftTry(recover).apply(${toArgList("Unknown")});
                          assertThat(unknown).isNotNull();
                          assertThat(unknown.isFailure()).isTrue();
                          assertThat(unknown.getCause()).isNotNull().isInstanceOf(NullPointerException.class);
                          assertThat(unknown.getCause().getMessage()).isNotEmpty().isEqualToIgnoringCase("recover return null for class java.security.NoSuchAlgorithmException: Unknown MessageDigest not available");
                      }

                      @$test
                      public void shouldUncheckedWork() {
                          final Function$i<${(1 to i).gen(j => "String")(using ", ")}, MessageDigest> unchecked = digest.unchecked();
                          final MessageDigest md5 = unchecked.apply(${toArgList("MD5")});
                          assertThat(md5).isNotNull();
                          assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                          assertThat(md5.getDigestLength()).isEqualTo(16);
                      }

                      @$test
                      public void shouldUncheckedThrowIllegalState() {
                          $assertThrows(${im.getType("java.security.NoSuchAlgorithmException")}.class, () -> {
                              final Function$i<${(1 to i).gen(j => "String")(using ", ")}, MessageDigest> unchecked = digest.unchecked();
                              unchecked.apply(${toArgList("Unknown")}); $comment Look ma, we throw an undeclared checked exception!
                          });
                      }

                      @$test
                      public void shouldLiftTryPartialFunction() {
                          final Function$i<${(1 to i).gen(j => "String")(using ", ")}, Try<MessageDigest>> liftTry = $name$i.liftTry(digest);
                          final ${im.getType("io.vavr.control.Try")}<MessageDigest> md5 = liftTry.apply(${toArgList("MD5")});
                          assertThat(md5.isSuccess()).isTrue();
                          assertThat(md5.get()).isNotNull();
                          assertThat(md5.get().getAlgorithm()).isEqualToIgnoringCase("MD5");
                          assertThat(md5.get().getDigestLength()).isEqualTo(16);
                          final ${im.getType("io.vavr.control.Try")}<MessageDigest> unknown = liftTry.apply(${toArgList("Unknown")});
                          assertThat(unknown.isFailure()).isTrue();
                          assertThat(unknown.getCause()).isNotNull();
                          assertThat(unknown.getCause().getMessage()).isEqualToIgnoringCase("Unknown MessageDigest not available");
                      }
                    """
                  }
                """)}
              """)}

              private static final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> recurrent1 = (${(1 to i).gen(j => s"i$j")(using ", ")}) -> $recFuncF1
              ${(i > 0).gen(xs"""
                private static final $name$i<${(1 to i + 1).gen(j => "Integer")(using ", ")}> recurrent2 = $className.recurrent1.memoized();
              """)}

              @$test
              public void shouldCalculatedRecursively()${checked.gen(" throws Throwable")} {
                  assertThat(recurrent1.apply(${(1 to i).gen(j => "11")(using ", ")})).isEqualTo(11);
                  ${(i > 0).gen(s"assertThat(recurrent1.apply(${(1 to i).gen(j => "22")(using ", ")})).isEqualTo(22);")}
              }

              @$test
              public void shouldComposeWithAndThen() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  final ${name}1<Object, Object> after = o -> null;
                  final $name$i<$generics> composed = f.andThen(after);
                  $assertThat(composed).isNotNull();
              }

              @Nested
              class ComposeTests {
                ${(1 to i).gen(j =>
                  val genArgs = (1 to i).gen(k => "String")(using ", ")
                  val params = (1 to i).gen(k => s"String s$k")(using ", ")
                  val values = (1 to i).gen(k => if (k == j) "\"xx\"" else s"\"s$k\"")(using ", ")
                  val expected = (1 to i).gen(k => if (k == j) "XX" else s"s$k")(using "")
                  val concat = (1 to i).gen(k => s"s$k")(using " + ")
                  xs"""

                  @$test
                  public void shouldCompose$j() ${checked.gen(" throws Throwable ")}{
                      final $name$i<$genArgs, String> concat = ($params) -> $concat;
                      final Function1<String, String> toUpperCase = String::toUpperCase;
                      assertThat(concat.compose$j(toUpperCase).apply($values)).isEqualTo(\"$expected\");
                  }

                  """
                )}

              }

              ${(i == 0).gen(xs"""
              @$test
              public void shouldNarrow()${checked.gen(" throws Throwable")}{
                  final $name$i<$wideGenericResult> wideFunction = () -> "Zero args";
                  final $name$i<$narrowGenericResult> narrowFunction = $name$i.narrow(wideFunction);

                  $assertThat(narrowFunction.apply()).isEqualTo("Zero args");
              }
              """)}

              ${(i > 0).gen(xs"""
              @$test
              public void shouldNarrow()${checked.gen(" throws Throwable")}{
                  final $name$i<$wideGenericArgs, $wideGenericResult> wideFunction = ($functionArgs) -> String.format("Numbers are: $wideFunctionPattern", $functionArgs);
                  final $name$i<$narrowGenericArgs, $narrowGenericResult> narrowFunction = $name$i.narrow(wideFunction);

                  $assertThat(narrowFunction.apply($narrowArgs)).isEqualTo("Numbers are: $narrowArgs");
              }
              """)}
          }
        """
      }
    })
  }

  def genMapOfEntriesTests(): Unit = {

    def genAllArity(im: ImportManager,
                mapName: String, mapBuilder: String,
                builderComparator: Boolean, keyComparator: Boolean): String = {
      val test = im.getType("org.junit.jupiter.api.Test")
      val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
      val assertThrows = im.getStatic("org.junit.jupiter.api.Assertions.assertThrows")
      val naturalComparator = if (builderComparator || keyComparator) im.getStatic(s"io.vavr.collection.Comparators.naturalComparator") else null
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

  /**
   * Generator of Tuple tests
   */
  def genTupleTests(): Unit = {

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
}
