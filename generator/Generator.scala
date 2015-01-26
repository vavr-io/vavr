/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */

import java.io.File
import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.StandardOpenOption

import GeneratorImplicits._

import scala.util.Properties.lineSeparator

val N = 26
val TARGET = "src-gen/main/java"

/**
 * ENTRY POINT
 */
def run() {

  genFunctions()
  genPropertyChecks()
  genTuples()
}

/**
 * Generator of javaslang.test.Property
 */
def genPropertyChecks(): Unit = {

  def genProperty(packageName: String, className: String): String = {
    xs"""
import javaslang.*;
import javaslang.control.*;
import javaslang.function.*;

public interface $className {

    CheckResult check(int size, int tries);

    default CheckResult check() {
        return check(100, 1000);
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

                ${(i+1 to N).gen(j => {
                    val missingGenerics = (i+1 to j).gen(k => s"T$k")(", ")
                    val allGenerics = (1 to j).gen(k => s"T$k")(", ")
                    val missingParametersDecl = (i+1 to j).gen(k => s"Arbitrary<T$k> a$k")(", ")
                    val allParameters = (1 to j).gen(k => s"a$k")(", ")
                    xs"""
                        public <$missingGenerics> ForAll$j<$allGenerics> forAll($missingParametersDecl) {
                            return new ForAll$j<>($allParameters);
                        }
                    """
                })("\n\n")}

                public Property suchThat(CheckedLambda$i<$generics, Boolean> predicate) {
                    return new SuchThat$i<>(${(1 to i).gen(j => s"a$j")(", ")}, predicate);
                }
            }
        """
    })("\n\n")}

    ${(1 to N).gen(i => {
        val generics = (1 to i).gen(j => s"T$j")(", ")
        val parametersDecl = (1 to i).gen(j => s"Arbitrary<T$j> a$j")(", ")
        xs"""
            static class SuchThat$i<$generics> implements Property {

                ${(1 to i).gen(j => xs"""
                    private final Arbitrary<T$j> a$j;
                """)("\n")}
                final CheckedLambda$i<$generics, Boolean> predicate;

                SuchThat$i($parametersDecl, CheckedLambda$i<$generics, Boolean> predicate) {
                    ${(1 to i).gen(j => xs"""
                        this.a$j = a$j;
                    """)("\n")}
                    this.predicate = predicate;
                }

                @Override
                public CheckResult<Tuple$i<$generics>> check(int size, int tries) {
                    return null; // TODO
                    /*
                    final Try<CheckResult<Tuple$i<$generics>>> overallCheckResult =
                        ${(1 to i).gen(j => {
                            val mapper = if (i == j) "map" else "flatMap"
                            xs"""Try.of(() -> a$j.apply(size)).recover(x -> { throw Errors.arbitraryError($j, size, x); }).$mapper((Gen<T$j> gen$j) ->"""
                        })("\n")} {
                            for (int i = 1; i < tries; i++) {
                                final int count = i;
                                final Try<CheckResult<Tuple$i<$generics>>> partialCheckResult =
                                    ${(1 to i).gen(j => {
                                        val mapper = if (i == j) "map" else "flatMap"
                                        xs"""Try.of(() -> gen$j.get()).recover(x -> { throw Errors.genError($j, size, x); }).$mapper((T$j val$j) ->"""
                                    })("\n")} {
                                        try {
                                            final boolean test = predicate.apply(${(1 to i).gen(j => s"val$j")(", ")});
                                            if (test) {
                                                return CheckResult.satisfied(count);
                                            } else {
                                                return CheckResult.falsified(count, Tuple.of(${(1 to i).gen(j => s"val$j")(", ")}));
                                            }
                                        } catch (Throwable x) {
                                            return CheckResult.erroneous(count, Errors.predicateError(x));
                                        }
                                    }${(1 to i).gen(j => ")")};
                                if (!partialCheckResult.get().isSatisfied()) {
                                    return partialCheckResult.get();
                                }
                            }
                            return CheckResult.satisfied(size);
                        }${(1 to i).gen(j => ")")};
                    return overallCheckResult.recover(x -> CheckResult.<Tuple$i<$generics>>erroneous(0, (Error) x)).get();
                    */
                }
            }
        """
    })("\n\n")}
}
  """
  }

  genJavaslangFile("javaslang.test", "Property")(genProperty)
}

/**
 * Generator of javaslang.function.*
 */
def genFunctions(): Unit = {

  def genFunctions(i: Int): Unit = {

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

    def additionalInterfaces(arity: Int, checked: Boolean): String = (arity, checked) match {
      case (0, false) => s", java.util.function.Supplier<R>"
      case (1, false) => s", java.util.function.Function<$generics, R>"
      case (2, false) => s", java.util.function.BiFunction<$generics, R>"
      case _ => ""
    }

    def returnType(max: Int, function: String): String = {
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

    def genFunction(name: String, checked: Boolean)(packageName: String, className: String): String = xs"""
    import javaslang.Tuple$i;

    import java.util.Objects;
    import java.util.function.Function;

    @FunctionalInterface
    public interface $className<${if (i > 0) s"$generics, " else ""}R> extends Lambda<R>${additionalInterfaces(i, checked)} {

        ${if (i == 1) xs"""
        static <T> ${name}1<T, T> identity() {
            return t -> t;
        }""" else ""}

        ${if ((i == 1 || i == 2) && !checked) "@Override" else ""}
        R apply($paramsDecl)${if (checked) " throws Throwable" else ""};

        ${if (i == 0 && !checked) xs"""
        @Override
        default R get() {
            return apply();
        }""" else ""}

        @Override
        default int arity() {
            return $i;
        }

        @Override
        default ${returnType(i, name)} curried() {
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

        @Override
        default <V> $className<${genericsFunction}V> andThen(Function<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return ($params) -> after.apply(apply($params));
        }

        ${if (i == 1) xs"""
        default <V> ${name}1<V, R> compose(Function<? super V, ? extends T1> before) {
            Objects.requireNonNull(before);
            return v -> apply(before.apply(v));
        }""" else ""}
    }
    """

    genJavaslangFile("javaslang.function", s"χ$i")(genFunction("χ", checked = true))
    genJavaslangFile("javaslang.function", s"CheckedLambda$i")(genFunction("CheckedLambda", checked = true))
    genJavaslangFile("javaslang.function", s"λ$i")(genFunction("λ", checked = false))
    genJavaslangFile("javaslang.function", s"Lambda$i")(genFunction("Lambda", checked = false))
  }

  (0 to N).foreach(genFunctions)
  ("A", "B", "C").gen(s => s)("")
}

/**
 * Generator of javaslang.Tuple*
 */
def genTuples(): Unit = {

  /*
   * Generates Tuple0
   */
  def genTuple0(packageName: String, className: String): String = xs"""
    import java.util.Objects;

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
            return Objects.hash();
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
  def genTuple(i: Int)(packageName: String, className: String): String = {
    val generics = (1 to i).gen(j => s"T$j")(", ")
    val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
    xs"""
    import java.util.Objects;

    /**
     * Implementation of a pair, a tuple containing $i elements.
     */
    public class $className<$generics> implements Tuple {

        private static final long serialVersionUID = 1L;

        ${(1 to i).gen(j => s"public final T$j _$j;")("\n")}

        public $className($paramsDecl) {
            ${(1 to i).gen(j => s"this._$j = t$j;")("\n")}
        }

        @Override
        public int arity() {
            return $i;
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
                final $className that = ($className) o;
                return ${(1 to i).gen(j => s"Objects.equals(this._$j, that._$j)")("\n                         && ")};
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(${(1 to i).gen(j => s"_$j")(", ")});
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
  def genBaseTuple(packageName: String, className: String): String = {

    def genFactoryMethod(i: Int) = {
      val generics = (1 to i).gen(j => s"T$j")(", ")
      val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
      val params = (1 to i).gen(j => s"t$j")(", ")
      xs"""
      static <$generics> Tuple$i<$generics> of($paramsDecl) {
          return new Tuple$i<>($params);
      }"""
    }

    xs"""
    public interface $className extends ValueObject {

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
    }"""
  }

  genJavaslangFile("javaslang", "Tuple")(genBaseTuple)

  genJavaslangFile("javaslang", "Tuple0")(genTuple0)

  (1 to N).foreach { i =>
    genJavaslangFile("javaslang", s"Tuple$i")(genTuple(i))
  }
}

/**
 * Adds the Javaslang header to generated classes.
 * @param packageName Java package name
 * @param className Simple java class name
 * @param gen A generator which produces a String.
 */
def genJavaslangFile(packageName: String, className: String)(gen: (String, String) => String) =
  genJavaFile(packageName, className)(xraw"""
  /**    / \____  _    ______   _____ / \____   ____  _____
   *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
   *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
   * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
   */
  """)(gen)//(StandardCharsets.UTF_8)

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
     J A V A   G E N E R A T O R   F R A M E W O R K
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

/**
 * Generates a Java file.
 * @param packageName Java package name
 * @param className Simple java class name
 * @param classHeader A class file header
 * @param gen A generator which produces a String.
 */
def genJavaFile(packageName: String, className: String)(classHeader: String)(gen: (String, String) => String)(implicit charset: Charset = StandardCharsets.UTF_8): Unit = {

  val dirName = packageName.replaceAll("\\.", File.separator)
  val fileName = className + ".java"

  genFile(dirName, fileName)(xraw"""
    $classHeader
    package $packageName;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
       G E N E R A T O R   C R A F T E D
    \*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

    ${gen.apply(packageName, className)}
  """) // TODO: pass a mutable ImportManager to gen
}

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
     C O R E   G E N E R A T O R   F R A M E W O R K
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

def genFile(dirName: String, fileName: String)(contents: => String)(implicit charset: Charset = StandardCharsets.UTF_8): Unit = {

  println(s"Generating $dirName${File.separator}$fileName")

  import java.nio.file.{Paths, Files}

  Files.write(
    Files.createDirectories(Paths.get(TARGET, dirName)).resolve(fileName),
    contents.getBytes(charset),
    StandardOpenOption.CREATE, StandardOpenOption.WRITE)
}

/**
 * Core generator API
 */
object GeneratorImplicits {

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
