/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
import StringContextImplicits._

import scala.util.Properties.lineSeparator

// entry point
def run() {

  // lazy due forward reference
  //  lazy val output = gen("Person", Seq("name" -> 'String, "age" -> 'Int))
  //  println(output)

  println(genTuple())

}

def genTuple() = xs"""
  ${classHeader()}
  package javaslang;

  import java.util.Objects;

  public interface Tuple extends ValueObject {

    int arity();

    // -- factory methods

    static Tuple0 empty() {
      return Tuple0.instance();
    }

    ${(1 to 13).map(i => xs"""TODO:genOf($i)""") mkString "\n\n"}

    }
  """

def classHeader() = xs"""
  /**    / \\____  _    ______   _____ / \\____   ____  _____
   *    /  \\__  \\/ \\  / \\__  \\ /  __//  \\__  \\ /    \\/ __  \\   Javaslang
   *  _/  // _\\  \\  \\/  / _\\  \\\\_  \\/  // _\\  \\  /\\  \\__/  /   Copyright 2014-2015 Daniel Dietrich
   * /___/ \\_____/\\____/\\_____/____/\\___\\_____/_/  \\_/____/    Licensed under the Apache License, Version 2.0
   */
  """

def gen(name: String, params: Seq[(String, Symbol)]) = xs"""
      package model

      case class $name(${genParams(params)}, id: Option[Long] = None)

      trait ${name}Component { self: Profile =>

        import driver.simple._
        import Database.threadLocalSession

        object $name extends Table[$name]("${name.toUpperCase}") {

          def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
          ${genColumns(params)}

          def * = ${params.map(_._1).mkString(" ~ ")} ~ id.? <> ($name, $name.unapply)

          def delete(id: Long) = db withSession {
            Query(this).where(_.id is id).delete
          }

          def findById(id: Long) = db withSession {
            Query(this).where(_.id is id).firstOption
          }

          def save(${name.toLowerCase}: $name) = db withSession {
            ${name.toLowerCase}.id.fold {
              this.insert(${name.toLowerCase})
            }{ id =>
              Query(this).where(_.id is id).update(${name.toLowerCase})
            }
          }
        }
      }
  """

def genParams(params: Seq[(String, Symbol)]) = params map {
  case (name, _type) => name + ": " + _type.name
} mkString ", "

def genColumns(params: Seq[(String, Symbol)]) = params map {
  case (name, _type) => xs"""def ${name.toLowerCase} = column[${_type.name}]("${name.toUpperCase}")"""
} mkString "\n"

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
