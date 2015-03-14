/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */

// temporarily needed to circumvent https://issues.scala-lang.org/browse/SI-3772 (see case class Generics)
import language.implicitConversions

import Generator._, JavaGenerator._

val N = 26
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

  genFunctions()
  genFunctors()
  genHigherKindeds()
  genMonads()
  genPropertyChecks()
  genTuples()

  /**
   * Generator of javaslang.algebra.Functor*
   */
  def genFunctors(): Unit = 1 to N foreach { i =>
    genJavaslangFile("javaslang.algebra", s"Functor$i")((im: ImportManager, packageName, className) => {
      val generics = (1 to i).gen(j => s"T$j")(", ")
      val paramTypes = (1 to i).gen(j => s"? super T$j")(", ")
      val resultType = if (i == 1) "? extends U1" else s"${im.getType(s"javaslang.Tuple$i")}<${(1 to i).gen(j => s"? extends U$j")(", ")}>"
      val resultGenerics = (1 to i).gen(j => s"U$j")(", ")
      val functionType = i match {
        case 1 => im.getType("java.util.function.Function")
        case 2 => im.getType("java.util.function.BiFunction")
        case _ => im.getType(s"javaslang.Function$i")
      }
      xs"""
        ${(i == 1).gen(xs"""
        /$javadoc
         * Defines a Functor by generalizing the map function.
         * <p>
         * All instances of the Functor interface should obey the two functor laws:
         * <ul>
         *     <li>{@code m.map(a -> a) ≡ m}</li>
         *     <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
         * </ul>
         *
         * @param <T1> Component type of this Functor.
         * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
         */
        """)}
        public interface $className<$generics> {

            <$resultGenerics> $className<$resultGenerics> map($functionType<$paramTypes, $resultType> f);
        }
    """
    })
  }

  /**
   * Generator of javaslang.algebra.HigherKinded*
   */
  def genHigherKindeds(): Unit = 1 to N foreach { i =>
    genJavaslangFile("javaslang.algebra", s"HigherKinded$i")((im: ImportManager, packageName, className) => xs"""
        ${(i == 1).gen(xs"""
        /$javadoc
         * <p>
         * A type <em>HigherKinded</em> declares a generic type constructor, which consists of an inner type (component type)
         * and an outer type (container type).
         * </p>
         * <p>
         * HigherKinded is needed to (partially) simulate Higher-Kinded/Higher-Order Types, which  are not part of the Java
         * language but needed for generic type constructors.
         * </p>
         * <p>
         * Example: {@link javaslang.algebra.Monad#flatMap(java.util.function.Function)}
         * </p>
         *
         * @param <T1> Component type of the type to be constructed.
         * @param <TYPE> Container type of the type to be constructed.
         */
        """)}
        public interface $className<${(1 to i).gen(j => s"T$j")(", ")}, TYPE extends $className<${"?, " * i}TYPE>> {

            // used for type declaration only
        }
    """)
  }

  /**
   * Generator of javaslang.algebra.Monad*
   */
  def genMonads(): Unit = 1 to N foreach { i =>
    genJavaslangFile("javaslang.algebra", s"Monad$i")((im: ImportManager, packageName, className) => {
      val generics = (1 to i).gen(j => s"T$j")(", ")
      val paramTypes = (1 to i).gen(j => s"? super T$j")(", ")
      val resultGenerics = (1 to i).gen(j => s"U$j")(", ")
      val functionType = i match {
        case 1 => im.getType("java.util.function.Function")
        case 2 => im.getType("java.util.function.BiFunction")
        case _ => im.getType(s"javaslang.Function$i")
      }
      xs"""
        ${(i == 1).gen(xs"""
        /$javadoc
         * Defines a Monad by generalizing the flatMap and unit functions.
         * <p>
         * All instances of the Monad interface should obey the three control laws:
         * <ul>
         *     <li><strong>Left identity:</strong> {@code unit(a).flatMap(f) ≡ f a}</li>
         *     <li><strong>Right identity:</strong> {@code m.flatMap(unit) ≡ m}</li>
         *     <li><strong>Associativity:</strong> {@code m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g)}</li>
         * </ul>
         * <p>
         *
         * @param <T1> Component type of this Monad$i.
         */
        """)}
        public interface $className<$generics, M extends HigherKinded$i<${"?, " * i}M>> extends Functor$i<$generics>, HigherKinded$i<$generics, M> {

            <$resultGenerics, MONAD extends HigherKinded$i<$resultGenerics, M>> $className<$resultGenerics, M> flatMap($functionType<$paramTypes, MONAD> f);
        }
    """
    })
  }

  /**
   * Generator of javaslang.test.Property
   */
  def genPropertyChecks(): Unit = {

    genJavaslangFile("javaslang.test", "Property")(genProperty)

    def genProperty(im: ImportManager, packageName: String, className: String): String = xs"""
      @FunctionalInterface
      public interface $className {

          /**
           * A thread-safe, equally distributed random number generator.
           */
          ${im.getType("java.util.function.Supplier")}<${im.getType("java.util.Random")}> RNG = ${im.getType("java.util.concurrent.ThreadLocalRandom")}::current;

          /**
           * Default size hint for generators: 100
           */
          int DEFAULT_SIZE = 100;

          /**
           * Default tries to check a property: 1000
           */
          int DEFAULT_TRIES = 1000;

          /**
           * Checks this property.
           *
           * @param randomNumberGenerator An implementation of {@link java.util.Random}.
           * @param size A (not necessarily positive) size hint.
           * @param tries A non-negative number of tries to falsify the given property.
           * @return A {@linkplain CheckResult}
           */
          CheckResult check(${im.getType("java.util.Random")} randomNumberGenerator, int size, int tries);

          /**
           * Checks this property using the default random number generator {@link #RNG}.
           *
           * @param size A (not necessarily positive) size hint.
           * @param tries A non-negative number of tries to falsify the given property.
           * @return A {@linkplain CheckResult}
           */
          default CheckResult check(int size, int tries) {
              if (tries < 0) {
                  throw new IllegalArgumentException("tries < 0");
              }
              return check(RNG.get(), size, tries);
          }

          /**
           * Checks this property using the default random number generator {@link #RNG} by calling {@link #check(int, int)},
           * where size is {@link #DEFAULT_SIZE} and tries is {@link #DEFAULT_TRIES}.
           *
           * @return A {@linkplain CheckResult}
           */
          default CheckResult check() {
              return check(RNG.get(), DEFAULT_SIZE, DEFAULT_TRIES);
          }

          default Property and(Property property) {
              return (rng, size, tries) -> {
                  final CheckResult result = check(rng, size, tries);
                  if (result.isSatisfied()) {
                      return property.check(rng, size, tries);
                  } else {
                      return result;
                  }
              };
          }

          default Property or(Property property) {
              return (rng, size, tries) -> {
                  final CheckResult result = check(rng, size, tries);
                  if (result.isSatisfied()) {
                      return result;
                  } else {
                      return property.check(rng, size, tries);
                  }
              };
          }

          ${(1 to N).gen(i => {
              val generics = (1 to i).gen(j => s"T$j")(", ")
              val parameters = (1 to i).gen(j => s"a$j")(", ")
              val parametersDecl = (1 to i).gen(j => s"Arbitrary<T$j> a$j")(", ")
              xs"""
                  static <$generics> ForAll$i<$generics> forAll($parametersDecl) {
                      return new ForAll$i<>($parameters);
                  }
              """
          })("\n\n")}

          ${(1 to N).gen(i => {
              val generics = (1 to i).gen(j => s"T$j")(", ")
              val params = (name: String) => (1 to i).gen(j => s"$name$j")(", ")
              val parametersDecl = (1 to i).gen(j => s"Arbitrary<T$j> a$j")(", ")
              xs"""
                  static class ForAll$i<$generics> {

                      ${(1 to i).gen(j => xs"""
                          private final Arbitrary<T$j> a$j;
                      """)("\n")}

                      ForAll$i($parametersDecl) {
                          ${(1 to i).gen(j => xs"""
                              this.a$j = a$j;
                          """)("\n")}
                      }

                      public Property$i<$generics> suchThat(${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate) {
                          final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Condition> proposition = (${params("t")}) -> new Condition(true, predicate.apply(${params("t")}));
                          return new Property$i<>(${params("a")}, proposition);
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
                  static class Property$i<$generics> implements Property {

                      ${(1 to i).gen(j => xs"""
                          private final Arbitrary<T$j> a$j;
                      """)("\n")}
                      final $checkedFunctionType<$generics, Condition> predicate;

                      Property$i($parametersDecl, $checkedFunctionType<$generics, Condition> predicate) {
                          ${(1 to i).gen(j => xs"""
                              this.a$j = a$j;
                          """)("\n")}
                          this.predicate = predicate;
                      }

                      public Property implies($checkedFunctionType<$generics, Boolean> postcondition) {
                          final $checkedFunctionType<$generics, Condition> implication = (${params("t")}) -> {
                              final Condition precondition = predicate.apply(${params("t")});
                              if (precondition.isFalse()) {
                                  return Condition.exFalsoQuodlibet();
                              } else {
                                  return new Condition(true, postcondition.apply(${params("t")}));
                              }
                          };
                          return new Property$i<>(${params("a")}, implication);
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
                                                  return CheckResult.falsified(i, $tupleType.of(${(1 to i).gen(j => s"val$j")(", ")}));
                                              }
                                          }
                                      } catch($failureType.NonFatal nonFatal) {
                                          return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new $someType<>($tupleType.of(${(1 to i).gen(j => s"val$j")(", ")})));
                                      }
                                  } catch($failureType.NonFatal nonFatal) {
                                      return CheckResult.erroneous(i, (Error) nonFatal.getCause(), $noneType.instance());
                                  }
                              }
                              return CheckResult.satisfied(tries, exhausted);
                          } catch($failureType.NonFatal nonFatal) {
                              return CheckResult.erroneous(0, (Error) nonFatal.getCause(), $noneType.instance());
                          }
                      }
                  }
              """
          })("\n\n")}

          static class Condition {

              final boolean precondition;
              final boolean postcondition;

              static Condition exFalsoQuodlibet() {
                  return new Condition(false, true);
              }

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
        val genericsReversed = (1 to i).reverse.gen(j => s"T$j")(", ")
        val genericsTuple = if (i > 0) s"<$generics>" else ""
        val genericsFunction = if (i > 0) s"$generics, " else ""
        val genericsReversedFunction = if (i > 0) s"$genericsReversed, " else ""
        val curried = if (i == 0) "v" else (1 to i).gen(j => s"t$j")(" -> ")
        val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
        val params = (1 to i).gen(j => s"t$j")(", ")
        val paramsReversed = (1 to i).reverse.gen(j => s"t$j")(", ")
        val tupled = (1 to i).gen(j => s"t._$j")(", ")
        val compositionType = if (checked) "CheckedFunction1" else im.getType("java.util.function.Function")

        def additionalInterfaces(arity: Int, checked: Boolean): String = (arity, checked) match {
          case (0, false) => s", ${im.getType("java.util.function.Supplier")}<R>"
          case (1, false) => s", ${im.getType("java.util.function.Function")}<$generics, R>"
          case (2, false) => s", ${im.getType("java.util.function.BiFunction")}<$generics, R>"
          case _ => ""
        }

        def curriedType(max: Int, function: String): String = {
          if (max == 0) {
            s"${function}1<Void, R>"
          } else {
            def returnType(curr: Int, max: Int): String = {
              val isParam = curr < max
              val next = if (isParam) returnType(curr + 1, max) else "R"
              s"${function}1<T$curr, $next>"
            }
            returnType(1, max)
          }
        }

        xs"""
          @FunctionalInterface
          public interface $className<${(i > 0).gen(s"$generics, ")}R> extends λ<R>${additionalInterfaces(i, checked)} {

              static final long serialVersionUID = 1L;

              ${(i == 1).gen(xs"""
              static <T> ${name}1<T, T> identity() {
                  return t -> t;
              }""")}

              ${((i == 1 || i == 2) && !checked).gen("@Override")}
              R apply($paramsDecl)${checked.gen(" throws Throwable")};

              ${(i == 0 && !checked).gen(xs"""
              @Override
              default R get() {
                  return apply();
              }""")}

              @Override
              default int arity() {
                  return $i;
              }

              @Override
              default ${curriedType(i, name)} curried() {
                  return $curried -> apply($params);
              }

              @Override
              default ${name}1<Tuple$i$genericsTuple, R> tupled() {
                  return t -> apply($tupled);
              }

              @Override
              default $className<${genericsReversedFunction}R> reversed() {
                  return ($paramsReversed) -> apply($params);
              }

              default <V> $className<${genericsFunction}V> andThen($compositionType<? super R, ? extends V> after) {
                  ${im.getType("java.util.Objects")}.requireNonNull(after);
                  return ($params) -> after.apply(apply($params));
              }

              ${(i == 1).gen(xs"""
              default <V> ${name}1<V, R> compose($compositionType<? super V, ? extends T1> before) {
                  ${im.getType("java.util.Objects")}.requireNonNull(before);
                  return v -> apply(before.apply(v));
              }""")}
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
    genJavaslangFile("javaslang", "Tuple0")(genTuple0)

    (1 to N).foreach { i =>
      genJavaslangFile("javaslang", s"Tuple$i")(genTuple(i))
    }

    /*
     * Generates Tuple0
     */
    def genTuple0(im: ImportManager, packageName: String, className: String): String = xs"""
      /**
       * Implementation of an empty tuple, a tuple containing no elements.
       */
      public final class $className implements Tuple {

          private static final long serialVersionUID = 1L;

          /**
           * The singleton instance of $className.
           */
          private static final $className INSTANCE = new $className();

          /**
           * Hidden constructor.
           */
          private $className() {
          }

          /**
           * Returns the singleton instance of $className.
           *
           * @return The singleton instance of $className.
           */
          public static $className instance() {
              return INSTANCE;
          }

          @Override
          public int arity() {
              return 0;
          }

          @Override
          public $className unapply() {
              return this;
          }

          @Override
          public boolean equals(Object o) {
              return o == this;
          }

          @Override
          public int hashCode() {
              return ${im.getType("java.util.Objects")}.hash();
          }

          @Override
          public String toString() {
              return "()";
          }

          // -- Serializable implementation

          /**
           * Instance control for object serialization.
           *
           * @return The singleton instance of $className.
           * @see java.io.Serializable
           */
          private Object readResolve() {
              return INSTANCE;
          }
      }
    """

    /*
     * Generates Tuple1..N
     */
    def genTuple(i: Int)(im: ImportManager, packageName: String, className: String): String = {
      val generics = (1 to i).gen(j => s"T$j")(", ")
      val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
      val params = (1 to i).gen(j => s"_$j")(", ")
      val paramTypes = (1 to i).gen(j => s"? super T$j")(", ")
      val resultType = if (i == 1) "? extends U1" else s"Tuple$i<${(1 to i).gen(j => s"? extends U$j")(", ")}>"
      val resultGenerics = (1 to i).gen(j => s"U$j")(", ")
      val untyped = (1 to i).gen(j => "?")(", ")
      val functionType = i match {
        case 1 => im.getType("java.util.function.Function")
        case 2 => im.getType("java.util.function.BiFunction")
        case _ => s"Function$i"
      }

      xs"""
        /**
         * Implementation of a pair, a tuple containing $i elements.
         */
        public class $className<$generics> implements Tuple, ${im.getType(s"javaslang.algebra.Monad$i")}<$generics, $className<$untyped>> {

            private static final long serialVersionUID = 1L;

            ${(1 to i).gen(j => s"public final T$j _$j;")("\n")}

            public $className($paramsDecl) {
                ${(1 to i).gen(j => s"this._$j = t$j;")("\n")}
            }

            @Override
            public int arity() {
                return $i;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <$resultGenerics, MONAD extends ${im.getType(s"javaslang.algebra.HigherKinded$i")}<$resultGenerics, $className<$untyped>>> $className<$resultGenerics> flatMap($functionType<$paramTypes, MONAD> f) {
                return ($className<$resultGenerics>) f.apply($params);
            }

            ${(i > 1).gen("""@SuppressWarnings("unchecked")""")}
            @Override
            public <$resultGenerics> $className<$resultGenerics> map($functionType<$paramTypes, $resultType> f) {
                ${if (i > 1) { xs"""
                  // normally the result of f would be mapped to the result type of map, but Tuple.map is a special case
                  return ($className<$resultGenerics>) f.apply($params);"""
                } else { xs"""
                  return new $className<>(f.apply($params));"""
                }}
            }

            @Override
            public $className<$generics> unapply() {
                return this;
            }

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
          static <$generics> Tuple$i<$generics> of($paramsDecl) {
              return new Tuple$i<>($params);
          }
        """
      }

      xs"""
        public interface $className extends ValueObject {

            static final long serialVersionUID = 1L;

            /**
             * Returns the number of elements of this tuple.
             *
             * @return The number of elements.
             */
            int arity();

            // -- factory methods

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

      genJavaslangFile("javaslang", s"CheckedFunction${i}Test", baseDir = TARGET_TEST)(genFunction("CheckedFunction", checked = true))
      genJavaslangFile("javaslang", s"Function${i}Test", baseDir = TARGET_TEST)(genFunction("Function", checked = false))

      def genFunction(name: String, checked: Boolean)(im: ImportManager, packageName: String, className: String): String = {

        val functionArgs = (1 to i).gen(j => s"o$j")(", ")
        val generics = (1 to i + 1).gen(j => "Object")(", ")

        val test = im.getType("org.junit.Test")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")

        def curriedType(max: Int, function: String): String = {
          if (max == 0) {
            s"${function}1<Void, Object>"
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
        public class PropertyTest {

            static <T> $predicate<T, Boolean> tautology() {
                return any -> true;
            }

            static <T> $predicate<T, Boolean> falsum() {
                return any -> false;
            }

            static Arbitrary<Object> objects = Gen.of(null).arbitrary();

            // -- Property.check methods

            @$test
            public void shouldCheckUsingDefaultConfiguration() {
                final CheckResult result = Property.forAll(objects).suchThat(tautology()).check();
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
            }

            @$test
            public void shouldCheckGivenSizeAndTries() {
                final CheckResult result = Property.forAll(objects).suchThat(tautology()).check(0, 0);
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isTrue();
            }

            @$test(expected = IllegalArgumentException.class)
            public void shouldThrowOnCheckGivenNegativeTries() {
                Property.forAll(objects).suchThat(tautology()).check(0, -1);
            }

            @$test
            public void shouldCheckGivenRandomAndSizeAndTries() {
                final CheckResult result = Property.forAll(objects).suchThat(tautology()).check(new $random(), 0, 0);
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isTrue();
            }

            // -- satisfaction

            @$test
            public void shouldCheckPythagoras() {

                final Arbitrary<Double> real = n -> Gen.choose(0, (double) n).filter(d -> d > .0d);

                // (∀a,b ∈ ℝ+ ∃c ∈ ℝ+ : a²+b²=c²) ≡ (∀a,b ∈ ℝ+ : √(a²+b²) ∈ ℝ+)
                final Property property = Property.forAll(real, real).suchThat((a, b) -> Math.sqrt(a * a + b * b) > .0d);
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
                final CheckResult result = Property
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
                final CheckResult result = Property.forAll(objects).suchThat(falsum()).implies(tautology()).check();
                $assertThat(result.isSatisfied()).isTrue();
                $assertThat(result.isExhausted()).isTrue();
            }

            // -- falsification

            @$test
            public void shouldFalsifyFalseProperty() {
                final Arbitrary<Integer> ones = n -> random -> 1;
                final CheckResult result = Property.forAll(ones).suchThat(one -> one == 2).check();
                $assertThat(result.isFalsified()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(1);
            }

            // -- error detection

            @$test
            public void shouldRecognizeArbitraryError() {
                final Arbitrary<?> arbitrary = n -> { throw new RuntimeException("woops"); };
                final CheckResult result = Property.forAll(arbitrary).suchThat(tautology()).check();
                $assertThat(result.isErroneous()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(0);
                $assertThat(result.sample().isEmpty()).isTrue();
            }

            @$test
            public void shouldRecognizeGenError() {
                final Arbitrary<?> arbitrary = Gen.fail("woops").arbitrary();
                final CheckResult result = Property.forAll(arbitrary).suchThat(tautology()).check();
                $assertThat(result.isErroneous()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(1);
                $assertThat(result.sample().isEmpty()).isTrue();
            }

            @$test
            public void shouldRecognizePropertyError() {
                final Arbitrary<Integer> a1 = n -> random -> 1;
                final Arbitrary<Integer> a2 = n -> random -> 2;
                final CheckResult result = Property.forAll(a1, a2).suchThat((a, b) -> {
                    throw new RuntimeException("woops");
                }).check();
                $assertThat(result.isErroneous()).isTrue();
                $assertThat(result.isExhausted()).isFalse();
                $assertThat(result.count()).isEqualTo(1);
                $assertThat(result.sample().isDefined()).isTrue();
                $assertThat(result.sample().get()).isEqualTo(Tuple.of(1, 2));
            }

            // -- Property checks

            ${(1 to N).gen(i => {

              val generics = (1 to i).gen(j => "Object")(", ")
              val arbitraries = (1 to i).gen(j => "objects")(", ")
              val arbitrariesMinus1 = (1 to i - 1).gen(j => "objects")(", ")
              val args = (1 to i).gen(j => s"o$j")(", ")

              xs"""
                @$test
                public void shouldApplyForAllOfArity$i() {
                    final Property.ForAll$i<${(1 to i).gen(j => "Object")(", ")}> forAll = Property.forAll(${(1 to i).gen(j => "null")(", ")});
                    $assertThat(forAll).isNotNull();
                }

                @$test
                public void shouldApplySuchThatOfArity$i() {
                    final Property.ForAll$i<$generics> forAll = Property.forAll($arbitraries);
                    final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> true;
                    final Property.Property$i<$generics> suchThat = forAll.suchThat(predicate);
                    $assertThat(suchThat).isNotNull();
                }

                @$test
                public void shouldCheckTrueProperty$i() {
                    final Property.ForAll$i<$generics> forAll = Property.forAll($arbitraries);
                    final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> true;
                    final CheckResult result = forAll.suchThat(predicate).check();
                    $assertThat(result.isSatisfied()).isTrue();
                    $assertThat(result.isExhausted()).isFalse();
                }

                @$test
                public void shouldCheckFalseProperty$i() {
                    final Property.ForAll$i<$generics> forAll = Property.forAll($arbitraries);
                    final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> false;
                    final CheckResult result = forAll.suchThat(predicate).check();
                    $assertThat(result.isFalsified()).isTrue();
                }

                @$test
                public void shouldCheckErroneousProperty$i() {
                    final Property.ForAll$i<$generics> forAll = Property.forAll($arbitraries);
                    final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> { throw new RuntimeException("woops"); };
                    final CheckResult result = forAll.suchThat(predicate).check();
                    $assertThat(result.isErroneous()).isTrue();
                }

                @$test
                public void shouldCheckProperty${i}ImplicationWithTruePrecondition() {
                    final Property.ForAll$i<$generics> forAll = Property.forAll($arbitraries);
                    final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p1 = ($args) -> true;
                    final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p2 = ($args) -> true;
                    final CheckResult result = forAll.suchThat(p1).implies(p2).check();
                    $assertThat(result.isSatisfied()).isTrue();
                    $assertThat(result.isExhausted()).isFalse();
                }

                @$test
                public void shouldCheckProperty${i}ImplicationWithFalsePrecondition() {
                    final Property.ForAll$i<$generics> forAll = Property.forAll($arbitraries);
                    final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p1 = ($args) -> false;
                    final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p2 = ($args) -> true;
                    final CheckResult result = forAll.suchThat(p1).implies(p2).check();
                    $assertThat(result.isSatisfied()).isTrue();
                    $assertThat(result.isExhausted()).isTrue();
                }

                @$test(expected = IllegalArgumentException.class)
                public void shouldThrowOnProperty${i}CheckGivenNegativeTries() {
                    Property
                        .forAll($arbitraries)
                        .suchThat(($args) -> true)
                        .check(Property.RNG.get(), 0, -1);
                }

                @$test
                public void shouldReturnErroneousProperty${i}CheckResultIfGenFails() {
                    final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
                    final CheckResult result = Property
                        .forAll(failingGen${(i > 1).gen(s", $arbitrariesMinus1")})
                        .suchThat(($args) -> true)
                        .check();
                    $assertThat(result.isErroneous()).isTrue();
                }

                @$test
                public void shouldReturnErroneousProperty${i}CheckResultIfArbitraryFails() {
                    final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
                    final CheckResult result = Property
                        .forAll(failingArbitrary${(i > 1).gen(s", $arbitrariesMinus1")})
                        .suchThat(($args) -> true)
                        .check();
                    $assertThat(result.isErroneous()).isTrue();
                }
              """})("\n\n")
            }

            // -- Property.and tests

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
                final Property p1 = Property.forAll(objects).suchThat(tautology());
                final Property p2 = Property.forAll(objects).suchThat(tautology());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
                final Property p1 = Property.forAll(objects).suchThat(tautology());
                final Property p2 = Property.forAll(objects).suchThat(falsum());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
                final Property p1 = Property.forAll(objects).suchThat(falsum());
                final Property p2 = Property.forAll(objects).suchThat(tautology());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }

            @$test
            public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
                final Property p1 = Property.forAll(objects).suchThat(falsum());
                final Property p2 = Property.forAll(objects).suchThat(falsum());
                final CheckResult result = p1.and(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }

            // -- Property.or tests

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
                final Property p1 = Property.forAll(objects).suchThat(tautology());
                final Property p2 = Property.forAll(objects).suchThat(tautology());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
                final Property p1 = Property.forAll(objects).suchThat(tautology());
                final Property p2 = Property.forAll(objects).suchThat(falsum());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
                final Property p1 = Property.forAll(objects).suchThat(falsum());
                final Property p2 = Property.forAll(objects).suchThat(tautology());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isTrue();
            }

            @$test
            public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
                final Property p1 = Property.forAll(objects).suchThat(falsum());
                final Property p2 = Property.forAll(objects).suchThat(falsum());
                final CheckResult result = p1.or(p2).check();
                $assertThat(result.isSatisfied()).isFalse();
            }
        }
      """
    })
  }

  /**
   * Generator of Tuple tests
   */
  def genTupleTests(): Unit = {

    (1 to N).foreach(i => {

      genJavaslangFile("javaslang", s"Tuple${i}Test", baseDir = TARGET_TEST)((im: ImportManager, packageName, className) => {

        val test = im.getType("org.junit.Test")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")

        val functionType = i match {
          case 1 => im.getType("java.util.function.Function")
          case 2 => im.getType("java.util.function.BiFunction")
          case _ => s"Function$i"
        }

        val generics = (1 to i).gen(j => s"Object")(", ")
        val genericsUnknown = (1 to i).gen(j => s"?")(", ")
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
              public void shouldFlatMap() {
                  final Tuple$i<$generics> tuple = createTuple();
                  final $functionType<$generics, Tuple$i<$generics>> mapper = ${s"($functionArgTypes)"} -> tuple;
                  final Tuple$i<$generics> actual = tuple.flatMap(mapper);
                  $assertThat(actual).isEqualTo(tuple);
              }

              @$test
              public void shouldMap() {
                  final Tuple$i<$generics> tuple = createTuple();
                  ${if (i == 1) {
                    s"final $functionType<$generics, Object> mapper = $functionArgTypes -> o1;"
                  } else {
                    s"final $functionType<$generics, Tuple$i<$genericsUnknown>> mapper = ($functionArgTypes) -> tuple;"
                  }}
                  final Tuple$i<$generics> actual = tuple.map(mapper);
                  $assertThat(actual).isEqualTo(tuple);
              }

              @$test
              public void shouldUnapply() {
                  final Tuple$i<$generics> tuple = createTuple();
                  $assertThat(tuple.unapply()).isEqualTo(tuple);
              }

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
      /**    / \____  _    ______   _____ / \____   ____  _____
       *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
       *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
       * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
       */
    """)(gen)(CHARSET)

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
     J A V A   G E N E R A T O R   F R A M E W O R K
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

object JavaGenerator {

  import Generator._
  import java.nio.charset.{Charset, StandardCharsets}

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

  implicit class StringExtensions(s: String) {

    // gets first char of s as string. throws if string is empty
    def first: String = s.substring(0, 1)

    // converts first char of s to upper case. throws if string is empty
    def firstUpper: String = s(0).toUpper + s.substring(1)

    // converts first char of s to lower case. throws if string is empty
    def firstLower: String = s(0).toLower + s.substring(1)
  }

  implicit class BooleanExtensions(condition: Boolean) {
    def gen(s: String): String =  if (condition) s else ""
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
          case lines: Array[String] if lines.length > 0 => lines reduce (_ + lineSeparator + space + _)
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
