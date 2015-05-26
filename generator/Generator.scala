/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */

// temporarily needed to circumvent https://issues.scala-lang.org/browse/SI-3772 (see case class Generics)
import Generator._
import JavaGenerator._

import scala.language.implicitConversions

val N = 8
val TARGET_MAIN = "src-gen/main/java"
val TARGET_TEST = "src-gen/test/java"
val CHARSET = java.nio.charset.StandardCharsets.UTF_8

/**
 * ENTRY POINT
 */
def run(): Unit = {
  generateMainClasses()
  generateTestClasses()
}

/**
 * Generate Javaslang src-gen/main/java classes
 */
def generateMainClasses(): Unit = {

  // Workaround: Use /$** instead of /** in a StringContext when IntelliJ IDEA otherwise shows up errors in the editor
  val javadoc = "**"

  genMatch()
  genFunctions()
  genPropertyChecks()
  genTuples()

  def genMatch(): Unit =
    genJavaslangFile("javaslang.control", "Match")((im: ImportManager, packageName: String, className: String) => {

      val function1 = im.getType("javaslang.Function1")
      val lazyy = im.getType("javaslang.Lazy")
      val list = im.getType("javaslang.collection.List")
      val option = im.getType("javaslang.control.Option")
      val none = im.getType("javaslang.control.None")
      val some = im.getType("javaslang.control.Some")

      val objects = im.getType("java.util.Objects")
      val function = im.getType("java.util.function.Function")
      val predicate = im.getType("java.util.function.Predicate")
      val supplier = im.getType("java.util.function.Supplier")

      val primitiveTypes = Seq("boolean", "byte", "char", "double", "float", "int", "long", "short")

      def toObject(primitiveType: String): String = primitiveType match {
        case "char" => "Character"
        case "int" => "Integer"
        case _ => primitiveType.firstUpper
      }

      xs"""
        /**
         * {@code Match} is a better switch for Java. Some characteristics of {@code Match} are:
         * <ul>
         * <li>it has a fluent API</li>
         * <li>it is a {@code Function<Object, R>}</li>
         * <li>it is able to match types, i.e. {@code Match.when((byte b) -> "a byte: " + b)}</li>
         * <li>it is able to match values, i.e. {@code Match.when(BigDecimal.ZERO, b -> "Zero: " + b)}</li>
         * </ul>
         *
         * Example of a Match as <a href="http://en.wikipedia.org/wiki/Partial_function"><strong>partial</strong> function</a>:
         *
         * <pre>
         * <code>
         * final Match&lt;Number&gt; toNumber = Match.as(Number.class)
         *     .when((Integer i) -&gt; i)
         *     .when((String s) -&gt; new BigDecimal(s));
         * final Number number = toNumber.apply(1.0d); // throws a MatchError
         * </code>
         * </pre>
         *
         * Example of a Match as <a href="http://en.wikipedia.org/wiki/Function_(mathematics)"><strong>total</strong> function</a>:
         *
         * <pre>
         * <code>
         * Match.as(Number.class)
         *     .when((Integer i) -&gt; i)
         *     .when((String s) -&gt; new BigDecimal(s))
         *     .otherwise(() -&gt; -1)
         *     .apply(1.0d); // result: -1
         * </code>
         * </pre>
         *
         * @param <R> The result type of the {@code Match}.
         * @since 1.0.0
         */
        public interface Match<R> extends $function<Object, R> {

            /**
             * Applies this {@code Match} to an {@code Object}.
             *
             * @param o an {@code Object}
             * @throws MatchError if no {@code Case} matched
             */
            @Override
            R apply(Object o);

            /**
             * Specifies the type of the match expression. In many cases it is not necessary to call {@code as}. This
             * method is intended to be used for readability reasons when the upper bound of the cases cannot be inferred,
             * i.e. instead of
             *
             * <pre>
             * <code>
             * final Match&lt;Number&gt; toNumber = Match
             *         .&lt;Number&gt; when((Integer i) -&gt; i)
             *         .when((String s) -&gt; new BigDecimal(s))
             * </code>
             * </pre>
             *
             * we write
             *
             * <pre>
             * <code>
             * final Match&lt;Number&gt; toNumber = Match.as(Number.class)
             *         .when((Integer i) -&gt; i)
             *         .when((String s) -&gt; new BigDecimal(s))
             * </code>
             * </pre>
             *
             * @param type the hint of type {@code R}
             * @param <R>  the type of the {@code Match} expression
             * @return a new match builder
             */
            static <R> Typed<R> as(Class<R> type) {
                $objects.requireNonNull(type, "type is null");
                return new Typed<>();
            }

            /**
             * Creates a {@code Match.Case} by value.
             *
             * @param <T> type of the prototype value
             * @param <R> result type of the matched case
             * @param prototype A specific value to be matched
             * @param function A function which is applied to the value given a match
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            static <T, R> Case<R> when(T prototype, $function1<? super T, ? extends R> function) {
                $objects.requireNonNull(function, "function is null");
                return Case.of(prototype, function);
            }

            /**
             * Creates a {@code Match.Case} by type.
             *
             * @param <R> result type of the matched case
             * @param function An {@code Object} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            static <R> Case<R> when($function1<?, ? extends R> function) {
                $objects.requireNonNull(function, "function is null");
                return Case.of(function);
            }

            ${primitiveTypes.gen(name => xs"""
              /$javadoc
               * Creates a {@code Match.Case} by primitive type {@code $name}.
               *
               * @param <R> result type of the matched case
               * @param function An {@code $name} to {@code R} function
               * @return a new {@code Case}
               * @throws NullPointerException if {@code function} is null
               */
              @SuppressWarnings("overloads")
              static <R> Case<R> when(${name.firstUpper}Function<? extends R> function) {
                  $objects.requireNonNull(function, "function is null");
                  return Case.of(function);
              }
            """)("\n\n")}

            /**
             * The result of {@code Match.as(Class)}, which explicitly sets the {@code Match} result type.
             *
             * @param <R> the result type
             * @since 1.2.1
             */
            final class Typed<R> implements Expression.HasCases<R> {

                private Typed() {
                }

                @Override
                public <T> Case<R> when(T prototype, $function1<? super T, ? extends R> function) {
                    $objects.requireNonNull(function, "function is null");
                    return Case.of(prototype, function);
                }

                @Override
                public Case<R> when($function1<?, ? extends R> function) {
                    $objects.requireNonNull(function, "function is null");
                    return Case.of(function);
                }

                ${primitiveTypes.gen(name => xs"""
                  @Override
                  public Case<R> when(${name.firstUpper}Function<? extends R> function) {
                      $objects.requireNonNull(function, "function is null");
                      return Case.of(function);
                  }
                """)("\n\n")}
            }

            /**
             * A {@code Match.Case} which matches an {@code Object} by <em>type</em> or by <em>value</em>.
             * <p>
             * Typically there is a chain of match cases. The first applicable match is applied to an object.
             * <p>
             * The {@code otherwise()} methods provide a default value which is returned if no case matches.
             *
             * @param <R> result type of the {@code Match.Case}
             * @since 1.0.0
             */
            final class Case<R> implements Match<R>, Expression.HasCases<R> {

                private final $list<$function<Object, $option<R>>> cases;
                private final $lazyy<Expression<R>> match;

                private Case(List<$function<Object, $option<R>>> cases) {
                    this.cases = cases;
                    this.match = $lazyy.of(() -> new Expression<>(cases.reverse(), $none.instance()));
                }

                private static <T, R> Case<R> of(T prototype, $function1<? super T, ? extends R> function) {
                    return new Case<>($list.of(Case.when(new $some<>(prototype), function)));
                }

                @SuppressWarnings("overloads")
                private static <R> Case<R> of($function1<?, ? extends R> function) {
                    return new Case<>($list.of(Case.when($none.instance(), function)));
                }

                ${primitiveTypes.gen(name => xs"""
                  @SuppressWarnings("overloads")
                  private static <R> Case<R> of(${name.firstUpper}Function<? extends R> function) {
                    return new Case<>($list.of(Case.when($none.instance(), ($function1<${toObject(name)}, R>) function::apply, ${toObject(name)}.class)));
                  }
                """)("\n\n")}

                @Override
                public R apply(Object o) {
                    return match.get().apply(o);
                }

                @Override
                public <T> Case<R> when(T prototype, $function1<? super T, ? extends R> function) {
                    $objects.requireNonNull(function, "function is null");
                    final $function<Object, $option<R>> when = when(new $some<>(prototype), function);
                    return new Case<>(cases.prepend(when));
                }

                @Override
                public Case<R> when($function1<?, ? extends R> function) {
                    $objects.requireNonNull(function, "function is null");
                    final $function<Object, $option<R>> when = when($none.instance(), function);
                    return new Case<>(cases.prepend(when));
                }

                ${primitiveTypes.gen(name => xs"""
                  @Override
                  public Case<R> when(${name.firstUpper}Function<? extends R> function) {
                      $objects.requireNonNull(function, "function is null");
                      final $function<Object, $option<R>> when = when($none.instance(), ($function1<${toObject(name)}, R>) function::apply, ${toObject(name)}.class);
                      return new Case<>(cases.prepend(when));
                  }
                """)("\n\n")}

                /**
                 * <p>Provides a default value which is returned if no case matches.</p>
                 * <p>Note that this method takes the default by value which means that the input is
                 * <em>eagerly evaluated</em> even if the {@code otherwise} clause of the expression is not executed.
                 * Unless you already have a default value calculated or as a literal it might be better
                 * to use the {@link Match.Case#otherwise(Supplier)} alternative to gain lazy evaluation.</p>
                 *
                 * @param defaultValue The default value.
                 * @return a Match-expression
                 */
                public Expression<R> otherwise(R defaultValue) {
                    return new Expression<>(cases.reverse(), new $some<>($lazyy.of(() -> defaultValue)));
                }

                /**
                 * <p>Provides a default value which is returned if no case matches.</p>
                 * @param defaultSupplier A Supplier returning the default value.
                 * @return a Match-expression
                 */
                public Expression<R> otherwise($supplier<R> defaultSupplier) {
                    $objects.requireNonNull(defaultSupplier, "defaultSupplier is null");
                    return new Expression<>(cases.reverse(), new $some<>($lazyy.of(defaultSupplier)));
                }

                private static <T, R> $function<Object, $option<R>> when($option<T> prototype, $function1<T, ? extends R> function) {
                    final ${im.getType("java.lang.invoke.MethodType")} type = function.getType();
                    // the compiler may add additional parameters to the lambda, our parameter is the last one
                    final Class<?> parameterType = type.parameterType(type.parameterCount() - 1);
                    return when(prototype, function, parameterType);
                }

                private static <T, R> $function<Object, $option<R>> when($option<T> prototype, $function1<T, ? extends R> function, Class<?> parameterType) {
                    final $predicate<Object> applicable = obj -> {
                        final boolean isCompatible = obj == null || parameterType.isAssignableFrom(obj.getClass());
                        return isCompatible
                                && prototype.map(val -> val == obj || (val != null && val.equals(obj))).orElse(obj != null);
                    };
                    return obj -> {
                        if (applicable.test(obj)) {
                            @SuppressWarnings("unchecked")
                            final R result = (($function1<Object, R>) function).apply(obj);
                            return new $some<>(result);
                        } else {
                            return $none.instance();
                        }
                    };
                }
            }

            /**
             * A final {@code Match} expression which may be applied to an {@code Object}.
             *
             * @param <R> result type of the {@code Match}
             * @since 1.0.0
             */
            final class Expression<R> implements Match<R> {

                private Iterable<$function<Object, $option<R>>> cases;
                private $option<$lazyy<R>> otherwise;

                private Expression(Iterable<$function<Object, $option<R>>> cases, $option<$lazyy<R>> otherwise) {
                    this.cases = cases;
                    this.otherwise = otherwise;
                }

                @Override
                public R apply(Object o) {
                    for ($function<Object, $option<R>> when : cases) {
                        final $option<R> result = when.apply(o);
                        if (result.isDefined()) {
                            return result.get();
                        }
                    }
                    return otherwise.orElseThrow(() -> new MatchError(o)).get();
                }

                // Note: placed this interface here, because interface Match cannot have private inner interfaces
                private interface HasCases<R> {

                    /**
                     * Creates a {@code Match.Case} by value.
                     *
                     * @param <T> type of the prototype value
                     * @param prototype A specific value to be matched
                     * @param function A function which is applied to the value given a match
                     * @return a new {@code Case}
                     * @throws NullPointerException if {@code function} is null
                     */
                    <T> HasCases<R> when(T prototype, $function1<? super T, ? extends R> function);

                    /**
                     * Creates a {@code Match.Case} by type.
                     *
                     * @param function An {@code Object} to {@code R} function
                     * @return a new {@code Case}
                     * @throws NullPointerException if {@code function} is null
                     */
                    @SuppressWarnings("overloads")
                    HasCases<R> when($function1<?, ? extends R> function);

                    ${primitiveTypes.gen(name => xs"""
                      /$javadoc
                       * Creates a {@code Match.Case} by primitive type {@code $name}.
                       *
                       * @param function An {@code $name} to {@code R} function
                       * @return a new {@code Case}
                       * @throws NullPointerException if {@code function} is null
                       */
                      @SuppressWarnings("overloads")
                      HasCases<R> when(${name.firstUpper}Function<? extends R> function);
                    """)("\n\n")}
                }
            }

            ${primitiveTypes.gen(name => xs"""
              /$javadoc
               * A function {@code f: $name -> R} that takes a primitive {@code $name} value and returns a value of type {@code R}.
               *
               * @param <R> return type of this function
               * @since 1.0.0
               */
              @FunctionalInterface
              interface ${name.firstUpper}Function<R> extends ${im.getType("java.io.Serializable")} {

                  /$javadoc
                   * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
                   */
                  long serialVersionUID = 1L;

                  /$javadoc
                   * Applies this function to the given value.
                   *
                   * @param value A $name value
                   * @return A new value of type R
                   */
                  R apply($name value);
              }
            """)("\n\n")}
        }
      """
    })

  /**
   * Generator of javaslang.test.Property
   */
  def genPropertyChecks(): Unit = {

    genJavaslangFile("javaslang.test", "Property")(genProperty)

    def genProperty(im: ImportManager, packageName: String, className: String): String = xs"""
      /**
       * A property builder which provides a fluent API to build checkable properties.
       * @since 1.2.0
       */
      public class $className {

          private final String name;

          /**
           * Construct a named property.
           * @param name property name
           */
          public $className(String name) {
              ${im.getType("java.util.Objects")}.requireNonNull(name, "name is null");
              if (name.isEmpty()) {
                  throw new IllegalArgumentException("name is empty");
              }
              this.name = name;
          }

          private static void logSatisfied(String name, int tries, boolean exhausted) {
              if (exhausted) {
                  log(String.format("%s: Exhausted after %s tests.", name, tries));
              } else {
                  log(String.format("%s: OK, passed %s tests.", name, tries));
              }
          }

          private static void logFalsified(String name, int currentTry) {
              log(String.format("%s: Falsified after %s passed tests.", name, currentTry - 1));
          }

          private static void logErroneous(String name, int currentTry, String errorMessage) {
              log(String.format("%s: Errored after %s passed tests with message: %s", name, Math.max(0, currentTry - 1), errorMessage));
          }

          private static void log(String msg) {
              System.out.println(msg);
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
                   * @since 1.2.0
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
                      public Property$i<$generics> suchThat(${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate) {
                          final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Condition> proposition = (${params("t")}) -> new Condition(true, predicate.apply(${params("t")}));
                          return new Property$i<>(name, ${params("a")}, proposition);
                      }
                  }
              """
          })("\n\n")}

          ${(1 to N).gen(i => {

              val checkedFunctionType = im.getType(s"javaslang.CheckedFunction$i")
              val failureType = im.getType("javaslang.control.Failure")
              val noneType = im.getType("javaslang.control.None")
              val randomType = im.getType("java.util.Random")
              val someType = im.getType("javaslang.control.Some")
              val tryType = im.getType("javaslang.control.Try")
              val tupleType = im.getType(s"javaslang.Tuple")

              val generics = (1 to i).gen(j => s"T$j")(", ")
              val params = (paramName: String) => (1 to i).gen(j => s"$paramName$j")(", ")
              val parametersDecl = (1 to i).gen(j => s"Arbitrary<T$j> a$j")(", ")

              xs"""
                  /$javadoc
                   * Represents a $i-ary checkable property.
                   * @since 1.2.0
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
                          try {
                              ${(1 to i).gen(j => {
                                  s"""final Gen<T$j> gen$j = $tryType.of(() -> a$j.apply(size)).recover(x -> { throw Errors.arbitraryError($j, size, x); }).get();"""
                              })("\n")}
                              boolean exhausted = true;
                              for (int i = 1; i <= tries; i++) {
                                  try {
                                      ${(1 to i).gen(j => {
                                        s"""final T$j val$j = $tryType.of(() -> gen$j.apply(random)).recover(x -> { throw Errors.genError($j, size, x); }).get();"""
                                      })("\n")}
                                      try {
                                          final Condition condition = $tryType.of(() -> predicate.apply(${(1 to i).gen(j => s"val$j")(", ")})).recover(x -> { throw Errors.predicateError(x); }).get();
                                          if (condition.precondition) {
                                              exhausted = false;
                                              if (!condition.postcondition) {
                                                  logFalsified(name, i);
                                                  return new CheckResult.Falsified(name, i, $tupleType.of(${(1 to i).gen(j => s"val$j")(", ")}));
                                              }
                                          }
                                      } catch($failureType.NonFatal nonFatal) {
                                          logErroneous(name, i, nonFatal.getCause().getMessage());
                                          return new CheckResult.Erroneous(name, i, (Error) nonFatal.getCause(), new $someType<>($tupleType.of(${(1 to i).gen(j => s"val$j")(", ")})));
                                      }
                                  } catch($failureType.NonFatal nonFatal) {
                                      logErroneous(name, i, nonFatal.getCause().getMessage());
                                      return new CheckResult.Erroneous(name, i, (Error) nonFatal.getCause(), $noneType.instance());
                                  }
                              }
                              logSatisfied(name, tries, exhausted);
                              return new CheckResult.Satisfied(name, tries, exhausted);
                          } catch($failureType.NonFatal nonFatal) {
                              logErroneous(name, 0, nonFatal.getCause().getMessage());
                              return new CheckResult.Erroneous(name, 0, (Error) nonFatal.getCause(), $noneType.instance());
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
      }
    """
  }

  /**
   * Generator of Functions
   */
  def genFunctions(): Unit = {

    (0 to N).foreach(i => {

      genJavaslangFile("javaslang", s"CheckedFunction$i")(genFunction("CheckedFunction", checked = true))
      genJavaslangFile("javaslang", s"Function$i")(genFunction("Function", checked = false))

      def genFunction(name: String, checked: Boolean)(im: ImportManager, packageName: String, className: String): String = {

        val generics = (1 to i).gen(j => s"T$j")(", ")
        val fullGenerics = s"<${(i > 0).gen(s"$generics, ")}R>"
        val genericsReversed = (1 to i).reverse.gen(j => s"T$j")(", ")
        val genericsTuple = if (i > 0) s"<$generics>" else ""
        val genericsFunction = if (i > 0) s"$generics, " else ""
        val genericsReversedFunction = if (i > 0) s"$genericsReversed, " else ""
        val curried = if (i == 0) "v" else (1 to i).gen(j => s"t$j")(" -> ")
        val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
        val params = (1 to i).gen(j => s"t$j")(", ")
        val paramsReversed = (1 to i).reverse.gen(j => s"t$j")(", ")
        val tupled = (1 to i).gen(j => s"t._$j")(", ")
        val compositionType = s"${checked.gen("Checked")}Function1"
        val Try = if (checked) im.getType("javaslang.control.Try") else ""
        val additionalExtends = (checked, i) match {
          case (false, 0) => ", " + im.getType("java.util.function.Supplier") + "<R>"
          case (false, 1) => ", " + im.getType("java.util.function.Function") + "<T1, R>"
          case (false, 2) => ", " + im.getType("java.util.function.BiFunction") + "<T1, T2, R>"
          case _ => ""
        }

        def curriedType(max: Int, function: String): String = {
          if (max == 0) {
            s"$className<R>"
          } else {
            def returnType(curr: Int, max: Int): String = {
              val isParam = curr < max
              val next = if (isParam) returnType(curr + 1, max) else "R"
              s"${function}1<T$curr, $next>"
            }
            returnType(1, max)
          }
        }

        def arguments(count: Int): String = count match {
          case 0 => "no arguments"
          case 1 => "one argument"
          case 2 => "two arguments"
          case 3 => "three arguments"
          case _ => s"$i arguments"
        }

        xs"""
          /$javadoc
           * Represents a function with ${arguments(i)}.
           ${(0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> argument $j of the function")("\n")}
           * @param <R> return type of the function
           * @since 1.1.0
           */
          @FunctionalInterface
          public interface $className$fullGenerics extends λ<R>$additionalExtends {

              /**
               * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
               */
              long serialVersionUID = 1L;

              /**
               * Lifts a <a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method
               * reference</a> to a {@code $className}.
               *
               * @param methodReference (typically) a method reference, e.g. {@code Type::method}
               ${(0 to i).gen(j => if (j == 0) "* @param <R> return type" else s"* @param <T$j> ${j.ordinal} argument")("\n")}
               * @return a {@code $className}
               */
              static $fullGenerics $className$fullGenerics lift($className$fullGenerics methodReference) {
                  return methodReference;
              }

              ${(i == 1).gen(xs"""
              /$javadoc
               * Returns the identity $className, i.e. the function that returns its input.
               *
               * @param <T> argument type (and return type) of the identity function
               * @return the identity $className
               */
              static <T> ${name}1<T, T> identity() {
                  return t -> t;
              }""")}

              /$javadoc
               * Applies this function to ${arguments(i)} and returns the result.
               ${(0 to i).gen(j => if (j == 0) "*" else s"* @param t$j argument $j")("\n")}
               * @return the result of function application
               * ${checked.gen("@throws Throwable if something goes wrong applying this function to the given arguments")}
               */
              R apply($paramsDecl)${checked.gen(" throws Throwable")};

              ${(1 to i - 1).gen(j => {
                val partialApplicationArgs = (1 to j).gen(k => s"T$k t$k")(", ")
                val resultFunctionGenerics = (j+1 to i).gen(k => s"T$k")(", ")
                val resultFunctionArgs = (j+1 to i).gen(k => s"T$k t$k")(", ")
                val fixedApplyArgs = (1 to j).gen(k => s"t$k")(", ")
                val variableApplyArgs = (j+1 to i).gen(k => s"t$k")(", ")
                xs"""
                  /$javadoc
                   * Applies this function partially to ${j.numerus("argument")}.
                   *
                   ${(1 to j).gen(k => s"* @param t$k argument $k")("\n")}
                   * @return a partial application of this function
                   * ${checked.gen("@throws Throwable if something goes wrong partially applying this function to the given arguments")}
                   */
                  default $name${i - j}<$resultFunctionGenerics, R> apply($partialApplicationArgs)${checked.gen(" throws Throwable")} {
                      return ($resultFunctionArgs) -> apply($fixedApplyArgs, $variableApplyArgs);
                  }
                """
              })("\n\n")}

              ${(!checked && i == 0).gen(xs"""
                /$javadoc
                 * Implementation of {@linkplain java.util.function.Supplier#get()}, just calls {@linkplain #apply()}.
                 *
                 * @return the result of {@code apply()}
                 */
                @Override
                default R get() {
                    return apply();
                }
              """)}

              @Override
              default int arity() {
                  return $i;
              }

              @Override
              default ${curriedType(i, name)} curried() {
                  return ${if (i < 2) "this" else s"$curried -> apply($params)"};
              }

              @Override
              default ${name}1<Tuple$i$genericsTuple, R> tupled() {
                  return t -> apply($tupled);
              }

              @Override
              default $className<${genericsReversedFunction}R> reversed() {
                  return ${if (i < 2) "this" else s"($paramsReversed) -> apply($params)"};
              }

              @Override
              default $className$fullGenerics memoized() {
                  ${val mappingFunction = (checked, i) match {
                      case (true, 0) => s"() -> $Try.of(this::apply).get()"
                      case (true, 1) => s"t -> $Try.of(() -> this.apply(t)).get()"
                      case (true, _) => s"t -> $Try.of(() -> tupled.apply(t)).get()"
                      case (false, 0) => s"this::apply"
                      case (false, 1) => s"this::apply"
                      case (false, _) => s"tupled::apply"
                    }
                    if (i == 0) xs"""
                      return Lazy.of($mappingFunction)::get;
                    """ else if (i == 1) xs"""
                      final ${im.getType("java.util.Map")}<$generics, R> cache = new ${im.getType("java.util.concurrent.ConcurrentHashMap")}<>();
                      return $params -> cache.computeIfAbsent($params, $mappingFunction);
                    """ else xs"""
                      final ${im.getType("java.util.Map")}<Tuple$i<$generics>, R> cache = new ${im.getType("java.util.concurrent.ConcurrentHashMap")}<>();
                      final ${checked.gen("Checked")}Function1<Tuple$i<$generics>, R> tupled = tupled();
                      return ($params) -> cache.computeIfAbsent(Tuple.of($params), $mappingFunction);
                    """
                  }
              }

              /$javadoc
               * Returns a composed function that first applies this $className to the given argument and then applies
               * {@linkplain $compositionType} {@code after} to the result.
               *
               * @param <V> return type of after
               * @param after the function applied after this
               * @return a function composed of this and after
               * @throws NullPointerException if after is null
               */
              default <V> $className<${genericsFunction}V> andThen($compositionType<? super R, ? extends V> after) {
                  ${im.getType("java.util.Objects")}.requireNonNull(after, "after is null");
                  return ($params) -> after.apply(apply($params));
              }

              ${(i == 1).gen(xs"""
                /$javadoc
                 * Returns a composed function that first applies the {@linkplain $compositionType} {@code before} the
                 * given argument and then applies this $className to the result.
                 *
                 * @param <V> argument type of before
                 * @param before the function applied before this
                 * @return a function composed of before and this
                 * @throws NullPointerException if before is null
                 */
                default <V> ${name}1<V, R> compose($compositionType<? super V, ? extends T1> before) {
                    ${im.getType("java.util.Objects")}.requireNonNull(before, "before is null");
                    return v -> apply(before.apply(v));
                }
              """)}
          }
        """
      }
    })
  }

  /**
   * Generator of javaslang.Tuple*
   */
  def genTuples(): Unit = {

    genJavaslangFile("javaslang", "Tuple")(genBaseTuple)

    (1 to N).foreach { i =>
      genJavaslangFile("javaslang", s"Tuple$i")(genTuple(i))
    }

    /*
     * Generates Tuple1..N
     */
    def genTuple(i: Int)(im: ImportManager, packageName: String, className: String): String = {
      val generics = (1 to i).gen(j => s"T$j")(", ")
      val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
      val params = (1 to i).gen(j => s"_$j")(", ")
      val paramTypes = (1 to i).gen(j => s"? super T$j")(", ")
      val resultType = if (i == 1) "? extends U1" else s"Tuple$i<${(1 to i).gen(j => s"U$j")(", ")}>"
      val resultGenerics = (1 to i).gen(j => s"U$j")(", ")
      val untyped = (1 to i).gen(j => "?")(", ")
      val functionType = s"Function$i"

      xs"""
        /**
         * A tuple of ${i.numerus("element")} which can be seen as cartesian product of ${i.numerus("component")}.
         ${(0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> type of the ${j.ordinal} element")("\n")}
         * @since 1.1.0
         */
        public final class $className<$generics> implements Tuple, ${im.getType("java.io.Serializable")} {

            private static final long serialVersionUID = 1L;

            ${(1 to i).gen(j => xs"""
              /$javadoc
               * The ${j.ordinal} element of this tuple.
               */
              public final T$j _$j;
            """)("\n\n")}

            /$javadoc
             * Constructs a tuple of ${i.numerus("element")}.
             ${(0 to i).gen(j => if (j == 0) "*" else s"* @param t$j the ${j.ordinal} element")("\n")}
             */
            public $className($paramsDecl) {
                ${(1 to i).gen(j => s"this._$j = t$j;")("\n")}
            }

            @Override
            public int arity() {
                return $i;
            }

            public <$resultGenerics> $className<$resultGenerics> map($functionType<$paramTypes, $resultType> f) {
                ${if (i > 1) { xs"""
                  return f.apply($params);"""
                } else { xs"""
                  return new $className<>(f.apply($params));"""
                }}
            }

            ${(i > 1).gen(xs"""
              public <$resultGenerics> $className<$resultGenerics> map(${(1 to i).gen(j => s"${im.getType("javaslang.Function1")}<? super T$j, ? extends U$j> f$j")(", ")}) {
                  return map((${(1 to i).gen(j => s"t$j")(", ")}) -> ${im.getType("javaslang.Tuple")}.of(${(1 to i).gen(j => s"f$j.apply(t$j)")(", ")}));
              }
            """)}

            @Override
            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                } else if (!(o instanceof $className)) {
                    return false;
                } else {
                    final $className<$untyped> that = ($className<$untyped>) o;
                    return ${(1 to i).gen(j => s"${im.getType("java.util.Objects")}.equals(this._$j, that._$j)")("\n                             && ")};
                }
            }

            ${(i == 1).gen("// if _1 == null, hashCode() returns Objects.hash(new T1[] { null }) = 31 instead of 0 = Objects.hash(null)")}
            @Override
            public int hashCode() {
                return ${im.getType("java.util.Objects")}.hash(${(1 to i).gen(j => s"_$j")(", ")});
            }

            @Override
            public String toString() {
                return String.format("(${(1 to i).gen(_ => s"%s")(", ")})", ${(1 to i).gen(j => s"_$j")(", ")});
            }
        }
      """
    }

    /*
     * Generates Tuple
     */
    def genBaseTuple(im: ImportManager, packageName: String, className: String): String = {

      def genFactoryMethod(i: Int) = {
        val generics = (1 to i).gen(j => s"T$j")(", ")
        val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
        val params = (1 to i).gen(j => s"t$j")(", ")
        xs"""
          /$javadoc
           * Creates a tuple of ${i.numerus("element")}.
           ${(0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> type of the ${j.ordinal} element")("\n")}
           ${(1 to i).gen(j => s"* @param t$j the ${j.ordinal} element")("\n")}
           * @return a tuple of ${i.numerus("element")}.
           */
          static <$generics> Tuple$i<$generics> of($paramsDecl) {
              return new Tuple$i<>($params);
          }
        """
      }

      xs"""
        /$javadoc
         * The base interface of all tuples.
         * @since 1.1.0
         */
        public interface $className {

            /**
             * Returns the number of elements of this tuple.
             *
             * @return the number of elements.
             */
            int arity();

            // -- factory methods

            /$javadoc
             * Creates the empty tuple.
             *
             * @return the empty tuple.
             */
            static Tuple0 empty() {
                return Tuple0.instance();
            }

            ${(1 to N).gen(genFactoryMethod)("\n\n")}
        }
      """
    }
  }
}

/**
 * Generate Javaslang src-gen/test/java classes
 */
def generateTestClasses(): Unit = {

  genFunctionTests()
  genPropertyCheckTests()
  genTupleTests()

  /**
   * Generator of Function tests
   */
  def genFunctionTests(): Unit = {

    (0 to N).foreach(i => {

      genJavaslangFile("javaslang", s"CheckedFunction${i}Test", baseDir = TARGET_TEST)(genFunctionTest("CheckedFunction", checked = true))
      genJavaslangFile("javaslang", s"Function${i}Test", baseDir = TARGET_TEST)(genFunctionTest("Function", checked = false))

      def genFunctionTest(name: String, checked: Boolean)(im: ImportManager, packageName: String, className: String): String = {

        val functionArgsDecl = (1 to i).gen(j => s"Object o$j")(", ")
        val functionArgs = (1 to i).gen(j => s"o$j")(", ")
        val generics = (1 to i + 1).gen(j => "Object")(", ")

        val test = im.getType("org.junit.Test")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")

        def curriedType(max: Int, function: String): String = {
          if (max == 0) {
            s"${function}0<Object>"
          } else {
            def returnType(curr: Int, max: Int): String = {
              val isParam = curr < max
              val next = if (isParam) returnType(curr + 1, max) else "Object"
              s"${function}1<Object, $next>"
            }
            returnType(1, max)
          }
        }

        xs"""
          public class $className {

              @$test
              public void shouldLift() {
                  class Type {
                      Object methodReference($functionArgsDecl) {
                          return null;
                      }
                  }
                  final Type type = new Type();
                  assertThat($name$i.lift(type::methodReference)).isNotNull();
              }

              ${(1 to i - 1).gen(j => {
                val partialArgs = (1 to j).gen(k => "null")(", ")
                xs"""
                  @$test
                  public void shouldPartiallyApplyWith${j}Arguments()${checked.gen(" throws Throwable")} {
                      final $name$i<$generics> f = ($functionArgs) -> null;
                      $assertThat(f.apply($partialArgs)).isNotNull();
                  }
                """
              })("\n\n")}

              @$test
              public void shouldGetArity() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  $assertThat(f.arity()).isEqualTo($i);
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
                  final ${name}1<Tuple$i${(i > 0).gen(s"<${(1 to i).gen(j => "Object")(", ")}>")}, Object> tupled = f.tupled();
                  $assertThat(tupled).isNotNull();
              }

              @$test
              public void shouldReverse() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  $assertThat(f.reversed()).isNotNull();
              }

              @$test
              public void shouldComposeWithAndThen() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  final ${name}1<Object, Object> after = o -> null;
                  final $name$i<$generics> composed = f.andThen(after);
                  $assertThat(composed).isNotNull();
              }

              ${(i == 1).gen(xs"""
                @$test
                public void shouldComposeWithCompose() {
                    final $name$i<$generics> f = ($functionArgs) -> null;
                    final ${name}1<Object, Object> before = o -> null;
                    final $name$i<$generics> composed = f.compose(before);
                    $assertThat(composed).isNotNull();
                }
              """)}
          }
        """
      }
    })
  }

  /**
    * Generator of Property-check tests
   */
  def genPropertyCheckTests(): Unit = {
    genJavaslangFile("javaslang.test", "PropertyTest", baseDir = TARGET_TEST)((im: ImportManager, packageName, className) => {

      // main classes
      val list = im.getType("javaslang.collection.List")
      val predicate = im.getType("javaslang.CheckedFunction1")
      val random = im.getType("java.util.Random")
      val tuple = im.getType("javaslang.Tuple")

      // test classes
      val test = im.getType("org.junit.Test")
      val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")

      xs"""
        public class $className {

            static <T> $predicate<T, Boolean> tautology() {
                return any -> true;
            }

            static <T> $predicate<T, Boolean> falsum() {
                return any -> false;
            }

            static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

            @$test(expected = NullPointerException.class)
            public void shouldThrowWhenPropertyNameIsNull() {
                new Property(null);
            }

            @$test(expected = IllegalArgumentException.class)
            public void shouldThrowWhenPropertyNameIsEmpty() {
                new Property("");
            }

            // -- Property.check methods

            @$test
            public void shouldCheckUsingDefaultConfiguration() {
                final CheckResult result = new Property("test").forAll(OBJECTS).suchThat(tautology()).check();
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
            }

            @$test
            public void shouldCheckGivenSizeAndTries() {
                final CheckResult result = new Property("test").forAll(OBJECTS).suchThat(tautology()).check(0, 0);
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isTrue();
            }

            @$test(expected = IllegalArgumentException.class)
            public void shouldThrowOnCheckGivenNegativeTries() {
                new Property("test").forAll(OBJECTS).suchThat(tautology()).check(0, -1);
            }

            @$test
            public void shouldCheckGivenRandomAndSizeAndTries() {
                final CheckResult result = new Property("test").forAll(OBJECTS).suchThat(tautology()).check(new $random(), 0, 0);
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isTrue();
            }

            // -- satisfaction

            @$test
            public void shouldCheckPythagoras() {

                final Arbitrary<Double> real = n -> Gen.choose(0, (double) n).filter(d -> d > .0d);

                // (∀a,b ∈ ℝ+ ∃c ∈ ℝ+ : a²+b²=c²) ≡ (∀a,b ∈ ℝ+ : √(a²+b²) ∈ ℝ+)
                final Checkable property = new Property("test").forAll(real, real).suchThat((a, b) -> Math.sqrt(a * a + b * b) > .0d);
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
                final CheckResult result = new Property("test")
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
                final CheckResult result = new Property("test").forAll(OBJECTS).suchThat(falsum()).implies(tautology()).check();
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isTrue();
            }

            // -- falsification

            @$test
            public void shouldFalsifyFalseProperty() {
                final Arbitrary<Integer> ones = n -> random -> 1;
                final CheckResult result = new Property("test").forAll(ones).suchThat(one -> one == 2).check();
                $assertThat(result.isFalsified()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(1);
            }

            // -- error detection

            @$test
            public void shouldRecognizeArbitraryError() {
                final Arbitrary<?> arbitrary = n -> { throw new RuntimeException("woops"); };
                final CheckResult result = new Property("test").forAll(arbitrary).suchThat(tautology()).check();
                $assertThat(result.isErroneous()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(0);
                $assertThat(result.sample().isEmpty()).isTrue();
            }

            @$test
            public void shouldRecognizeGenError() {
                final Arbitrary<?> arbitrary = Gen.fail("woops").arbitrary();
                final CheckResult result = new Property("test").forAll(arbitrary).suchThat(tautology()).check();
                $assertThat(result.isErroneous()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(1);
                $assertThat(result.sample().isEmpty()).isTrue();
            }

            @$test
            public void shouldRecognizePropertyError() {
                final Arbitrary<Integer> a1 = n -> random -> 1;
                final Arbitrary<Integer> a2 = n -> random -> 2;
                final CheckResult result = new Property("test").forAll(a1, a2).suchThat((a, b) -> {
                    throw new RuntimeException("woops");
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
                final Checkable p1 = new Property("test").forAll(OBJECTS).suchThat(tautology());
                final Checkable p2 = new Property("test").forAll(OBJECTS).suchThat(tautology());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
                final Checkable p1 = new Property("test").forAll(OBJECTS).suchThat(tautology());
                final Checkable p2 = new Property("test").forAll(OBJECTS).suchThat(falsum());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
                final Checkable p1 = new Property("test").forAll(OBJECTS).suchThat(falsum());
                final Checkable p2 = new Property("test").forAll(OBJECTS).suchThat(tautology());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
                final Checkable p1 = new Property("test").forAll(OBJECTS).suchThat(falsum());
                final Checkable p2 = new Property("test").forAll(OBJECTS).suchThat(falsum());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }

            // -- Property.or tests

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
                final Checkable p1 = new Property("test").forAll(OBJECTS).suchThat(tautology());
                final Checkable p2 = new Property("test").forAll(OBJECTS).suchThat(tautology());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
                final Checkable p1 = new Property("test").forAll(OBJECTS).suchThat(tautology());
                final Checkable p2 = new Property("test").forAll(OBJECTS).suchThat(falsum());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
                final Checkable p1 = new Property("test").forAll(OBJECTS).suchThat(falsum());
                final Checkable p2 = new Property("test").forAll(OBJECTS).suchThat(tautology());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
                final Checkable p1 = new Property("test").forAll(OBJECTS).suchThat(falsum());
                final Checkable p2 = new Property("test").forAll(OBJECTS).suchThat(falsum());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }
        }
      """
    })

    for (i <- 1 to N) {
      genJavaslangFile("javaslang.test", s"PropertyCheck${i}Test", baseDir = TARGET_TEST)((im: ImportManager, packageName, className) => {

        val generics = (1 to i).gen(j => "Object")(", ")
        val arbitraries = (1 to i).gen(j => "OBJECTS")(", ")
        val arbitrariesMinus1 = (1 to i - 1).gen(j => "OBJECTS")(", ")
        val args = (1 to i).gen(j => s"o$j")(", ")

        // test classes
        val test = im.getType("org.junit.Test")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")

        xs"""
          public class $className {

              static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

              @$test
              public void shouldApplyForAllOfArity$i() {
                  final Property.ForAll$i<${(1 to i).gen(j => "Object")(", ")}> forAll = new Property("test").forAll(${(1 to i).gen(j => "null")(", ")});
                  $assertThat(forAll).isNotNull();
              }

              @$test
              public void shouldApplySuchThatOfArity$i() {
                  final Property.ForAll$i<$generics> forAll = new Property("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> true;
                  final Property.Property$i<$generics> suchThat = forAll.suchThat(predicate);
                  $assertThat(suchThat).isNotNull();
              }

              @$test
              public void shouldCheckTrueProperty$i() {
                  final Property.ForAll$i<$generics> forAll = new Property("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> true;
                  final CheckResult result = forAll.suchThat(predicate).check();
                  $assertThat(result.isSatisfied()).isTrue();
                  $assertThat(result.isExhausted()).isFalse();
              }

              @$test
              public void shouldCheckFalseProperty$i() {
                  final Property.ForAll$i<$generics> forAll = new Property("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> false;
                  final CheckResult result = forAll.suchThat(predicate).check();
                  $assertThat(result.isFalsified()).isTrue();
              }

              @$test
              public void shouldCheckErroneousProperty$i() {
                  final Property.ForAll$i<$generics> forAll = new Property("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> { throw new RuntimeException("woops"); };
                  final CheckResult result = forAll.suchThat(predicate).check();
                  $assertThat(result.isErroneous()).isTrue();
              }

              @$test
              public void shouldCheckProperty${i}ImplicationWithTruePrecondition() {
                  final Property.ForAll$i<$generics> forAll = new Property("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p1 = ($args) -> true;
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p2 = ($args) -> true;
                  final CheckResult result = forAll.suchThat(p1).implies(p2).check();
                  $assertThat(result.isSatisfied()).isTrue();
                  $assertThat(result.isExhausted()).isFalse();
              }

              @$test
              public void shouldCheckProperty${i}ImplicationWithFalsePrecondition() {
                  final Property.ForAll$i<$generics> forAll = new Property("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p1 = ($args) -> false;
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p2 = ($args) -> true;
                  final CheckResult result = forAll.suchThat(p1).implies(p2).check();
                  $assertThat(result.isSatisfied()).isTrue();
                  $assertThat(result.isExhausted()).isTrue();
              }

              @$test(expected = IllegalArgumentException.class)
              public void shouldThrowOnProperty${i}CheckGivenNegativeTries() {
                  new Property("test")
                      .forAll($arbitraries)
                      .suchThat(($args) -> true)
                      .check(Checkable.RNG.get(), 0, -1);
              }

              @$test
              public void shouldReturnErroneousProperty${i}CheckResultIfGenFails() {
                  final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
                  final CheckResult result = new Property("test")
                      .forAll(failingGen${(i > 1).gen(s", $arbitrariesMinus1")})
                      .suchThat(($args) -> true)
                      .check();
                  $assertThat(result.isErroneous()).isTrue();
              }

              @$test
              public void shouldReturnErroneousProperty${i}CheckResultIfArbitraryFails() {
                  final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
                  final CheckResult result = new Property("test")
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

  /**
   * Generator of Tuple tests
   */
  def genTupleTests(): Unit = {

    (1 to N).foreach(i => {

      genJavaslangFile("javaslang", s"Tuple${i}Test", baseDir = TARGET_TEST)((im: ImportManager, packageName, className) => {

        val test = im.getType("org.junit.Test")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
        val functionType = s"Function$i"
        val generics = (1 to i).gen(j => s"Object")(", ")
        val functionArgTypes = (1 to i).gen(j => s"o$j")(", ")
        val nullArgs = (1 to i).gen(j => "null")(", ")

        xs"""
          public class Tuple${i}Test {

              @$test
              public void shouldCreateTuple() {
                  final Tuple$i<$generics> tuple = createTuple();
                  $assertThat(tuple).isNotNull();
              }

              @$test
              public void shouldGetArity() {
                  final Tuple$i<$generics> tuple = createTuple();
                  $assertThat(tuple.arity()).isEqualTo($i);
              }

              @$test
              public void shouldMap() {
                  final Tuple$i<$generics> tuple = createTuple();
                  ${if (i == 1) {
                    s"final $functionType<$generics, Object> mapper = $functionArgTypes -> o1;"
                  } else {
                    s"final $functionType<$generics, Tuple$i<$generics>> mapper = ($functionArgTypes) -> tuple;"
                  }}
                  final Tuple$i<$generics> actual = tuple.map(mapper);
                  $assertThat(actual).isEqualTo(tuple);
              }

              ${(i > 1).gen(xs"""
                @$test
                public void shouldMapComponents() {
                  final Tuple$i<$generics> tuple = createTuple();
                  ${(1 to i).gen(j => xs"""final Function1<Object, Object> f$j = Function1.identity();""")("\n")}
                  final Tuple$i<$generics> actual = tuple.map(${(1 to i).gen(j => s"f$j")(", ")});
                  $assertThat(actual).isEqualTo(tuple);
                }
              """)}

              @$test
              public void shouldRecognizeEquality() {
                  final Tuple$i<$generics> tuple1 = createTuple();
                  final Tuple$i<$generics> tuple2 = createTuple();
                  $assertThat(tuple1).isEqualTo(tuple2);
              }

              @$test
              public void shouldRecognizeNonEquality() {
                  final Tuple$i<$generics> tuple1 = createTuple();
                  final Object other = new Object();
                  $assertThat(tuple1).isNotEqualTo(other);
              }

              @$test
              public void shouldComputeCorrectHashCode() {
                  final int actual = createTuple().hashCode();
                  final int expected = ${im.getType("java.util.Objects")}.hash(${if (i == 1) "new Object[] { null }" else nullArgs});
                  $assertThat(actual).isEqualTo(expected);
              }

              @$test
              public void shouldImplementToString() {
                  final String actual = createTuple().toString();
                  final String expected = "($nullArgs)";
                  $assertThat(actual).isEqualTo(expected);
              }

              private Tuple$i<$generics> createTuple() {
                  return new Tuple$i<>($nullArgs);
              }
          }
        """
      })
    })
  }
}

/**
 * Adds the Javaslang header to generated classes.
 * @param packageName Java package name
 * @param className Simple java class name
 * @param gen A generator which produces a String.
 */
def genJavaslangFile(packageName: String, className: String, baseDir: String = TARGET_MAIN)(gen: (ImportManager, String, String) => String, knownSimpleClassNames: List[String] = List()) =
  genJavaFile(baseDir, packageName, className)(xraw"""
      /*     / \____  _    ______   _____ / \____   ____  _____
       *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
       *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
       * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
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

    import java.io.File

    val dirName = packageName.replaceAll("\\.", File.separator)
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
        imports.get(fullQualifiedName).get
      } else if (knownSimpleClassNames.contains(simpleName) || imports.values.exists(simpleName.equals(_))) {
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
      if (i / 10 == 1) {
        s"${i}th"
      } else {
        i % 10 match {
          case 1 => "1st"
          case 2 => "2nd"
          case 3 => "3rd"
          case _ => s"${i}th"
        }
      }

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
   * Generates a String based on a sequence of objects. Objects are converted to Strings via toString.
   * {{{
   * // val a = "A"
   * // val b = "B"
   * // val c = "C"
   * Seq("a", "b", "c").gen(s => raw"""val $s = "${s.toUpperCase}"""")("\n")
   * }}}
   * @param seq A Seq
   */
  implicit class SeqExtensions(seq: Seq[Any]) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      seq.map(x => f.apply(x.toString)) mkString delimiter
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
