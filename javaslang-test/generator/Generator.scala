/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */

// temporarily needed to circumvent https://issues.scala-lang.org/browse/SI-3772 (see case class Generics)
import Generator._
import JavaGenerator._

import scala.language.implicitConversions

val N = 8
val TARGET_MAIN = "javaslang-test/src-gen/main/java"
val TARGET_TEST = "javaslang-test/src-gen/test/java"
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

  genPropertyChecks()
  genArbitraryTuple()
  genShrinkTuple()

  /**
   * Generator of javaslang.test.Property
   */
  def genPropertyChecks(): Unit = {

    genJavaslangFile("javaslang.test", "Property")(genProperty)

    def genProperty(im: ImportManager, packageName: String, className: String): String = xs"""
      /**
       * A property builder which provides a fluent API to build checkable properties.
       *
       * @author Daniel Dietrich
       * @since 1.2.0
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
                      return new ForAll$i<>(name, ArbitraryTuple.of($parameters));
                  }
              """
          })("\n\n")}

          ${(1 to N).gen(i => {
              val generics = (1 to i).gen(j => s"T$j")(", ")
              val tupleGeneric = s"Tuple$i<$generics>"
              xs"""
                  /$javadoc
                   * Represents a logical for all quantor.
                   *
                   ${(1 to i).gen(j => s"* @param <T$j> ${j.ordinal} variable type of this for all quantor")("\n")}
                   * @author Daniel Dietrich
                   * @since 1.2.0
                   */
                  public static class ForAll$i<$generics> {

                      private final String name;
                      private final Arbitrary<$tupleGeneric> arbitrary;

                      ForAll$i(String name, Arbitrary<$tupleGeneric> arbitrary) {
                          this.name = name;
                          this.arbitrary = arbitrary;
                      }

                      /$javadoc
                       * Returns a checkable property that checks values of the $i variables of this {@code ForAll} quantor.
                       *
                       * @param predicate A $i-ary predicate
                       * @return a new {@code Property$i} of $i variables.
                       */
                      public Property$i<$generics> suchThat(${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate) {
                          final ${im.getType(s"javaslang.CheckedFunction1")}<$tupleGeneric, Condition> proposition = (t) -> new Condition(true, predicate.tupled().apply(t));
                          return new Property$i<>(name, proposition, arbitrary, Shrink.empty());
                      }
                  }
              """
          })("\n\n")}

          ${(1 to N).gen(i => {

              val checkedFunctionType = im.getType(s"javaslang.CheckedFunction$i")
              val generics = (1 to i).gen(j => s"T$j")(", ")
              val tupleGeneric = s"Tuple$i<$generics>"

              val params = (paramName: String) => (1 to i).gen(j => s"$paramName$j")(", ")
              val parametersShrinks = (1 to i).gen(j => s"Shrink<T$j> s$j")(", ")

              xs"""
                  /$javadoc
                   * Represents a $i-ary checkable property.
                   *
                   * @author Daniel Dietrich
                   * @since 1.2.0
                   */
                  public static class Property$i<$generics> extends PropertyCheck<$tupleGeneric> {

                      Property$i(String name, CheckedFunction1<$tupleGeneric, Condition> predicate, Arbitrary<$tupleGeneric> arbitrary, Shrink<$tupleGeneric> shrink) {
                          super(name, arbitrary, predicate, shrink);
                      }

                      /$javadoc
                       * Returns an implication which composes this Property as pre-condition and a given post-condition.
                       *
                       * @param postcondition The postcondition of this implication
                       * @return A new Checkable implication
                       */
                      public Checkable<$tupleGeneric> implies($checkedFunctionType<$generics, Boolean> postcondition) {
                          final CheckedFunction1<$tupleGeneric, Condition> implication = (t) -> {
                              final Condition precondition = predicate.apply(t);
                              if (precondition.isFalse()) {
                                  return Condition.EX_FALSO_QUODLIBET;
                              } else {
                                  return new Condition(true, postcondition.tupled().apply(t));
                              }
                          };
                          return new Property$i<>(name, implication, arbitrary, shrink);
                      }

                      public Property$i<$generics> shrinking($parametersShrinks) {
                          return new Property$i<>(name, predicate, arbitrary, ShrinkTuple.of(${params("s")}));
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

  def genArbitraryTuple(): Unit = {
    genJavaslangFile("javaslang.test", "ArbitraryTuple")(genArbitrary)
    def genArbitrary(im: ImportManager, packageName: String, className: String): String = xs"""
    public class ArbitraryTuple {
        ${(1 to N).gen(i => {
          val generics = (1 to i).gen(j => s"T$j")(", ")
          val parametersDecl = (1 to i).gen(j => s"Arbitrary<T$j> a$j")(", ")
          xs"""
              /$javadoc
               * Generates an arbitrary tuple of $i given variables
               *
               ${(1 to i).gen(j => s"* @param a$j ${j.ordinal} variable of this tuple")("\n")}
               ${(1 to i).gen(j => s"* @param <T$j> ${j.ordinal} variable type of this tuple")("\n")}
               * @return A new generator
               */
              public static <$generics> Arbitrary<${im.getType(s"javaslang.Tuple$i")}<$generics>> of($parametersDecl) {
                  return size -> random -> Tuple.of(
                          ${(1 to i).gen(j => s"a$j.apply(size).apply(random)")(",\n")});
              }
          """
        })("\n\n")}
    }
    """
  }

  def genShrinkTuple(): Unit = {
    genJavaslangFile("javaslang.test", "ShrinkTuple")(genShrink)
    def genShrink(im: ImportManager, packageName: String, className: String): String = xs"""
    public class ShrinkTuple {
        ${(1 to N).gen(i => {
            val generics = (1 to i).gen(j => s"T$j")(", ")
            val parametersT = (1 to i).gen(j => s"t$j")(", ")
            val parametersDecl = (1 to i).gen(j => s"Shrink<T$j> s$j")(", ")
            val a: Int => String = k => (1 to i).gen(j => if (j == k) "a" else s"t$j")(", ")
            xs"""
                /$javadoc
                 * Generates a shrink for tuple of $i given shrinks
                 *
                 ${(1 to i).gen(j => s"* @param s$j ${j.ordinal} shrink of this tuple")("\n")}
                 ${(1 to i).gen(j => s"* @param <T$j> ${j.ordinal} type of this tuple")("\n")}
                 * @return A new generator
                 */
                public static <$generics> Shrink<${im.getType(s"javaslang.Tuple$i")}<$generics>> of($parametersDecl) {
                    return t -> t.apply(($parametersT) -> concat(${im.getType(s"javaslang.collection.Stream")}.of(
                            ${(1 to i).gen(j => s"s$j.apply(t$j).map(a -> Tuple.of(${a(j)}))")(",\n")})));
                }
            """
        })("\n\n")}

        private static <T> Stream<T> concat(Stream<Stream<T>> streams) {
            return streams.foldLeft(Stream.empty(), Stream::appendAll);
        }
    }
    """
  }
}

/**
 * Generate Javaslang src-gen/test/java classes
 */
def generateTestClasses(): Unit = {

  genPropertyCheckTests()
  genShrinkArbitraryTupleTest()

  /**
    * Generator of Property-check tests
   */
  def genPropertyCheckTests(): Unit = {
    for (i <- 1 to N) {
      genJavaslangFile("javaslang.test", s"PropertyCheck${i}Test", baseDir = TARGET_TEST)((im: ImportManager, packageName, className) => {

        val generics = (1 to i).gen(j => "Object")(", ")
        val arbitraries = (1 to i).gen(j => "OBJECTS")(", ")
        val arbitrariesMinus1 = (1 until i).gen(j => "OBJECTS")(", ")
        val args = (1 to i).gen(j => s"o$j")(", ")

        // test classes
        val test = im.getType("org.junit.Test")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
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
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> true;
                  final Property.Property$i<$generics> suchThat = forAll.suchThat(predicate);
                  $assertThat(suchThat).isNotNull();
              }

              @$test
              public void shouldCheckTrueProperty$i() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> true;
                  final CheckResult<?> result = forAll.suchThat(predicate).check();
                  $assertThat(result.isSatisfied()).isTrue();
                  $assertThat(result.isExhausted()).isFalse();
              }

              @$test
              public void shouldCheckFalseProperty$i() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> false;
                  final CheckResult<?> result = forAll.suchThat(predicate).check();
                  $assertThat(result.isFalsified()).isTrue();
              }

              @$test
              public void shouldCheckErroneousProperty$i() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> predicate = ($args) -> { throw new RuntimeException("$woops"); };
                  final CheckResult<?> result = forAll.suchThat(predicate).check();
                  $assertThat(result.isErroneous()).isTrue();
              }

              @$test
              public void shouldCheckProperty${i}ImplicationWithTruePrecondition() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p1 = ($args) -> true;
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p2 = ($args) -> true;
                  final CheckResult<?> result = forAll.suchThat(p1).implies(p2).check();
                  $assertThat(result.isSatisfied()).isTrue();
                  $assertThat(result.isExhausted()).isFalse();
              }

              @$test
              public void shouldCheckProperty${i}ImplicationWithFalsePrecondition() {
                  final Property.ForAll$i<$generics> forAll = Property.def("test").forAll($arbitraries);
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p1 = ($args) -> false;
                  final ${im.getType(s"javaslang.CheckedFunction$i")}<$generics, Boolean> p2 = ($args) -> true;
                  final CheckResult<?> result = forAll.suchThat(p1).implies(p2).check();
                  $assertThat(result.isSatisfied()).isTrue();
                  $assertThat(result.isExhausted()).isTrue();
              }

              @$test(expected = IllegalArgumentException.class)
              public void shouldThrowOnProperty${i}CheckGivenNegativeTries() {
                  Property.def("test")
                      .forAll($arbitraries)
                      .suchThat(($args) -> true)
                      .check(Checkable.RNG.get(), 0, -1);
              }

              @$test
              public void shouldReturnErroneousProperty${i}CheckResultIfGenFails() {
                  final Arbitrary<Object> failingGen = Gen.fail("$woops").arbitrary();
                  final CheckResult<?> result = Property.def("test")
                      .forAll(failingGen${(i > 1).gen(s", $arbitrariesMinus1")})
                      .suchThat(($args) -> true)
                      .check();
                  $assertThat(result.isErroneous()).isTrue();
              }

              @$test
              public void shouldReturnErroneousProperty${i}CheckResultIfArbitraryFails() {
                  final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("$woops"); };
                  final CheckResult<?> result = Property.def("test")
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

  def genShrinkArbitraryTupleTest(): Unit = {
    genJavaslangFile("javaslang.test", "ShrinkArbitraryTupleTest", baseDir = TARGET_TEST)(genShrinkTest)
    def genShrinkTest(im: ImportManager, packageName: String, className: String): String = xs"""
    public class ShrinkArbitraryTupleTest {
        ${(1 to N).gen(i => {
        val generics = (1 to i).gen(j => s"Integer")(", ")
        val tuple = im.getType(s"javaslang.Tuple$i<$generics>")
        val test = im.getType("org.junit.Test")
        xs"""
        @$test
        public void shouldShrinkArbitraryTuple$i() throws Exception {
            final Shrink<$tuple> shrink = ShrinkTuple.of(
                            ${(1 to i).gen(j => s"Shrink.integer()")(",\n")});

            final Arbitrary<$tuple> tuples = ArbitraryTuple.of(
                            ${(1 to i).gen(j => s"Arbitrary.integer().filter(i -> i > 0)")(",\n")});

            Property.def("tuple$i: Not contains initial value")
                    .forAll(tuples.map(tuple -> Tuple.of(tuple, shrink.apply(tuple))))
                    .suchThat(pair((tuple, shrinks) -> !shrinks.contains(tuple)))
                    .check()
                    .assertIsSatisfied();

            Property.def("tuple$i: Shrinks each component")
                    .forAll(tuples.map(shrink::apply))
                    .suchThat(shrinks ->
                            ${(1 to i).gen(j => s"shrinks.exists(t -> t._$j == 0)")(" &&\n")})
                    .check()
                    .assertIsSatisfied();
        }"""})("\n\n")}

        private static <L, R> CheckedFunction1<Tuple2<L, R>, Boolean> pair(${im.getType("java.util.function.BiPredicate")}<L, R> predicate) {
            return t -> predicate.test(t._1, t._2);
        }
    }
    """
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
      /*     / \____  _    _  ____   ______  / \ ____  __    _______
       *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
       *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
       * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
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
