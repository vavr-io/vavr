/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, https://vavr.io
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

// temporarily needed to circumvent https://issues.scala-lang.org/browse/SI-3772 (see case class Generics)
import Generator._
import JavaGenerator._

import scala.language.implicitConversions

val N = 8
val TARGET_MAIN = s"${project.getBasedir()}/src-gen/main/java"
val TARGET_TEST = s"${project.getBasedir()}/src-gen/test/java"
val CHARSET = java.nio.charset.StandardCharsets.UTF_8

/**
 * ENTRY POINT
 */
def run(): Unit = {
  generateMainClasses()
  generateTestClasses()
}

/**
 * Generate Vavr src-gen/main/java classes
 */
def generateMainClasses(): Unit = {

  // Workaround: Use /$** instead of /** in a StringContext when IntelliJ IDEA otherwise shows up errors in the editor
  val javadoc = "**"

  genPropertyChecks()

  /**
   * Generator of io.vavr.test.Property
   */
  def genPropertyChecks(): Unit = {

    genVavrFile("io.vavr.test", "Property")(genProperty)

    def genProperty(im: ImportManager, packageName: String, className: String): String = xs"""
      /**
       * A property builder which provides a fluent API to build checkable properties.
       *
       * @author Daniel Dietrich
       */
      public class $className {

          private final String name;

          private $className(String name) {
              this.name = name;
          }

          /**
           * Defines a new Property.
           *
           * @param name property name
           * @return a new {@code Property}
           * @throws NullPointerException if name is null.
           * @throws IllegalArgumentException if name is empty or consists of whitespace only
           */
          public static Property def(String name) {
              ${im.getType("java.util.Objects")}.requireNonNull(name, "name is null");
              if (name.trim().isEmpty()) {
                  throw new IllegalArgumentException("name is empty");
              }
              return new Property(name);
          }

          private static void logSatisfied(String name, int tries, long millis, boolean exhausted) {
              if (exhausted) {
                  log(String.format("%s: Exhausted after %s tests in %s ms.", name, tries, millis));
              } else {
                  log(String.format("%s: OK, passed %s tests in %s ms.", name, tries, millis));
              }
          }

          private static void logFalsified(String name, int currentTry, long millis) {
              log(String.format("%s: Falsified after %s passed tests in %s ms.", name, currentTry - 1, millis));
          }

          private static void logErroneous(String name, int currentTry, long millis, String errorMessage) {
              log(String.format("%s: Errored after %s passed tests in %s ms with message: %s", name, Math.max(0, currentTry - 1), millis, errorMessage));
          }

          private static void log(String msg) {
              System.out.println(msg);
          }

          /**
           * Creates a CheckError caused by an exception when obtaining a generator.
           *
           * @param position The position of the argument within the argument list of the property, starting with 1.
           * @param size     The size hint passed to the {@linkplain Arbitrary} which caused the error.
           * @param cause    The error which occurred when the {@linkplain Arbitrary} tried to obtain the generator {@linkplain Gen}.
           * @return a new CheckError instance.
           */
          private static CheckError arbitraryError(int position, int size, Throwable cause) {
              return new CheckError(String.format("Arbitrary %s of size %s: %s", position, size, cause.getMessage()), cause);
          }

          /**
           * Creates a CheckError caused by an exception when generating a value.
           *
           * @param position The position of the argument within the argument list of the property, starting with 1.
           * @param size     The size hint of the arbitrary which called the generator {@linkplain Gen} which caused the error.
           * @param cause    The error which occurred when the {@linkplain Gen} tried to generate a random value.
           * @return a new CheckError instance.
           */
          private static CheckError genError(int position, int size, Throwable cause) {
              return new CheckError(String.format("Gen %s of size %s: %s", position, size, cause.getMessage()), cause);
          }

          /**
           * Creates a CheckError caused by an exception when testing a Predicate.
           *
           * @param cause The error which occurred when applying the {@linkplain java.util.function.Predicate}.
           * @return a new CheckError instance.
           */
          private static CheckError predicateError(Throwable cause) {
              return new CheckError("Applying predicate: " + cause.getMessage(), cause);
          }

          ${(1 to N).gen(i => {
              val generics = (1 to i).gen(j => s"T$j")(", ")
              val parameters = (1 to i).gen(j => s"a$j")(", ")
              val parametersDecl = (1 to i).gen(j => s"Arbitrary<T$j> a$j")(", ")
              xs"""
                  /$javadoc
                   * Returns a logical for all quantor of $i given variables.
                   *
                   ${(1 to i).gen(j => s"* @param <T$j> ${j.ordinal} variable type of this for all quantor")("\n")}
                   ${(1 to i).gen(j => s"* @param a$j ${j.ordinal} variable of this for all quantor")("\n")}
                   * @return a new {@code ForAll$i} instance of $i variables
                   */
                  public <$generics> ForAll$i<$generics> forAll($parametersDecl) {
                      return new ForAll$i<>(name, $parameters);
                  }
              """
          })("\n\n")}

          ${(1 to N).gen(i => {
              val generics = (1 to i).gen(j => s"T$j")(", ")
              val params = (name: String) => (1 to i).gen(j => s"$name$j")(", ")
              val parametersDecl = (1 to i).gen(j => s"Arbitrary<T$j> a$j")(", ")
              xs"""
                  /$javadoc
                   * Represents a logical for all quantor.
                   *
                   ${(1 to i).gen(j => s"* @param <T$j> ${j.ordinal} variable type of this for all quantor")("\n")}
                   * @author Daniel Dietrich
                   */
                  public static class ForAll$i<$generics> {

                      private final String name;
                      ${(1 to i).gen(j => xs"""
                          private final Arbitrary<T$j> a$j;
                      """)("\n")}

                      ForAll$i(String name, $parametersDecl) {
                          this.name = name;
                          ${(1 to i).gen(j => xs"""
                              this.a$j = a$j;
                          """)("\n")}
                      }

                      /$javadoc
                       * Returns a checkable property that checks values of the $i variables of this {@code ForAll} quantor.
                       *
                       * @param predicate A $i-ary predicate
                       * @return a new {@code Property$i} of $i variables.
                       */
                      public Property$i<$generics> suchThat(${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Boolean> predicate) {
                          final ${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Condition> proposition = (${params("t")}) -> new Condition(true, predicate.apply(${params("t")}));
                          return new Property$i<>(name, ${params("a")}, proposition);
                      }
                  }
              """
          })("\n\n")}

          ${(1 to N).gen(i => {

              val checkedFunctionType = im.getType(s"io.vavr.CheckedFunction$i")
              val optionType = im.getType("io.vavr.control.Option")
              val randomType = im.getType("java.util.Random")
              val tryType = im.getType("io.vavr.control.Try")
              val checkException = "CheckException"
              val tupleType = im.getType(s"io.vavr.Tuple")

              val generics = (1 to i).gen(j => s"T$j")(", ")
              val params = (paramName: String) => (1 to i).gen(j => s"$paramName$j")(", ")
              val parametersDecl = (1 to i).gen(j => s"Arbitrary<T$j> a$j")(", ")

              xs"""
                  /$javadoc
                   * Represents a $i-ary checkable property.
                   *
                   * @author Daniel Dietrich
                   */
                  public static class Property$i<$generics> implements Checkable {

                      private final String name;
                      ${(1 to i).gen(j => xs"""
                          private final Arbitrary<T$j> a$j;
                      """)("\n")}
                      private final $checkedFunctionType<$generics, Condition> predicate;

                      Property$i(String name, $parametersDecl, $checkedFunctionType<$generics, Condition> predicate) {
                          this.name = name;
                          ${(1 to i).gen(j => xs"""
                              this.a$j = a$j;
                          """)("\n")}
                          this.predicate = predicate;
                      }

                      /$javadoc
                       * Returns an implication which composes this Property as pre-condition and a given post-condition.
                       *
                       * @param postcondition The postcondition of this implication
                       * @return A new Checkable implication
                       */
                      public Checkable implies($checkedFunctionType<$generics, Boolean> postcondition) {
                          final $checkedFunctionType<$generics, Condition> implication = (${params("t")}) -> {
                              final Condition precondition = predicate.apply(${params("t")});
                              if (precondition.isFalse()) {
                                  return Condition.EX_FALSO_QUODLIBET;
                              } else {
                                  return new Condition(true, postcondition.apply(${params("t")}));
                              }
                          };
                          return new Property$i<>(name, ${params("a")}, implication);
                      }

                      @Override
                      public CheckResult check($randomType random, int size, int tries) {
                          ${im.getType("java.util.Objects")}.requireNonNull(random, "random is null");
                          if (tries < 0) {
                              throw new IllegalArgumentException("tries < 0");
                          }
                          final long startTime = System.currentTimeMillis();
                          try {
                              ${(1 to i).gen(j => {
                                  s"""final Gen<T$j> gen$j = $tryType.of(() -> a$j.apply(size)).recover(x -> { throw arbitraryError($j, size, x); }).get();"""
                              })("\n")}
                              boolean exhausted = true;
                              for (int i = 1; i <= tries; i++) {
                                  try {
                                      ${(1 to i).gen(j => {
                                        s"""final T$j val$j = $tryType.of(() -> gen$j.apply(random)).recover(x -> { throw genError($j, size, x); }).get();"""
                                      })("\n")}
                                      try {
                                          final Condition condition = $tryType.of(() -> predicate.apply(${(1 to i).gen(j => s"val$j")(", ")})).recover(x -> { throw predicateError(x); }).get();
                                          if (condition.precondition) {
                                              exhausted = false;
                                              if (!condition.postcondition) {
                                                  logFalsified(name, i, System.currentTimeMillis() - startTime);
                                                  return new CheckResult.Falsified(name, i, $tupleType.of(${(1 to i).gen(j => s"val$j")(", ")}));
                                              }
                                          }
                                      } catch(CheckError err) {
                                          logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                                          return new CheckResult.Erroneous(name, i, err, $optionType.some($tupleType.of(${(1 to i).gen(j => s"val$j")(", ")})));
                                      }
                                  } catch(CheckError err) {
                                      logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                                      return new CheckResult.Erroneous(name, i, err, $optionType.none());
                                  }
                              }
                              logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
                              return new CheckResult.Satisfied(name, tries, exhausted);
                          } catch(CheckError err) {
                              logErroneous(name, 0, System.currentTimeMillis() - startTime, err.getMessage());
                              return new CheckResult.Erroneous(name, 0, err, $optionType.none());
                          }
                      }
                  }
              """
          })("\n\n")}

          /**
           * Internally used to model conditions composed of pre- and post-condition.
           */
          static class Condition {

              static final Condition EX_FALSO_QUODLIBET = new Condition(false, true);

              final boolean precondition;
              final boolean postcondition;

              Condition(boolean precondition, boolean postcondition) {
                  this.precondition = precondition;
                  this.postcondition = postcondition;
              }

              // ¬(p => q) ≡ ¬(¬p ∨ q) ≡ p ∧ ¬q
              boolean isFalse() {
                  return precondition && !postcondition;
              }
          }

          /**
           * Internally used to provide more specific error messages.
           */
          static class CheckError extends Error {

              private static final long serialVersionUID = 1L;

              CheckError(String message, Throwable cause) {
                  super(message, cause);
              }
          }
      }
    """
  }
}

/**
 * Generate Vavr src-gen/test/java classes
 */
def generateTestClasses(): Unit = {

  genPropertyCheckTests()

  /**
   * Generator of Property-check tests
   */
  def genPropertyCheckTests(): Unit = {
    genVavrFile("io.vavr.test", "PropertyTest", baseDir = TARGET_TEST)((im: ImportManager, packageName, className) => {

      // main classes
      val list = im.getType("io.vavr.collection.List")
      val predicate = im.getType("io.vavr.CheckedFunction1")
      val random = im.getType("java.util.Random")
      val tuple = im.getType("io.vavr.Tuple")

      // test classes
      val test = im.getType("org.junit.jupiter.api.Test")
      val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
      val assertThrows = im.getStatic("org.junit.jupiter.api.Assertions.assertThrows")
      val woops  = "yay! (this is a negative test)"

      xs"""
        public class $className {

            static <T> $predicate<T, Boolean> tautology() {
                return any -> true;
            }

            static <T> $predicate<T, Boolean> falsum() {
                return any -> false;
            }

            static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

            @$test
            public void shouldThrowWhenPropertyNameIsNull() {
                $assertThrows(${im.getType("java.lang.NullPointerException")}.class, () -> Property.def(null));
            }

            @$test
            public void shouldThrowWhenPropertyNameIsEmpty() {
                $assertThrows(${im.getType("java.lang.IllegalArgumentException")}.class, () -> Property.def(""));
            }

            // -- Property.check methods

            @$test
            public void shouldCheckUsingDefaultConfiguration() {
                final CheckResult result = Property.def("test").forAll(OBJECTS).suchThat(tautology()).check();
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
            }

            @$test
            public void shouldCheckGivenSizeAndTries() {
                final CheckResult result = Property.def("test").forAll(OBJECTS).suchThat(tautology()).check(0, 0);
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isTrue();
            }

            @$test
            public void shouldThrowOnCheckGivenNegativeTries() {
                $assertThrows(${im.getType("java.lang.IllegalArgumentException")}.class, () -> Property.def("test")
                  .forAll(OBJECTS)
                  .suchThat(tautology())
                  .check(0, -1));
            }

            @$test
            public void shouldCheckGivenRandomAndSizeAndTries() {
                final CheckResult result = Property.def("test").forAll(OBJECTS).suchThat(tautology()).check(new $random(), 0, 0);
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isTrue();
            }

            // -- satisfaction

            @$test
            public void shouldCheckPythagoras() {

                final Arbitrary<Double> real = n -> Gen.choose(0, (double) n).filter(d -> d > .0d);

                // (∀a,b ∈ ℝ+ ∃c ∈ ℝ+ : a²+b²=c²) ≡ (∀a,b ∈ ℝ+ : √(a²+b²) ∈ ℝ+)
                final Checkable property = Property.def("test").forAll(real, real).suchThat((a, b) -> Math.sqrt(a * a + b * b) > .0d);
                final CheckResult result = property.check();

                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
            }

            @$test
            public void shouldCheckZipAndThenUnzipIsIdempotentForListsOfSameLength() {
                // ∀is,ss: length(is) = length(ss) → unzip(zip(is, ss)) = (is, ss)
                final Arbitrary<$list<Integer>> ints = Arbitrary.list(size -> Gen.choose(0, size));
                final Arbitrary<$list<String>> strings = Arbitrary.list(
                        Arbitrary.string(
                            Gen.frequency(
                                Tuple.of(1, Gen.choose('A', 'Z')),
                                Tuple.of(1, Gen.choose('a', 'z')),
                                Tuple.of(1, Gen.choose('0', '9'))
                            )));
                final CheckResult result = Property.def("test")
                        .forAll(ints, strings)
                        .suchThat((is, ss) -> is.length() == ss.length())
                        .implies((is, ss) -> is.zip(ss).unzip(t -> t).equals($tuple.of(is, ss)))
                        .check();
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
            }

            // -- exhausting

            @$test
            public void shouldRecognizeExhaustedParameters() {
                final CheckResult result = Property.def("test").forAll(OBJECTS).suchThat(falsum()).implies(tautology()).check();
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isTrue();
            }

            // -- falsification

            @$test
            public void shouldFalsifyFalseProperty() {
                final Arbitrary<Integer> ones = n -> random -> 1;
                final CheckResult result = Property.def("test").forAll(ones).suchThat(one -> one == 2).check();
                $assertThat(result.isFalsified()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(1);
            }

            // -- error detection

            @$test
            public void shouldRecognizeArbitraryError() {
                final Arbitrary<?> arbitrary = n -> { throw new RuntimeException("$woops"); };
                final CheckResult result = Property.def("test").forAll(arbitrary).suchThat(tautology()).check();
                $assertThat(result.isErroneous()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(0);
                $assertThat(result.sample().isEmpty()).isTrue();
            }

            @$test
            public void shouldRecognizeGenError() {
                final Arbitrary<?> arbitrary = Gen.fail("$woops").arbitrary();
                final CheckResult result = Property.def("test").forAll(arbitrary).suchThat(tautology()).check();
                $assertThat(result.isErroneous()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(1);
                $assertThat(result.sample().isEmpty()).isTrue();
            }

            @$test
            public void shouldRecognizePropertyError() {
                final Arbitrary<Integer> a1 = n -> random -> 1;
                final Arbitrary<Integer> a2 = n -> random -> 2;
                final CheckResult result = Property.def("test").forAll(a1, a2).suchThat((a, b) -> {
                    throw new RuntimeException("$woops");
                }).check();
                $assertThat(result.isErroneous()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(1);
                $assertThat(result.sample().isDefined()).isTrue();
                $assertThat(result.sample().get()).isEqualTo(Tuple.of(1, 2));
            }

            // -- Property.and tests

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
                final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
                final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
                final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
                final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
                final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
                final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
                final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
                final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }

            // -- Property.or tests

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
                final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
                final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
                final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
                final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
                final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
                final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
                final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
                final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }
        }
      """
    })

    for (i <- 1 to N) {
      genVavrFile("io.vavr.test", s"PropertyCheck${i}Test", baseDir = TARGET_TEST)((im: ImportManager, packageName, className) => {

        val generics = (1 to i).gen(j => "Object")(", ")
        val arbitraries = (1 to i).gen(j => "OBJECTS")(", ")
        val arbitrariesMinus1 = (1 until i).gen(j => "OBJECTS")(", ")
        val args = (1 to i).gen(j => s"o$j")(", ")

        // test classes
        val test = im.getType("org.junit.jupiter.api.Test")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
        val assertThrows = im.getStatic("org.junit.jupiter.api.Assertions.assertThrows")
        val woops = "yay! (this is a negative test)"

        xs"""
          public class $className {

              static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

              @$test
              public void shouldApplyForAllOfArity$i() {
                  final Property.ForAll$i<${(1 to i).gen(j => "Object")(", ")}> forAll = Property.def("test").forAll(${(1 to i).gen(j => "null")(", ")});
                  $assertThat(forAll).isNotNull();
              }

              @$test
              public void shouldApplySuchThatOfArity$i() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> true;
                  final Property.Property$i<$generics> suchThat = forAll.suchThat(predicate);
                  $assertThat(suchThat).isNotNull();
              }

              @$test
              public void shouldCheckTrueProperty$i() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> true;
                  final CheckResult result = forAll.suchThat(predicate).check();
                  $assertThat(result.isSatisfied()).isTrue();
                  $assertThat(result.isExhausted()).isFalse();
              }

              @$test
              public void shouldCheckFalseProperty$i() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> false;
                  final CheckResult result = forAll.suchThat(predicate).check();
                  $assertThat(result.isFalsified()).isTrue();
              }

              @$test
              public void shouldCheckErroneousProperty$i() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> { throw new RuntimeException("$woops"); };
                  final CheckResult result = forAll.suchThat(predicate).check();
                  $assertThat(result.isErroneous()).isTrue();
              }

              @$test
              public void shouldCheckProperty${i}ImplicationWithTruePrecondition() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Boolean> p1 = ($args) -> true;
                  final ${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Boolean> p2 = ($args) -> true;
                  final CheckResult result = forAll.suchThat(p1).implies(p2).check();
                  $assertThat(result.isSatisfied()).isTrue();
                  $assertThat(result.isExhausted()).isFalse();
              }

              @$test
              public void shouldCheckProperty${i}ImplicationWithFalsePrecondition() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Boolean> p1 = ($args) -> false;
                  final ${im.getType(s"io.vavr.CheckedFunction$i")}<$generics, Boolean> p2 = ($args) -> true;
                  final CheckResult result = forAll.suchThat(p1).implies(p2).check();
                  $assertThat(result.isSatisfied()).isTrue();
                  $assertThat(result.isExhausted()).isTrue();
              }

              @$test
              public void shouldThrowOnProperty${i}CheckGivenNegativeTries() {
                  $assertThrows(${im.getType("java.lang.IllegalArgumentException")}.class, () -> Property.def("test")
                    .forAll($arbitraries)
                    .suchThat(($args) -> true)
                    .check(Checkable.RNG.get(), 0, -1));
              }

              @$test
              public void shouldReturnErroneousProperty${i}CheckResultIfGenFails() {
                  final Arbitrary<Object> failingGen = Gen.fail("$woops").arbitrary();
                  final CheckResult result = Property.def("test")
                      .forAll(failingGen${(i > 1).gen(s", $arbitrariesMinus1")})
                      .suchThat(($args) -> true)
                      .check();
                  $assertThat(result.isErroneous()).isTrue();
              }

              @$test
              public void shouldReturnErroneousProperty${i}CheckResultIfArbitraryFails() {
                  final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("$woops"); };
                  final CheckResult result = Property.def("test")
                      .forAll(failingArbitrary${(i > 1).gen(s", $arbitrariesMinus1")})
                      .suchThat(($args) -> true)
                      .check();
                  $assertThat(result.isErroneous()).isTrue();
              }
          }
         """
      })
    }
  }
}

/**
 * Adds the Vavr header to generated classes.
 * @param packageName Java package name
 * @param className Simple java class name
 * @param gen A generator which produces a String.
 */
def genVavrFile(packageName: String, className: String, baseDir: String = TARGET_MAIN)(gen: (ImportManager, String, String) => String, knownSimpleClassNames: List[String] = List()) =
  genJavaFile(baseDir, packageName, className)(xraw"""
    /*  __    __  __  __    __  ___
     * \  \  /  /    \  \  /  /  __/
     *  \  \/  /  /\  \  \/  /  /
     *   \____/__/  \__\____/__/
     *
     * Copyright 2014-2024 Vavr, https://vavr.io
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
  """)(gen)(CHARSET)

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
     J A V A   G E N E R A T O R   F R A M E W O R K
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

object JavaGenerator {

  import java.nio.charset.{Charset, StandardCharsets}

  import Generator._

  /**
   * Generates a Java file.
   * @param packageName Java package name
   * @param className Simple java class name
   * @param classHeader A class file header
   * @param gen A generator which produces a String.
   */
  def genJavaFile(baseDir: String, packageName: String, className: String)(classHeader: String)(gen: (ImportManager, String, String) => String, knownSimpleClassNames: List[String] = List())(implicit charset: Charset = StandardCharsets.UTF_8): Unit = {

    // DEV-NOTE: using File.separator instead of "/" does *not* work on windows!
    val dirName = packageName.replaceAll("[.]", "/")
    val fileName = className + ".java"
    val importManager = new ImportManager(packageName, knownSimpleClassNames)
    val classBody = gen.apply(importManager, packageName, className)

    genFile(baseDir, dirName, fileName)(xraw"""
      $classHeader
      package $packageName;

      /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
         G E N E R A T O R   C R A F T E D
      \*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

      ${importManager.getImports}

      $classBody
    """)
  }

  /**
   * A <em>stateful</em> ImportManager which generates an import section of a Java class file.
   * @param packageNameOfClass package name of the generated class
   * @param knownSimpleClassNames a list of class names which may not be imported from other packages
   */
  class ImportManager(packageNameOfClass: String, knownSimpleClassNames: List[String], wildcardThreshold: Int = 5) {

    import scala.collection.mutable

    val nonStaticImports = new mutable.HashMap[String, String]
    val staticImports = new mutable.HashMap[String, String]

    def getType(fullQualifiedName: String): String = simplify(fullQualifiedName, nonStaticImports)

    def getStatic(fullQualifiedName: String): String = simplify(fullQualifiedName, staticImports)

    def getImports: String = {

      def optimizeImports(imports: Seq[String], static: Boolean): String = {
        val counts = imports.map(getPackageName).groupBy(s => s).map { case (s, list) => s -> list.length }
        val directImports = imports.filter(s => counts(getPackageName(s)) <= wildcardThreshold)
        val wildcardImports = counts.filter { case (_, count) => count > wildcardThreshold }.keySet.toIndexedSeq.map(s => s"$s.*")
        (directImports ++ wildcardImports).sorted.map(fqn => s"import ${static.gen("static ")}$fqn;").mkString("\n")
      }

      val staticImportSection = optimizeImports(staticImports.keySet.toIndexedSeq, static = true)
      val nonStaticImportSection = optimizeImports(nonStaticImports.keySet.toIndexedSeq, static = false)
      Seq(staticImportSection, nonStaticImportSection).mkString("\n\n")
    }

    private def simplify(fullQualifiedName: String, imports: mutable.HashMap[String, String]): String = {
      val simpleName = getSimpleName(fullQualifiedName)
      val packageName = getPackageName(fullQualifiedName)
      if (packageName.isEmpty && !packageNameOfClass.isEmpty) {
        throw new IllegalStateException(s"Can't import class '$simpleName' located in default package")
      } else if (packageName == packageNameOfClass) {
        simpleName
      } else if (imports.contains(fullQualifiedName)) {
        imports(fullQualifiedName)
      } else if (simpleName != "*" && (knownSimpleClassNames.contains(simpleName) || imports.values.exists(simpleName.equals(_)))) {
        fullQualifiedName
      } else {
        imports += fullQualifiedName -> simpleName
        simpleName
      }
    }

    private def getPackageName(fqn: String): String = fqn.substring(0, Math.max(fqn.lastIndexOf("."), 0))
    private def getSimpleName(fqn: String): String = fqn.substring(fqn.lastIndexOf(".") + 1)
  }
}

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
     C O R E   G E N E R A T O R   F R A M E W O R K
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

/**
 * Core generator API
 */
object Generator {

  import java.nio.charset.{Charset, StandardCharsets}
  import java.nio.file.{Files, Paths, StandardOpenOption}

  /**
   * Generates a file by writing string contents to the file system.
   *
   * @param baseDir The base directory, e.g. src-gen
   * @param dirName The directory relative to baseDir, e.g. main/java
   * @param fileName The file name within baseDir/dirName
   * @param createOption One of java.nio.file.{StandardOpenOption.CREATE_NEW, StandardOpenOption.CREATE}, default: CREATE_NEW
   * @param contents The string contents of the file
   * @param charset The charset, by default UTF-8
   */
  def genFile(baseDir: String, dirName: String, fileName: String, createOption: StandardOpenOption = StandardOpenOption.CREATE_NEW)(contents: => String)(implicit charset: Charset = StandardCharsets.UTF_8): Unit = {

    // println(s"Generating $dirName${File.separator}$fileName")

    Files.write(
      Files.createDirectories(Paths.get(baseDir, dirName)).resolve(fileName),
      contents.getBytes(charset),
      createOption, StandardOpenOption.WRITE)
  }

  implicit class IntExtensions(i: Int) {

    // returns i as ordinal, i.e. 1st, 2nd, 3rd, 4th, ...
    def ordinal: String =
      s"$i" + (if (i >= 4 && i <= 20) {
        "th"
      } else {
        i % 10 match {
          case 1 => "st"
          case 2 => "nd"
          case 3 => "rd"
          case _ => "th"
        }
      })

    // returns the grammatical number of a string, i.e. `i.numerus("name")` is
    // 0: "no name", 1: "one name", 2: "two names", 3: "three names", 4: "4 names", ...
    def numerus(noun: String): String = Math.abs(i) match {
      case 0 => s"no ${noun}s"
      case 1 => s"one $noun"
      case 2 => s"two ${noun}s"
      case 3 => s"three ${noun}s"
      case _ => s"$i ${noun}s"
    }
  }

  implicit class StringExtensions(s: String) {

    // gets first char of s as string. throws if string is empty
    def first: String = s.substring(0, 1)

    // converts first char of s to upper case. throws if string is empty
    def firstUpper: String = s(0).toUpper + s.substring(1)

    // converts first char of s to lower case. throws if string is empty
    def firstLower: String = s(0).toLower + s.substring(1)
  }

  implicit class BooleanExtensions(condition: Boolean) {
    def gen(s: => String): String =  if (condition) s else ""
  }

  implicit class OptionExtensions(option: Option[Any]) {
    def gen(f: String => String): String =  option.map(any => f.apply(any.toString)).getOrElse("")
    def gen: String = option.map(any => any.toString).getOrElse("")
  }

  /**
   * Generates a String based on ints within a specific range.
   * {{{
   * (1 to 3).gen(i => s"x$i")(", ") // x1, x2, x3
   * (1 to 3).reverse.gen(i -> s"x$i")(", ") // x3, x2, x1
   * }}}
   * @param range A Range
   */
  implicit class RangeExtensions(range: Range) {
    def gen(f: Int => String = String.valueOf)(implicit delimiter: String = ""): String =
      range map f mkString delimiter
  }

  /**
   * Generates a String based on an Iterable of objects. Objects are converted to Strings via toString.
   * {{{
   * // val a = "A"
   * // val b = "B"
   * // val c = "C"
   * Seq("a", "b", "c").gen(s => raw"""val $s = "${s.toUpperCase}"""")("\n")
   * }}}
   *
   * @param iterable An Interable
   */
  implicit class IterableExtensions(iterable: Iterable[Any]) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      iterable.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple1Extensions(tuple: Tuple1[Any]) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      f.apply(tuple._1.toString) mkString delimiter
  }

  implicit class Tuple2Extensions(tuple: (Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  /**
   * Generates a String based on a tuple of objects. Objects are converted to Strings via toString.
   * {{{
   * // val seq = Seq("a", "1", "true")
   * s"val seq = Seq(${("a", 1, true).gen(s => s""""$s"""")(", ")})"
   * }}}
   * @param tuple A Tuple
   */
  implicit class Tuple3Extensions(tuple: (Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple4Extensions(tuple: (Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple5Extensions(tuple: (Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple6Extensions(tuple: (Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple7Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple8Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple9Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple10Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple11Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple12Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple13Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple14Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple15Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple16Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple17Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple18Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple19Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple20Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple21Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple22Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  /**
   * Provides StringContext extensions, e.g. indentation of cascaded rich strings.
   * @param sc Current StringContext
   * @see <a href="https://gist.github.com/danieldietrich/5174348">this gist</a>
   */
  implicit class StringContextExtensions(sc: StringContext) {

    import scala.util.Properties.lineSeparator

    /**
     * Formats escaped strings.
     * @param args StringContext parts
     * @return An aligned String
     */
    def xs(args: Any*): String = align(sc.s, args)

    /**
     * Formats raw/unescaped strings.
     * @param args StringContext parts
     * @return An aligned String
     */
    def xraw(args: Any*): String = align(sc.raw, args)

    /**
     * Indenting a rich string, removing first and last newline.
     * A rich string consists of arguments surrounded by text parts.
     */
    private def align(interpolator: Seq[Any] => String, args: Seq[Any]): String = {

      // indent embedded strings, invariant: parts.length = args.length + 1
      val indentedArgs = for {
        (part, arg) <- sc.parts zip args.map(s => if (s == null) "" else s.toString)
      } yield {
        // get the leading space of last line of current part
        val space = """([ \t]*)[^\s]*$""".r.findFirstMatchIn(part).map(_.group(1)).getOrElse("")
        // add this leading space to each line (except the first) of current arg
        arg.split("\r?\n") match {
          case lines: Array[String] if lines.nonEmpty => lines reduce (_ + lineSeparator + space + _)
          case whitespace => whitespace mkString ""
        }
      }

      // remove first and last newline and split string into separate lines
      // adding termination symbol \u0000 in order to preserve empty strings between last newlines when splitting
      val split = (interpolator(indentedArgs).replaceAll( """(^[ \t]*\r?\n)|(\r?\n[ \t]*$)""", "") + '\u0000').split("\r?\n")

      // find smallest indentation
      val prefix = split filter (!_.trim().isEmpty) map { s =>
        """^\s+""".r.findFirstIn(s).getOrElse("")
      } match {
        case prefixes: Array[String] if prefixes.length > 0 => prefixes reduce { (s1, s2) =>
          if (s1.length <= s2.length) s1 else s2
        }
        case _ => ""
      }

      // align all lines
      val aligned = split map { s =>
        if (s.startsWith(prefix)) s.substring(prefix.length) else s
      } mkString lineSeparator dropRight 1 // dropping termination character \u0000

      // combine multiple newlines to two
      aligned.replaceAll("""[ \t]*\r?\n ([ \t]*\r?\n)+""", lineSeparator * 2)
    }
  }
}
