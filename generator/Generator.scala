/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */

import java.nio.charset.{Charset, StandardCharsets}
import java.nio.file.StandardOpenOption

import StringContextImplicits._

import scala.util.Properties.lineSeparator

val N = 13
val TARGET = "target/generated-sources"

// entry point
def run() {

  genJavaFile("javaslang", "Tuple.java")(genTuple)

}

def genTuple(): String = {

  def genFactoryMethod(i: Int) = {
    val generics = gen(1 to i)(j => s"T$j")(", ")
    val paramsDecl = gen(1 to i)(j => s"T$j t$j")(", ")
    val params = gen(1 to i)(j => s"t$j")(", ")
    xs"""
    static <$generics> Tuple$i<$generics> of($paramsDecl) {
        return new Tuple$i<>($params);
    }
    """
  }

  def genInnerTupleClass(i: Int) = {
    val generics = gen(1 to i)(j => s"T$j")(", ")
    val paramsDecl = gen(1 to i)(j => s"T$j t$j")(", ")
    xs"""
    /**
     * Implementation of a pair, a tuple containing $i elements.
     */
    static class Tuple$i<$generics> implements Tuple {

        private static final long serialVersionUID = 1L;

        ${gen(1 to i)(j => s"public final T$j _$j;")("\n")}

        public Tuple$i($paramsDecl) {
            ${gen(1 to i)(j => s"this._$j = t$j;")("\n")}
        }

        @Override
        public int arity() {
            return $i;
        }

        @Override
        public Tuple$i<$generics> unapply() {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Tuple$i)) {
                return false;
            } else {
                final Tuple$i that = (Tuple$i) o;
                return ${gen(1 to i)(j => s"Objects.equals(this._$j, that._$j)")("\n                         && ")};
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(${gen(1 to i)(j => s"_$j")(", ")});
        }

        @Override
        public String toString() {
            return String.format("(${gen(1 to i)(_ => s"%s")(", ")})", ${gen(1 to i)(j => s"_$j")(", ")});
        }
    }
    """
  }

  xs"""
  package javaslang;

  import java.util.Objects;

  public interface Tuple extends ValueObject {

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

      ${gen(1 to N)(genFactoryMethod)("\n\n")}

      /**
       * Implementation of an empty tuple, a tuple containing no elements.
       */
      public static final class Tuple0 implements Tuple {

          private static final long serialVersionUID = 1L;

          /**
           * The singleton instance of Tuple0.
           */
          private static final Tuple0 INSTANCE = new Tuple0();

          /**
           * Hidden constructor.
           */
          private Tuple0() {
          }

          /**
           * Returns the singleton instance of Tuple0.
           *
           * @return The singleton instance of Tuple0.
           */
          public static Tuple0 instance() {
              return INSTANCE;
          }

          @Override
          public int arity() {
              return 0;
          }

          @Override
          public Tuple0 unapply() {
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
           * @return The singleton instance of Tuple0.
           * @see java.io.Serializable
           */
          private Object readResolve() {
              return INSTANCE;
          }
      }

      ${gen(1 to N)(genInnerTupleClass)("\n\n")}
  }
  """
}

/**
 * Generates a Java file.
 * @param pkg A path of the java package
 * @param fileName A file name, may contain path segments.
 * @param gen A generator which produces a String.
 */
def genJavaFile(pkg: String, fileName: String)(gen: () => String)(implicit charset: Charset = StandardCharsets.UTF_8): Unit = {

  val fileContents =   xs"""
    ${classHeader()}
    ${gen.apply()}
  """

  import java.nio.file.{Paths, Files}

  Files.write(
    Files.createDirectories(Paths.get(TARGET, pkg)).resolve(fileName),
    fileContents.getBytes(charset),
    StandardOpenOption.CREATE, StandardOpenOption.WRITE)
}

/**
 * Applies f for a range of Ints using delimiter to mkString the output.
 * @param range A range of Ints
 * @param f A generator which takes an Int and produces a String
 * @param delimiter The delimiter of the strings parts
 * @return Generated String
 */
def gen(range: Range)(f: Int => String)(implicit delimiter: String = "") = range.map(i => f.apply(i)) mkString delimiter

/**
 * The header for Java files.
 * @return A header as String
 */
def classHeader() = xs"""
  /**    / \\____  _    ______   _____ / \\____   ____  _____
   *    /  \\__  \\/ \\  / \\__  \\ /  __//  \\__  \\ /    \\/ __  \\   Javaslang
   *  _/  // _\\  \\  \\/  / _\\  \\\\_  \\/  // _\\  \\  /\\  \\__/  /   Copyright 2014-2015 Daniel Dietrich
   * /___/ \\_____/\\____/\\_____/____/\\___\\_____/_/  \\_/____/    Licensed under the Apache License, Version 2.0
   */
  """

/**
 * (C)opyright by Daniel Dietrich
 */
object StringContextImplicits {

  implicit class StringContextExtension(sc: StringContext) {

    def xs(args: Any*): String = align(sc.s, args)

    def xraw(args: Any*): String = align(sc.raw, args)

    /**
     * Indenting a rich string, removing first and last newline.
     * A rich string consists of arguments surrounded by text parts.
     */
    private def align(interpolator: Seq[Any] => String, args: Seq[Any]) = {

      // indent embedded strings, invariant: parts.length = args.length + 1
      val indentedArgs = for {
        (part, arg) <- sc.parts zip args.map(s => if (s == null) "" else s.toString)
      } yield {
        // get the leading space of last line of current part
        val space = """([ \t]*)[^\s]*$""".r.findFirstMatchIn(part).map(_.group(1)).getOrElse("")
        // add this leading space to each line (except the first) of current arg
        arg.split("\r?\n") reduce (_ + lineSeparator + space + _)
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
      split map { s =>
        if (s.startsWith(prefix)) s.substring(prefix.length) else s
      } mkString lineSeparator dropRight 1 // dropping termination character \u0000
    }
  }

}
