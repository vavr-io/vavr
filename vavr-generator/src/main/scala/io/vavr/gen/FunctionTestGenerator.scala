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
 * Generator of Function tests
 */
def genFunctionTests(targetTest: String): Unit = {

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

      // Object-typed curried form e.g. Function1<Object, Function1<Object, Object>>
      // Cannot share with FunctionGenerator: that version uses T1..Ti type vars, not Object
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
